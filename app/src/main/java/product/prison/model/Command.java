package product.prison.model;

import java.io.Serializable;

public class Command implements Serializable {
    private int command;

    private Play play;

    public void setCommand(int command){
        this.command = command;
    }
    public int getCommand(){
        return this.command;
    }
    public void setPlay(Play play){
        this.play = play;
    }
    public Play getPlay(){
        return this.play;
    }

}
