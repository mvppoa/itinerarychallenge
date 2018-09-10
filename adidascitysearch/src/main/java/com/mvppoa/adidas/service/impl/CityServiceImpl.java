package com.mvppoa.adidas.service.impl;

import com.mvppoa.adidas.repository.CityRepository;
import com.mvppoa.adidas.service.CityService;
import com.mvppoa.adidas.service.dto.ConnectionDTO;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Service Implementation for managing Itinerary.
 */
@Service
@Transactional
public class CityServiceImpl implements CityService {

    private static final String TIME_ZONE = "time_zone";
    private static final String NAME = "name";
    private static final String TRAVEL_TIME = "travel_time";

    private final Logger log = LoggerFactory.getLogger(CityServiceImpl.class);

    private final CityRepository cityRepository;

    public CityServiceImpl(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    /**
     * Find nearest route to a city given the origin and the destination
     * @param origin Origin city
     * @param destination Destination city
     * @return the shortest route from city A to B
     */
    @Override
    public ConnectionDTO findNearestRouteToCity(String origin, String destination) {

        log.info("Searching connections");

        List<Object> resultSet = new ArrayList<>();
        cityRepository.findByIncomingAndOutgoing(origin, destination).forEach(resultSet::add);

        List<ConnectionDTO> connectionsDTOS = new ArrayList<>();
        resultSet.stream().forEach(resultNode -> {
            connectionsDTOS.add(fillReturnObject((List<Map<Object,Object>>)resultNode));
        });

        Collections.sort(connectionsDTOS);

        return !connectionsDTOS.isEmpty() ? connectionsDTOS.get(0) : new ConnectionDTO();

    }

    private ConnectionDTO fillReturnObject(List<Map<Object, Object>> objects) {

        ConnectionDTO connectionsDTO = new ConnectionDTO();
        connectionsDTO.setOriginCityTimezone(objects.get(0).get(TIME_ZONE).toString());
        connectionsDTO.setDestinyCityTimezone(objects.get(objects.size()-1).get(TIME_ZONE).toString());

        List<String> cities = new ArrayList<>();
        Long totalTravelTimeInSeconds = 0L;

        for (Integer iteratorPosition = 0; iteratorPosition < objects.size(); iteratorPosition++) {
            if (iteratorPosition % 2 == 0) {
                cities.add(objects.get(iteratorPosition).get(NAME).toString());
            } else {
                totalTravelTimeInSeconds += Long.parseLong(objects.get(iteratorPosition).get(TRAVEL_TIME).toString());
            }
        }

        connectionsDTO.setTotalTravelTimeInSeconds(totalTravelTimeInSeconds);
        connectionsDTO.setConnections(Strings.join(cities,'-'));

        return connectionsDTO;
    }
}
