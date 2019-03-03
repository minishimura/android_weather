package android.lifeistech.com.okantest;

import com.google.gson.annotations.SerializedName;


public class Weather {

    @SerializedName("cod")
    private String cod;

    public String getCod() {
        return cod;
    }

}


