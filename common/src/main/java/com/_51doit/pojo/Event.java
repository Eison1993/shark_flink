package com._51doit.pojo;

public class Event {

    private String ad_trace_id;

    private String is_rec;

    private String pgid;

    private String referal;

    private String title;

    private String skuid;

    private String url;

    public String getAd_trace_id() {
        return ad_trace_id;
    }

    public void setAd_trace_id(String ad_trace_id) {
        this.ad_trace_id = ad_trace_id;
    }

    public String getIs_rec() {
        return is_rec;
    }

    public void setIs_rec(String is_rec) {
        this.is_rec = is_rec;
    }

    public String getPgid() {
        return pgid;
    }

    public void setPgid(String pgid) {
        this.pgid = pgid;
    }

    public String getReferal() {
        return referal;
    }

    public void setReferal(String referal) {
        this.referal = referal;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSkuid() {
        return skuid;
    }

    public void setSkuid(String skuid) {
        this.skuid = skuid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Event{" +
                "ad_trace_id='" + ad_trace_id + '\'' +
                ", is_rec='" + is_rec + '\'' +
                ", pgid='" + pgid + '\'' +
                ", referal='" + referal + '\'' +
                ", title='" + title + '\'' +
                ", skuid='" + skuid + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
