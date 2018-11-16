package com.example.umasurakod.groupathon;
import android.widget.ImageView;
import java.util.Date;

public class Event {
    private String eventName;
    private Date eventDate;
    private Date cDate;              //group created date
    private String admin ;           //group created by
    private ImageView groupImg;


    public Event(String eventName,Date eventDate,Date cDate,String admin,ImageView groupImg) {
        this.eventName=eventName;
        this.eventDate=eventDate;
        this.cDate=cDate;
        this.admin=admin;
        this.groupImg=groupImg;
    }



    public String geteventName() { return eventName; }

    public Date geteventDate() { return eventDate; }

    public Date getcreatedDate() {
        return cDate;
    }

    public String getAdmin() { return admin; }

    public ImageView getgroupImg(){ return groupImg; }


    public void seteventName(String eventName) { this.eventName=eventName; }

    public void seteventDate(Date eventDate) { this.eventDate=eventDate; }

    public void setcreatedDate (Date cDate) { this.cDate=cDate; }

    public void setAdmin(String admin) { this.admin=admin; }
}
