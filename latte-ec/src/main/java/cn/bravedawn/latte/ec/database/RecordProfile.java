package cn.bravedawn.latte.ec.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by 冯晓 on 2017/10/17.
 */

@Entity(nameInDb = "user_record")
public class RecordProfile {

    @Id
    private long id;
    private String url;
    private String title;
    private String colorAvatar;
    private String resource;
    private String channel;
    private boolean mstar;
    @Generated(hash = 1497629287)
    public RecordProfile(long id, String url, String title, String colorAvatar,
            String resource, String channel, boolean mstar) {
        this.id = id;
        this.url = url;
        this.title = title;
        this.colorAvatar = colorAvatar;
        this.resource = resource;
        this.channel = channel;
        this.mstar = mstar;
    }
    @Generated(hash = 1901040962)
    public RecordProfile() {
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getUrl() {
        return this.url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getColorAvatar() {
        return this.colorAvatar;
    }
    public void setColorAvatar(String colorAvatar) {
        this.colorAvatar = colorAvatar;
    }
    public String getResource() {
        return this.resource;
    }
    public void setResource(String resource) {
        this.resource = resource;
    }
    public String getChannel() {
        return this.channel;
    }
    public void setChannel(String channel) {
        this.channel = channel;
    }
    public boolean getMstar() {
        return this.mstar;
    }
    public void setMstar(boolean mstar) {
        this.mstar = mstar;
    }

}
