package com.mvppoa.adidas.domain;

import lombok.Data;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Set;

@NodeEntity
@Data
public class City {

    @Id
    private Long id;

    private String name;

    @Property(name = "time_zone")
    private String timeZone;


}
