package org.sid.travelplanner.api.dto.places;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PlaceResponseDTO {
    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public AddressDetails getAddressDetails() {
        return addressDetails;
    }

    public void setAddressDetails(AddressDetails addressDetails) {
        this.addressDetails = addressDetails;
    }

    @JsonProperty("place_id")
    private String placeId;

    @JsonProperty("display_name")
    private String displayName;

    private String type;
    private String category;
    private double lat;
    private double lon;

    @JsonProperty("address")
    private AddressDetails addressDetails;

    @Data
    public static class AddressDetails {
        private String road;
        private String house_number;
        private String postcode;
        private String city;
        private String state;
        private String country;

        @Override
        public String toString() {
            StringBuilder address = new StringBuilder();
            if (road != null) address.append(road);
            if (house_number != null) address.append(" ").append(house_number);
            if (city != null) address.append(", ").append(city);
            if (state != null) address.append(", ").append(state);
            if (country != null) address.append(", ").append(country);
            if (postcode != null) address.append(" ").append(postcode);
            return address.toString().trim();
        }
    }

    // Helper method pour obtenir une adresse formatée
    public String getFormattedAddress() {
        return addressDetails != null ? addressDetails.toString() : displayName;
    }

    // Helper method pour déterminer la catégorie
    public String getCategory() {
        if (type != null) {
            switch (type.toLowerCase()) {
                case "restaurant":
                case "cafe":
                case "bar":
                    return "dining";
                case "museum":
                case "gallery":
                case "theatre":
                    return "culture";
                case "hotel":
                case "hostel":
                case "guest_house":
                    return "accommodation";
                default:
                    return type;
            }
        }
        return "other";
    }
}