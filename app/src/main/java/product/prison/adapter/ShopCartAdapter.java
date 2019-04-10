package product.prison.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import product.prison.R;
import product.prison.model.Food;

public class ShopCartAdapter extends BaseAdapter {
    private Context context;
    private List<Food> list;

    public ShopCartAdapter(Context context, List<Food> list) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.list = list;

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    private View view;
    private TextView shop_car_no, shop_car_name, shop_num, shop_car_price, shop_all_price;
    private ImageButton shop_jia, shop_jian;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        final int pos = position;

        view = LayoutInflater.from(context).inflate(R.layout.adapter_shop_cart,
                null);

        shop_car_no = view.findViewById(R.id.shop_car_no);
        shop_car_name = view.findViewById(R.id.shop_car_name);
        shop_num = view.findViewById(R.id.shop_num);

        shop_car_price = view.findViewById(R.id.shop_car_price);
        shop_all_price = view.findViewById(R.id.shop_all_price);

        shop_jia = view.findViewById(R.id.shop_jia);
        shop_jian = view.findViewById(R.id.shop_jian);

        try {
            shop_car_no.setText((position + 1) + "");
            shop_car_name.setText(list.get(position).getName());
            shop_num.setText("" + list.get(position).getCount());
            shop_car_price.setText("" + list.get(position).getPrice());
            shop_all_price.setText("" + (list.get(position).getPrice() * list.get(position).getCount()));
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            System.out.println(e.toString());
        }

        shop_jia.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                try {
                    int count = list.get(pos).getCount();
                    count++;
                    list.get(pos).setCount(count);
                    updateview(pos);
                } catch (Exception e) {
                    // TODO: handle exception
                }

            }
        });
        shop_jian.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                try {
                    int count = list.get(pos).getCount();
                    count++;
                    list.get(pos).setCount(count);
                    updateview(pos);
                } catch (Exception e) {
                    // TODO: handle exception
                }

            }
        });
        return view;
    }

    private void updateview(int pos) {
        // TODO Auto-generated method stub

        // System.out.println(pos);

        int visiblePosition = cart_list.getFirstVisiblePosition();

        View views = cart_list.getChildAt(pos - visiblePosition);

        final Food dishs = list.get(pos);

        shop_num = views.findViewById(R.id.shop_num);

        shop_num.setText(dishs.getCount());

        list.set(pos, dishs);

    }

    ListView cart_list;

    public void setListview(ListView cart_list) {
        // TODO Auto-generated method stub
        this.cart_list = cart_list;
    }
}
