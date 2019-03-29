package product.prison.model;

import java.util.List;

public class LogoData {
private Logo logo;

private List<Backs> backs ;

public void setLogo(Logo logo){
this.logo = logo;
}
public Logo getLogo(){
return this.logo;
}
public void setBacks(List<Backs> backs){
this.backs = backs;
}
public List<Backs> getBacks(){
return this.backs;
}

}