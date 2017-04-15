package com.vikingsen.cheesedemo.ux.cheesedetail


import android.app.Dialog
import android.os.Bundle
import android.support.v7.app.AppCompatDialogFragment
import android.text.InputType

import com.afollestad.materialdialogs.MaterialDialog
import com.vikingsen.cheesedemo.R

class AddCommentDialogFragment : AppCompatDialogFragment() {

    private var onTextListener: OnTextListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialDialog.Builder(context)
                .title(R.string.add_comment)
                .inputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES or InputType.TYPE_TEXT_FLAG_MULTI_LINE)
                .input(R.string.comment, 0, false) { _, input ->
                    if (onTextListener != null) {
                        onTextListener!!.onTextSubmitted(input)
                    }
                }
                .negativeText(R.string.cancel)
                .build()
    }

    fun setOnTextListener(onTextListener: OnTextListener) {
        this.onTextListener = onTextListener
    }

    interface OnTextListener {
        fun onTextSubmitted(text: CharSequence)
    }

    companion object {

        const val TAG = "AddCommentDialogFragment"
    }
}
