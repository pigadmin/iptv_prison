package product.prison;

import android.os.Bundle;

public interface IBaseView {
    void initView(Bundle savedInstanceState);
    void loadData();
    int getContentId();
}
