package com.example.beatbud_1;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;

public class Groups implements Parcelable {

    private String name;
    private String date_in;
    private String date_out;
    private Integer max_people;
    private ArrayList<String> persons;
    private String festival;
    private boolean smoking;
    private boolean after_party;
    private boolean couple;
    private boolean drink;
    private String name_owner;

    public Groups(String name_owner,String name, String date_in, String date_out, Integer max_people, ArrayList<String> persons, String festival, boolean smoking, boolean after_party, boolean couple, boolean drink) {
        this.name = name;
        this.name_owner = name_owner;
        this.date_in = date_in;
        this.date_out = date_out;
        this.max_people = max_people;
        this.persons = persons;
        this.festival = festival;
        this.smoking = smoking;
        this.after_party = after_party;
        this.couple = couple;
        this.drink = drink;
    }

    public Groups(ArrayList<String> persons) {
        this.persons = persons;
    }

    public Groups(String name) {
        this.name = name;
    }

    public Groups(String name, ArrayList<String> persons, String name_owner) {
        this.name = name;
        this.persons = persons;
        this.name_owner = name_owner;
    }

    public Groups() {
    }

    public String getName_owner() {
        return name_owner;
    }

    public void setName_owner(String name_owner) {
        this.name_owner = name_owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate_in() {
        return date_in;
    }

    public void setDate_in(String date_in) {
        this.date_in = date_in;
    }

    public String getDate_out() {
        return date_out;
    }

    public void setDate_out(String date_out) {
        this.date_out = date_out;
    }

    public Integer getMax_people() {
        return max_people;
    }

    public void setMax_people(Integer max_people) {
        this.max_people = max_people;
    }


    public ArrayList<String> getPersons() {
        return persons;
    }

    public void setPersons(ArrayList<String> persons) {
        this.persons = persons;
    }

    public String getFestival() {
        return festival;
    }

    public void setFestival(String festival) {
        this.festival = festival;
    }

    public boolean isSmoking() {
        return smoking;
    }

    public void setSmoking(boolean smoking) {
        this.smoking = smoking;
    }

    public boolean isAfter_party() {
        return after_party;
    }

    public void setAfter_party(boolean after_party) {
        this.after_party = after_party;
    }

    public boolean isCouple() {
        return couple;
    }

    public void setCouple(boolean couple) {
        this.couple = couple;
    }

    public boolean isDrink() {
        return drink;
    }

    public void setDrink(boolean drink) {
        this.drink = drink;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
