package com.example.banruou.ui.auth

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.InputFilter
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView

import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.banruou.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


data class UserRegister(
    val DIACHI: String,
    val EMAIL: String,
    val GIOITINH: String,
    val HO: String,
    val MANQ: String = "1", // default value
    val NGAYSINH: String,
    val PASSWORD: String,
    val SDT: String,
    val TEN: String,
    val USERNAME: String
)

class RegisterFragment : Fragment() {

    private lateinit var viewModel: RegisterViewModel
    private lateinit var firstName: EditText
    private lateinit var lastName: EditText
    private lateinit var username: EditText
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var confirmPassword: EditText
    private lateinit var address: EditText
    private lateinit var phone: EditText
    private lateinit var birthday: EditText
    private lateinit var genderGroup: RadioGroup
    private lateinit var male: RadioButton
    private lateinit var female: RadioButton
    private lateinit var errorFirstName: TextView
    private lateinit var errorLastName: TextView
    private lateinit var errorUsername: TextView
    private lateinit var errorEmail: TextView
    private lateinit var errorPassword: TextView
    private lateinit var errorConfirmPassword: TextView
    private lateinit var errorAddress: TextView
    private lateinit var errorPhone: TextView
    private lateinit var errorBirthday: TextView
    private lateinit var errorGender: TextView
    private lateinit var calendarIcon: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_register, container, false)
        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)

        firstName = root.findViewById(R.id.first_name)
        lastName = root.findViewById(R.id.last_name)
        username = root.findViewById(R.id.username)
        email = root.findViewById(R.id.email)
        password = root.findViewById(R.id.password)
        confirmPassword = root.findViewById(R.id.confirm_password)
        address = root.findViewById(R.id.address)
        phone = root.findViewById(R.id.phone)
        birthday = root.findViewById(R.id.birthday)
        calendarIcon = root.findViewById(R.id.calendar_icon)
        genderGroup = root.findViewById(R.id.gender_group)
        male = root.findViewById(R.id.male)
        female = root.findViewById(R.id.female)
        errorFirstName = root.findViewById(R.id.error_first_name)
        errorLastName = root.findViewById(R.id.error_last_name)
        errorUsername = root.findViewById(R.id.error_username)
        errorEmail = root.findViewById(R.id.error_email)
        errorPassword = root.findViewById(R.id.error_password)
        errorConfirmPassword = root.findViewById(R.id.error_confirm_password)
        errorAddress = root.findViewById(R.id.error_address)
        errorPhone = root.findViewById(R.id.error_phone)
        errorBirthday = root.findViewById(R.id.error_birthday)
        errorGender = root.findViewById(R.id.error_gender)
        val toolbar: Toolbar = root.findViewById(R.id.toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_back)
        toolbar.setTitle("Đăng Ký")
        toolbar.setTitleTextColor(Color.WHITE)
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        val registerButton = root.findViewById<Button>(R.id.register_button)

        registerButton.setOnClickListener {
            hideKeyboard()
            validateForm()
        }

        // Highlight the asterisks in red
        highlightAsterisk(root.findViewById(R.id.label_first_name))
        highlightAsterisk(root.findViewById(R.id.label_last_name))
        highlightAsterisk(root.findViewById(R.id.label_username))
        highlightAsterisk(root.findViewById(R.id.label_email))
        highlightAsterisk(root.findViewById(R.id.label_password))
        highlightAsterisk(root.findViewById(R.id.label_confirm_password))
        highlightAsterisk(root.findViewById(R.id.label_address))
        highlightAsterisk(root.findViewById(R.id.label_phone))
        highlightAsterisk(root.findViewById(R.id.label_birthday))
        highlightAsterisk(root.findViewById(R.id.label_gender))

        // Set up birthday EditText to open DatePickerDialog
        birthday.setOnClickListener {
            showDatePickerDialog()
        }

        calendarIcon.setOnClickListener {
            showDatePickerDialog()
        }

        // Set input filter to restrict format to dd-MM-yyyy
        birthday.filters = arrayOf(InputFilter.LengthFilter(10), DateInputFilter())

        // Lắng nghe kết quả đăng ký từ ViewModel
        viewModel.registrationResult.observe(viewLifecycleOwner, Observer { result ->
            if (result) {
                showPopup("Đăng ký thành công!", true)
            } else {
                showPopup("Đăng ký thất bại. Vui lòng thử lại.", false)
            }
        })

        return root
    }

    private fun highlightAsterisk(textView: TextView) {
        val text = textView.text.toString()
        if (text.contains("*")) {
            val spannable = SpannableStringBuilder(text)
            val start = text.indexOf("*")
            spannable.setSpan(
                ForegroundColorSpan(ContextCompat.getColor(requireContext(), android.R.color.holo_red_dark)),
                start,
                start + 1,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            textView.text = spannable
        }
    }

    private fun validateForm() {
        val firstNameText = firstName.text.toString().trim()
        val lastNameText = lastName.text.toString().trim()
        val usernameText = username.text.toString().trim()
        val emailText = email.text.toString().trim()
        val passwordText = password.text.toString().trim()
        val confirmPasswordText = confirmPassword.text.toString().trim()
        val addressText = address.text.toString().trim()
        val phoneText = phone.text.toString().trim()
        val birthdayText = birthday.text.toString().trim()
        val genderId = genderGroup.checkedRadioButtonId

        var isValid = true

        if (firstNameText.isEmpty()) {
            errorFirstName.text = "Bạn cần phải thêm họ"
            errorFirstName.visibility = View.VISIBLE
            isValid = false
        } else {
            errorFirstName.visibility = View.GONE
        }

        if (lastNameText.isEmpty()) {
            errorLastName.text = "Bạn cần phải thêm tên"
            errorLastName.visibility = View.VISIBLE
            isValid = false
        } else {
            errorLastName.visibility = View.GONE
        }

        if (usernameText.isEmpty()) {
            errorUsername.text = "Bạn cần phải thêm tên đăng nhập"
            errorUsername.visibility = View.VISIBLE
            isValid = false
        } else {
            errorUsername.visibility = View.GONE
        }

        if (emailText.isEmpty()) {
            errorEmail.text = "Bạn cần phải thêm email"
            errorEmail.visibility = View.VISIBLE
            isValid = false
        } else {
            errorEmail.visibility = View.GONE
        }

        if (passwordText.isEmpty()) {
            errorPassword.text = "Bạn cần phải thêm mật khẩu"
            errorPassword.visibility = View.VISIBLE
            isValid = false
        } else {
            errorPassword.visibility = View.GONE
        }

        if (confirmPasswordText.isEmpty()) {
            errorConfirmPassword.text = "Bạn cần phải thêm lại mật khẩu"
            errorConfirmPassword.visibility = View.VISIBLE
            isValid = false
        } else {
            errorConfirmPassword.visibility = View.GONE
        }

        if (passwordText != confirmPasswordText) {
            errorConfirmPassword.text = "Mật khẩu không khớp"
            errorConfirmPassword.visibility = View.VISIBLE
            isValid = false
        }

        if (addressText.isEmpty()) {
            errorAddress.text = "Bạn cần phải thêm địa chỉ"
            errorAddress.visibility = View.VISIBLE
            isValid = false
        } else {
            errorAddress.visibility = View.GONE
        }

        if (phoneText.isEmpty()) {
            errorPhone.text = "Bạn cần phải thêm số điện thoại"
            errorPhone.visibility = View.VISIBLE
            isValid = false
        } else {
            errorPhone.visibility = View.GONE
        }

        if (birthdayText.isEmpty()) {
            errorBirthday.text = "Bạn cần phải thêm ngày sinh"
            errorBirthday.visibility = View.VISIBLE
            isValid = false
        } else if (!isValidDate(birthdayText)) {
            errorBirthday.text = "Ngày sinh không hợp lệ"
            errorBirthday.visibility = View.VISIBLE
            isValid = false
        } else {
            errorBirthday.visibility = View.GONE
        }

        if (genderId == -1) {
            errorGender.text = "Bạn cần phải chọn giới tính"
            errorGender.visibility = View.VISIBLE
            isValid = false
        } else {
            errorGender.visibility = View.GONE
        }
        val birthdayTextFormatted = convertDateFormat(birthdayText)

        if (isValid) {
            val genderText = if (male.isChecked) "Nam" else "Nữ"

            val userRegister = UserRegister(
                DIACHI = addressText,
                EMAIL = emailText,
                GIOITINH = genderText,
                HO = firstNameText,
                NGAYSINH = birthdayTextFormatted,
                PASSWORD = passwordText,
                SDT = phoneText,
                TEN = lastNameText,
                USERNAME = usernameText
            )

            viewModel.register(userRegister)
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            val formattedDate = String.format("%02d-%02d-%d", selectedDay, selectedMonth + 1, selectedYear)
            birthday.setText(formattedDate)
        }, year, month, day)

        datePickerDialog.show()
    }

    private fun showPopup(message: String, isSuccess: Boolean) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                if (isSuccess) {
                    findNavController().navigateUp()
                }
            }
        builder.create().show()
    }

    private fun hideKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    private fun isValidDate(date: String): Boolean {
        return try {
            val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            sdf.isLenient = false
            sdf.parse(date)
            true
        } catch (e: Exception) {
            false
        }
    }
}
private fun convertDateFormat(inputDate: String): String {
    val inputFormat = SimpleDateFormat("dd-MM-yy", Locale.getDefault())
    val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val date = inputFormat.parse(inputDate)
    return outputFormat.format(date)
}
class DateInputFilter : InputFilter {
    override fun filter(source: CharSequence, start: Int, end: Int, dest: Spanned, dstart: Int, dend: Int): CharSequence? {
        val builder = StringBuilder(dest).insert(dstart, source, start, end).toString()
        if (builder.length > 10) {
            return ""
        }
        return if (builder.matches(Regex("^\\d{0,2}(-\\d{0,2}(-\\d{0,4})?)?$"))) {
            null
        } else {
            ""
        }
    }
}