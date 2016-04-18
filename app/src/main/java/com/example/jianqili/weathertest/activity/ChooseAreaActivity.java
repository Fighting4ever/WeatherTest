package com.example.jianqili.weathertest.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jianqili.weathertest.R;
import com.example.jianqili.weathertest.db.CoolWeatherDB;
import com.example.jianqili.weathertest.model.City;
import com.example.jianqili.weathertest.model.County;
import com.example.jianqili.weathertest.model.Province;
import com.example.jianqili.weathertest.utils.HttpCallbackListener;
import com.example.jianqili.weathertest.utils.HttpUtil;
import com.example.jianqili.weathertest.utils.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fighting4Ever on 2016/4/18.
 */
public class ChooseAreaActivity extends Activity {
    public static final int LEVEL_PROVINCE = 0;

    public static final int LEVEL_CITY = 1;

    public static final int LEVEL_COUNTY = 2;

    private ProgressDialog progressDialog;
    private TextView titleText;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private CoolWeatherDB coolWeatherDB;
    private List<String> dataList = new ArrayList<>();

    private List<Province> provinceList;

    private List<City> cityList;

    private List<County> countyList;

    private Province selectedProvince;
    private City selectedCity;
    private  int currentLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.choose_area);
        listView = (ListView) findViewById(R.id.list_view);
        titleText = (TextView) findViewById(R.id.title_text);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(adapter);
        coolWeatherDB = CoolWeatherDB.getInstance(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentLevel == LEVEL_PROVINCE){
                    selectedProvince = provinceList.get(position);
                    queryCities();
                }else if(currentLevel == LEVEL_CITY){
                    selectedCity = cityList.get(position);
                    queryCountries();
                }
            }
        });
        queryProvinces();
    }

    private void queryProvinces(){
        provinceList = coolWeatherDB.loadProvinces();
        if (provinceList.size() > 0){
            dataList.clear();
            for (Province province : provinceList){
                dataList.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            titleText.setText("China");
            currentLevel = LEVEL_PROVINCE;
        }else {
            queryFromServer(null, "province");
        }
    }

    private void queryCities(){
        cityList = coolWeatherDB.loadCities(selectedProvince.getId());
        if (cityList.size() > 0){
            dataList.clear();
            for (City city : cityList){
                dataList.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            titleText.setText(selectedProvince.getProvinceName());
            currentLevel = LEVEL_CITY;
        }else {
            queryFromServer(selectedProvince.getProvinceCode(), "city");
        }
    }

    private void queryCountries(){
        countyList = coolWeatherDB.loadCountries(selectedCity.getId());
        if (countyList.size() > 0){
            dataList.clear();
            for (County county : countyList){
                dataList.add(county.getCountyName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            titleText.setText(selectedCity.getCityName());
            currentLevel = LEVEL_COUNTY;
        }else {
            queryFromServer(selectedCity.getCityCode(), "county");
        }
    }

    private void queryFromServer(final String code, final String type){
        String address;
        if (!TextUtils.isEmpty(code)){
            address = "http://www.weather.com.cn/data/list3/city" + code + ".xml";
        }else{
            address = "http://www.weather.com.cn/data/list3/city.xml";
        }
        showProgressDialog();
        HttpUtil.SendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                boolean result = false;
                if (type.equals("province")){
                    result = Utility.handleProvincesResponse(coolWeatherDB, response);
                }else if (type.equals("city")){
                    result = Utility.handleCitiesResponse(coolWeatherDB, response, selectedProvince.getId());
                }else if (type.equals("county")){
                    result = Utility.handleCountriesResponse(coolWeatherDB, response, selectedCity.getId());
                }

                if (result){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if (type.equals("province")){
                                queryProvinces();
                            }else if (type.equals("city")){
                                queryCities();
                            }else if (type.equals("county")){
                                queryCountries();
                            }
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(ChooseAreaActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void showProgressDialog(){
        if (progressDialog == null){
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Loading...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    private void closeProgressDialog(){
        if (progressDialog != null){
            progressDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        if (currentLevel == LEVEL_COUNTY){
            queryCities();
        }else if (currentLevel == LEVEL_CITY){
            queryProvinces();
        }else {
            finish();
        }
    }
}
