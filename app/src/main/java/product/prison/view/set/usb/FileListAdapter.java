package product.prison.view.set.usb;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.io.File;
import java.util.List;

import product.prison.R;

/* 锟皆讹拷锟斤拷锟紸dapter锟斤拷锟教筹拷android.widget.BaseAdapter */
public class FileListAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private Bitmap mIcon_folder;
	private Bitmap mIcon_file;
	private Bitmap mIcon_image;
	private Bitmap mIcon_audio;
	private Bitmap mIcon_video;
	private Bitmap mIcon_apk;
	private List<String> items;
	private List<String> paths;
	private List<String> sizes;
	private int isZoom = 0;

	/* MyAdapter锟侥癸拷锟斤拷锟斤拷 */
	public FileListAdapter(Context context, List<String> it, List<String> pa,
                           List<String> si, int zm) {

		mInflater = LayoutInflater.from(context);
		items = it;
		paths = pa;
		sizes = si;
		isZoom = zm;
		mIcon_folder = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.usb_5bg); // 锟侥硷拷锟叫碉拷图锟侥硷拷
		mIcon_file = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.usb_4bg); // 锟侥硷拷锟斤拷图锟侥硷拷
		mIcon_image = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.usb_3bg); // 图片锟斤拷图锟侥硷拷
		mIcon_audio = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.usb_2bg); // 锟斤拷频锟斤拷图锟侥硷拷
		mIcon_video = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.usb_1bg); // 锟斤拷频锟斤拷图锟侥硷拷
		mIcon_apk = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.usb_5bg); // apk锟侥硷拷
	}

	/* 锟斤拷坛锟紹aseAdapter锟斤拷锟斤拷锟斤拷写锟斤拷锟铰凤拷锟斤拷 */
	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup par) {
		Bitmap bitMap = null;
		ViewHolder holder = null;
		if (convertView == null) {
			/* 使锟斤拷锟皆讹拷锟斤拷锟絣ist_items锟斤拷为Layout */
			convertView = mInflater.inflate(R.layout.usb_list_item, null);
			/* 锟斤拷始锟斤拷holder锟斤拷text锟斤拷icon */
			holder = new ViewHolder();
			holder.f_title = ((TextView) convertView.findViewById(R.id.f_title));
			holder.f_text = ((TextView) convertView.findViewById(R.id.f_text));
			holder.f_icon = ((ImageView) convertView.findViewById(R.id.f_icon));
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		File f = new File(paths.get(position).toString());
		/* 锟斤拷锟斤拷锟侥硷拷锟斤拷锟侥硷拷锟叫碉拷锟斤拷锟斤拷锟斤拷icon */
		holder.f_title.setText(f.getName());
		String f_type = MyUtil.getMIMEType(f, false);
		if (f.isDirectory()) {
			holder.f_icon.setImageBitmap(mIcon_folder);
			holder.f_text.setText("");
		} else {
			holder.f_text.setText(sizes.get(position));
			if ("image".equals(f_type)) {
				if (isZoom == 1) {
					bitMap = MyUtil.fitSizePic(f);
					if (bitMap != null) {
						holder.f_icon.setImageBitmap(bitMap);
					} else {
						holder.f_icon.setImageBitmap(mIcon_image);
					}
				} else {
					holder.f_icon.setImageBitmap(mIcon_image);
				}
				bitMap = null;
			} else if ("audio".equals(f_type)) {
				holder.f_icon.setImageBitmap(mIcon_audio);
			} else if ("video".equals(f_type)) {
				holder.f_icon.setImageBitmap(mIcon_video);
			} else if ("apk".equals(f_type)) {
				holder.f_icon.setImageBitmap(mIcon_apk);
			} else {
				holder.f_icon.setImageBitmap(mIcon_file);
			}
		}
		return convertView;
	}

	/**
	 * 锟斤拷锟斤拷锟斤拷写get set锟斤拷锟斤拷锟斤拷锟叫э拷锟? class ViewHolder
	 * */
	private class ViewHolder {
		TextView f_title;
		TextView f_text;
		ImageView f_icon;
	}
}