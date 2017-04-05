package com.vikingsen.cheesedemo.ux.cheesedetail;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.InputType;

import com.afollestad.materialdialogs.MaterialDialog;
import com.vikingsen.cheesedemo.R;

public class AddCommentDialogFragment extends AppCompatDialogFragment {

    public static final String TAG = "AddCommentDialogFragment";

    private OnTextListener onTextListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new MaterialDialog.Builder(getContext())
                .title(R.string.add_comment)
                .inputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES | InputType.TYPE_TEXT_FLAG_MULTI_LINE)
                .input(R.string.comment, 0, false, (dialog, input) -> {
                    if (onTextListener != null) {
                        onTextListener.onTextSubmitted(input);
                    }
                })
                .negativeText(R.string.cancel)
                .build();
    }

    public void setOnTextListener(OnTextListener onTextListener) {
        this.onTextListener = onTextListener;
    }

    interface OnTextListener {
        void onTextSubmitted(CharSequence text);
    }
}
