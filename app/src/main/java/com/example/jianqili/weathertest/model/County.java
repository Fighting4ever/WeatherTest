package com.example.jianqili.weathertest.model;

/**
 * Created by Fighting4Ever on 2016/4/18.
 */
public class County {
    private int id;
    private String countyName;
    private String countyCode;
    private int cityId;

    public int getId() {
        return id;
    }

    public String getCountyName() {
        return countyName;
    }

    public String getCountyCode() {
        return countyCode;
    }

    public int getCityId() {
        return cityId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public void setCountyCode(String countyCode) {
        this.countyCode = countyCode;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
}
