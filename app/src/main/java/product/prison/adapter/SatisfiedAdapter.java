package product.prison.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import product.prison.R;
import product.prison.app.MyApp;
import product.prison.model.QuetionVo;
import product.prison.model.Satisfied;
import product.prison.model.SatisfiedDetails;
import product.prison.model.TGson;
import product.prison.utils.EditMax;
import product.prison.utils.Logs;
import product.prison.utils.Utils;

public class SatisfiedAdapter extends BaseAdapter {
    private Context context;
    private List<SatisfiedDetails> list = new ArrayList<SatisfiedDetails>();

    public SatisfiedAdapter(Context context, List<SatisfiedDetails> list) {
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

    public void post(int id) {
        // TODO Auto-generated method stub
        exam_answer(id);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    private TextView satisfied_title;
    private View view;
    private EditText satisfied_no;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        view = LayoutInflater.from(context).inflate(R.layout.adapter_satisfied,
                null);

        satisfied_title = view.findViewById(R.id.satisfied_title);
        satisfied_no = view.findViewById(R.id.satisfied_no);
        EditMax.set(satisfied_no, 1, 3);
        try {
            satisfied_title.setText((position + 1) + "、" + list.get(position).getName());
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        satisfied_no.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // 将editText中改变的值设置的HashMap中
                try {
//                    if (Integer.parseInt(s.toString()) > 3 || s.toString().equals("")) {
//                        hashMap.put(position, 1 + "");
//                    } else {
                    hashMap.put(position, s.toString());
//                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // // 如果hashMap不为空，就设置的editText
        // if (hashMap.get(position) != null) {
        // satisfied_no.setText(hashMap.get(position));
        // }

        return view;
    }

    HashMap<Integer, String> hashMap = new HashMap<Integer, String>();


    private void exam_answer(int id) {
        String answer = "";

        List<QuetionVo> quetionVos = new ArrayList<>();
        for (int i = 0; i < hashMap.size(); i++) {

            QuetionVo quetionVo = new QuetionVo();
            quetionVo.setQuestionId(list.get(i).getId() + "");
            quetionVo.setAnswer(hashMap.get(i));
            // answer += list.get(i).getId() + ":" + hashMap.get(i) + ",";
            //  answer += hashMap.get(i) + ",";
            quetionVos.add(quetionVo);
        }
        answer = Utils.gson.toJson(quetionVos);
        Logs.e(answer);
        RequestParams params = new RequestParams(MyApp.apiurl + "exam_answer");
        params.addBodyParameter("mac", MyApp.mac);
        params.addBodyParameter("answer", answer);
        params.addBodyParameter("id", id + "");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    Logs.e("exam_answer "+result);
                    TGson<String> json = Utils.gson.fromJson(result,
                            new TypeToken<TGson<String>>() {
                            }.getType());
                    if (!json.getCode().equals("200")) {
                        Toast.makeText(context, json.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                    Toast.makeText(context, json.getMsg(), Toast.LENGTH_SHORT).show();
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


}
