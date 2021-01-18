package de.kdsoftworx.ls_babyalbum2.Helper;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;

import de.kdsoftworx.ls_babyalbum2.R;

public class CShowProgress {

    public static CShowProgress mCShowProgress;
    public Dialog mDialog;

    public static CShowProgress getInstance() {
        if (mCShowProgress == null) {
            mCShowProgress = new CShowProgress();
        }
        return mCShowProgress;
    }

    public void showProgress(Context mContext) {
        mDialog = new Dialog(mContext);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.custom_progress_bar_layout);
        mDialog.findViewById(R.id.progressBar1);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.setCancelable(true);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.show();

    }

    public void hideProgress() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }
}
