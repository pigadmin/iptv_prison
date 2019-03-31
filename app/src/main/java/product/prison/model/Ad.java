package product.prison.model;

import java.util.List;

public class Ad {
    private int id;

    private String name;

    private String inter;

    private List<Details> details ;

    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return this.id;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public void setInter(String inter){
        this.inter = inter;
    }
    public String getInter(){
        return this.inter;
    }
    public void setDetails(List<Details> details){
        this.details = details;
    }
    public List<Details> getDetails(){
        return this.details;
    }

}
