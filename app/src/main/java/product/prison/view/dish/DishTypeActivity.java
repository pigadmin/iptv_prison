package product.prison.view.dish;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import product.prison.BaseActivity;
import product.prison.R;
import product.prison.adapter.DishTypeAdapter;
import product.prison.adapter.VideoTypeAdapter;
import product.prison.app.MyApp;
import product.prison.model.FoodCat;
import product.prison.model.TGson;
import product.prison.model.VodTypeData;
import product.prison.utils.Logs;
import product.prison.utils.Utils;
import product.prison.view.video.VideoActivity;

public class DishTypeActivity extends BaseActivity implements DishTypeAdapter.OnItemClickListener {

    private RecyclerView mainrecyle;
    private StaggeredGridLayoutManager layoutManager;
    private List<FoodCat> list = new ArrayList<>();
    private DishTypeAdapter adapter;

    @Override
    public void initView(Bundle savedInstanceState) {
        mainrecyle = f(R.id.mainrecyle);
        layoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.HORIZONTAL);


        mainrecyle.setLayoutManager(layoutManager);
    }

    @Override
    public void loadData() {
        getDishStyle();
    }


    private void getDishStyle() {
        RequestParams params = new RequestParams(MyApp.apiurl + "getDishStyle");
        params.addBodyParameter("mac", MyApp.mac);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    Logs.e(result);
                    TGson<List<FoodCat>> json = Utils.gson.fromJson(result,
                            new TypeToken<TGson<List<FoodCat>>>() {
                            }.getType());
                    if (!json.getCode().equals("200")) {
                        Toast.makeText(getApplicationContext(), json.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                    list = json.getData();
                    if (list == null || list.isEmpty())
                        return;
                    adapter = new DishTypeAdapter(getApplicationContext(), list);
                    mainrecyle.setAdapter(adapter);
                    adapter.setOnItemClickListener(DishTypeActivity.this);
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
            int id = list.get(position).getId();
            startActivity(new Intent(getApplicationContext(), DishActivity.class).putExtra("key", id));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
