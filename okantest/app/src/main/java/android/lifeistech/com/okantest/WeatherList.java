package android.lifeistech.com.okantest;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherList {
    @SerializedName("list")
    public List<ListA> lists;

    public List<ListA> getList() {
        return lists;
    }

    public class ListA{
        @SerializedName("dt")
        private String dt;

        @SerializedName("weather")
        public List<Weather> weathers;

        public String getDt() {
            return dt;
        }
        public List<Weather> getWeathers() {
            return weathers;
        }

        public class Weather{
            @SerializedName("main")
            public String main;

            public String getMain() {
                return main;
            }
        }
    }

}
