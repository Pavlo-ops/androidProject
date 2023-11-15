package com.shpp.application.level_4.presentation.fragments

import androidx.lifecycle.ViewModel
import com.shpp.application.level_4.presentation.utils.Parser

class MyProfileViewModel : ViewModel() {
    fun getNameByEmail(email: String) : String = Parser.parseEmail(email)
}