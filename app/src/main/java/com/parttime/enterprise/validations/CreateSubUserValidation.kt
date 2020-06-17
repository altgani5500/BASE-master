package com.parttime.enterprise.validations

import android.content.Context
import android.text.TextUtils
import com.parttime.enterprise.R

import java.util.regex.Matcher
import java.util.regex.Pattern

class CreateSubUserValidation {
    fun isInputValidAdduSer(
        context: Context,
        name: String,
        email: String,
        pass: String,
        roleId: String,
        IsConnection: Boolean
    ): ValidationMessageReturn {
        if (name.trim().isNotEmpty()) {
            if (!TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                return if (isValidPassword(pass)) {
                    if (roleId.trim().isNotEmpty() && roleId.toInt()!=0) {
                        if (IsConnection) {
                            ValidationMessageReturn(status = 1, errMessage = "")
                        } else {
                            ValidationMessageReturn(status = 0, errMessage = context.getString(R.string.network_error))
                        }
                    } else {
                        ValidationMessageReturn(status = 0, errMessage = context.getString(R.string.roles))
                    }
                } else {
                    ValidationMessageReturn(status = 0, errMessage = context.getString(R.string.password_err_new))
                }
            } else {
                return ValidationMessageReturn(status = 0, errMessage = context.getString(R.string.email_id))
            }
        } else {
            return ValidationMessageReturn(status = 0, errMessage = context.getString(R.string.enter_name_please))
        }
    }


    private fun isValidPassword(password: String): Boolean {
        val pattern: Pattern
        val matcher: Matcher
        val specialCharacters = "-@%\\[\\}+'!/#$^?:;,\\(\"\\)~`.*=&\\{>\\]<_"
        val PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[$specialCharacters])(?=\\S+$).{6,20}$"
        pattern = Pattern.compile(PASSWORD_REGEX)
        matcher = pattern.matcher(password)
        return matcher.matches()
    }
}