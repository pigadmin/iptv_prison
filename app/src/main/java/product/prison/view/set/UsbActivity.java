package product.prison.view.set;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import product.prison.BaseActivity;
import product.prison.R;
import product.prison.adapter.CommonListAdapter;
import product.prison.view.set.usb.FileListAdapter;
import product.prison.view.set.usb.MyUtil;

/**
 * Created by zhu on 2017/9/26.
 */

public class UsbActivity extends BaseActivity implements CommonListAdapter.OnItemClickListener {
    private int filestype = 0;
    // private String rootPath = "/mnt/external_sd/";
    // private String rootPath = "/mnt/usb_storage/";
    private String rootPath = "/mnt/usbhost/Storage01/";
    private List<String> items = new ArrayList<String>();
    private List<String> paths = new ArrayList<String>();
    private List<String> sizes = new ArrayList<String>();

    private RecyclerView left_list;
    private GridView right_grid;
    private LinearLayoutManager layoutmanager;
    private CommonListAdapter listAdapter;
    private String[] list = null;
    private String currentpath;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            if (!rootPath.equals("")) {
                loadData();
            }
            rootPath = "";

            super.handleMessage(msg);
        }
    };

    private void openfiles(File file) {
        // TODO Auto-generated method stub

        if (file.isDirectory()) {
            getFileDir(file.getPath(), filestype);
        } else {
            openFile(file);
        }
    }

    private void openFile(File f) {
        try {
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Intent.ACTION_VIEW);
            String type = "*/*";
            type = MyUtil.getMIMEType(f, true);
            intent.setDataAndType(Uri.fromFile(f), type);
            startActivity(intent);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    private void clean() {
        // TODO Auto-generated method stub
        if (!items.isEmpty()) {
            items.clear();
        }
        if (!paths.isEmpty()) {
            paths.clear();
        }
        if (!sizes.isEmpty()) {
            sizes.clear();
        }
    }

    private void getFileDir(String filePath, int type) {
        try {
            currentpath = filePath;
            File f = new File(filePath);
            File[] files = f.listFiles();

            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isDirectory()) {
                        try {
                            getFileDir(files[i].getAbsolutePath(), type);
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                        }

                    }
                }

                for (int i = 0; i < files.length; i++) {

                    if (files[i].isFile()) {
                        String fName = files[i].getName();
                        String end = fName.substring(
                                fName.lastIndexOf(".") + 1, fName.length())
                                .toLowerCase();
                        switch (type) {
                            case 0:
                                if (end.equals("3gp") || end.equals("mp4")) {
                                    items.add(files[i].getName());
                                    paths.add(files[i].getPath());
                                    sizes.add(MyUtil.fileSizeMsg(files[i]));
                                }
                                break;
                            case 1:
                                if (end.equals("m4a") || end.equals("mp3")
                                        || end.equals("mid") || end.equals("xmf")
                                        || end.equals("ogg") || end.equals("wav")) {
                                    items.add(files[i].getName());
                                    paths.add(files[i].getPath());
                                    sizes.add(MyUtil.fileSizeMsg(files[i]));
                                }
                                break;
                            case 2:
                                if (end.equals("jpg") || end.equals("gif")
                                        || end.equals("png") || end.equals("jpeg")
                                        || end.equals("bmp")) {
                                    items.add(files[i].getName());
                                    paths.add(files[i].getPath());
                                    sizes.add(MyUtil.fileSizeMsg(files[i]));
                                }
                                break;
                            case 3:
                                if (end.equals("txt") || end.equals("text")) {
                                    items.add(files[i].getName());
                                    paths.add(files[i].getPath());
                                    sizes.add(MyUtil.fileSizeMsg(files[i]));
                                }
                                break;
                            case 4:
                                if (!end.equals("3gp") && !end.equals("mp4")
                                        && !end.equals("m4a") && !end.equals("mp3")
                                        && !end.equals("mid") && !end.equals("xmf")
                                        && !end.equals("ogg") && !end.equals("wav")
                                        && !end.equals("jpg") && !end.equals("gif")
                                        && !end.equals("png")
                                        && !end.equals("jpeg")
                                        && !end.equals("bmp") && !end.equals("txt")
                                        && !end.equals("text")) {

                                    items.add(files[i].getName());
                                    paths.add(files[i].getPath());
                                    sizes.add(MyUtil.fileSizeMsg(files[i]));
                                }
                                break;

                            default:
                                break;
                        }

                    }
                }
            } else {
                rootPath = "/mnt/usbhost/Storage02/";
                handler.sendEmptyMessage(0);
            }
        } catch (Exception e) {
            // TODO: handle exception

        }

    }


    @Override
    public void initView(Bundle savedInstanceState) {
        left_list = f(R.id.left_list);
        right_grid = f(R.id.right_grid);

        layoutmanager = new LinearLayoutManager(this);
        layoutmanager.setOrientation(LinearLayoutManager.VERTICAL);
        left_list.setLayoutManager(layoutmanager);

        list = new String[]{getString(R.string.usb_1), getString(R.string.usb_2), getString(R.string.usb_3),
                getString(R.string.usb_4), getString(R.string.usb_5)};

        right_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                File file = new File(paths.get(position));
                // fileOrDirHandle(file, "short");
                openfiles(file);
            }

        });
    }

    @Override
    public void loadData() {
        try {

            listAdapter = new CommonListAdapter(getApplicationContext(), list);
            left_list.setAdapter(listAdapter);
            listAdapter.setOnItemClickListener(UsbActivity.this);

            getFileDir(rootPath, filestype);
            right_grid.setAdapter(new FileListAdapter(this, items,
                    paths, sizes, 2));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getContentId() {
        return R.layout.activity_video;
    }

    @Override
    public void onItemClick(View view, int position) {
        filestype = position;
        clean();
        loadData();
    }
}
