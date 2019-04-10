package product.prison.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import product.prison.R;
import product.prison.model.Food;
import product.prison.model.OrderData;
import product.prison.utils.LtoDate;

public class ShopOrderAdapter extends BaseAdapter {
    private Context context;
    private List<OrderData> list;

    public ShopOrderAdapter(Context context, List<OrderData> lists) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.list = lists;
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
    private TextView shop_order_no, shop_order_name, shop_num,
            order_price, sendif;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        view = LayoutInflater.from(context).inflate(
                R.layout.adapter_shop_order, null);

        shop_order_no = view.findViewById(R.id.shop_order_no);
        shop_order_name = view.findViewById(R.id.shop_order_name);
        shop_num = view.findViewById(R.id.shop_num);
        order_price = view.findViewById(R.id.order_price);
        sendif = view.findViewById(R.id.sendif);
        try {
            shop_order_no.setText(LtoDate.Hm(list.get(position).getOrderTime()));
            shop_order_name.setText(list.get(position).getDish().getName());
            shop_num.setText(list.get(position).getOrderNum() + "");
            order_price.setText("￥" + (list.get(position).getDish().getPrice() * list.get(position).getOrderNum()));
            if (list.get(position).getSend_if() == 0) {
                sendif.setText("提交中");
            } else if (list.get(position).getSend_if() == 1) {
                sendif.setText("已完成");
            }

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        return view;
    }

}
