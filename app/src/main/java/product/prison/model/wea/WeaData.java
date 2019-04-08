package product.prison.model.wea;

import java.io.Serializable;
import java.util.List;

public class WeaData implements Serializable {
    public String getWea() {
        return wea;
    }


    String wea;
    String tem;

    public String getTem() {
        return tem;
    }

    public void setTem(String tem) {
        this.tem = tem;
    }

    public String getTem1() {
        return tem1;
    }

    public void setTem1(String tem1) {
        this.tem1 = tem1;
    }

    public String getTem2() {
        return tem2;
    }

    public void setTem2(String tem2) {
        this.tem2 = tem2;
    }

    String tem1;
    String tem2;
}
