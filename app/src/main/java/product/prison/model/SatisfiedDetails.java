package product.prison.model;

/**
 * Created by zhu on 2017/10/23.
 */

public class SatisfiedDetails {
    private int id;

    private int satisfy_id;

    private String name;

    private int man;

    private int jiben;

    private int bumanyi;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setSatisfy_id(int satisfy_id) {
        this.satisfy_id = satisfy_id;
    }

    public int getSatisfy_id() {
        return this.satisfy_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setMan(int man) {
        this.man = man;
    }

    public int getMan() {
        return this.man;
    }

    public void setJiben(int jiben) {
        this.jiben = jiben;
    }

    public int getJiben() {
        return this.jiben;
    }

    public void setBumanyi(int bumanyi) {
        this.bumanyi = bumanyi;
    }

    public int getBumanyi() {
        return this.bumanyi;
    }
}
