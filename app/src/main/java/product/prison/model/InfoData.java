package product.prison.model;

import java.io.Serializable;
import java.util.List;

public class InfoData implements Serializable {
    private int id;

    private int type;

    private String path;

    private String name;

    private String content;

    private int position;

    private int templateType;

    private List<Details> details;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return this.path;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return this.content;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return this.position;
    }

    public void setTemplateType(int templateType) {
        this.templateType = templateType;
    }

    public int getTemplateType() {
        return this.templateType;
    }

    public void setDetails(List<Details> details) {
        this.details = details;
    }

    public List<Details> getDetails() {
        return this.details;
    }

}