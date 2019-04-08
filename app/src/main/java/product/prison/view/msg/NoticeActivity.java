package product.prison.view.msg;

import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import product.prison.BaseActivity;
import product.prison.R;
import product.prison.adapter.NoticAdapter;
import product.prison.app.MyApp;
import product.prison.model.Nt;
import product.prison.model.TGson;
import product.prison.model.TMenu;
import product.prison.utils.ImageUtils;
import product.prison.utils.Logs;
import product.prison.utils.Utils;
import product.prison.view.ad.RsType;

public class NoticeActivity extends BaseActivity implements OnItemClickListener {
    private List<Nt> list = new ArrayList<>();

    private ListView notice_list;
    private NoticAdapter adapter;

    private ImageView notice_img;
    private VideoView notice_video;
    private TextView notice_txt;


    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
        // TODO Auto-generated method stub
        try {
            gone();
            String url = list.get(position).getContent();
            Logs.e(url);
            if (url.startsWith("h")) {
                String temp = url.substring(url.lastIndexOf("."));
                switch (RsType.type.get(temp.toLowerCase())) {
                    case 1:
                        notice_img.setVisibility(View.VISIBLE);
                        ImageUtils.display(notice_img, url);
                        break;
                    case 2:
                    case 3:
                        notice_video.setVisibility(View.VISIBLE);
                        if (notice_video.isPlaying())
                            notice_video.stopPlayback();
                        notice_video.setVideoPath(url);

                        break;
                }
            } else {
                notice_txt.setVisibility(View.VISIBLE);
                notice_txt.setText(url);
            }
            Nt nt = MyApp.db.findAll(Nt.class).get(position);
            nt.setRead(true);
            MyApp.db.update(nt);

            adapter.update(MyApp.db.findAll(Nt.class));
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    private void gone() {
        try {
            if (notice_txt.isShown()) {
                notice_txt.setVisibility(View.GONE);
            }
            if (notice_img.isShown()) {
                notice_img.setVisibility(View.GONE);
            }
            if (notice_video.isShown()) {
                notice_video.setVisibility(View.GONE);
            }
            if (notice_video.isPlaying())
                notice_video.stopPlayback();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void initView(Bundle savedInstanceState) {
        notice_list = f(R.id.notice_list);

        notice_txt = f(R.id.notice_txt);
        notice_img = f(R.id.notice_img);
        notice_video = f(R.id.notice_video);
        notice_video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                mp.setLooping(true);
            }
        });
        notice_video.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                return true;
            }
        });
        notice_list.setOnItemClickListener(this);
    }

    @Override
    public void loadData() {
        getNotice();
    }

    private void getNotice() {
        try {
            list = MyApp.db.findAll(Nt.class);
            if (list.isEmpty())
                return;
            adapter = new NoticAdapter(this, list);
            notice_list.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getContentId() {
        return R.layout.activity_notice;
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
