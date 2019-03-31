package product.prison.model;

public class Servermessage {
private int sioPort;

private String serverip;

private int serverPort;

public void setSioPort(int sioPort){
this.sioPort = sioPort;
}
public int getSioPort(){
return this.sioPort;
}
public void setServerip(String serverip){
this.serverip = serverip;
}
public String getServerip(){
return this.serverip;
}
public void setServerPort(int serverPort){
this.serverPort = serverPort;
}
public int getServerPort(){
return this.serverPort;
}

}