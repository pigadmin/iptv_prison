package product.prison.view.dish;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
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
import product.prison.adapter.FoodGalleryAdapter;
import product.prison.adapter.ShopCartAdapter;
import product.prison.adapter.VideoListAdapter;
import product.prison.app.MyApp;
import product.prison.model.TGson;
import product.prison.utils.Logs;
import product.prison.utils.Utils;

public class CartActivity extends BaseActivity implements VideoListAdapter.OnItemClickListener, View.OnClickListener {

    private RecyclerView left_list;
    private LinearLayoutManager layoutmanager;
    private VideoListAdapter listAdapter;
    private ImageView right_image;
    private List<String> list = new ArrayList<>();
    private ListView cart_list;
    private TextView shop_cart_clean, shop_allprice;
    private ShopCartAdapter shopCartAdapter;
    private TextView shop_cart_ok;
    private MyApp app;


    @Override
    public void initView(Bundle savedInstanceState) {
        app = (MyApp) getApplication();
        left_list = f(R.id.left_list);
        right_image = f(R.id.right_image);

        layoutmanager = new LinearLayoutManager(this);
        layoutmanager.setOrientation(LinearLayoutManager.VERTICAL);
        left_list.setLayoutManager(layoutmanager);
        list.add(getString(R.string.cat));

        cart_list = f(R.id.cart_list);


        shop_cart_clean = f(R.id.shop_cart_clean);
        shop_cart_clean.setOnClickListener(this);

        shop_cart_ok = f(R.id.shop_cart_ok);
        shop_cart_ok.setOnClickListener(this);
        shop_allprice = f(R.id.shop_allprice);

    }

    public void settotal() {
        int allprice = 0;
        for (int i = 0; i < app.getShopCats().size(); i++) {
            allprice += app.getShopCats().get(i)
                    .getCount()
                    * app.getShopCats().get(i)
                    .getPrice();
        }
        shop_allprice.setText(getString(R.string.shop_allprice) + allprice);
    }


    @Override
    public void loadData() {
        try {
            listAdapter = new VideoListAdapter(getApplicationContext(), list);
            left_list.setAdapter(listAdapter);
            listAdapter.setOnItemClickListener(CartActivity.this);


            shopCartAdapter = new ShopCartAdapter(this,
                    app.getShopCats());

            cart_list.setAdapter(shopCartAdapter);
            shopCartAdapter.setListview(cart_list);


            settotal();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void orderDish() {
//        String order = list.get(current).getId() + ","
//                + count + ";";

        String order = "";
        for (int i = 0; i < app.getOrDishs().size(); i++) {
            order += app.getOrDishs().get(i).getId() + ","
                    + app.getOrDishs().get(i).getCount() + ";";
        }
        Logs.e(order);
        RequestParams params = new RequestParams(MyApp.apiurl + "orderDish");
        params.addBodyParameter("mac", MyApp.mac);
        params.addBodyParameter("order", order);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    Logs.e("orderDish "+result);
                    TGson<String> json = Utils.gson.fromJson(result,
                            new TypeToken<TGson<String>>() {
                            }.getType());
                    if (json.getCode().equals("200")) {
                        Toast.makeText(getApplicationContext(), getString(R.string.shop_orderscu), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), json.getMsg(), Toast.LENGTH_SHORT).show();
                    }

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
        return R.layout.activity_cat;
    }


    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public void onClick(View view) {
        try {
            if (view == shop_cart_clean) {
                app.getShopCats().clear();
                shopCartAdapter.notifyDataSetChanged();
                settotal();
            } else if (view == shop_cart_ok) {

                if (app.getShopCats().isEmpty()) {
                    Toast.makeText(this, getString(R.string.shop_cartnull), Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    for (int i = 0; i < app.getShopCats().size(); i++) {
                        app.getOrDishs().add(
                                app.getShopCats().get(i));
                    }

                    app.getShopCats().clear();
                    shopCartAdapter.notifyDataSetChanged();
                    settotal();
                    orderDish();
                }


            }
        } catch (Exception e) {
            // TODO: handle exception

        }
    }
}
