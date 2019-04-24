package product.prison.view.video;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import product.prison.BaseActivity;
import product.prison.R;
import product.prison.adapter.LiveAdapter;
import product.prison.app.MyApp;
import product.prison.model.Livesingles;
import product.prison.model.TGson;
import product.prison.utils.Logs;
import product.prison.utils.Utils;

public class VideoApkActivity extends BaseActivity implements LiveAdapter.OnItemClickListener {

    private RecyclerView mainrecyle;
    private StaggeredGridLayoutManager layoutManager;
    private List<Livesingles> list = new ArrayList<>();
    private LiveAdapter adapter;

    @Override
    public void initView(Bundle savedInstanceState) {
        mainrecyle = f(R.id.mainrecyle);
        layoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.HORIZONTAL);


        mainrecyle.setLayoutManager(layoutManager);
    }

    @Override
    public void loadData() {
        vodapp();
    }

    private void vodapp() {
        RequestParams params = new RequestParams(MyApp.apiurl + "vodapp");
        params.addBodyParameter("mac", MyApp.mac);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    Logs.e("vodapp "+result);
                    TGson<List<Livesingles>> json = Utils.gson.fromJson(result,
                            new TypeToken<TGson<List<Livesingles>>>() {
                            }.getType());
                    if (!json.getCode().equals("200")) {
                        Toast.makeText(getApplicationContext(), json.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                    if (json.getData() == null || json.getData().isEmpty())
                        return;
                    list = json.getData();
                    adapter = new LiveAdapter(getApplicationContext(), list);
                    mainrecyle.setAdapter(adapter);
                    adapter.setOnItemClickListener(VideoApkActivity.this);
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
        if (Utils.isInstall(this, list.get(position).getPackage_name())) {
            Utils.startApk(this, list.get(position).getPackage_name(), "");
        } else {
            if (!list.get(position).getPath().startsWith("h")) {
                Toast.makeText(getApplicationContext(), list.get(position).getName() + " " + getString(R.string.urlerror), Toast.LENGTH_SHORT).show();
                return;
            }
            new Utils().Download(this, list.get(position).getPath(), true);
        }
    }
}
