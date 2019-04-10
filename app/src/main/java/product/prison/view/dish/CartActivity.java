package product.prison.view.dish;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import product.prison.adapter.VideoListAdapter;
import product.prison.app.MyApp;
import product.prison.model.TGson;
import product.prison.utils.Logs;
import product.prison.utils.Utils;

public class CartActivity extends BaseActivity implements VideoListAdapter.OnItemClickListener {

    private RecyclerView left_list;
    private LinearLayoutManager layoutmanager;
    private VideoListAdapter listAdapter;
    private ImageView right_image;
    private List<String> list = new ArrayList<>();

    @Override
    public void initView(Bundle savedInstanceState) {
        left_list = f(R.id.left_list);
        right_image = f(R.id.right_image);

        layoutmanager = new LinearLayoutManager(this);
        layoutmanager.setOrientation(LinearLayoutManager.VERTICAL);
        left_list.setLayoutManager(layoutmanager);
        list.add(getString(R.string.cat));
    }

    private int styleId;

    @Override
    public void loadData() {
        listAdapter = new VideoListAdapter(getApplicationContext(), list);
        left_list.setAdapter(listAdapter);
        listAdapter.setOnItemClickListener(CartActivity.this);

//        getDish();
    }

    private FoodGalleryAdapter galleryAdapter;


    private void setdate() {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    AlertDialog add_dialog;
    ImageButton ok, cancle;
    ImageButton jia, jian;
    TextView number;
    int count;

    public void add() {
        // TODO Auto-generated method stub
        try {
            count = 1;
            if (add_dialog != null) {
                add_dialog.dismiss();
                add_dialog = null;
            }
            add_dialog = new AlertDialog.Builder(this).create();
            add_dialog.show();
            add_dialog.setContentView(R.layout.dialog_food);
            jia = add_dialog.findViewById(R.id.jia);
            jian = add_dialog.findViewById(R.id.jian);
            number = add_dialog.findViewById(R.id.number);
            jia.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    count = Integer.parseInt(number.getText().toString());
                    count++;
                    number.setText(count + "");
                }
            });

            jian.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    count = Integer.parseInt(number.getText().toString());
                    if (count > 1) {
                        count--;
                    } else {
                        count = 9;
                    }
                    number.setText(count + "");

                }
            });

            ok = add_dialog.findViewById(R.id.ok);
            ok.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
//                    orderDish();
                }


            });
            cancle = add_dialog.findViewById(R.id.cancle);
            cancle.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    add_dialog.dismiss();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void orderDish() {
//        String order = list.get(current).getId() + ","
//                + count + ";";
//        Logs.e(order);
        RequestParams params = new RequestParams(MyApp.apiurl + "orderDish");
        params.addBodyParameter("mac", MyApp.mac);
//        params.addBodyParameter("order", order);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    Logs.e(result);
                    TGson<String> json = Utils.gson.fromJson(result,
                            new TypeToken<TGson<String>>() {
                            }.getType());
                    if (json.getCode().equals("200")) {
                        if (add_dialog != null)
                            add_dialog.dismiss();
                        add_dialog = null;
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
        return R.layout.activity_video;
    }


    @Override
    public void onItemClick(View view, int position) {

    }
}
