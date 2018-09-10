package com.mvppoa.adidas.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.mvppoa.adidas.domain.Itinerary;
import com.mvppoa.adidas.domain.*; // for static metamodels
import com.mvppoa.adidas.repository.ItineraryRepository;
import com.mvppoa.adidas.service.dto.ItineraryCriteria;

import com.mvppoa.adidas.service.dto.ItineraryDTO;
import com.mvppoa.adidas.service.mapper.ItineraryMapper;

/**
 * Service for executing complex queries for Itinerary entities in the database.
 * The main input is a {@link ItineraryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ItineraryDTO} or a {@link Page} of {@link ItineraryDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ItineraryQueryService extends QueryService<Itinerary> {

    private final Logger log = LoggerFactory.getLogger(ItineraryQueryService.class);

    private final ItineraryRepository itineraryRepository;

    private final ItineraryMapper itineraryMapper;

    public ItineraryQueryService(ItineraryRepository itineraryRepository, ItineraryMapper itineraryMapper) {
        this.itineraryRepository = itineraryRepository;
        this.itineraryMapper = itineraryMapper;
    }

    /**
     * Return a {@link List} of {@link ItineraryDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ItineraryDTO> findByCriteria(ItineraryCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Itinerary> specification = createSpecification(criteria);
        return itineraryMapper.toDto(itineraryRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ItineraryDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ItineraryDTO> findByCriteria(ItineraryCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Itinerary> specification = createSpecification(criteria);
        return itineraryRepository.findAll(specification, page)
            .map(itineraryMapper::toDto);
    }

    /**
     * Function to convert ItineraryCriteria to a {@link Specification}
     */
    private Specification<Itinerary> createSpecification(ItineraryCriteria criteria) {
        Specification<Itinerary> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Itinerary_.id));
            }
            if (criteria.getOriginCity() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOriginCity(), Itinerary_.originCity));
            }
            if (criteria.getDestinationCity() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDestinationCity(), Itinerary_.destinationCity));
            }
            if (criteria.getDepartureTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDepartureTime(), Itinerary_.departureTime));
            }
            if (criteria.getArrivalTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getArrivalTime(), Itinerary_.arrivalTime));
            }
        }
        return specification;
    }

}
