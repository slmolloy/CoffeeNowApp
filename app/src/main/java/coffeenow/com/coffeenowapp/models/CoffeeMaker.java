package coffeenow.com.coffeenowapp.models;

import java.util.Date;
import java.util.Locale;

public class CoffeeMaker {
    public static final String CM_ID = "_id";
    public static final String CM_NAME = "name";
    public static final String CM_TOKEN = "token";
    public static final String CM_LOCATION = "location";
    public static final String CM_VOLUME = "volume";
    public static final String CM_PRIVATE = "isPrivate";
    public static final String CM_ON = "isOn";
    public static final String CM_CURRENT_VOLUME = "currentVolume";
    public static final String CM_CREATED = "createdAt";
    public static final String CM_LAT = "latitude";
    public static final String CM_LONG = "longitude";

    private String id;
    private String name;
    private String token;
    private String location;
    private int volume;
    private boolean isPrivate;
    private boolean isOn;
    private int currentVolume;
    private Date createdAt;
    private Double latitude;
    private Double longitude;

    public CoffeeMaker(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public boolean isOn() {
        return isOn;
    }

    public void setOn(boolean on) {
        isOn = on;
    }

    public int getCurrentVolume() {
        return currentVolume;
    }

    public void setCurrentVolume(int currentVolume) {
        this.currentVolume = currentVolume;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(),
                "%s (%d cups) -- Lat: %.3f Long: %.3f",
                getName(), getVolume(), getLatitude(), getLongitude());
    }
}
