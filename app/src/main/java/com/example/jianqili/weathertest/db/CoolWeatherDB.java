package com.example.jianqili.weathertest.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.jianqili.weathertest.model.City;
import com.example.jianqili.weathertest.model.Country;
import com.example.jianqili.weathertest.model.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jianqi.li on 2016/4/18.
 */
public class CoolWeatherDB {
    public static final String DB_NAME = "cool_weather";

    public static final int VERSION = 1;

    private static CoolWeatherDB coolWeatherDB;
    private SQLiteDatabase db;

    private CoolWeatherDB(Context context) {
        CoolWeatherOpenHelper dbHelper = new CoolWeatherOpenHelper(context,
                DB_NAME, null, VERSION);
        db = dbHelper.getWritableDatabase();
    }

    public synchronized static CoolWeatherDB getInstance(Context context){
        if (coolWeatherDB == null){
            coolWeatherDB = new CoolWeatherDB(context);
        }
        return coolWeatherDB;
    }

    public void saveProvince(Province province){
        if (province != null){
            ContentValues values = new ContentValues();
            values.put("province_name", province.getProvinceName());
            values.put("province_code", province.getProvinceCode());
            db.insert("Province", null, values);
        }
    }
    public List<Province> loadProvinces(){
        List<Province> list = new ArrayList<>();
        Cursor cursor = db.query("Province", null, null, null, null, null, null);
        if (cursor.moveToFirst()){
            do {
                Province province = new Province();
                province.setId(cursor.getInt(cursor.getColumnIndex("id")));
                province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
                province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
                list.add(province);
            }while (cursor.moveToNext());
        }
        return list;
    }

    public void saveCity(City city){
        if (city != null){
            ContentValues values = new ContentValues();
            values.put("city_name", city.getCityName());
            values.put("city_code", city.getCityCode());
            values.put("province_id", city.getProvinceId());
            db.insert("City", null, values);
        }
    }

    public List<City> loadCities(int provinceId){
        List<City> list = new ArrayList<>();
        Cursor cursor = db.query("City", null, "province_id = ?",
                new String[]{String.valueOf(provinceId)}, null, null, null);
        City city = new City();
        if (cursor.moveToFirst()){
            do {
                city.setId(cursor.getInt(cursor.getColumnIndex("id")));
                city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
                city.setCityName(cursor.getString(cursor.getColumnIndex("city_code")));
                city.setProvinceId(provinceId);
                list.add(city);
            }while (cursor.moveToNext());
        }
        return list;
    }

    public void saveCountry(Country country){
        ContentValues values = new ContentValues();
        if (country != null) {
            values.put("country_code", country.getCountryCode());
            values.put("country_code", country.getCountryCode());
            values.put("city_id", country.getCityId());
            db.insert("Country", null, values);
        }
    }

    public List<Country> loadCountries(int cityId){
        List<Country> list = new ArrayList<>();
        Cursor cursor = db.query("Country", null, "city_id = ?",
                new String[]{String.valueOf(cityId)}, null, null, null);
        Country country = new Country();
        if (cursor.moveToFirst()){
            do {
                country.setId(cursor.getInt(cursor.getColumnIndex("id")));
                country.setCountryCode(cursor.getString(cursor.getColumnIndex("country_code")));
                country.setCountryName(cursor.getString(cursor.getColumnIndex("country_name")));
                country.setCityId(cityId);
                list.add(country);
            }while (cursor.moveToNext());
        }
        return list;
    }
}
