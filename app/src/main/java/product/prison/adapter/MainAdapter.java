package product.prison.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.List;

import product.prison.R;
import product.prison.model.TMenu;
import product.prison.utils.ImageUtils;

/**
 * Created by zhu on 2017/9/26.
 */

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> implements View.OnClickListener {
    List<TMenu> menus;
    Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mImageView;
        TextView mTextView;
        LinearLayout menu_linearlayout;

        public ViewHolder(View v) {
            super(v);
            mImageView = v.findViewById(R.id.main_menu_icon);
            mTextView = v.findViewById(R.id.main_menu_text);
            menu_linearlayout = v.findViewById(R.id.menu_linearlayout);
            v.setOnClickListener(MainAdapter.this);
        }
    }

    public MainAdapter(Context context, List<TMenu> menus) {
        this.context = context;
        this.menus = menus;
    }

    private OnItemClickListener mOnItemClickListener = null;


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v, (int) v.getTag());
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public MainAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_main, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            holder.mTextView.setText(menus.get(position).getName());
            holder.itemView.setTag(position);
            ImageUtils.display(holder.mImageView, menus.get(position).getIcon());
            if (position % 3 == 0) {
                holder.menu_linearlayout.setBackgroundResource(R.mipmap.ic_launcher);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(480, 240);

                lp.setMargins(0, 9, 9, 0);
                holder.menu_linearlayout.setLayoutParams(lp);

            } else {
                holder.menu_linearlayout.setBackgroundResource(R.mipmap.ic_launcher);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(239, 240);
                lp.setMargins(2, 9, 9, 0);
                holder.menu_linearlayout.setLayoutParams(lp);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return menus.size();
    }
}
