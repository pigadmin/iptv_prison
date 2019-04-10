package product.prison.view.dish;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import product.prison.BaseActivity;
import product.prison.R;
import product.prison.adapter.DishListAdapter;
import product.prison.adapter.FoodGalleryAdapter;
import product.prison.adapter.ShopOrderAdapter;
import product.prison.adapter.VideoListAdapter;
import product.prison.app.MyApp;
import product.prison.model.Food;
import product.prison.model.OrderData;
import product.prison.model.TGson;
import product.prison.utils.ImageUtils;
import product.prison.utils.Logs;
import product.prison.utils.Utils;

public class OrderActivity extends BaseActivity implements VideoListAdapter.OnItemClickListener {

    private RecyclerView left_list;
    private LinearLayoutManager layoutmanager;
    private VideoListAdapter listAdapter;
    private ImageView right_image;
    private List<String> list = new ArrayList<>();
    private TextView shop_order_clean, shop_allprices;
    private ListView order_list;
    private ImageButton shop_order_ok;
    private ShopOrderAdapter shopOrderAdapter;
    private List<OrderData> order = new ArrayList<>();

    @Override
    public void initView(Bundle savedInstanceState) {
        left_list = f(R.id.left_list);
        right_image = f(R.id.right_image);

        layoutmanager = new LinearLayoutManager(this);
        layoutmanager.setOrientation(LinearLayoutManager.VERTICAL);
        left_list.setLayoutManager(layoutmanager);
        list.add(getString(R.string.order));

        order_list = f(R.id.order_list);


        shop_order_clean = f(R.id.shop_order_clean);


        shop_allprices = f(R.id.shop_allprices);

    }


    @Override
    public void loadData() {
        listAdapter = new VideoListAdapter(getApplicationContext(), list);
        left_list.setAdapter(listAdapter);
        listAdapter.setOnItemClickListener(OrderActivity.this);
        getOrder();
    }


    private void getOrder() {
        RequestParams params = new RequestParams(MyApp.apiurl + "getOrder");
        params.addBodyParameter("mac", MyApp.mac);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    Logs.e(result);
                    TGson<List<OrderData>> json = Utils.gson.fromJson(result,
                            new TypeToken<TGson<List<OrderData>>>() {
                            }.getType());
                    if (!json.getCode().equals("200")) {
                        Toast.makeText(getApplicationContext(), json.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                    order = json.getData();
                    shopOrderAdapter = new ShopOrderAdapter(getApplicationContext(),
                            order);
                    order_list.setAdapter(shopOrderAdapter);

                    int allprice = 0;
                    for (OrderData orderData : order) {
                        allprice += orderData.getTotalPrice();
                    }
                    shop_allprices.setText(getString(R.string.shop_allprice) + allprice);
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
        return R.layout.activity_order;
    }


    @Override
    public void onItemClick(View view, int position) {

    }
}
