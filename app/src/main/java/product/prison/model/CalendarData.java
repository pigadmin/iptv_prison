package product.prison.model;

import java.util.List;

import product.prison.utils.Calendar;

public class CalendarData {
    private int id;

    private String scheduleName;

    private List<Calendar> details;

    private int type;

    private List<UserDetails> userDetails;

    private String scheduleDay;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setScheduleName(String scheduleName) {
        this.scheduleName = scheduleName;
    }

    public String getScheduleName() {
        return this.scheduleName;
    }

    public void setDetails(List<Calendar> details) {
        this.details = details;
    }

    public List<Calendar> getDetails() {
        return this.details;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }

    public void setUserDetails(List<UserDetails> userDetails) {
        this.userDetails = userDetails;
    }

    public List<UserDetails> getUserDetails() {
        return this.userDetails;
    }

    public void setScheduleDay(String scheduleDay) {
        this.scheduleDay = scheduleDay;
    }

    public String getScheduleDay() {
        return this.scheduleDay;
    }

}