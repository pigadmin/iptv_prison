package product.prison.service;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import product.prison.R;

public class Call {


    public Call(Context context) {
        try {
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            WindowManager.LayoutParams params = new WindowManager.LayoutParams();
            params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
            params.format = PixelFormat.TRANSLUCENT;
            params.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            View view = LayoutInflater.from(context).inflate(R.layout.float_call, null);
            windowManager.addView(view, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
