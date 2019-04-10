package product.prison.view.info;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

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
    private List<Details> list = new ArrayList<>();
    private ImageView right_image;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    try {
                        if (crrent > list.get(index).getPics().size() - 1) {
                            crrent = 0;
                        }
                        String url = list.get(index).getPics().get(crrent).getFilePath();
                        if (url.startsWith("h")) {
                            ImageUtils.display(right_image, url);
                        }
//                        Logs.e("下标" + crrent + " url " + url);

                        crrent++;
                        if (list.get(index).getPics().size() < 2)
                            return;
                        handler.sendEmptyMessageDelayed(0, 5 * 1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

    @Override
    public void initView(Bundle savedInstanceState) {
        left_list = f(R.id.left_list);
        right_image = f(R.id.right_image);

        layoutmanager = new LinearLayoutManager(this);
        layoutmanager.setOrientation(LinearLayoutManager.VERTICAL);
        left_list.setLayoutManager(layoutmanager);
    }

    private int crrent = 0;
    private int index = 0;

    @Override
    public void loadData() {
        try {
            list = (List<Details>) getIntent().getExtras().get("key");
//            Logs.e(list.size() + "info.size() ");
            listAdapter = new InfoListAdapter(getApplicationContext(), list);
            left_list.setAdapter(listAdapter);
            listAdapter.setOnItemClickListener(InfoActivity.this);

            index = 0;
            image();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void image() {
        if (list.isEmpty())
            return;
//        Logs.e("@@" + list.get(position).getIcon());
        crrent = 0;
        handler.sendEmptyMessage(0);
    }


    @Override
    public int getContentId() {
        return R.layout.activity_info;
    }


    @Override
    public void onItemClick(View view, int position) {
        try {
            index = position;
            image();
            listAdapter.update(position);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
