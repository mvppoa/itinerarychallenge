package com.mvppoa.adidas.service.mapper;

import com.mvppoa.adidas.domain.*;
import com.mvppoa.adidas.service.dto.ItineraryDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Itinerary and its DTO ItineraryDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ItineraryMapper extends EntityMapper<ItineraryDTO, Itinerary> {



    default Itinerary fromId(Long id) {
        if (id == null) {
            return null;
        }
        Itinerary itinerary = new Itinerary();
        itinerary.setId(id);
        return itinerary;
    }
}
