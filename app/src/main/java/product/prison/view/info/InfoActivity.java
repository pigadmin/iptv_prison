package product.prison.view.info;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import it.sephiroth.android.library.picasso.Picasso;
import product.prison.BaseActivity;
import product.prison.R;
import product.prison.adapter.InfoListAdapter;
import product.prison.adapter.TeleplayGridAdapter;
import product.prison.adapter.VideoGridAdapter;
import product.prison.adapter.VideoListAdapter;
import product.prison.app.MyApp;
import product.prison.model.Details;
import product.prison.model.InfoData;
import product.prison.model.TGson;
import product.prison.model.Vod;
import product.prison.model.VodData;
import product.prison.model.VodTypeData;
import product.prison.utils.ImageUtils;
import product.prison.utils.Logs;
import product.prison.utils.Utils;
import product.prison.view.video.PlayerActivity;

public class InfoActivity extends BaseActivity implements InfoListAdapter.OnItemClickListener {

    private RecyclerView left_list;
    private LinearLayoutManager layoutmanager;
    private InfoListAdapter listAdapter;
    private InfoData infoData;
    private List<Details> list = new ArrayList<>();
    private ImageView right_image;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    try {
                        if (crrent < 0) {
                            crrent = list.get(index).getPics().size() - 1;
                        }
                        if (crrent > list.get(index).getPics().size() - 1) {
                            crrent = 0;
                        }
                        String url = list.get(index).getPics().get(crrent).getFilePath();
                        if (url.startsWith("h")) {
//                            ImageUtils.display(right_image, url);
                            Picasso.with(getApplicationContext()).load(url).into(right_image);

//                            ImageOptions imageOptions = new ImageOptions.Builder().
//                                    setUseMemCache(true).
//                                    setConfig(Bitmap.Config.RGB_565).
//                                    setIgnoreGif(false).
//                                    setPlaceholderScaleType(ImageView.ScaleType.CENTER_CROP).
//                                    setImageScaleType(ImageView.ScaleType.CENTER_CROP).
//                                    build();
//
//                            x.image().bind(right_image, url, imageOptions);
                        }
                        Logs.e("下标" + crrent + " url " + url);
                        crrent++;
                        if (list.get(index).getPics().size() < 2)
                            return;
                        handler.sendEmptyMessageDelayed(0, 5 * 1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 1:
                    try {
                        if (crrent < 0) {
                            crrent = infoData.getPics().size() - 1;
                        }
                        if (crrent > infoData.getPics().size() - 1) {
                            crrent = 0;
                        }
                        String url = infoData.getPics().get(crrent).getFilePath();
                        if (url.startsWith("h")) {
//                            ImageUtils.display(right_image, url);
                            Picasso.with(getApplicationContext()).load(url).into(right_image);

//                            ImageOptions imageOptions = new ImageOptions.Builder().
//                                    setUseMemCache(true).
//                                    setConfig(Bitmap.Config.RGB_565).
//                                    setIgnoreGif(false).
//                                    setPlaceholderScaleType(ImageView.ScaleType.CENTER_CROP).
//                                    setImageScaleType(ImageView.ScaleType.CENTER_CROP).
//                                    build();
//
//                            x.image().bind(right_image, url, imageOptions);
                        }
                        Logs.e("下标" + crrent + " url " + url);
                        crrent++;
                        if (infoData.getPics().size() < 2)
                            return;
                        handler.sendEmptyMessageDelayed(1, 5 * 1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        try {
            if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                if (!list.isEmpty()) {
                    if (list.get(index).getPics().size() < 2)
                        return false;
                    crrent -= 2;
                    handler.removeMessages(0);
                    handler.sendEmptyMessage(0);

                } else {
                    if (infoData.getPics().size() < 2)
                        return false;
                    crrent -= 2;
                    handler.removeMessages(1);
                    handler.sendEmptyMessage(1);
                }
            } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                if (!list.isEmpty()) {
                    if (list.get(index).getPics().size() < 2)
                        return false;
                    handler.removeMessages(0);
                    handler.sendEmptyMessage(0);
                } else {
                    if (infoData.getPics().size() < 2)
                        return false;
                    handler.removeMessages(1);
                    handler.sendEmptyMessage(1);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeMessages(0);
    }

    ImageButton left, right;

    @Override
    public void initView(Bundle savedInstanceState) {
        left_list = f(R.id.left_list);
        right_image = f(R.id.right_image);
        left = f(R.id.left);
        right = f(R.id.right);

        layoutmanager = new LinearLayoutManager(this);
        layoutmanager.setOrientation(LinearLayoutManager.VERTICAL);
        left_list.setLayoutManager(layoutmanager);


        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!list.isEmpty()) {
                    crrent -= 2;
                    handler.removeMessages(0);
                    handler.sendEmptyMessage(0);

                } else {
                    crrent -= 2;
                    handler.removeMessages(1);
                    handler.sendEmptyMessage(1);
                }
            }
        });
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!list.isEmpty()) {
                    handler.removeMessages(0);
                    handler.sendEmptyMessage(0);
                } else {
                    handler.removeMessages(1);
                    handler.sendEmptyMessage(1);
                }
            }
        });
    }

    private int crrent = 0;
    private int index = 0;

    @Override
    public void loadData() {
        try {

            infoData = (InfoData) getIntent().getExtras().get("key");
            list = infoData.getDetails();
            //            Logs.e(list.size() + "info.size() ");
            index = 0;
            crrent = 0;
            if (!list.isEmpty()) {
                listAdapter = new InfoListAdapter(getApplicationContext(), list);
                left_list.setAdapter(listAdapter);
                listAdapter.setOnItemClickListener(InfoActivity.this);
                image();
            } else {
                left_list.setVisibility(View.GONE);
                checksize();
                handler.sendEmptyMessage(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void image() {

//        Logs.e("@@" + list.get(position).getIcon());
        checksize();
        handler.sendEmptyMessage(0);


    }

    private void checksize() {
        if (!list.isEmpty()) {
            if (list.get(index).getPics().size() < 2) {
                left.setVisibility(View.GONE);
                right.setVisibility(View.GONE);
            } else {
                left.setVisibility(View.VISIBLE);
                right.setVisibility(View.VISIBLE);
            }
        } else {
            if (infoData.getPics().size() < 2) {
                left.setVisibility(View.GONE);
                right.setVisibility(View.GONE);
            } else {
                left.setVisibility(View.VISIBLE);
                right.setVisibility(View.VISIBLE);
            }
        }
    }


    @Override
    public int getContentId() {
        return R.layout.activity_info;
    }


    @Override
    public void onItemClick(View view, int position) {
        try {
//            view.setBackgroundResource(R.drawable.left_list_c);

            index = position;
            image();
            listAdapter.update(position);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
