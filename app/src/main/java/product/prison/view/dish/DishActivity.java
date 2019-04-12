package product.prison.view.dish;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import product.prison.app.MyApp;
import product.prison.model.Food;
import product.prison.model.TGson;
import product.prison.utils.ImageUtils;
import product.prison.utils.Logs;
import product.prison.utils.Utils;

public class DishActivity extends BaseActivity implements DishListAdapter.OnItemClickListener {

    private RecyclerView left_list;
    private LinearLayoutManager layoutmanager;
    private DishListAdapter listAdapter;
    private ImageView right_image;
    private Button shop_order, shop_cat;
    private MyApp app;
    private LinearLayout right_l;

    @Override
    public void initView(Bundle savedInstanceState) {
        app = (MyApp) getApplication();
        left_list = f(R.id.left_list);
        right_image = f(R.id.right_image);
        right_l= f(R.id.right_l);

        layoutmanager = new LinearLayoutManager(this);
        layoutmanager.setOrientation(LinearLayoutManager.VERTICAL);
        left_list.setLayoutManager(layoutmanager);

        shop_order = f(R.id.shop_order);
        shop_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), OrderActivity.class));
            }
        });
        shop_cat = f(R.id.shop_cat);
        shop_cat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CartActivity.class));
            }
        });
        right_l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add();
            }
        });
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
                    Logs.e(result);
                    TGson<List<Food>> json = Utils.gson.fromJson(result,
                            new TypeToken<TGson<List<Food>>>() {
                            }.getType());
                    if (!json.getCode().equals("200")) {
                        Toast.makeText(getApplicationContext(), json.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                    list = json.getData();
                    if (list == null || list.isEmpty())
                        return;


                    listAdapter = new DishListAdapter(getApplicationContext(), list);
                    left_list.setAdapter(listAdapter);
                    listAdapter.setOnItemClickListener(DishActivity.this);

                    current = 0;
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
            String url = list.get(current).getIcon();
            ImageUtils.display(right_image, url);
//            info_title.setText(list.get(current).getName());
//            info_web.setText(list.get(current).getDiscription());
//            ImageUtils.display(info_img, list.get(current).getIcon());
//            info_time.setText(getString(R.string.shop_time) + list.get(current).getSupply_time());
//            info_price.setText(getString(R.string.shop_price) + list.get(current).getPrice());
//            sale_num.setText(getString(R.string.shop_sale).replace("0", list.get(current).getSaleNum() + ""));
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
                    addcat(count);
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

    private void addcat(int c) {
        try {
            if (app.getShopCats().isEmpty()) {
                addnew(c);
            } else {
                int re = -1;
                for (int i = 0; i < app.getShopCats().size(); i++) {
                    if (list.get(current).getId() == app.getShopCats().get(i).getId())
                        re = i;
                }
                if (re == -1) {
                    addnew(c);
                } else {
                    int tmp = app.getShopCats().get(re).getCount() + c;
                    app.getShopCats().get(re).setCount(tmp);
                }
            }
            Toast.makeText(this, getString(R.string.shop_add_cart_scuess), Toast.LENGTH_SHORT).show();
            add_dialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addnew(int c) {
        Food cat = list.get(current);
        cat.setCount(c);
        app.getShopCats().add(cat);
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
        return R.layout.activity_dish;
    }


    @Override
    public void onItemClick(View view, int position) {
        current = position;
        setdate();

    }
}
