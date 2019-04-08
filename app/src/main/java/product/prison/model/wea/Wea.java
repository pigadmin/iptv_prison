package product.prison.model.wea;

import java.io.Serializable;
import java.util.List;

public class Wea implements Serializable {

    private String city;

    private List<WeaData> data;


    public String getCity() {
        return this.city;
    }


    public List<WeaData> getData() {
        return this.data;
    }


}
