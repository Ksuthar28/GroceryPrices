package com.sample.marketprices.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import java.util.*

object MyExtension {

    /*
    Convert each word first char as cap
     */
    fun String.capWords(): String {
        return this.split(" ").joinToString(" ") { world ->
            world.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    Locale.getDefault()
                ) else it.toString()
            }
        }
    }

    /*
    Dismiss keyboard
     */
    fun View.dismissKeyboard(context: Context) = try {
        val inputMethodManager: InputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(this.windowToken, 0)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}