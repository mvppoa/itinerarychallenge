package com.mvppoa.adidas.service.dto;


/**
 * Created by jt on 1/10/17.
 */
public class ConnectionDTO implements Comparable{

    private String connections;
    private Long totalTravelTimeInSeconds;

    private String originCityTimezone;
    private String destinyCityTimezone;

    public ConnectionDTO() {
    }

    public ConnectionDTO(String connections, Long totalTravelTimeInSeconds, String originCityTimezone, String destinyCityTimezone) {
        this.connections = connections;
        this.totalTravelTimeInSeconds = totalTravelTimeInSeconds;
        this.originCityTimezone = originCityTimezone;
        this.destinyCityTimezone = destinyCityTimezone;
    }

    public String getConnections() {
        return connections;
    }

    public void setConnections(String connections) {
        this.connections = connections;
    }

    public Long getTotalTravelTimeInSeconds() {
        return totalTravelTimeInSeconds;
    }

    public void setTotalTravelTimeInSeconds(Long totalTravelTimeInSeconds) {
        this.totalTravelTimeInSeconds = totalTravelTimeInSeconds;
    }

    public String getOriginCityTimezone() {
        return originCityTimezone;
    }

    public void setOriginCityTimezone(String originCityTimezone) {
        this.originCityTimezone = originCityTimezone;
    }

    public String getDestinyCityTimezone() {
        return destinyCityTimezone;
    }

    public void setDestinyCityTimezone(String destinyCityTimezone) {
        this.destinyCityTimezone = destinyCityTimezone;
    }


    @Override
    public int compareTo(Object connectionsDTO) {
        if (this.totalTravelTimeInSeconds < ((ConnectionDTO)connectionsDTO).totalTravelTimeInSeconds) {
            return -1;
        }
        if (this.totalTravelTimeInSeconds > ((ConnectionDTO)connectionsDTO).totalTravelTimeInSeconds) {
            return 1;
        }
        return 0;
    }

}
