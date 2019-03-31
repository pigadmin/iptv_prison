package product.prison.model;

import java.io.Serializable;

public class MsgSgLives implements Serializable {
    private int id;

    private int mid;

    private Singles livesingle;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public int getMid() {
        return this.mid;
    }

    public void setLivesingle(Singles livesingle) {
        this.livesingle = livesingle;
    }

    public Singles getLivesingle() {
        return this.livesingle;
    }


}