package product.prison.model;

public class LivePreView {
    private int id;

    private int liveId;

    private String programName;

    private long startTime;

    private long endTime;

    private int isRecord;

    private int status;

    private String filePath;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setLiveId(int liveId) {
        this.liveId = liveId;
    }

    public int getLiveId() {
        return this.liveId;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public String getProgramName() {
        return this.programName;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getStartTime() {
        return this.startTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getEndTime() {
        return this.endTime;
    }

    public void setIsRecord(int isRecord) {
        this.isRecord = isRecord;
    }

    public int getIsRecord() {
        return this.isRecord;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return this.status;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return this.filePath;
    }
}
