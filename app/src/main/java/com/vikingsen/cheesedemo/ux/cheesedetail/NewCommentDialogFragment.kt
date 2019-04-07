package com.vikingsen.cheesedemo.ux.cheesedetail

import android.app.Dialog
import android.os.Bundle
import android.text.InputType
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.viewModels
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.vikingsen.cheesedemo.R

class NewCommentDialogFragment : AppCompatDialogFragment() {

    private val viewModel by viewModels<CheeseDetailViewModel>({ requireParentFragment() })

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialDialog(requireContext())
            .title(R.string.new_comment)
            .input(
                inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES or InputType.TYPE_TEXT_FLAG_MULTI_LINE
            ) { _, comment ->
                viewModel.newComment(comment)
            }
            .negativeButton(R.string.cancel)
    }
}