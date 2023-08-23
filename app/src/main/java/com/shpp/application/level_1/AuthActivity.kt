package com.shpp.application.level_1

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.shpp.application.R
import com.shpp.application.databinding.AuthActivityBinding
import com.shpp.application.level_1.utils.Constants.EMAIL
import com.shpp.application.level_1.utils.Constants.MIN_LENGTH_PASSWORD
import com.shpp.application.level_1.utils.Constants.PASSWORD
import com.shpp.application.level_1.utils.Constants.SHARED_PREFERENCES

class AuthActivity : AppCompatActivity() {

    private val binding: AuthActivityBinding by lazy {
        AuthActivityBinding.inflate(layoutInflater)
    }

    private val sharedPreferences: SharedPreferences by lazy {
        getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        addEmailListener()
        addPasswordListener()
        autoLogin()
        binding.registerButton.setOnClickListener { startMainActivity(); }
    }


    private fun autoLogin() {
        binding.editEmail.setText(sharedPreferences.getString(EMAIL, ""))
        binding.editPassword.setText(sharedPreferences.getString(PASSWORD, ""))
    }

    private fun startMainActivity() {
        with(binding) {
            if (isPasswordCorrect(editPassword.text.toString()) && isEmailCorrect(editEmail.text.toString())) {
                saveAutoLog()
                val intentToAuth = Intent(this@AuthActivity, MainActivity::class.java)
                intentToAuth.putExtra(Intent.EXTRA_TEXT, editEmail.text.toString())
                startActivity(intentToAuth)
                overridePendingTransition(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left
                )
            }
        }
    }

    private fun saveAutoLog() {
        with(binding) {
            if (checkboxRemember.isChecked) {
                sharedPreferences.edit()
                    .putString(PASSWORD, editPassword.text.toString())
                    .putString(EMAIL, editEmail.text.toString())
                    .apply()
            } else {
                sharedPreferences.edit()
                    .clear()
                    .apply()
            }
        }
    }

    private fun addEmailListener() {
        binding.editEmail.doOnTextChanged { text, _, _, _ ->
            if (!isEmailCorrect(text.toString())) {
                binding.editEmail.error = resources.getString(R.string.error_email)
            } else {
                binding.editEmail.error = null
            }
        }
    }

    private fun addPasswordListener() {
        binding.editPassword.doOnTextChanged { text, _, _, _ ->
            if (!isPasswordCorrect(text.toString())) {
                binding.editPassword.error = resources.getString(R.string.error_password)
            } else {
                binding.editPassword.error = null
            }
        }
    }

    private fun isPasswordCorrect(password: String): Boolean {
        val hasNumberAndLetter: Boolean = password.any { it.isDigit() } &&
                password.any { it.isUpperCase() } &&
                password.any { it.isLowerCase() }
        return !(password.length < MIN_LENGTH_PASSWORD || !hasNumberAndLetter)
    }

    private fun isEmailCorrect(textEmail: String) =
        Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$").matches(textEmail)
}