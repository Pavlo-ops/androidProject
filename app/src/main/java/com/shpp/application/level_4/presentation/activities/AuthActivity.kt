package com.shpp.application.level_4.presentation.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.core.app.ActivityOptionsCompat
import androidx.core.widget.doOnTextChanged
import com.shpp.application.R
import com.shpp.application.databinding.AuthActivityBinding
import com.shpp.application.level_4.App
import com.shpp.application.level_4.utils.Constants.EMAIL
import com.shpp.application.level_4.utils.Constants.MIN_LENGTH_PASSWORD
import com.shpp.application.level_4.utils.Constants.PASSWORD
import com.shpp.application.level_4.utils.Constants.SHARED_PREFERENCES

/**
 * AuthActivity.kt
 * @author Pavlo Kokhanevych
 */
class AuthActivity : BaseActivity<AuthActivityBinding>(AuthActivityBinding::inflate) {

    private val sharedPreferences: SharedPreferences by lazy {
        getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        autoLogin()
    }

    override fun setListeners() {
        addEmailListener()
        addPasswordListener()
        binding.registerButton.setOnClickListener { startMainActivity(); }
    }

    private fun autoLogin() { // TODO: not correctly
        binding.editEmail.setText(sharedPreferences.getString(EMAIL, ""))
        binding.editPassword.setText(sharedPreferences.getString(PASSWORD, ""))
    }

    private fun startMainActivity() {
        with(binding) {
            if (isPasswordCorrect(editPassword.text.toString()) && isEmailCorrect(editEmail.text.toString())) {
                saveAutoLog()
                App.email = editEmail.text.toString()
                val intentToAuth = Intent(this@AuthActivity, MainActivity::class.java)


                val options = ActivityOptionsCompat.makeCustomAnimation(
                    this@AuthActivity,
                    R.anim.slide_in_right,
                    R.anim.slide_out_left
                )

                startActivity(intentToAuth, options.toBundle())
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
            binding.editEmail.error = if (!isEmailCorrect(text.toString())) {
                getString(R.string.error_email)
            } else {
                null
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

    private fun isPasswordCorrect(password: String): Boolean { // TODO: to view model
        val hasNumberAndLetter: Boolean = password.any { it.isDigit() } &&
                password.any { it.isUpperCase() } &&
                password.any { it.isLowerCase() }
        return !(password.length < MIN_LENGTH_PASSWORD || !hasNumberAndLetter)
    }

    private fun isEmailCorrect(textEmail: String) = // TODO: to view model
        Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$").matches(textEmail)
}