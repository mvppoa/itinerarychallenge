package com.mvppoa.adidas.service;

import com.mvppoa.adidas.service.dto.ConnectionDTO;
import com.mvppoa.adidas.service.dto.ItineraryDTO;
import com.mvppoa.adidas.service.dto.ItineraryWithoutArrivalTimeDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing Itinerary.
 */
public interface CityPathService {

    /**
     * Save a itinerary.
     *
     * @param itineraryDTO the entity to save
     * @return the persisted entity
     */

    /**
     * Get shortest route from city A to B
     *
     * @return connection with shortest route and time from city A to B
     */
    ConnectionDTO findShortestRouteFromOriginToDestination(String origin, String destination);
}
