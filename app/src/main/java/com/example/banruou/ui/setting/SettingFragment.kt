package com.example.banruou.ui.setting

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.banruou.R
import com.example.banruou.data.model.UserResponse
import com.example.banruou.data.singleton.UserSession
import org.w3c.dom.Text

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SettingFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private val settingViewModel: SettingViewModel by viewModels()
    val TAG = "SettingFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userId = UserSession.userInfo?.userId ?: ""
        val btnOrder: LinearLayout = view.findViewById(R.id.llOrder)
        btnOrder.setOnClickListener {
            findNavController().navigate(R.id.action_settingFragment_to_orderFragment)
        }

        val logoutButton: LinearLayout = view.findViewById(R.id.logout)
        logoutButton.setOnClickListener {
            Log.d(TAG, "Logout")

            showLogoutConfirmationDialog()
        }

        if (userId.isNotEmpty()) {
            settingViewModel.fetchUserById(userId)
        }

        settingViewModel.user.observe(viewLifecycleOwner, Observer { user ->
            Log.d(TAG, "user: " + user?.ten)
            val nameTextView: TextView = view.findViewById(R.id.textViewName)
            if (user != null) {
                val hovaten = user.ho + " " + user.ten
                nameTextView.text = hovaten
            }
            updateUserUI(user)
        })
    }

    private fun updateUserUI(user: UserResponse?) {
        // Update UI here
    }

    private fun logout() {
        val sharedPreferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            remove("access_token")
            apply()
        }

        findNavController().navigate(R.id.action_settingFragment_to_loginFragment)
    }
    private fun showLogoutConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Xác nhận đăng xuất")
            .setMessage("Bạn có thật sự muốn đăng xuất?")
            .setPositiveButton("OK") { dialog, which ->
                logout()
            }
            .setNegativeButton("Không") { dialog, which ->
                dialog.dismiss()
            }
            .create()
            .show()
    }
    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SettingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
