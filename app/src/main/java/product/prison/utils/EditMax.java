package product.prison.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

/**
 * Created by zhu on 2017/10/12.
 */

public class EditMax {

    public static void set(final EditText edit, final int min, final int max) {

        edit.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                if (s.toString() != null && !s.toString().equals("")) {
                    if (min != -1 && max != -1) {//最大值和最小值自设
                        int a = 0;
                        try {
                            a = Integer.parseInt(s.toString());
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            a = 0;
                        }
                        if (a > max) {
                            edit.setText(max + "");
                        }
                        if (a < min) {
                            edit.setText(min + "");
                        }
                        return;
                    }
                }
            }
        });
    }
}
