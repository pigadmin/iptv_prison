package product.prison.model;

import java.io.Serializable;
import java.util.List;

public class Mings implements Serializable {

    private int id;

    private String name;

    private long beginTime;

    private long endTime;

    private int type;

    private String targetAgent;

    private String msginsertSs;

    private List<MsgLives> msgLives ;
//
    private List<MsgSgLives> msgSgLives ;

    private String msgDetails;

    private int status;

    private int cpType;

    private String cpTime;

    private int used;

    private int sourceType;

    private List<Sources> sources ;

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
    public void setBeginTime(long beginTime){
        this.beginTime = beginTime;
    }
    public long getBeginTime(){
        return this.beginTime;
    }
    public void setEndTime(long endTime){
        this.endTime = endTime;
    }
    public long getEndTime(){
        return this.endTime;
    }
    public void setType(int type){
        this.type = type;
    }
    public int getType(){
        return this.type;
    }
    public void setTargetAgent(String targetAgent){
        this.targetAgent = targetAgent;
    }
    public String getTargetAgent(){
        return this.targetAgent;
    }
    public void setMsginsertSs(String msginsertSs){
        this.msginsertSs = msginsertSs;
    }
    public String getMsginsertSs(){
        return this.msginsertSs;
    }
    public void setMsgLives(List<MsgLives> msgLives){
        this.msgLives = msgLives;
    }
    public List<MsgLives> getMsgLives(){
        return this.msgLives;
    }
    public void setMsgSgLives(List<MsgSgLives> msgSgLives){
        this.msgSgLives = msgSgLives;
    }
    public List<MsgSgLives> getMsgSgLives(){
        return this.msgSgLives;
    }
    public void setMsgDetails(String msgDetails){
        this.msgDetails = msgDetails;
    }
    public String getMsgDetails(){
        return this.msgDetails;
    }
    public void setStatus(int status){
        this.status = status;
    }
    public int getStatus(){
        return this.status;
    }
    public void setCpType(int cpType){
        this.cpType = cpType;
    }
    public int getCpType(){
        return this.cpType;
    }
    public void setCpTime(String cpTime){
        this.cpTime = cpTime;
    }
    public String getCpTime(){
        return this.cpTime;
    }
    public void setUsed(int used){
        this.used = used;
    }
    public int getUsed(){
        return this.used;
    }
    public void setSourceType(int sourceType){
        this.sourceType = sourceType;
    }
    public int getSourceType(){
        return this.sourceType;
    }
    public void setSources(List<Sources> sources){
        this.sources = sources;
    }
    public List<Sources> getSources(){
        return this.sources;
    }
}
