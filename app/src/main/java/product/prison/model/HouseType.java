package product.prison.model;

public class HouseType {
private int id;

private String name;

private double price;

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
public void setPrice(double price){
this.price = price;
}
public double getPrice(){
return this.price;
}

}