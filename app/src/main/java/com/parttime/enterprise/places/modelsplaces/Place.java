package com.parttime.enterprise.places.modelsplaces;

/**
 * Created by apples on 04/04/17.
 */

public class Place {
    private String placeId;
    private String placeName;
    private String placeDescription;

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getPlaceDescription() {
        return placeDescription;
    }

    public void setPlaceDescription(String placeDescription) {
        this.placeDescription = placeDescription;
    }

    @Override
    public String toString() {
        return "Place{" +
                "placeId='" + placeId + '\'' +
                ", placeName='" + placeName + '\'' +
                ", placeDescription='" + placeDescription + '\'' +
                '}';
    }
}
