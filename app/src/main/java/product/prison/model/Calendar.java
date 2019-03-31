package product.prison.model;

import java.io.Serializable;

public class Calendar implements Serializable {
    private int id;

    private String name;

    private int calendarId;

    private int programListId;

    private String time_begin;

    private String time_end;

    private String programURL;

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

    public void setCalendarId(int calendarId) {
        this.calendarId = calendarId;
    }

    public int getCalendarId() {
        return this.calendarId;
    }

    public void setProgramListId(int programListId) {
        this.programListId = programListId;
    }

    public int getProgramListId() {
        return this.programListId;
    }

    public void setTime_begin(String time_begin) {
        this.time_begin = time_begin;
    }

    public String getTime_begin() {
        return this.time_begin;
    }

    public void setTime_end(String time_end) {
        this.time_end = time_end;
    }

    public String getTime_end() {
        return this.time_end;
    }

    public void setProgramURL(String programURL) {
        this.programURL = programURL;
    }

    public String getProgramURL() {
        return this.programURL;
    }

}
