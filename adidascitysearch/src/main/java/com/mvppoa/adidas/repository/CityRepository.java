package com.mvppoa.adidas.repository;

import com.mvppoa.adidas.domain.City;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CityRepository extends Neo4jRepository<City, Long> {
    @Query("MATCH p = allShortestPaths((s {name: {incoming}})-[r:connect_to*]->(d {name: {outgoing}})) RETURN p")
    List<Object> findByIncomingAndOutgoing(@Param("incoming") String incoming, @Param("outgoing") String outgoing);

}
