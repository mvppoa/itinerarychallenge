package com.mvppoa.adidas.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;



import io.github.jhipster.service.filter.ZonedDateTimeFilter;


/**
 * Criteria class for the Itinerary entity. This class is used in ItineraryResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /itineraries?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ItineraryCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter originCity;

    private StringFilter destinationCity;

    private ZonedDateTimeFilter departureTime;

    private ZonedDateTimeFilter arrivalTime;

    public ItineraryCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getOriginCity() {
        return originCity;
    }

    public void setOriginCity(StringFilter originCity) {
        this.originCity = originCity;
    }

    public StringFilter getDestinationCity() {
        return destinationCity;
    }

    public void setDestinationCity(StringFilter destinationCity) {
        this.destinationCity = destinationCity;
    }

    public ZonedDateTimeFilter getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(ZonedDateTimeFilter departureTime) {
        this.departureTime = departureTime;
    }

    public ZonedDateTimeFilter getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(ZonedDateTimeFilter arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    @Override
    public String toString() {
        return "ItineraryCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (originCity != null ? "originCity=" + originCity + ", " : "") +
                (destinationCity != null ? "destinationCity=" + destinationCity + ", " : "") +
                (departureTime != null ? "departureTime=" + departureTime + ", " : "") +
                (arrivalTime != null ? "arrivalTime=" + arrivalTime + ", " : "") +
            "}";
    }

}
