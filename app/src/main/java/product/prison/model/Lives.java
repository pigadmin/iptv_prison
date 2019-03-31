package product.prison.model;

import java.io.Serializable;

/**
 * Created by zhu on 2017/9/18.
 */

public class Lives implements Serializable {
    private int id;

    private String name;

    private String freq;

    private int position;

    private int zhishi;

    private int radiozhishi;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setFreq(String freq) {
        this.freq = freq;
    }

    public String getFreq() {
        return this.freq;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return this.position;
    }

    public void setZhishi(int zhishi) {
        this.zhishi = zhishi;
    }

    public int getZhishi() {
        return this.zhishi;
    }

    public void setRadiozhishi(int radiozhishi) {
        this.radiozhishi = radiozhishi;
    }

    public int getRadiozhishi() {
        return this.radiozhishi;
    }

}
