package product.prison.model;

import java.io.Serializable;

public class TranscribeData implements Serializable {
    private int id;

    private String name;

    private long start_time;

    private long end_time;

    private String path;

    private int live_id;

    private int status;

    private String job_name;

    private long create_time;

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

    public void setStart_time(long start_time) {
        this.start_time = start_time;
    }

    public long getStart_time() {
        return this.start_time;
    }

    public void setEnd_time(long end_time) {
        this.end_time = end_time;
    }

    public long getEnd_time() {
        return this.end_time;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return this.path;
    }

    public void setLive_id(int live_id) {
        this.live_id = live_id;
    }

    public int getLive_id() {
        return this.live_id;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return this.status;
    }

    public void setJob_name(String job_name) {
        this.job_name = job_name;
    }

    public String getJob_name() {
        return this.job_name;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public long getCreate_time() {
        return this.create_time;
    }

}