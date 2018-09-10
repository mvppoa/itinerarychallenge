package com.mvppoa.adidas.repository;

import com.mvppoa.adidas.domain.Itinerary;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Itinerary entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ItineraryRepository extends JpaRepository<Itinerary, Long>, JpaSpecificationExecutor<Itinerary> {

}
