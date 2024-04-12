package com.example.airquality.Model;
import com.google.gson.annotations.SerializedName;

public class Asset {

    @SerializedName("attributes")
    public Attributes attributes;
    public class Attributes
    {
        @SerializedName("rainfall")
        public Rainfall Rain;
        public class Rainfall {
            @SerializedName("value")
            public String value;
        }
        @SerializedName("temperature")
        public AssetID.Attributes.Temp temp;

        public class Temp {
            @SerializedName("value")
            public String value;
        }
        @SerializedName("humidity")
        public AssetID.Attributes.Hum hum;

        public class Hum {
            @SerializedName("value")
            public String value;
        }
        @SerializedName("place")
        public AssetID.Attributes.Place place;

        public class Place {
            @SerializedName("value")
            public String value;
        }
        @SerializedName("windDirection")
        public WindDe windDe;

        public class WindDe {
            @SerializedName("value")
            public String value;
        }
        @SerializedName("windSpeed")
        public WindSp winsp;

        public class WindSp {
            @SerializedName("value")
            public String value;
        }


    }

}