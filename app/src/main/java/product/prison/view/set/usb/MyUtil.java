package product.prison.view.set.usb;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;

public class MyUtil {

	public static String getMIMEType(File f, boolean isOpen) {
		String type = "";
		String fName = f.getName();
		String end = fName
				.substring(fName.lastIndexOf(".") + 1, fName.length())
				.toLowerCase();
		if (isOpen) {
			if (end.equals("m4a") || end.equals("mp3") || end.equals("mid")
					|| end.equals("xmf") || end.equals("ogg")
					|| end.equals("wav")) {
				type = "audio";
			} else if (end.equals("3gp") || end.equals("mp4")
					|| end.equals("rmvb") || end.equals("avi")) {
				type = "video";
			} else if (end.equals("jpg") || end.equals("gif")
					|| end.equals("png") || end.equals("jpeg")
					|| end.equals("bmp")) {
				type = "image";
			} else if (end.equals("txt") || end.equals("text")) {
				type = "text";
			} else {
				type = "*";
			}
			type += "/*";
		} else {
			if (end.equals("m4a") || end.equals("mp3") || end.equals("mid")
					|| end.equals("xmf") || end.equals("ogg")
					|| end.equals("wav")) {
				type = "audio";
			} else if (end.equals("3gp") || end.equals("mp4")
					|| end.equals("rmvb") || end.equals("avi")) {
				type = "video";
			} else if (end.equals("jpg") || end.equals("gif")
					|| end.equals("png") || end.equals("jpeg")
					|| end.equals("bmp")) {
				type = "image";
			} else if (end.equals("txt") || end.equals("text")) {
				type = "text";
			} else if (end.equals("apk")) {
				type = "apk";
			}
		}
		return type;
	}

	public static Bitmap fitSizePic(File f) {
		Bitmap resizeBmp = null;
		BitmapFactory.Options opts = new BitmapFactory.Options();
		if (f.length() < 20480) { // 0-20k
			opts.inSampleSize = 1;
		} else if (f.length() < 51200) { // 20-50k
			opts.inSampleSize = 2;
		} else if (f.length() < 307200) { // 50-300k
			opts.inSampleSize = 4;
		} else if (f.length() < 819200) { // 300-800k
			opts.inSampleSize = 6;
		} else if (f.length() < 1048576) { // 800-1024k
			opts.inSampleSize = 8;
		} else {
			opts.inSampleSize = 10;
		}
		resizeBmp = BitmapFactory.decodeFile(f.getPath(), opts);
		return resizeBmp;
	}

	public static String fileSizeMsg(File f) {
		int sub_index = 0;
		String show = "";
		if (f.isFile()) {
			long length = f.length();
			if (length >= 1073741824) {
				sub_index = (String.valueOf((float) length / 1073741824))
						.indexOf(".");
				show = ((float) length / 1073741824 + "000").substring(0,
						sub_index + 3) + "GB";
			} else if (length >= 1048576) {
				sub_index = (String.valueOf((float) length / 1048576))
						.indexOf(".");
				show = ((float) length / 1048576 + "000").substring(0,
						sub_index + 3) + "MB";
			} else if (length >= 1024) {
				sub_index = (String.valueOf((float) length / 1024))
						.indexOf(".");
				show = ((float) length / 1024 + "000").substring(0,
						sub_index + 3) + "KB";
			} else if (length < 1024) {
				show = String.valueOf(length) + "B";
			}
		}
		return show;
	}

	public static boolean checkDirPath(String newName) {
		boolean ret = false;
		if (newName.indexOf("\\") == -1) {
			ret = true;
		}
		return ret;
	}

	public static boolean checkFilePath(String newName) {
		boolean ret = false;
		if (newName.indexOf("\\") == -1) {
			ret = true;
		}
		return ret;
	}
}
