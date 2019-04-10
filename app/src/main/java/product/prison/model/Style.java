package product.prison.model;

import java.io.Serializable;

/**
 * Created by zhu on 2017/10/11.
 */

public class Style implements Serializable {
    private int id;

    private String name;

    private String icon;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIcon() {
        return this.icon;
    }

}
