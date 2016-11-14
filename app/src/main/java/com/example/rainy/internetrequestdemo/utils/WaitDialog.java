package com.example.rainy.internetrequestdemo.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.Window;

import com.example.rainy.internetrequestdemo.R;

/**
 * Author: Rainy <br>
 * Description: InternetRequestDemo <br>
 * Since: 2016/11/14 0014 上午 10:43 <br>
 */

public class WaitDialog extends ProgressDialog{

    public WaitDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCanceledOnTouchOutside(false);
        setProgressStyle(STYLE_SPINNER);
        setMessage(context.getText(R.string.wait_dialog_title));
    }
}
