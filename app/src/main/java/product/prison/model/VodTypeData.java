package product.prison.model;

import java.io.Serializable;

public class VodTypeData implements Serializable {
private int id;

private String name;

private String icon;

private Sources sources;

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
public void setIcon(String icon){
this.icon = icon;
}
public String getIcon(){
return this.icon;
}
public void setSources(Sources sources){
this.sources = sources;
}
public Sources getSources(){
return this.sources;
}

}