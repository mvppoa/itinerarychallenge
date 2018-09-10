package com.mvppoa.adidas.service;

import com.mvppoa.adidas.service.dto.ConnectionDTO;

/**
 * Service Interface for managing Itinerary.
 */
public interface CityService {

    /**
     * Get all the itineraries.
     *
     * @return the list of entities
     */
    ConnectionDTO findNearestRouteToCity(String origin, String destination);

}
