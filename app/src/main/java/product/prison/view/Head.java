package product.prison.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import product.prison.R;
import product.prison.app.MyApp;
import product.prison.utils.ImageUtils;
import product.prison.utils.Logs;
import product.prison.utils.Utils;

public class Head extends LinearLayout {
    View view;

    MyApp app;

    public Head(Context context, AttributeSet attrs) {
        super(context, attrs);
        view = LayoutInflater.from(context).inflate(R.layout.head, this);
        app = (MyApp) context.getApplicationContext();
        initview();

        setvalue();
    }


    private ImageView logo;
    private TextView time;

    private void initview() {
        logo = view.findViewById(R.id.logo);
        time = view.findViewById(R.id.time);
    }

    private void setvalue() {
        try {
            handler.sendEmptyMessage(0);
//            Logs.e(app.getLogoData().getLogo().getLogoPath());
            ImageUtils.display(logo, app.getLogoData().getLogo().getLogoPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    time.setText(Utils.formatMyTime("HH:mm", app.getServertime()));
                    handler.sendEmptyMessageDelayed(0, 60 * 1000);
                    break;
            }
        }
    };
}
