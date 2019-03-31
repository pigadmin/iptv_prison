package product.prison.model;

import java.io.Serializable;
import java.util.List;

public class Material implements Serializable {
    private int id;

    private String name;

    private String path;

    private int size;

    private int fileType;

    private List<ScDetails> details;

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

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return this.path;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getSize() {
        return this.size;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }

    public int getFileType() {
        return this.fileType;
    }

    public void setDetails(List<ScDetails> details) {
        this.details = details;
    }

    public List<ScDetails> getDetails() {
        return this.details;
    }

}
