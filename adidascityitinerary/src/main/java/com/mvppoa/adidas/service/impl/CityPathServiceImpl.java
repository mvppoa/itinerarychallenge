package com.mvppoa.adidas.service.impl;

import com.mvppoa.adidas.client.CityPathClient;
import com.mvppoa.adidas.domain.Itinerary;
import com.mvppoa.adidas.repository.ItineraryRepository;
import com.mvppoa.adidas.service.CityPathService;
import com.mvppoa.adidas.service.ItineraryService;
import com.mvppoa.adidas.service.dto.ConnectionDTO;
import com.mvppoa.adidas.service.dto.ItineraryDTO;
import com.mvppoa.adidas.service.dto.ItineraryWithoutArrivalTimeDTO;
import com.mvppoa.adidas.service.mapper.ItineraryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Itinerary.
 */
@Service
public class CityPathServiceImpl implements CityPathService {

    private final Logger log = LoggerFactory.getLogger(CityPathServiceImpl.class);

    private final CityPathClient cityPathClient;

    public CityPathServiceImpl(CityPathClient cityPathClient) {
        this.cityPathClient = cityPathClient;
    }

    @Override
    public ConnectionDTO findShortestRouteFromOriginToDestination(String origin, String destination) {
        log.info("Fetching register from city {} to city {}", origin, destination);
        return cityPathClient.getShortestPathAndConnections(origin,destination);
    }
}
