package product.prison.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import product.prison.R;
import product.prison.model.Food;
import product.prison.utils.ImageUtils;


public class FoodGalleryAdapter extends BaseAdapter {
    private Context context;
    private List<Food> list = new ArrayList<>();

    public FoodGalleryAdapter(Context context, List<Food> list) {
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.adapter_gallery, null);
            holder.gallery_bg = convertView
                    .findViewById(R.id.gallery_bg);
            holder.gallery_title = convertView
                    .findViewById(R.id.gallery_title);
            holder.gallery_left = convertView
                    .findViewById(R.id.gallery_left);
            holder.gallery_right = convertView
                    .findViewById(R.id.gallery_right);

            FrameLayout.LayoutParams layoutParams;
            FrameLayout.LayoutParams layoutParams2;
            if (selectItem == position) {
                layoutParams =
                        new FrameLayout.LayoutParams(142, 107);

                if (list.size() > 1) {
                    holder.gallery_left.setVisibility(View.VISIBLE);
                    holder.gallery_right.setVisibility(View.VISIBLE);
                }
                holder.gallery_title.setVisibility(View.GONE);
            } else {
                layoutParams =
                        new FrameLayout.LayoutParams(118, 78);

            }
            layoutParams.gravity = Gravity.CENTER;
            holder.gallery_bg.setLayoutParams(layoutParams);

            holder.gallery_title.setText(list.get(position).getName());
            ImageUtils.display(holder.gallery_bg, list.get(position).getIcon());


        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        return convertView;

    }

    int selectItem;

    public void setSelectItem(int selectItem) {
        this.selectItem = selectItem;
    }

    public class ViewHolder {
        private ImageView gallery_bg;
        private TextView gallery_title;
        private ImageView gallery_left, gallery_right;

    }

}
