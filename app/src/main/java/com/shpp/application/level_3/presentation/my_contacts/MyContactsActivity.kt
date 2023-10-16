package com.shpp.application.level_3.presentation.my_contacts

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.shpp.application.R
import com.shpp.application.databinding.MyContactsActivityBinding
import com.shpp.application.level_3.App
import com.shpp.application.level_3.presentation.my_contacts.fragments.MyContactsFragment
import com.shpp.application.level_3.utils.Constants.TRANSACTION_TO_CONTACTS

class MyContactsActivity : AppCompatActivity() {

    private val binding: MyContactsActivityBinding by lazy {
        MyContactsActivityBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if(!App.isFeatureNavigationEnable && savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.frame_container, MyContactsFragment(), TRANSACTION_TO_CONTACTS)
                .addToBackStack(TRANSACTION_TO_CONTACTS)
                .commit()
        }
    }
}