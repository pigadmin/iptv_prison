package product.prison.model;

public class MsgData {
    private long endtime;

    private String content;

    private int id;

    private String title;

    private long starttime;

    private int type;

    private int status;

    public void setEndtime(long endtime) {
        this.endtime = endtime;
    }

    public long getEndtime() {
        return this.endtime;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return this.content;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public void setStarttime(long starttime) {
        this.starttime = starttime;
    }

    public long getStarttime() {
        return this.starttime;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return this.status;
    }


    private String fontColor;


    private long rollSpeed;


    private int fontSize;

    public void setFontColor(String fontColor) {
        this.fontColor = fontColor;
    }

    public String getFontColor() {
        return this.fontColor;
    }

    public void setRollSpeed(long rollSpeed) {
        this.rollSpeed = rollSpeed;
    }

    public long getRollSpeed() {
        return this.rollSpeed;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public int getFontSize() {
        return this.fontSize;
    }

}