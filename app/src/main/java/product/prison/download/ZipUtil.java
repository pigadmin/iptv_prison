package product.prison.download;

import android.content.Context;
import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


public class ZipUtil {

    public static final String status = Environment.getExternalStorageState();
    public static final String basePath = Environment
            .getExternalStorageDirectory().getPath();

    // 素材文件夹
//	public static final String mtrial_file = basePath + File.separator
//			+ projectname + File.separator + "material";
//	public static final String program_file = basePath + File.separator
//			+ projectname + File.separator + "program";
//	public static String rs_file = basePath + File.separator + projectname
//			+ File.separator + "rs";

    public static final String zip_file = basePath + File.separator
            + "prison" + File.separator + "zip";

    public static final String main_file = basePath + File.separator
            + "prison" + File.separator + "main";

    public static String res_file = basePath + File.separator + "prison"
            + File.separator + "res";


    // 主描述文件
    public static String readMainFile(String fname) {
        String path = main_file + File.separator + fname + File.separator
                + "main.json";
        File file = new File(path);
        // System.out.println(path+"***"+file.exists());
        if (file.exists()) {
            try {
                FileReader fileRead = new FileReader(file);
                BufferedReader buf = new BufferedReader(fileRead);
                String line;
                StringBuffer strBuff = new StringBuffer();
                while ((line = buf.readLine()) != null) {
                    strBuff.append(line);
                }
                buf.close();
                return strBuff.toString();
            } catch (Exception e) {

            }
        }
        return null;
    }

    // 返回素材列表
    public static Map<String, String> materailList(String fname) {

        // String path = rs_file + File.separator + fname;
        String path = res_file;

        File file = new File(path);

        if (file.exists()) {
            String[] strs = file.list();
            if (strs != null && strs.length > 0) {
                HashMap<String, String> file_path_map = new HashMap<String, String>();
                for (int i = 0; i < strs.length; i++) {
                    String st = strs[i];
                    file_path_map.put(st, path + File.separator + st);
                    // System.out
                    // .println("@@@@@@@@@" + path + File.separator + st);
                }
                return file_path_map;
            }
        } else {
            // 素材文件夹不存在
        }

        return Collections.EMPTY_MAP;
    }

    // 返回最新文件夹名称
    public static String gainNewestFileName() {
        try {
            File file = new File(main_file);
            // System.out.println(program_file+"---"+file.exists());
            if (file.exists()) {
                String[] sa = file.list();
//                if (sa != null && sa.length > 0) {
//                    List<Long> files = new ArrayList<Long>();
//                    for (int i = 0; i < sa.length; i++) {
//                        files.add(Long.valueOf(sa[i]));
//                    }
//                    Collections.sort(files);
//                    return files.get(files.size() - 1) + "";
//                }
                return sa[0];
            } else {

            }
            return null; // 播放本地内容（******需要处理******）
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return null;
    }

    // 返回最新文件夹名称
    public static String gainoldFileName() {
        try {
            File file = new File(main_file);
            // System.out.println(program_file+"---"+file.exists());
            if (file.exists()) {
                String[] sa = file.list();
                if (sa != null && sa.length > 0) {
                    List<Long> files = new ArrayList<Long>();
                    for (int i = 0; i < sa.length; i++) {
                        files.add(Long.valueOf(sa[i]));
                    }
                    Collections.sort(files);
                    return files.get(files.size() - 2) + "";
                }
            } else {

            }
            return null; // 播放本地内容（******需要处理******）
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }

    // 下载素材
    public static void download(Context context, String url, String name) {
        BPRDownloading.resdownload(context, url, zip_file,
                name, new BPRDownloading.DownloadProgressUpdater() {
                    public void downloadProgressUpdate(int percent) {
                    }
                });
    }

    public static void downloadRes(Context context, List<String> url,
                                   List<String> name) {
        // TODO Auto-generated method stub

        for (int i = 0; i < url.size(); i++) {
            // Log.v("TAG", url.get(i));

            // System.out.println(name.get(i));

            BPRDownloading.resdownload(context, url.get(i),
                    res_file, name.get(i), new BPRDownloading.DownloadProgressUpdater() {
                        public void downloadProgressUpdate(int percent) {
                        }
                    });
        }

    }

    public static void downloadvideo(Context context, List<String> url,
                                     List<String> name) {
        // TODO Auto-generated method stub

        for (int i = 0; i < url.size(); i++) {

            BPRDownloading.resdownload(context, url.get(i),
                    res_file, name.get(i), new BPRDownloading.DownloadProgressUpdater() {
                        public void downloadProgressUpdate(int percent) {
                        }
                    });
        }

    }

    // 解压
    public static void unZipFiles(String name) {

        String zip_path = "";

        // SD卡状态
        System.out.println(status.equals(Environment.MEDIA_MOUNTED));

        if (status.equals(Environment.MEDIA_MOUNTED)) {
            if (isExist(zip_file)) {

                // 素材文件夹
                createFile(main_file);
                // 具体素材
                zip_path = zip_file + File.separator + name;

                try {
                    FileInputStream input = new FileInputStream(zip_path);
                    ZipInputStream zipin = new ZipInputStream(
                            new BufferedInputStream(input));

                    ZipEntry zipEntry;
                    while ((zipEntry = zipin.getNextEntry()) != null) {
                        File file = new File(main_file + File.separator
                                + name.replace(".zip", "").trim()
                                + File.separator + zipEntry.getName());

                        if (zipEntry.isDirectory()) {
                            file.mkdirs();
                        } else {

                            File parent = file.getParentFile();
                            if (!parent.exists()) {
                                parent.mkdirs();
                            }

                            FileOutputStream fout = new FileOutputStream(file);
                            int index = 0;
                            byte[] buffer = new byte[2048];
                            while ((index = zipin.read(buffer)) != -1) {
                                fout.write(buffer, 0, index);
                            }
                            fout.close();
                        }
                    }
                    zipin.closeEntry();
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            } else {
                // 素材为空
            }
        }
    }

    public static boolean isExist(String path) {
        File file = new File(path);
        if (file.exists()) {
            return true;
        }
        return false;
    }

    private static void createFile(String path) {

        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }

    }

}
