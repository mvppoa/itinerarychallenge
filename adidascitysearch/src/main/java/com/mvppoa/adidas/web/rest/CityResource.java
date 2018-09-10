package com.mvppoa.adidas.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mvppoa.adidas.service.CityService;
import com.mvppoa.adidas.service.dto.ConnectionDTO;
import com.mvppoa.adidas.web.rest.util.HeaderUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;

/**
 * REST controller for searching cities.
 */
@RestController
@RequestMapping("/api")
public class CityResource {

    private final Logger log = LoggerFactory.getLogger(CityResource.class);

    private static final String ENTITY_NAME = "city";

    private final CityService cityService;

    public CityResource(CityService cityService) {
        this.cityService = cityService;
    }

    /**
     * GET  /cities/path : search the shortest path to a city
     *
     * @param origin      the city origin
     * @param destination the city destination
     * @return the ResponseEntity with status 200 (OK) and with body the shortest path to a city represented by ConnectionDTO,
     * or with status 400 (Bad Request) if the ConnectionDTO is not valid,
     * or with status 500 (Internal Server Error) if the there was a problem with the search
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @ApiOperation(value = "Search the shortest path to a city", response = ConnectionDTO.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "The ResponseEntity with status 200 (OK) and with body the shortest path to a city represented by ConnectionDTO"),
        @ApiResponse(code = 400, message = "ConnectionDTO is not valid"),
        @ApiResponse(code = 500, message = "(Internal Server Error) if the there was a problem with the search"),
        @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    @GetMapping("/cities/path")
    @Timed
    public ResponseEntity<ConnectionDTO> cityShortestPath(@RequestParam("origin") String origin,
                                                          @RequestParam("destination") String destination) throws URISyntaxException {

        log.debug("REST request to search the shortest path from {} to {}", origin, destination);

        ConnectionDTO result = cityService.findNearestRouteToCity(origin, destination);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createAlert(ENTITY_NAME, result.toString()))
            .body(result);

    }
}
