package xsf.xokhttp.bean;

import java.io.Serializable;
import java.util.List;

import xsf.xokhttp.bean.weather.IndexItem;
import xsf.xokhttp.bean.weather.WeatherData;

/**
 * Author: xsfelvis
 * Time: created at 2016/9/4.
 */
public class WeatherNew implements Serializable{
    public String currentCity;
    public String pm25;
    public List<IndexItem> index;
    public List<WeatherData> weather_data;


    @Override
    public String toString() {
        return "Result [currentCity=" + currentCity + ", index=" + index
                + ", pm25=" + pm25 + ", weather_data=" + weather_data + "]";
    }
}
