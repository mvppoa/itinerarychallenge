package com.mvppoa.adidas.web.rest;

import com.mvppoa.adidas.AdidascityitineraryApp;

import com.mvppoa.adidas.domain.Itinerary;
import com.mvppoa.adidas.repository.ItineraryRepository;
import com.mvppoa.adidas.service.ItineraryService;
import com.mvppoa.adidas.service.dto.ItineraryDTO;
import com.mvppoa.adidas.service.mapper.ItineraryMapper;
import com.mvppoa.adidas.web.rest.errors.ExceptionTranslator;
import com.mvppoa.adidas.service.dto.ItineraryCriteria;
import com.mvppoa.adidas.service.ItineraryQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;


import static com.mvppoa.adidas.web.rest.TestUtil.sameInstant;
import static com.mvppoa.adidas.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ItineraryResource REST controller.
 *
 * @see ItineraryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AdidascityitineraryApp.class)
public class ItineraryResourceIntTest {

    private static final String DEFAULT_ORIGIN_CITY = "AAAAAAAAAA";
    private static final String UPDATED_ORIGIN_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_DESTINATION_CITY = "AAAAAAAAAA";
    private static final String UPDATED_DESTINATION_CITY = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DEPARTURE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DEPARTURE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_ARRIVAL_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_ARRIVAL_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private ItineraryRepository itineraryRepository;


    @Autowired
    private ItineraryMapper itineraryMapper;


    @Autowired
    private ItineraryService itineraryService;

    @Autowired
    private ItineraryQueryService itineraryQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restItineraryMockMvc;

    private Itinerary itinerary;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ItineraryResource itineraryResource = new ItineraryResource(itineraryService, itineraryQueryService);
        this.restItineraryMockMvc = MockMvcBuilders.standaloneSetup(itineraryResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Itinerary createEntity(EntityManager em) {
        Itinerary itinerary = new Itinerary()
            .originCity(DEFAULT_ORIGIN_CITY)
            .destinationCity(DEFAULT_DESTINATION_CITY)
            .departureTime(DEFAULT_DEPARTURE_TIME)
            .arrivalTime(DEFAULT_ARRIVAL_TIME);
        return itinerary;
    }

    @Before
    public void initTest() {
        itinerary = createEntity(em);
    }

    @Test
    @Transactional
    public void createItinerary() throws Exception {
        int databaseSizeBeforeCreate = itineraryRepository.findAll().size();

        // Create the Itinerary
        ItineraryDTO itineraryDTO = itineraryMapper.toDto(itinerary);
        restItineraryMockMvc.perform(post("/api/itineraries/complete")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(itineraryDTO)))
            .andExpect(status().isCreated());

        // Validate the Itinerary in the database
        List<Itinerary> itineraryList = itineraryRepository.findAll();
        assertThat(itineraryList).hasSize(databaseSizeBeforeCreate + 1);
        Itinerary testItinerary = itineraryList.get(itineraryList.size() - 1);
        assertThat(testItinerary.getOriginCity()).isEqualTo(DEFAULT_ORIGIN_CITY);
        assertThat(testItinerary.getDestinationCity()).isEqualTo(DEFAULT_DESTINATION_CITY);
        assertThat(testItinerary.getDepartureTime()).isEqualTo(DEFAULT_DEPARTURE_TIME);
        assertThat(testItinerary.getArrivalTime()).isEqualTo(DEFAULT_ARRIVAL_TIME);
    }

    @Test
    @Transactional
    public void createItineraryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = itineraryRepository.findAll().size();

        // Create the Itinerary with an existing ID
        itinerary.setId(1L);
        ItineraryDTO itineraryDTO = itineraryMapper.toDto(itinerary);

        // An entity with an existing ID cannot be created, so this API call must fail
        restItineraryMockMvc.perform(post("/api/itineraries/complete")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(itineraryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Itinerary in the database
        List<Itinerary> itineraryList = itineraryRepository.findAll();
        assertThat(itineraryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllItineraries() throws Exception {
        // Initialize the database
        itineraryRepository.saveAndFlush(itinerary);

        // Get all the itineraryList
        restItineraryMockMvc.perform(get("/api/itineraries?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(itinerary.getId().intValue())))
            .andExpect(jsonPath("$.[*].originCity").value(hasItem(DEFAULT_ORIGIN_CITY.toString())))
            .andExpect(jsonPath("$.[*].destinationCity").value(hasItem(DEFAULT_DESTINATION_CITY.toString())))
            .andExpect(jsonPath("$.[*].departureTime").value(hasItem(sameInstant(DEFAULT_DEPARTURE_TIME))))
            .andExpect(jsonPath("$.[*].arrivalTime").value(hasItem(sameInstant(DEFAULT_ARRIVAL_TIME))));
    }


    @Test
    @Transactional
    public void getItinerary() throws Exception {
        // Initialize the database
        itineraryRepository.saveAndFlush(itinerary);

        // Get the itinerary
        restItineraryMockMvc.perform(get("/api/itineraries/{id}", itinerary.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(itinerary.getId().intValue()))
            .andExpect(jsonPath("$.originCity").value(DEFAULT_ORIGIN_CITY.toString()))
            .andExpect(jsonPath("$.destinationCity").value(DEFAULT_DESTINATION_CITY.toString()))
            .andExpect(jsonPath("$.departureTime").value(sameInstant(DEFAULT_DEPARTURE_TIME)))
            .andExpect(jsonPath("$.arrivalTime").value(sameInstant(DEFAULT_ARRIVAL_TIME)));
    }

    @Test
    @Transactional
    public void getAllItinerariesByOriginCityIsEqualToSomething() throws Exception {
        // Initialize the database
        itineraryRepository.saveAndFlush(itinerary);

        // Get all the itineraryList where originCity equals to DEFAULT_ORIGIN_CITY
        defaultItineraryShouldBeFound("originCity.equals=" + DEFAULT_ORIGIN_CITY);

        // Get all the itineraryList where originCity equals to UPDATED_ORIGIN_CITY
        defaultItineraryShouldNotBeFound("originCity.equals=" + UPDATED_ORIGIN_CITY);
    }

    @Test
    @Transactional
    public void getAllItinerariesByOriginCityIsInShouldWork() throws Exception {
        // Initialize the database
        itineraryRepository.saveAndFlush(itinerary);

        // Get all the itineraryList where originCity in DEFAULT_ORIGIN_CITY or UPDATED_ORIGIN_CITY
        defaultItineraryShouldBeFound("originCity.in=" + DEFAULT_ORIGIN_CITY + "," + UPDATED_ORIGIN_CITY);

        // Get all the itineraryList where originCity equals to UPDATED_ORIGIN_CITY
        defaultItineraryShouldNotBeFound("originCity.in=" + UPDATED_ORIGIN_CITY);
    }

    @Test
    @Transactional
    public void getAllItinerariesByOriginCityIsNullOrNotNull() throws Exception {
        // Initialize the database
        itineraryRepository.saveAndFlush(itinerary);

        // Get all the itineraryList where originCity is not null
        defaultItineraryShouldBeFound("originCity.specified=true");

        // Get all the itineraryList where originCity is null
        defaultItineraryShouldNotBeFound("originCity.specified=false");
    }

    @Test
    @Transactional
    public void getAllItinerariesByDestinationCityIsEqualToSomething() throws Exception {
        // Initialize the database
        itineraryRepository.saveAndFlush(itinerary);

        // Get all the itineraryList where destinationCity equals to DEFAULT_DESTINATION_CITY
        defaultItineraryShouldBeFound("destinationCity.equals=" + DEFAULT_DESTINATION_CITY);

        // Get all the itineraryList where destinationCity equals to UPDATED_DESTINATION_CITY
        defaultItineraryShouldNotBeFound("destinationCity.equals=" + UPDATED_DESTINATION_CITY);
    }

    @Test
    @Transactional
    public void getAllItinerariesByDestinationCityIsInShouldWork() throws Exception {
        // Initialize the database
        itineraryRepository.saveAndFlush(itinerary);

        // Get all the itineraryList where destinationCity in DEFAULT_DESTINATION_CITY or UPDATED_DESTINATION_CITY
        defaultItineraryShouldBeFound("destinationCity.in=" + DEFAULT_DESTINATION_CITY + "," + UPDATED_DESTINATION_CITY);

        // Get all the itineraryList where destinationCity equals to UPDATED_DESTINATION_CITY
        defaultItineraryShouldNotBeFound("destinationCity.in=" + UPDATED_DESTINATION_CITY);
    }

    @Test
    @Transactional
    public void getAllItinerariesByDestinationCityIsNullOrNotNull() throws Exception {
        // Initialize the database
        itineraryRepository.saveAndFlush(itinerary);

        // Get all the itineraryList where destinationCity is not null
        defaultItineraryShouldBeFound("destinationCity.specified=true");

        // Get all the itineraryList where destinationCity is null
        defaultItineraryShouldNotBeFound("destinationCity.specified=false");
    }

    @Test
    @Transactional
    public void getAllItinerariesByDepartureTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        itineraryRepository.saveAndFlush(itinerary);

        // Get all the itineraryList where departureTime equals to DEFAULT_DEPARTURE_TIME
        defaultItineraryShouldBeFound("departureTime.equals=" + DEFAULT_DEPARTURE_TIME);

        // Get all the itineraryList where departureTime equals to UPDATED_DEPARTURE_TIME
        defaultItineraryShouldNotBeFound("departureTime.equals=" + UPDATED_DEPARTURE_TIME);
    }

    @Test
    @Transactional
    public void getAllItinerariesByDepartureTimeIsInShouldWork() throws Exception {
        // Initialize the database
        itineraryRepository.saveAndFlush(itinerary);

        // Get all the itineraryList where departureTime in DEFAULT_DEPARTURE_TIME or UPDATED_DEPARTURE_TIME
        defaultItineraryShouldBeFound("departureTime.in=" + DEFAULT_DEPARTURE_TIME + "," + UPDATED_DEPARTURE_TIME);

        // Get all the itineraryList where departureTime equals to UPDATED_DEPARTURE_TIME
        defaultItineraryShouldNotBeFound("departureTime.in=" + UPDATED_DEPARTURE_TIME);
    }

    @Test
    @Transactional
    public void getAllItinerariesByDepartureTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        itineraryRepository.saveAndFlush(itinerary);

        // Get all the itineraryList where departureTime is not null
        defaultItineraryShouldBeFound("departureTime.specified=true");

        // Get all the itineraryList where departureTime is null
        defaultItineraryShouldNotBeFound("departureTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllItinerariesByDepartureTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        itineraryRepository.saveAndFlush(itinerary);

        // Get all the itineraryList where departureTime greater than or equals to DEFAULT_DEPARTURE_TIME
        defaultItineraryShouldBeFound("departureTime.greaterOrEqualThan=" + DEFAULT_DEPARTURE_TIME);

        // Get all the itineraryList where departureTime greater than or equals to UPDATED_DEPARTURE_TIME
        defaultItineraryShouldNotBeFound("departureTime.greaterOrEqualThan=" + UPDATED_DEPARTURE_TIME);
    }

    @Test
    @Transactional
    public void getAllItinerariesByDepartureTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        itineraryRepository.saveAndFlush(itinerary);

        // Get all the itineraryList where departureTime less than or equals to DEFAULT_DEPARTURE_TIME
        defaultItineraryShouldNotBeFound("departureTime.lessThan=" + DEFAULT_DEPARTURE_TIME);

        // Get all the itineraryList where departureTime less than or equals to UPDATED_DEPARTURE_TIME
        defaultItineraryShouldBeFound("departureTime.lessThan=" + UPDATED_DEPARTURE_TIME);
    }


    @Test
    @Transactional
    public void getAllItinerariesByArrivalTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        itineraryRepository.saveAndFlush(itinerary);

        // Get all the itineraryList where arrivalTime equals to DEFAULT_ARRIVAL_TIME
        defaultItineraryShouldBeFound("arrivalTime.equals=" + DEFAULT_ARRIVAL_TIME);

        // Get all the itineraryList where arrivalTime equals to UPDATED_ARRIVAL_TIME
        defaultItineraryShouldNotBeFound("arrivalTime.equals=" + UPDATED_ARRIVAL_TIME);
    }

    @Test
    @Transactional
    public void getAllItinerariesByArrivalTimeIsInShouldWork() throws Exception {
        // Initialize the database
        itineraryRepository.saveAndFlush(itinerary);

        // Get all the itineraryList where arrivalTime in DEFAULT_ARRIVAL_TIME or UPDATED_ARRIVAL_TIME
        defaultItineraryShouldBeFound("arrivalTime.in=" + DEFAULT_ARRIVAL_TIME + "," + UPDATED_ARRIVAL_TIME);

        // Get all the itineraryList where arrivalTime equals to UPDATED_ARRIVAL_TIME
        defaultItineraryShouldNotBeFound("arrivalTime.in=" + UPDATED_ARRIVAL_TIME);
    }

    @Test
    @Transactional
    public void getAllItinerariesByArrivalTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        itineraryRepository.saveAndFlush(itinerary);

        // Get all the itineraryList where arrivalTime is not null
        defaultItineraryShouldBeFound("arrivalTime.specified=true");

        // Get all the itineraryList where arrivalTime is null
        defaultItineraryShouldNotBeFound("arrivalTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllItinerariesByArrivalTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        itineraryRepository.saveAndFlush(itinerary);

        // Get all the itineraryList where arrivalTime greater than or equals to DEFAULT_ARRIVAL_TIME
        defaultItineraryShouldBeFound("arrivalTime.greaterOrEqualThan=" + DEFAULT_ARRIVAL_TIME);

        // Get all the itineraryList where arrivalTime greater than or equals to UPDATED_ARRIVAL_TIME
        defaultItineraryShouldNotBeFound("arrivalTime.greaterOrEqualThan=" + UPDATED_ARRIVAL_TIME);
    }

    @Test
    @Transactional
    public void getAllItinerariesByArrivalTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        itineraryRepository.saveAndFlush(itinerary);

        // Get all the itineraryList where arrivalTime less than or equals to DEFAULT_ARRIVAL_TIME
        defaultItineraryShouldNotBeFound("arrivalTime.lessThan=" + DEFAULT_ARRIVAL_TIME);

        // Get all the itineraryList where arrivalTime less than or equals to UPDATED_ARRIVAL_TIME
        defaultItineraryShouldBeFound("arrivalTime.lessThan=" + UPDATED_ARRIVAL_TIME);
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultItineraryShouldBeFound(String filter) throws Exception {
        restItineraryMockMvc.perform(get("/api/itineraries?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(itinerary.getId().intValue())))
            .andExpect(jsonPath("$.[*].originCity").value(hasItem(DEFAULT_ORIGIN_CITY.toString())))
            .andExpect(jsonPath("$.[*].destinationCity").value(hasItem(DEFAULT_DESTINATION_CITY.toString())))
            .andExpect(jsonPath("$.[*].departureTime").value(hasItem(sameInstant(DEFAULT_DEPARTURE_TIME))))
            .andExpect(jsonPath("$.[*].arrivalTime").value(hasItem(sameInstant(DEFAULT_ARRIVAL_TIME))));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultItineraryShouldNotBeFound(String filter) throws Exception {
        restItineraryMockMvc.perform(get("/api/itineraries?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingItinerary() throws Exception {
        // Get the itinerary
        restItineraryMockMvc.perform(get("/api/itineraries/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateItinerary() throws Exception {
        // Initialize the database
        itineraryRepository.saveAndFlush(itinerary);

        int databaseSizeBeforeUpdate = itineraryRepository.findAll().size();

        // Update the itinerary
        Itinerary updatedItinerary = itineraryRepository.findById(itinerary.getId()).get();
        // Disconnect from session so that the updates on updatedItinerary are not directly saved in db
        em.detach(updatedItinerary);
        updatedItinerary
            .originCity(UPDATED_ORIGIN_CITY)
            .destinationCity(UPDATED_DESTINATION_CITY)
            .departureTime(UPDATED_DEPARTURE_TIME)
            .arrivalTime(UPDATED_ARRIVAL_TIME);
        ItineraryDTO itineraryDTO = itineraryMapper.toDto(updatedItinerary);

        restItineraryMockMvc.perform(put("/api/itineraries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(itineraryDTO)))
            .andExpect(status().isOk());

        // Validate the Itinerary in the database
        List<Itinerary> itineraryList = itineraryRepository.findAll();
        assertThat(itineraryList).hasSize(databaseSizeBeforeUpdate);
        Itinerary testItinerary = itineraryList.get(itineraryList.size() - 1);
        assertThat(testItinerary.getOriginCity()).isEqualTo(UPDATED_ORIGIN_CITY);
        assertThat(testItinerary.getDestinationCity()).isEqualTo(UPDATED_DESTINATION_CITY);
        assertThat(testItinerary.getDepartureTime()).isEqualTo(UPDATED_DEPARTURE_TIME);
        assertThat(testItinerary.getArrivalTime()).isEqualTo(UPDATED_ARRIVAL_TIME);
    }

    @Test
    @Transactional
    public void updateNonExistingItinerary() throws Exception {
        int databaseSizeBeforeUpdate = itineraryRepository.findAll().size();

        // Create the Itinerary
        ItineraryDTO itineraryDTO = itineraryMapper.toDto(itinerary);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restItineraryMockMvc.perform(put("/api/itineraries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(itineraryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Itinerary in the database
        List<Itinerary> itineraryList = itineraryRepository.findAll();
        assertThat(itineraryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteItinerary() throws Exception {
        // Initialize the database
        itineraryRepository.saveAndFlush(itinerary);

        int databaseSizeBeforeDelete = itineraryRepository.findAll().size();

        // Get the itinerary
        restItineraryMockMvc.perform(delete("/api/itineraries/{id}", itinerary.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Itinerary> itineraryList = itineraryRepository.findAll();
        assertThat(itineraryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Itinerary.class);
        Itinerary itinerary1 = new Itinerary();
        itinerary1.setId(1L);
        Itinerary itinerary2 = new Itinerary();
        itinerary2.setId(itinerary1.getId());
        assertThat(itinerary1).isEqualTo(itinerary2);
        itinerary2.setId(2L);
        assertThat(itinerary1).isNotEqualTo(itinerary2);
        itinerary1.setId(null);
        assertThat(itinerary1).isNotEqualTo(itinerary2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ItineraryDTO.class);
        ItineraryDTO itineraryDTO1 = new ItineraryDTO();
        itineraryDTO1.setId(1L);
        ItineraryDTO itineraryDTO2 = new ItineraryDTO();
        assertThat(itineraryDTO1).isNotEqualTo(itineraryDTO2);
        itineraryDTO2.setId(itineraryDTO1.getId());
        assertThat(itineraryDTO1).isEqualTo(itineraryDTO2);
        itineraryDTO2.setId(2L);
        assertThat(itineraryDTO1).isNotEqualTo(itineraryDTO2);
        itineraryDTO1.setId(null);
        assertThat(itineraryDTO1).isNotEqualTo(itineraryDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(itineraryMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(itineraryMapper.fromId(null)).isNull();
    }
}
