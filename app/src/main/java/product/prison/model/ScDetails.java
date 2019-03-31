package product.prison.model;

import java.io.Serializable;

public class ScDetails implements Serializable {
    private String path;

    private int id;

    private int materialId;

    public void setPath(String path){
        this.path = path;
    }
    public String getPath(){
        return this.path;
    }
    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return this.id;
    }
    public void setMaterialId(int materialId){
        this.materialId = materialId;
    }
    public int getMaterialId(){
        return this.materialId;
    }

}
