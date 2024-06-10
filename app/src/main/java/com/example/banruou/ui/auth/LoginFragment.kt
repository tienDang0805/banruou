package com.example.banruou.ui.auth

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.banruou.R
import com.example.banruou.data.model.LoginRequest
import com.example.banruou.data.model.UserResponse
import com.example.banruou.data.network.ApiService
import com.example.banruou.data.network.RetrofitInstance
import com.example.banruou.data.singleton.UserSession
import com.example.banruou.ui.setting.SettingViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class LoginFragment : Fragment() {
    private val apiService = RetrofitInstance.retrofit.create(ApiService::class.java)
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private val settingViewModel: SettingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkLoginState()

        view.findViewById<TextView>(R.id.text_register_now).setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
        view.findViewById<Button>(R.id.login)?.setOnClickListener {
            val username = view.findViewById<EditText>(R.id.username).text.toString()
            val password = view.findViewById<EditText>(R.id.password).text.toString()
            val loginRequest = LoginRequest(username, password)

            view.findViewById<ProgressBar>(R.id.loading).visibility = View.VISIBLE // Hiển thị ProgressBar

            // CoroutineScope for handling asynchronous API call
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    // Call the login API
                    val response = apiService.login(loginRequest)
                    val accessToken = "Bearer " + response.accessToken // Thêm "Bearer " vào đầu chuỗi access token
                    val userInfo = apiService.getUserInfo(accessToken)

                    Log.d("LoginFragment", "response: $response")
                    Log.d("LoginFragment", "userInfo: $userInfo")
                    UserSession.userInfo = userInfo
                    val userSessionInfoResponse: Response<UserResponse> = apiService.getUserById(userInfo.userId)

                    if (userSessionInfoResponse.isSuccessful) {
                        val userSessionInfo: UserResponse? = userSessionInfoResponse.body()
                        if (userSessionInfo != null) {
                            UserSession.userResponse = userSessionInfo
                            Log.d("UserSession.userResponse", "userInfo: ${UserSession.userResponse}")
                        } else {
                            // Xử lý trường hợp body của response là null
                        }
                    } else {
                        // Xử lý trường hợp request không thành công
                        val errorMessage = userSessionInfoResponse.message()
                        // Log hoặc thông báo lỗi
                    }
                    // Process the response (e.g., save access token, navigate to next screen)
                    withContext(Dispatchers.Main) {
                        // Ẩn ProgressBar
                        view.findViewById<ProgressBar>(R.id.loading).visibility = View.GONE
                        saveLoginState(accessToken) // Lưu trạng thái đăng nhập
                        navigateToMainNavigation()
                    }
                } catch (e: Exception) {
                    // Handle exceptions (e.g., network error)
                    withContext(Dispatchers.Main) {
                        // Ẩn ProgressBar
                        view.findViewById<ProgressBar>(R.id.loading).visibility = View.GONE

                        // Hiển thị thông báo lỗi cho người dùng
                        Toast.makeText(context, "Login failed: ${e.message}", Toast.LENGTH_SHORT).show()
                        Log.d("LoginFragment", "error: ${e.message}")
                    }
                }
            }
        }
    }

    private fun saveLoginState(accessToken: String) {
        val sharedPreferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("access_token", accessToken)
            apply()
        }
    }

    private fun checkLoginState() {
        Log.d("LoginFragment", "Checking login state")

        val sharedPreferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val accessToken = sharedPreferences.getString("access_token", null)

        if (accessToken != null) {
            Log.d("LoginFragment", "Access token found: $accessToken")
            // Nếu đã có access token, gọi API để lấy thông tin người dùng
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val userInfo = apiService.getUserInfo(accessToken)
                    UserSession.userInfo = userInfo

                    val userSessionInfoResponse: Response<UserResponse> = apiService.getUserById(userInfo.userId)
                    if (userSessionInfoResponse.isSuccessful) {
                        val userSessionInfo: UserResponse? = userSessionInfoResponse.body()
                        if (userSessionInfo != null) {
                            UserSession.userResponse = userSessionInfo
                            Log.d("UserSession.userResponse", "userInfo: ${UserSession.userResponse}")
                            withContext(Dispatchers.Main) {
                                navigateToMainNavigation()
                            }
                        } else {
                            // Xử lý trường hợp body của response là null
                            withContext(Dispatchers.Main) {
                                Toast.makeText(context, "Failed to get user information", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        // Xử lý trường hợp request không thành công
                        val errorMessage = userSessionInfoResponse.message()
                        Log.e("LoginFragment", "Failed to get user info: $errorMessage")
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "Failed to get user information", Toast.LENGTH_SHORT).show()
                        }
                    }
                } catch (e: Exception) {
                    Log.e("LoginFragment", "Exception: ${e.message}")
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Failed to get user information", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } else {
            Log.d("LoginFragment", "No access token found")
        }
    }

    private fun navigateToMainNavigation() {
        // Lấy NavController của Fragment hiện tại
        val navController = findNavController()
        // Navigate đến MainNavigation bằng action đã định nghĩa trong nav_graph
        navController.navigate(R.id.action_loginFragment_to_main_nav_graph)
    }
}