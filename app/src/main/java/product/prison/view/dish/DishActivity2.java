package product.prison.view.dish;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Gallery;
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
import product.prison.app.MyApp;
import product.prison.model.Food;
import product.prison.model.TGson;
import product.prison.utils.ImageUtils;
import product.prison.utils.Logs;
import product.prison.utils.Utils;

public class DishActivity2 extends BaseActivity implements AdapterView.OnItemClickListener {

    private Gallery info_gallery;
    private TextView info_title;
    private TextView info_web;
    private ImageView info_img;
    private TextView info_time, info_price, sale_num;

    private ImageButton shop_cat, shop_order;

    @Override
    public void initView(Bundle savedInstanceState) {

        info_gallery = f(R.id.info_gallery);
        info_title = f(R.id.info_title);
        info_web = f(R.id.info_web);
        info_img = f(R.id.info_img);
        info_time = f(R.id.info_time);
        info_price = f(R.id.info_price);
        sale_num = f(R.id.sale_num);


        info_gallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                current = position;
                setdate();
                galleryAdapter.setSelectItem(position);
                galleryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        info_gallery.setOnItemClickListener(this);

    }


    private int styleId;


    @Override
    public void loadData() {
        styleId = getIntent().getIntExtra("key", 0);
        getDish();
    }

    private List<Food> list = new ArrayList<>();
    private FoodGalleryAdapter galleryAdapter;
    private int current = 0;

    private void getDish() {
        RequestParams params = new RequestParams(MyApp.apiurl + "getDish");
        params.addBodyParameter("mac", MyApp.mac);
        params.addBodyParameter("styleId", styleId + "");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    Logs.e("getDish "+result);
                    TGson<List<Food>> json = Utils.gson.fromJson(result,
                            new TypeToken<TGson<List<Food>>>() {
                            }.getType());
                    if (!json.getCode().equals("200")) {
                        Toast.makeText(getApplicationContext(), json.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                    list = json.getData();
                    if (list == null || list.isEmpty())
                        return;

                    galleryAdapter = new FoodGalleryAdapter(getApplicationContext(), list);
                    info_gallery.setAdapter(galleryAdapter);

                    if (!info_gallery.isFocused()) {
                        info_gallery.requestFocus();
                    }

                    info_gallery.setGravity(Gravity.CENTER);
                    current = galleryAdapter.getCount() / 2;
                    info_gallery.setSelection(current);
                    setdate();
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

    private void setdate() {
        try {
            info_title.setText(list.get(current).getName());
            info_web.setText(list.get(current).getDiscription());
            ImageUtils.display(info_img, list.get(current).getIcon());
            info_time.setText(getString(R.string.shop_time) + list.get(current).getSupply_time());
            info_price.setText(getString(R.string.shop_price) + list.get(current).getPrice());
            sale_num.setText(getString(R.string.shop_sale).replace("0", list.get(current).getSaleNum() + ""));
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
                    orderDish();
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
        String order = list.get(current).getId() + ","
                + count + ";";
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
        return R.layout.activity_food;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        add();
    }
}
