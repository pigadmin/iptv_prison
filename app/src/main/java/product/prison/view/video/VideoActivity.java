package product.prison.view.video;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import product.prison.BaseActivity;
import product.prison.R;
import product.prison.adapter.VideoGridAdapter;
import product.prison.adapter.VideoListAdapter;
import product.prison.app.MyApp;
import product.prison.model.TGson;
import product.prison.model.Vod;
import product.prison.model.VodData;
import product.prison.model.VodTypeData;
import product.prison.utils.Logs;
import product.prison.utils.Utils;
import product.prison.view.PlayerActivity;

public class VideoActivity extends BaseActivity implements VideoListAdapter.OnItemClickListener, AdapterView.OnItemClickListener {

    private VodTypeData vodTypeData;
    private RecyclerView left_list;
    private LinearLayoutManager layoutmanager;
    private VideoListAdapter listAdapter;
    private List<String> list = new ArrayList<>();
    private int type = 0;
    private List<VodData> grid = new ArrayList<>();
    private GridView right_grid;
    private VideoGridAdapter gridAdapter;

    @Override
    public void initView(Bundle savedInstanceState) {
        left_list = f(R.id.left_list);
        right_grid = f(R.id.right_grid);

        layoutmanager = new LinearLayoutManager(this);
        layoutmanager.setOrientation(LinearLayoutManager.VERTICAL);
        left_list.setLayoutManager(layoutmanager);

        list.add(getString(R.string.news));
        list.add(getString(R.string.hot));
    }


    @Override
    public void loadData() {
        try {
            vodTypeData = (VodTypeData) getIntent().getExtras().get("key");
            type = vodTypeData.getId();

            listAdapter = new VideoListAdapter(getApplicationContext(), list);
            left_list.setAdapter(listAdapter);
            listAdapter.setOnItemClickListener(VideoActivity.this);

            vod();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getContentId() {
        return R.layout.activity_video;
    }

    @Override
    public void onItemClick(View view, int position) {
        if (list.get(position).equals(getString(R.string.news))) {
            sort = 1;
        } else if (list.get(position).equals(getString(R.string.news))) {
            sort = 2;
        }
        vod();
    }

    private int sort = 1;

    private void vod() {
        RequestParams params = new RequestParams(MyApp.apiurl + "vod");
        params.addBodyParameter("mac", MyApp.mac);
        params.addBodyParameter("type", type + "");
        params.addBodyParameter("pageNo", "1");
        params.addBodyParameter("pageSize", "99999");
        params.addBodyParameter("sort", sort + "");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    Logs.e(result);
                    TGson<Vod> json = Utils.gson.fromJson(result,
                            new TypeToken<TGson<Vod>>() {
                            }.getType());
                    grid = json.getData().getData();
//                    if (grid == null || grid.isEmpty())
//                        return;
                    Logs.e(grid.size() + "");
                    gridAdapter = new VideoGridAdapter(VideoActivity.this, grid);
                    right_grid.setAdapter(gridAdapter);
                    right_grid.setOnItemClickListener(VideoActivity.this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
//            Bundle bundle = new Bundle();
//            bundle.putSerializable("key", grid.get(position));

            VodData vodData = grid.get(position);
            Intent intent = new Intent(VideoActivity.this, PlayerActivity.class);
            intent.putExtra("key", vodData);
            startActivity(intent);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
