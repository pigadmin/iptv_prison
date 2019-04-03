package product.prison.view.game;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import product.prison.BaseActivity;
import product.prison.R;
import product.prison.adapter.GameAdapter;
import product.prison.adapter.TeleplayGridAdapter;
import product.prison.adapter.VideoGridAdapter;
import product.prison.adapter.VideoListAdapter;
import product.prison.app.MyApp;
import product.prison.model.GameData;
import product.prison.model.TGson;
import product.prison.model.Vod;
import product.prison.model.VodData;
import product.prison.model.VodTypeData;
import product.prison.utils.Logs;
import product.prison.utils.Utils;
import product.prison.view.video.PlayerActivity;

public class GameActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private List<GameData> grid = new ArrayList<>();
    private GridView right_grid;
    private GameAdapter gridAdapter;

    @Override
    public void initView(Bundle savedInstanceState) {
        right_grid = f(R.id.right_grid);
        right_grid.setOnItemClickListener(GameActivity.this);

    }


    @Override
    public void loadData() {
        getApp();
    }


    @Override
    public int getContentId() {
        return R.layout.activity_game;
    }


    private void getApp() {
        RequestParams params = new RequestParams(MyApp.apiurl + "getApp");
        params.addBodyParameter("mac", MyApp.mac);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    Logs.e(result);
                    TGson<List<GameData>> json = Utils.gson.fromJson(result,
                            new TypeToken<TGson<List<GameData>>>() {
                            }.getType());
                    if (!json.getCode().equals("200")) {
                        Toast.makeText(getApplicationContext(), json.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                    grid = json.getData();
                    Logs.e(grid.size() + "");
                    gridAdapter = new GameAdapter(getApplicationContext(), grid);
                    right_grid.setAdapter(gridAdapter);
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
            if (Utils.isInstall(this, grid.get(position).getPackage_name())) {
                Utils.startApk(this, grid.get(position).getPackage_name(), "");
            } else {
                if (!grid.get(position).getPath().startsWith("h")) {
                    Toast.makeText(getApplicationContext(), grid.get(position).getName() + " " + getString(R.string.urlerror), Toast.LENGTH_SHORT).show();
                    return;
                }
                new Utils().Download(this, grid.get(position).getPath(), true);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
