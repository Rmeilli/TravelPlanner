package org.sid.travelplanner.api.dto.places;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class PlaceDTO {
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public PlaceDTO(String id, String name, String address, double latitude, double longitude, String type, String category) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
        this.category = category;
    }

    private String id;
    private String name;
    private String address;
    private double latitude;
    private double longitude;
    private String type;
    private String category;

    private PlaceDTO() {}

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final PlaceDTO placeDTO;

        private Builder() {
            placeDTO = new PlaceDTO();
        }

        public Builder id(String id) {
            placeDTO.id = id;
            return this;
        }

        public Builder name(String name) {
            placeDTO.name = name;
            return this;
        }

        public Builder address(String address) {
            placeDTO.address = address;
            return this;
        }

        public Builder latitude(double latitude) {
            placeDTO.latitude = latitude;
            return this;
        }

        public Builder longitude(double longitude) {
            placeDTO.longitude = longitude;
            return this;
        }

        public Builder type(String type) {
            placeDTO.type = type;
            return this;
        }

        public Builder category(String category) {
            placeDTO.category = category;
            return this;
        }

        public PlaceDTO build() {
            return placeDTO;
        }
    }
}
