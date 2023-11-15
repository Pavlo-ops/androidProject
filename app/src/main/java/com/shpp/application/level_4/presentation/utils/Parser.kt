package com.shpp.application.level_4.presentation.utils

object Parser {
    fun parseEmail(email: String): String {
        val fullName = StringBuilder(email.substring(0, email.indexOf("@")))
        fullName[0] = fullName[0].uppercaseChar()

        if (fullName.indexOf('.') != -1) {
            fullName[fullName.indexOf('.')] = ' '
            fullName[fullName.indexOf(' ') + 1] = fullName[fullName.indexOf(' ') + 1].uppercaseChar()
        }

        return fullName.toString()
    }
}