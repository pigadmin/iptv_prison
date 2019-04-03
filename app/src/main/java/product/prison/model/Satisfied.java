package product.prison.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhu on 2017/10/23.
 */

public class Satisfied implements Serializable {
    private int id;

    private String name;

    private long createDate;

    private List<SatisfiedDetails> details;

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

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public long getCreateDate() {
        return this.createDate;
    }

    public void setDetails(List<SatisfiedDetails> details) {
        this.details = details;
    }

    public List<SatisfiedDetails> getDetails() {
        return this.details;
    }
}
