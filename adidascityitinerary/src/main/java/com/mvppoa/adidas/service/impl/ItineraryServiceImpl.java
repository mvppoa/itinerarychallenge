package com.mvppoa.adidas.service.impl;

import com.mvppoa.adidas.client.CityPathClient;
import com.mvppoa.adidas.service.CityPathService;
import com.mvppoa.adidas.service.ItineraryService;
import com.mvppoa.adidas.domain.Itinerary;
import com.mvppoa.adidas.repository.ItineraryRepository;
import com.mvppoa.adidas.service.dto.ConnectionDTO;
import com.mvppoa.adidas.service.dto.ItineraryDTO;
import com.mvppoa.adidas.service.dto.ItineraryWithoutArrivalTimeDTO;
import com.mvppoa.adidas.service.mapper.ItineraryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
/**
 * Service Implementation for managing Itinerary.
 */
@Service
@Transactional
public class ItineraryServiceImpl implements ItineraryService {

    private final Logger log = LoggerFactory.getLogger(ItineraryServiceImpl.class);

    private final ItineraryRepository itineraryRepository;
    private final CityPathService cityPathService;
    private final ItineraryMapper itineraryMapper;

    public ItineraryServiceImpl(ItineraryRepository itineraryRepository, CityPathService cityPathService, ItineraryMapper itineraryMapper) {
        this.itineraryRepository = itineraryRepository;
        this.cityPathService = cityPathService;
        this.itineraryMapper = itineraryMapper;
    }

    /**
     * Save a itinerary.
     *
     * @param itineraryWithoutArrivalTimeDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ItineraryDTO save(ItineraryWithoutArrivalTimeDTO itineraryWithoutArrivalTimeDTO) {
        log.debug("Request to save Itinerary : {}", itineraryWithoutArrivalTimeDTO);

        ConnectionDTO connectionDTO = cityPathService.findShortestRouteFromOriginToDestination(itineraryWithoutArrivalTimeDTO.getOriginCity(),itineraryWithoutArrivalTimeDTO.getDestinationCity());

        ItineraryDTO itineraryDTO = new ItineraryDTO();
        itineraryDTO.setOriginCity(itineraryWithoutArrivalTimeDTO.getOriginCity());
        itineraryDTO.setDestinationCity(itineraryWithoutArrivalTimeDTO.getDestinationCity());
        itineraryDTO.setDepartureTime(itineraryWithoutArrivalTimeDTO.getDepartureTime().withZoneSameInstant(ZoneId.of(connectionDTO.getOriginCityTimezone())));
        itineraryDTO.setArrivalTime(itineraryWithoutArrivalTimeDTO.getDepartureTime().withZoneSameInstant(ZoneId.of(connectionDTO.getDestinyCityTimezone())).plusSeconds(connectionDTO.getTotalTravelTimeInSeconds()));

        return save(itineraryDTO);
    }

    /**
     * Save a itinerary.
     *
     * @param itineraryDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ItineraryDTO save(ItineraryDTO itineraryDTO) {
        log.debug("Request to save Itinerary : {}", itineraryDTO);
        Itinerary itinerary = itineraryMapper.toEntity(itineraryDTO);
        itinerary = itineraryRepository.save(itinerary);
        return itineraryMapper.toDto(itinerary);
    }

    /**
     * Get all the itineraries.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<ItineraryDTO> findAll() {
        log.debug("Request to get all Itineraries");
        return itineraryRepository.findAll().stream()
            .map(itineraryMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one itinerary by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ItineraryDTO> findOne(Long id) {
        log.debug("Request to get Itinerary : {}", id);
        return itineraryRepository.findById(id)
            .map(itineraryMapper::toDto);
    }

    /**
     * Delete the itinerary by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Itinerary : {}", id);
        itineraryRepository.deleteById(id);
    }
}
