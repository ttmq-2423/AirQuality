package com.example.airquality.Model;
import com.google.gson.annotations.SerializedName;
public class AssetID {
    @SerializedName("id")
    public String id;
    @SerializedName("name")
    public String name;
    @SerializedName("attributes")
    public Attributes attributes;

    public class Attributes {
        @SerializedName("rainfall")
        public Rainfall rainfall;

        public class Rainfall {
            @SerializedName("value")
            public String value;
        }

        @SerializedName("temperature")
        public Temp temp;

        public class Temp {
            @SerializedName("value")
            public String value;
        }

        @SerializedName("humidity")
        public Hum hum;

        public class Hum {
            @SerializedName("value")
            public String value;

            public String getValue() {
                return value;
            }
        }
        @SerializedName("brightness")
        public  Bright bright;
        public class Bright{
            @SerializedName("value")
            public String value;

        }
        @SerializedName("colourTemperature")
        public ColourTemp colour;
        public class ColourTemp{
            @SerializedName("value")
            public String value;

        }
        @SerializedName("email")
        public Email email;
        public class Email{
            @SerializedName("value")
            public String value;

        }


        @SerializedName("place")
        public Place place;

        public class Place {
            @SerializedName("value")
            public String value;

            public String getValue() {
                return value;
            }
        }

        @SerializedName("location")
        public mlocation location;

        public class mlocation {
            @SerializedName("value")
            public mvalue value;

            public class mvalue {
                double[] coordinates;

                public double[] getCoordinates() {
                    return coordinates;
                }
            }

            public mvalue getValue() {
                return value;
            }
        }
    }
}
