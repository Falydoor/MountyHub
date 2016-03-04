package com.mountyhub.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mountyhub.app.domain.Gear;
import com.mountyhub.app.repository.GearRepository;
import com.mountyhub.app.web.rest.util.HeaderUtil;
import com.mountyhub.app.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Gear.
 */
@RestController
@RequestMapping("/api")
public class GearResource {

    private final Logger log = LoggerFactory.getLogger(GearResource.class);
        
    @Inject
    private GearRepository gearRepository;
    
    /**
     * POST  /gears -> Create a new gear.
     */
    @RequestMapping(value = "/gears",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Gear> createGear(@Valid @RequestBody Gear gear) throws URISyntaxException {
        log.debug("REST request to save Gear : {}", gear);
        if (gear.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("gear", "idexists", "A new gear cannot already have an ID")).body(null);
        }
        Gear result = gearRepository.save(gear);
        return ResponseEntity.created(new URI("/api/gears/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("gear", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /gears -> Updates an existing gear.
     */
    @RequestMapping(value = "/gears",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Gear> updateGear(@Valid @RequestBody Gear gear) throws URISyntaxException {
        log.debug("REST request to update Gear : {}", gear);
        if (gear.getId() == null) {
            return createGear(gear);
        }
        Gear result = gearRepository.save(gear);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("gear", gear.getId().toString()))
            .body(result);
    }

    /**
     * GET  /gears -> get all the gears.
     */
    @RequestMapping(value = "/gears",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Gear>> getAllGears(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Gears");
        Page<Gear> page = gearRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/gears");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /gears/:id -> get the "id" gear.
     */
    @RequestMapping(value = "/gears/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Gear> getGear(@PathVariable Long id) {
        log.debug("REST request to get Gear : {}", id);
        Gear gear = gearRepository.findOne(id);
        return Optional.ofNullable(gear)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /gears/:id -> delete the "id" gear.
     */
    @RequestMapping(value = "/gears/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteGear(@PathVariable Long id) {
        log.debug("REST request to delete Gear : {}", id);
        gearRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("gear", id.toString())).build();
    }
}
