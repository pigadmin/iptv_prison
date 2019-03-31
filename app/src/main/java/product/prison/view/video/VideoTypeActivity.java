package product.prison.view.video;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import product.prison.BaseActivity;
import product.prison.R;
import product.prison.adapter.VideoTypeAdapter;
import product.prison.app.MyApp;
import product.prison.model.TGson;
import product.prison.model.VodTypeData;
import product.prison.utils.Logs;
import product.prison.utils.Utils;

public class VideoTypeActivity extends BaseActivity implements VideoTypeAdapter.OnItemClickListener {

    private RecyclerView mainrecyle;
    private StaggeredGridLayoutManager layoutManager;
    private List<VodTypeData> list = new ArrayList<>();
    private VideoTypeAdapter adapter;

    @Override
    public void initView(Bundle savedInstanceState) {
        mainrecyle = f(R.id.mainrecyle);
        layoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.HORIZONTAL);


        mainrecyle.setLayoutManager(layoutManager);
    }

    @Override
    public void loadData() {
        vodtype();


    }


    private void vodtype() {
        RequestParams params = new RequestParams(MyApp.apiurl + "vodtype");
        params.addBodyParameter("mac", MyApp.mac);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    Logs.e(result);
                    TGson<List<VodTypeData>> json = Utils.gson.fromJson(result,
                            new TypeToken<TGson<List<VodTypeData>>>() {
                            }.getType());
                    list = json.getData();
                    if (list == null || list.isEmpty())
                        return;
                    adapter = new VideoTypeAdapter(getApplicationContext(), list);
                    mainrecyle.setAdapter(adapter);
                    adapter.setOnItemClickListener(VideoTypeActivity.this);
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
    public int getContentId() {
        return R.layout.activity_main;
    }


    @Override
    public void onItemClick(View view, int position) {
        try {
            Bundle bundle = new Bundle();
            bundle.putSerializable("key", (Serializable) list.get(position));
            startActivity(new Intent(getApplicationContext(), VideoActivity.class).putExtras(bundle));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
