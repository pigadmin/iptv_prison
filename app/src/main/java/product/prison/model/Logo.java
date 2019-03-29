package product.prison.model;

public class Logo {
private int id;

private String logoPath;

private String bgPath;

private int type;

public void setId(int id){
this.id = id;
}
public int getId(){
return this.id;
}
public void setLogoPath(String logoPath){
this.logoPath = logoPath;
}
public String getLogoPath(){
return this.logoPath;
}
public void setBgPath(String bgPath){
this.bgPath = bgPath;
}
public String getBgPath(){
return this.bgPath;
}
public void setType(int type){
this.type = type;
}
public int getType(){
return this.type;
}

}