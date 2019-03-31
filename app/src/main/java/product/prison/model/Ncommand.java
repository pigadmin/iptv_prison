package product.prison.model;

public class Ncommand {
private int id;

private int command;

private int type;

private Servermessage servermessage;

public void setId(int id){
this.id = id;
}
public int getId(){
return this.id;
}
public void setCommand(int command){
this.command = command;
}
public int getCommand(){
return this.command;
}
public void setType(int type){
this.type = type;
}
public int getType(){
return this.type;
}
public void setServermessage(Servermessage servermessage){
this.servermessage = servermessage;
}
public Servermessage getServermessage(){
return this.servermessage;
}

}