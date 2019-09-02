package com.example.beatbud_1;

import java.util.Date;

public class Festival {

    private String name;
    private Date date;
    private String imageurl;

    public Festival(String name, Date date,String imageurl) {
        this.name = name;
        this.date = date;
        this.imageurl=imageurl;
    }

    public Festival() {
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
