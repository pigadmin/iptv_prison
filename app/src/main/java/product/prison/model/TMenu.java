package product.prison.model;

import java.util.List;

public class TMenu {
    private int position;


    private int id;


    private String icon;


    private int templateType;


    private int status;


    private List<Subs> subs;


    private String name;


    private int type;


    public void setPosition(int position) {

        this.position = position;

    }

    public int getPosition() {

        return this.position;

    }

    public void setId(int id) {

        this.id = id;

    }

    public int getId() {

        return this.id;

    }

    public void setIcon(String icon) {

        this.icon = icon;

    }

    public String getIcon() {

        return this.icon;

    }

    public void setTemplateType(int templateType) {

        this.templateType = templateType;

    }

    public int getTemplateType() {

        return this.templateType;

    }

    public void setStatus(int status) {

        this.status = status;

    }

    public int getStatus() {

        return this.status;

    }

    public void setSubs(List<Subs> subs) {

        this.subs = subs;

    }

    public List<Subs> getSubs() {

        return this.subs;

    }

    public void setName(String name) {

        this.name = name;

    }

    public String getName() {

        return this.name;

    }

    public void setType(int type) {

        this.type = type;

    }

    public int getType() {

        return this.type;

    }
}
