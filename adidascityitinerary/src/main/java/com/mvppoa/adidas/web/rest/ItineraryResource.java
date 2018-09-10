package com.mvppoa.adidas.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mvppoa.adidas.service.ItineraryService;
import com.mvppoa.adidas.service.dto.ConnectionDTO;
import com.mvppoa.adidas.service.dto.ItineraryWithoutArrivalTimeDTO;
import com.mvppoa.adidas.web.rest.errors.BadRequestAlertException;
import com.mvppoa.adidas.web.rest.util.HeaderUtil;
import com.mvppoa.adidas.service.dto.ItineraryDTO;
import com.mvppoa.adidas.service.dto.ItineraryCriteria;
import com.mvppoa.adidas.service.ItineraryQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Itinerary.
 */
@RestController
@RequestMapping("/api")
public class ItineraryResource {

    private final Logger log = LoggerFactory.getLogger(ItineraryResource.class);

    private static final String ENTITY_NAME = "itinerary";

    private final ItineraryService itineraryService;

    private final ItineraryQueryService itineraryQueryService;

    public ItineraryResource(ItineraryService itineraryService, ItineraryQueryService itineraryQueryService) {
        this.itineraryService = itineraryService;
        this.itineraryQueryService = itineraryQueryService;
    }

    /**
     * POST  /itineraries : Create a new itinerary without checking city paths.
     *
     * @param itineraryDTO the itineraryDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new itineraryDTO, or with status 400 (Bad Request) if the itinerary has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @ApiOperation(value = "Create a new itinerary without checking city paths", response = ItineraryDTO.class)
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "(Created) and with body the new itineraryDTO"),
        @ApiResponse(code = 400, message = "(Bad Request) if the itinerary has already an ID")
    })
    @PostMapping("/itineraries")
    @Timed
    public ResponseEntity<ItineraryDTO> createItinerary(@RequestBody ItineraryWithoutArrivalTimeDTO itineraryDTO) throws URISyntaxException {
        log.debug("REST request to save Itinerary : {}", itineraryDTO);
        if (itineraryDTO.getId() != null) {
            throw new BadRequestAlertException("A new itinerary cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ItineraryDTO result = itineraryService.save(itineraryDTO);
        return ResponseEntity.created(new URI("/api/itineraries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * POST  /itineraries : Create a new itinerary.
     *
     * @param itineraryDTO the itineraryDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new itineraryDTO, or with status 400 (Bad Request) if the itinerary has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @ApiOperation(value = "Create a new itinerary", response = ItineraryDTO.class)
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "(Created) and with body the new itineraryDTO"),
        @ApiResponse(code = 400, message = "(Bad Request) if the itinerary has already an ID")
    })
    @PostMapping("/itineraries/complete")
    @Timed
    public ResponseEntity<ItineraryDTO> createCompleteItinerary(@RequestBody ItineraryDTO itineraryDTO) throws URISyntaxException {
        log.debug("REST request to save Itinerary : {}", itineraryDTO);
        if (itineraryDTO.getId() != null) {
            throw new BadRequestAlertException("A new itinerary cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ItineraryDTO result = itineraryService.save(itineraryDTO);
        return ResponseEntity.created(new URI("/api/itineraries/complete/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /itineraries : Updates an existing itinerary.
     *
     * @param itineraryDTO the itineraryDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated itineraryDTO,
     * or with status 400 (Bad Request) if the itineraryDTO is not valid,
     * or with status 500 (Internal Server Error) if the itineraryDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @ApiOperation(value = "Updates an existing itinerary", response = ItineraryDTO.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "(OK) and with body the updated itineraryDTO"),
        @ApiResponse(code = 400, message = "(Bad Request) if the itineraryDTO is not valid"),
        @ApiResponse(code = 500, message = "(Internal Server Error) if the itineraryDTO couldn't be updated")
    })
    @PutMapping("/itineraries")
    @Timed
    public ResponseEntity<ItineraryDTO> updateItinerary(@RequestBody ItineraryDTO itineraryDTO) throws URISyntaxException {
        log.debug("REST request to update Itinerary : {}", itineraryDTO);
        if (itineraryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ItineraryDTO result = itineraryService.save(itineraryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, itineraryDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /itineraries : get all the itineraries.
     *
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of itineraries in body
     */
    @ApiOperation(value = "Get all the itineraries", response = List.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "(OK) and the list of itineraries in body"),
    })
    @GetMapping("/itineraries")
    @Timed
    public ResponseEntity<List<ItineraryDTO>> getAllItineraries(ItineraryCriteria criteria) {
        log.debug("REST request to get Itineraries by criteria: {}", criteria);
        List<ItineraryDTO> entityList = itineraryQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * GET  /itineraries/:id : get the "id" itinerary.
     *
     * @param id the id of the itineraryDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the itineraryDTO, or with status 404 (Not Found)
     */
    @ApiOperation(value = "Get the \"id\" itinerary", response = List.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "(OK) and with body the itineraryDTO"),
        @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    @GetMapping("/itineraries/{id}")
    @Timed
    public ResponseEntity<ItineraryDTO> getItinerary(@PathVariable Long id) {
        log.debug("REST request to get Itinerary : {}", id);
        Optional<ItineraryDTO> itineraryDTO = itineraryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(itineraryDTO);
    }

    /**
     * DELETE  /itineraries/:id : delete the "id" itinerary.
     *
     * @param id the id of the itineraryDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @ApiOperation(value = "Delete the \"id\" itinerary", response = Void.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "(OK) to represent successful deletion"),
    })
    @DeleteMapping("/itineraries/{id}")
    @Timed
    public ResponseEntity<Void> deleteItinerary(@PathVariable Long id) {
        log.debug("REST request to delete Itinerary : {}", id);
        itineraryService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
