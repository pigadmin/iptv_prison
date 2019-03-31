package product.prison.model;


import java.io.Serializable;
import java.util.List;

public class Live implements Serializable {
    private List<LiveData> data;

    private int type;

    public void setData(List<LiveData> data) {
        this.data = data;
    }

    public List<LiveData> getData() {
        return this.data;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }

}
