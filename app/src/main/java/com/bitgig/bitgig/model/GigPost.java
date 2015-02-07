package com.bitgig.bitgig.model;

/**
 * Created by irvin on 2/7/2015.
 */
public class GigPost implements Cloneable, Comparable<GigPost> {

    public GigPost(String title, String userName, long btc, String loc){
        this.title = title;
        this.userName = userName;
        this.btc = btc;
        this.loc = loc;
    }

    @Override
    public Object clone()  {
        try {
            return super.clone();
        } catch (CloneNotSupportedException unused) {
            // does not happen (since we implement Cloneable)
            return new GigPost(this.title, this.userName, this.btc, this.loc);
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getBtc() {
        return btc;
    }

    public void setBtc(long btc) {
        this.btc = btc;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    private String title;
    private String userName;
    private long btc;
    private String loc;

    @Override
    public int compareTo(GigPost another) {
        return this.btc < another.btc ? - 1: (this.btc > another.btc? 1:0);
    }
}
