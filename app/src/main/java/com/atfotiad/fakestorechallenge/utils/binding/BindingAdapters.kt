package com.atfotiad.fakestorechallenge.utils.binding

import android.widget.EditText
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import kotlinx.coroutines.flow.MutableStateFlow

/** Custom BindingAdapter for EditText. For two-way data binding.
 * [BindingAdapters] is a object that contains the binding adapters for the app.
 * @property setMutableText is a function that sets the mutable text.
 * @property getMutableText is a function that gets the mutable text.
 * @property setListener is a function that sets the listener for changing the text
 * */
object BindingAdapters {
    @JvmStatic
    @BindingAdapter("mutableText")
    fun setMutableText(view: EditText, text: MutableStateFlow<String>) {
        val newText = text.value
        if (view.text.toString() != newText) {
            view.setText(newText)
        }
    }

    @JvmStatic
    @InverseBindingAdapter(attribute = "mutableText", event = "mutableTextAttrChanged")
    fun getMutableText(view: EditText): String {
        return view.text.toString()
    }

    @JvmStatic
    @BindingAdapter("mutableTextAttrChanged")
    fun setListener(view: EditText, listener: InverseBindingListener?) {
        if (listener != null) {
            view.addTextChangedListener(object : android.text.TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    listener.onChange()
                }

                override fun afterTextChanged(s: android.text.Editable?) {
                }
            })
        }
    }
}