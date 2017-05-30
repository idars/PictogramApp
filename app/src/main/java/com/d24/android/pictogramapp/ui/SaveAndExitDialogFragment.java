package com.d24.android.pictogramapp.ui;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.d24.android.pictogramapp.R;

import com.d24.android.pictogramapp.R;

public class SaveAndExitDialogFragment extends DialogFragment {

    public interface SaveAndExitDialogListener {
        public void onDialogConfirmExit(boolean saveAndExit);
    }

    SaveAndExitDialogListener mListener;
    Context mContext;
    TextView mText;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mContext = context;
        Activity activity = getActivity();

        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (SaveAndExitDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement SaveAndExitDialogListener");
        }
    }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            int margin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
            params.setMargins(margin, 0, margin, 0);

            mText = new TextView(mContext);
            mText.setText(R.string.dialog_exit_info);
            mText.setLayoutParams(params);

            FrameLayout frame = new FrameLayout(mContext);
            frame.addView(mText);

            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.dialog_save_and_exit)
                    .setView(frame)
                    .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            mListener.onDialogConfirmExit(false);
                        }
                    })
                    .setPositiveButton(R.string.dialog_confirm, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            mListener.onDialogConfirmExit(true);
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }




    /*@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        int margin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
        params.setMargins(margin, 0, margin, 0);

        mField = new EditText(mContext);
        mField.setHint("Name");
        mField.setLayoutParams(params);

        FrameLayout frame = new FrameLayout(mContext);
        frame.addView(mField);

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.dialog_save)
                .setView(frame)
                .setPositiveButton(R.string.button_save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String filename = mField.getText().toString();
                        if (!filename.isEmpty()) {
                            mListener.onDialogPositiveClick(
                                    SaveDialogFragment.this,
                                    mField.getText().toString()
                            );
                        }
                    }
                })
                .setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SaveDialogFragment.this.getDialog().cancel();
                    }
                });

        return builder.create();
    }*/

}
