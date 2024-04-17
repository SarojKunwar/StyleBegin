package com.example.stylebegin

import android.text.TextUtils
import android.util.Patterns


object Utils {
    fun isValidEmail(target: CharSequence?): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }
    const val PUBLISHABLE_KEY="pk_test_51P4PAVBJft6JSmTuC0fgvLFxM6oclV3axZTHkuvkjRAapQprTDvrxIqLbpYeMbfSxYDgMtBs4WOH4bwqWiyCTsr60070wV3ywK"
    const val SECRET_KEY="sk_test_51P4PAVBJft6JSmTu6WAr3luhVrO3cD6cBSo4DyeO41x6n8KiJzFeUDA1i9ZYo3MQBFLb3aod7V2NYBuawkQKrRJH00gmjrFAR5"
}