package com.example.jianqili.weathertest.utils;

import android.text.TextUtils;
import android.util.Log;

import com.example.jianqili.weathertest.db.CoolWeatherDB;
import com.example.jianqili.weathertest.model.City;
import com.example.jianqili.weathertest.model.County;
import com.example.jianqili.weathertest.model.Province;

/**
 * Created by Fighting4Ever on 2016/4/18.
 */
public class Utility {
    public synchronized static boolean handleProvincesResponse(CoolWeatherDB coolWeatherDB, String response){
        if (!TextUtils.isEmpty(response)){
            String[] allProvinces = response.split(",");
            if (allProvinces != null && allProvinces.length > 0){
                for (String p : allProvinces){
                    String[] array = p.split("\\|");
                    Province province = new Province();
                    province.setProvinceCode(array[0]);
                    province.setProvinceName(array[1]);
                    coolWeatherDB.saveProvince(province);
                }
                return true;
            }
        }
        return false;
    }

    public synchronized static boolean handleCitiesResponse(CoolWeatherDB coolWeatherDB, String response, int provinceId){
        if (!TextUtils.isEmpty(response)){
            String[] allCities = response.split(",");
            if (allCities != null && allCities.length > 0){
                for (String c : allCities){
                    String[] array = c.split("\\|");
                    City city = new City();
                    city.setCityCode(array[0]);
                    city.setCityName(array[1]);
                    city.setProvinceId(provinceId);
                    coolWeatherDB.saveCity(city);
                }
                return true;
            }
        }
        return false;
    }

    public synchronized static boolean handleCountriesResponse(CoolWeatherDB coolWeatherDB, String response, int cityId){
        if (!TextUtils.isEmpty(response)){
            String[] allCountries = response.split(",");
            if (allCountries != null && allCountries.length > 0){
                for (String c : allCountries){
                    String[] array = c.split("\\|");
                    County county = new County();
                    county.setCountyCode(array[0]);
                    Log.v("lijianqi", array[1]);
                    county.setCountyName(array[1]);
                    county.setCityId(cityId);
                    coolWeatherDB.saveCounty(county);
                }
                return true;
            }
        }
        return false;
    }
}
