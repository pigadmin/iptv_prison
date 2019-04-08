package product.prison.view.custom;

import android.content.Context;
import android.graphics.Canvas;
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
import product.prison.model.wea.NewWea;
import product.prison.model.wea.Wea;
import product.prison.model.wea.WeatherImage;
import product.prison.utils.ImageUtils;
import product.prison.utils.Logs;
import product.prison.utils.Utils;

public class Head extends LinearLayout {
    private View view;
    private MyApp app;
    private ImageView logo;
    private ImageView wea_image;
    private TextView time;
    private TextView city;
    private TextView tmp;
    private Wea wea;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                switch (msg.what) {
                    case 0:
                        time.setText(Utils.formatMyTime("HH:mm", app.getServertime()));
                        handler.sendEmptyMessageDelayed(0, 60 * 1000);
                        break;
                    case 1:
                        if (wea == null)
                            return;
                        Logs.e(wea.getCity());
                        Logs.e(Utils.gson.toJson(wea.getData().get(0)));
                        wea_image.setBackgroundResource(WeatherImage.parseIcon(wea.getData().get(0).getWea()));
                        city.setText(wea.getCity());
                        tmp.setText(wea.getData().get(0).getTem());
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public Head(Context context, AttributeSet attrs) {
        super(context, attrs);
        view = LayoutInflater.from(context).inflate(R.layout.head, this);
        app = (MyApp) context.getApplicationContext();
        initview();

        setvalue();


    }


    private void initview() {
        logo = view.findViewById(R.id.logo);
        wea_image = view.findViewById(R.id.wea_image);
        time = view.findViewById(R.id.time);
        city = view.findViewById(R.id.city);
        tmp = view.findViewById(R.id.tmp);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        handler.removeMessages(0);
        handler.sendEmptyMessageDelayed(0, 100);
    }

    private void setvalue() {
        try {
            wea = app.getWea();

            handler.removeMessages(1);
            handler.sendEmptyMessageDelayed(1, 1000);
//            Logs.e(app.getLogoData().getLogo().getLogoPath());
            if (app.getLogoData() == null)
                return;
            String url = app.getLogoData().getLogo().getLogoPath();
            if (url.isEmpty() || !url.startsWith("h"))
                return;
            ImageUtils.display(logo, url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
