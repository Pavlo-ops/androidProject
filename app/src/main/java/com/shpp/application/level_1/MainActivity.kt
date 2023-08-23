package com.shpp.application.level_1

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.shpp.application.R
import com.shpp.application.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.buttonLogOut.setOnClickListener { startAuthActivity(); }
        changeTextNameView()
    }

    private fun startAuthActivity() {
        val intent = Intent(this@MainActivity, AuthActivity::class.java)
        startActivity(intent)
        overridePendingTransition(
            R.anim.slide_in_left,
            R.anim.slide_out_right
        )
    }

    private fun changeTextNameView() {
        val email = intent.getStringExtra(Intent.EXTRA_TEXT)
        if (email != null) {
            binding.textName.text = parseEmail(email.toString())
        }
    }

    private fun parseEmail(stringExtra: String): CharSequence {

        // Cuts part text after '@'
        val fullName: StringBuilder =
            StringBuilder(stringExtra.substring(0, stringExtra.indexOf("@")))

        fullName[0] = fullName[0].uppercaseChar()

        // Splits name and surname
        if (fullName.indexOf('.') != -1) {
            fullName[fullName.indexOf('.')] = ' '
            fullName[fullName.indexOf(' ') + 1] =
                fullName[fullName.indexOf(' ') + 1].uppercaseChar()
        }

        return fullName
    }
}