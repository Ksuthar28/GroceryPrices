package com.sample.marketprices.adapters

import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.util.*


@BindingAdapter("capWord")
fun textCapWord(textView: TextView, text: String) {
    val string: String = text.split(" ").joinToString(" ") { world ->
        world.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(
                Locale.getDefault()
            ) else it.toString()
        }
    }
    textView.text = string
}

