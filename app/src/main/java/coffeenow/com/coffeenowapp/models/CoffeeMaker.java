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
    public static final String CM_OWNER = "owner";
    public static final String CM_USERNAME = "username";

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
    private String owner;
    private String username;

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
        if (latitude == null) {
            this.latitude = 0.0;
        } else {
            this.latitude = latitude;
        }
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        if (longitude == null) {
            this.longitude = 0.0;
        } else {
            this.longitude = longitude;
        }
    }

    @Override
    public String toString() {
        if (getLatitude() == null || getLatitude() == 0.0 || getLongitude() == null || getLongitude() == 0.0) {
            return String.format(Locale.getDefault(),
                    "%s's %s (%d cups)",
                    getUsername(), getName(), getVolume());
        } else {
            return String.format(Locale.getDefault(),
                    "%s's %s (%d cups) -- Lat: %.4f Long: %.4f",
                    getUsername(), getName(), getVolume(), getLatitude(), getLongitude());
        }
    }
}
