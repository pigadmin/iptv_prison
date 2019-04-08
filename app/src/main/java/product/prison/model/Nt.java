package product.prison.model;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

@Table(name = "Nt")
public class Nt implements Serializable {
    @Column(name = "id", isId = true, autoGen = true)
    public int id;
    @Column(name = "read")
    public boolean read;
    @Column(name = "content")
    public String content;
    @Column(name = "ctiem")
    public long ctiem;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getCtiem() {
        return ctiem;
    }

    public void setCtiem(long ctiem) {
        this.ctiem = ctiem;
    }

    @Override
    public String toString() {

        return super.toString();
    }
}
