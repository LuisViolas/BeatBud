package com.example.beatbud_1;

import java.util.ArrayList;
import java.util.List;

public class Profile {

    private String name;
    private String email;
    private String gender;
    private String about;
    private String birthday;
    private ArrayList<String> savedGroups;
    private String phone;
    private String imageurl;

    public Profile(String name, String email,String gender,String imageurl) {
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.about ="About Yourself";
        this.phone ="Phone Number";
        this.savedGroups =  new ArrayList<String>();
        this.imageurl = imageurl;
    }

    public Profile(String name, String email, String gender, String about, String birthday, String phone) {
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.about = about;
        this.birthday = birthday;
        this.phone = phone;
        this.savedGroups =  getSavedGroups();
    }

    public Profile() {
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public ArrayList<String> getSavedGroups() {
        return savedGroups;
    }

    public void setSavedGroups(ArrayList<String> savedGroups) {
        this.savedGroups = savedGroups;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
}
