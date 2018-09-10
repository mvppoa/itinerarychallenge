package com.mvppoa.adidas.client;

import com.codahale.metrics.annotation.Timed;
import com.mvppoa.adidas.config.FeignConfigure;
import com.mvppoa.adidas.service.dto.ConnectionDTO;
import com.mvppoa.adidas.web.rest.errors.ConnectionServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.websocket.server.PathParam;

/**
 * @author Marcelo.Fachinelli
 */

@FeignClient(value = "adidascitysearch", path = "/api", configuration = FeignConfigure.class)
@RefreshScope
public interface CityPathClient {

    @GetMapping("/cities/path")
    @Timed
    ConnectionDTO getShortestPathAndConnections(@RequestParam("origin") String origin, @RequestParam("destination") String destination);

    @Component
    class CityPathClientFallback implements CityPathClient {

        private final Logger log = LoggerFactory.getLogger(CityPathClientFallback.class);

        @Override
        public ConnectionDTO getShortestPathAndConnections(@PathParam("origin") String origin, @PathParam("destination") String destination) {
            log.error("Could not access: adidascitysearch");
            throw new ConnectionServerException("Could not access: adidascitysearch");
        }

    }
}
