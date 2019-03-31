package product.prison.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhu on 2017/9/18.
 */

public class Sources implements Serializable {

    private int id;

    private int typeId;

    private String name;

    private String act;

    private String des;

    private String icon;

    private long createDate;

    private String vv;


    private int fileType;

    private int types;

    private double cost;

    private List<VodDetails> details;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getTypeId() {
        return this.typeId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setAct(String act) {
        this.act = act;
    }

    public String getAct() {
        return this.act;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getDes() {
        return this.des;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIcon() {
        return this.icon;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public long getCreateDate() {
        return this.createDate;
    }

    public void setVv(String vv) {
        this.vv = vv;
    }

    public String getVv() {
        return this.vv;
    }


    public void setFileType(int fileType) {
        this.fileType = fileType;
    }

    public int getFileType() {
        return this.fileType;
    }

    public void setTypes(int types) {
        this.types = types;
    }

    public int getTypes() {
        return this.types;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getCost() {
        return this.cost;
    }

    public void setDetails(List<VodDetails> details) {
        this.details = details;
    }

    public List<VodDetails> getDetails() {
        return this.details;
    }


}
