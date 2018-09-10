package com.mvppoa.adidas.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the Itinerary entity.
 */
public class ItineraryWithoutArrivalTimeDTO implements Serializable {

    private Long id;

    private String originCity;

    private String destinationCity;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXXX")
    private ZonedDateTime departureTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOriginCity() {
        return originCity;
    }

    public void setOriginCity(String originCity) {
        this.originCity = originCity;
    }

    public String getDestinationCity() {
        return destinationCity;
    }

    public void setDestinationCity(String destinationCity) {
        this.destinationCity = destinationCity;
    }

    public ZonedDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(ZonedDateTime departureTime) {
        this.departureTime = departureTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ItineraryWithoutArrivalTimeDTO itineraryDTO = (ItineraryWithoutArrivalTimeDTO) o;
        if (itineraryDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), itineraryDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ItineraryDTO{" +
            "id=" + getId() +
            ", originCity='" + getOriginCity() + "'" +
            ", destinationCity='" + getDestinationCity() + "'" +
            ", departureTime='" + getDepartureTime() + "'" +
            "}";
    }
}
