package com.imast.findingme.model;

public class LostPet {

    private int id;
    private String status;
    private String info;
    private String report_date;
    private String lost_date;
    private double latitude;
    private double longitude;
    private String found_date;
    private int pet_id;
    private int user_id;
    private int district_id;
    private Pet pet;

    public LostPet(int id, String status, String info, String report_date, String lost_date, double latitude, double longitude, String found_date, int pet_id, int user_id, int district_id, Pet pet) {
        this.id = id;
        this.status = status;
        this.info = info;
        this.report_date = report_date;
        this.lost_date = lost_date;
        this.latitude = latitude;
        this.longitude = longitude;
        this.found_date = found_date;
        this.pet_id = pet_id;
        this.user_id = user_id;
        this.district_id = district_id;
        this.pet = pet;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getReport_date() {
        return report_date;
    }

    public void setReport_date(String report_date) {
        this.report_date = report_date;
    }

    public String getLost_date() {
        return lost_date;
    }

    public void setLost_date(String lost_date) {
        this.lost_date = lost_date;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getFound_date() {
        return found_date;
    }

    public void setFound_date(String found_date) {
        this.found_date = found_date;
    }

    public int getPet_id() {
        return pet_id;
    }

    public void setPet_id(int pet_id) {
        this.pet_id = pet_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getDistrict_id() {
        return district_id;
    }

    public void setDistrict_id(int district_id) {
        this.district_id = district_id;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }
}
