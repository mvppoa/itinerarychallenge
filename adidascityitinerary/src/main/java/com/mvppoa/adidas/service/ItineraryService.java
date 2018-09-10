package com.mvppoa.adidas.service;

import com.mvppoa.adidas.service.dto.ItineraryDTO;
import com.mvppoa.adidas.service.dto.ItineraryWithoutArrivalTimeDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing Itinerary.
 */
public interface ItineraryService {

    /**
     * Save a itinerary.
     *
     * @param itineraryDTO the entity to save
     * @return the persisted entity
     */
    ItineraryDTO save(ItineraryWithoutArrivalTimeDTO itineraryDTO);

    /**
     * Save a complete itinerary.
     *
     * @param itineraryDTO the entity to save
     * @return the persisted entity
     */
    ItineraryDTO save(ItineraryDTO itineraryDTO);

    /**
     * Get all the itineraries.
     *
     * @return the list of entities
     */
    List<ItineraryDTO> findAll();


    /**
     * Get the "id" itinerary.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<ItineraryDTO> findOne(Long id);

    /**
     * Delete the "id" itinerary.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
