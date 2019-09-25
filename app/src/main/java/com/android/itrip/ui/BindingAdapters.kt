/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.itrip.ui

import android.content.Context
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.databinding.BindingAdapter


/**
 * A collection of [BindingAdapter]s for different UI-related tasks.
 *
 * In Kotlin you can write the Binding Adapters in the traditional way:
 *
 * ```
 * @BindingAdapter("property")
 * @JvmStatic fun propertyMethod(view: ViewClass, parameter1: Param1, parameter2: Param2...)
 * ```
 *
 * Or using extension functions:
 *
 * ```
 * @BindingAdapter("property")
 * @JvmStatic fun ViewClass.propertyMethod(parameter1: Param1, parameter2: Param2...)
 * ```
 *
 * See [EditText.clearTextOnFocus].
 *
 * Also, keep in mind that @JvmStatic is only necessary if you define the methods inside a class or
 * object. Consider moving the Binding Adapters to the top level of the file.
 */
object BindingAdapters {

    /**
     * When defined in an [EditText], this [BindingAdapter] will clear the text on focus and
     * set the previous value if the user doesn't enter one. When the focus leaves, it calls
     * the listener that was passed as an argument.
     *
     * Note that `android:selectAllOnFocus="true"` does something similar but not exactly the same.
     *
     * @see [clearTextOnFocus] for a version without a listener.
     */
    @BindingAdapter("clearOnFocusAndDispatch")
    @JvmStatic fun clearOnFocusAndDispatch(view: EditText, listener: View.OnFocusChangeListener?) {
        view.onFocusChangeListener = View.OnFocusChangeListener { focusedView, hasFocus ->
            val textView = focusedView as TextView
            if (hasFocus) {
                textView.text = ""
            } else {
                listener?.onFocusChange(focusedView, hasFocus)
            }
        }
    }


    /**
     * Clears the text on focus.
     *
     * This method is using extension functions. It's equivalent to:
     * ```
     * @JvmStatic fun clearTextOnFocus(view: EditText, enabled: Boolean)...
     * ```
     */
    @BindingAdapter("clearTextOnFocus")
    @JvmStatic fun EditText.clearTextOnFocus(enabled: Boolean) {
        if (enabled) {
            clearOnFocusAndDispatch(this, null)
        } else {
            this.onFocusChangeListener = null
        }
    }

    /**
     * Hides keyboard when the [EditText] is focused.
     *
     * Note that there can only be one [TextView.OnEditorActionListener] on each [EditText] and
     * this [BindingAdapter] sets it.
     */
    @BindingAdapter("hideKeyboardOnInputDone")
    @JvmStatic fun hideKeyboardOnInputDone(view: EditText, enabled: Boolean) {
        if (!enabled) return
        val listener = TextView.OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                view.clearFocus()
                val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE)
                        as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
            false
        }
        view.setOnEditorActionListener(listener)
    }


}
