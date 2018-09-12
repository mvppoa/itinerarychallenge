# ItineraryChallenge

Solution for the itinerary challenge.

This project has a two micro services that registers into eureka and each one has it's own database.
Neo4j was used to handle the connection between the cities to search the fastest path with less connections.
Postgres was used to save the itinerary between the cities.

You can either create a raw itinerary from one city to another or let the application handle
the distance and travel time between the origin and the destination city.

## Technologies Used

Bellow is the list of the used technologies for this project.

- Docker: Used to run applications in containers and ease their the deploy and scaling. https://www.docker.com/
- Docker Compose: Used to build multiple docker applications using a single command.
- JHipster: Used in order to generate the boilerplate for the apps. https://www.jhipster.tech/
- SpringBoot2: Used to create the micro-services, many libraries were used varying from spring-web-mvc to spring-cloud. https://spring.io/projects/spring-boot
- Eureka: Used for service discovery. https://github.com/Netflix/eureka
- Feign Client: Used to fetch data and to handle the circuit break pattern (uses hystrix and ribbon). https://cloud.spring.io/spring-cloud-netflix/multi/multi_spring-cloud-feign.html
- Swagger: Used to provide information to navigate the site's REST interfaces dynamically by including hypermedia links with the responses. https://swagger.io/
- Postgres: Relational database used to store itinerary data. https://www.postgresql.org/
- Neo4: Graph NoSQL database used to store the path and the distance between the cities. https://neo4j.com/ 
- Java8: The micro services were created to run using java8

## Documentation

You may find the documentation for the two created micro services in the following urls:
- https://app.swaggerhub.com/apis/mvppoa/adidascityitinerary-api/docs/0.0.1
- https://app.swaggerhub.com/apis/mvppoa/adidascitysearch-api/docs/0.0.1

In order to execute a few commands you may try the following commands:

The list of cities you may find bellow in the database load script.

##### Fetch the shortest route between two cities:
```
GET http://localhost:8081/api/cities/path?origin=Palo Alto&destination=Milpitas
```

##### Add a new itinerary using the shortest route
```
POST http://localhost:8082/api/itineraries

{
	"originCity": "Milpitas",
	"destinationCity": "Palo Alto",
	"departureTime": "2018-09-10T03:00:00.001-0700"
}
```

##### Fetching an itinerary
```
GET http://localhost:8082/api/itineraries/1051
```

## How to run

Download the project to your machine and run the command

```
docker-compose up -d
```

To check data on neo4j, use the following url:
```
http://localhost:7474/browse/

Username: neo4j
Password: test
```

To check jhipster's eureka service discovery implementation:
```
http://localhost:8761/

Username: admin
Password: admin
```
There is already some data loaded up in the project. 
If you want to start fresh database you may use the following commands:

#### Neo4J
```
CREATE (n01:City {name: 'Mountain View', time_zone: 'GMT-7'}),
 (n02:City {name: 'Palo Alto', timeZone: 'GMT-7'}),
 (n18:City {name: 'Sunnyvale', timeZone: 'GMT-7'}),
 (n14:City {name: 'Fremont', timeZone: 'GMT-7'}),
 (n15:City {name: 'Milpitas', timeZone: 'GMT-7'}),
 (n16:City {name: 'Santa Clara', timeZone: 'GMT-7'}),
 (n17:City {name: 'San Jose', timeZone: 'GMT-7'}),
 (n19:City {name: 'Cupertino', timeZone: 'GMT-7'}),
 (n26:City {name: 'Athetron', timeZone: 'GMT-7'}),
 (n01)-[:connect_to {travel_time: 900}]->(n02), // mtv - pa
 (n01)-[:connect_to {travel_time: 900}]->(n18), // mtv - snyl
 (n02)-[:connect_to {travel_time: 800}]->(n26), // pa - atht
 (n26)-[:connect_to {travel_time: 2787}]->(n14), // atht - frmt
 (n14)-[:connect_to {travel_time: 1826}]->(n15), // frmt - mlpt
 (n16)-[:connect_to {travel_time: 831}]->(n18), // stcl - snyl
 (n16)-[:connect_to {travel_time: 668}]->(n17), // stcl - sjs
 (n16)-[:connect_to {travel_time: 1206}]->(n19), // stcl - cptn
 (n17)-[:connect_to {travel_time: 1923}]->(n15), // sjs - mlpt
 (n17)-[:connect_to {travel_time: 1679}]->(n19), // stcl - cptn
 (n18)-[:connect_to {travel_time: 1630}]->(n15), // snyl - mlpt
 (n18)-[:connect_to {travel_time: 538}]->(n19), // snyl - cptn
 (n02)-[:connect_to {travel_time: 900}]->(n01), // pa - mtv <<==start reverse direction
 (n18)-[:connect_to {travel_time: 900}]->(n01), // snyl -mtv
 (n26)-[:connect_to {travel_time: 800}]->(n02), //atht - pa
 (n14)-[:connect_to {travel_time: 2787}]->(n26), // frmt - atht
 (n15)-[:connect_to {travel_time: 1826}]->(n14), // mlpt - frmt 
 (n18)-[:connect_to {travel_time: 831}]->(n16), // snyl - stcl
 (n17)-[:connect_to {travel_time: 668}]->(n16), // sjs - stcl
 (n19)-[:connect_to {travel_time: 1206}]->(n16), // cptn - stcl
 (n17)-[:connect_to {travel_time: 1923}]->(n15), // mlpt - sjs
 (n19)-[:connect_to {travel_time: 1679}]->(n17), // cptn - stcl
 (n15)-[:connect_to {travel_time: 1630}]->(n18), // mlpt - snyl
 (n19)-[:connect_to {travel_time: 500}]->(n18) // cptn - snyl
```

#### Postgres
```
INSERT INTO public.itinerary (id, origin_city, destination_city, departure_time, arrival_time) VALUES (111051, 'Palo Alto', 'Milpitas', '2018-09-10 07:00:00.001000', '2018-09-10 07:13:20.001000');
INSERT INTO public.itinerary (id, origin_city, destination_city, departure_time, arrival_time) VALUES (111101, 'Milpitas', 'Palo Alto', '2018-09-10 07:00:00.001000', '2018-09-10 07:57:10.001000');
```

If you wish to run the ms without docker, use the docker-file-lite.yml file in order to 

## TODO
- Add Kubernetes support (this means changing from docker-compose to kubernetes)
- Add Zuul gateway in order to route 
- Enable security by implementing a oauth2 service and enabling then in the ms projects (the ms' are already prepared to use security, I was just in doubt if which authentication I could add to the project (OAUTH2 / JWT), in the end I almost ran out of time in order to implement this so I left it for a future release).
- Add CircleCI for continuous integration
- Add more unit tests in order to cover more code
- Create a new micro service in order to segregate the functionalities (The itinerary microservice wasn't supposed to connect directly into citypath ms).
- Refine neo4j ms to add more features and optimize the query
- Fix code smells using sonar lint
- Add ELK stack in order to store the logs
- Add elasticsearch for itinerary ms 
- Pipeline proposal. I was thinking of creating a model based on https://www.atlassian.com/continuous-delivery/continuous-delivery-workflows-with-feature-branching-and-gitflow

## References
- https://neo4j.com/graphgist/learning-cypher-with-san-francisco-bay-map
- https://spring.io/guides/gs/accessing-data-neo4j/
- https://www.jhipster.tech/microservices-architecture/
