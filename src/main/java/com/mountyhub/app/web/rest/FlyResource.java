package com.mountyhub.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mountyhub.app.domain.Fly;
import com.mountyhub.app.repository.FlyRepository;
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
 * REST controller for managing Fly.
 */
@RestController
@RequestMapping("/api")
public class FlyResource {

    private final Logger log = LoggerFactory.getLogger(FlyResource.class);
        
    @Inject
    private FlyRepository flyRepository;
    
    /**
     * POST  /flys -> Create a new fly.
     */
    @RequestMapping(value = "/flys",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Fly> createFly(@Valid @RequestBody Fly fly) throws URISyntaxException {
        log.debug("REST request to save Fly : {}", fly);
        if (fly.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("fly", "idexists", "A new fly cannot already have an ID")).body(null);
        }
        Fly result = flyRepository.save(fly);
        return ResponseEntity.created(new URI("/api/flys/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("fly", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /flys -> Updates an existing fly.
     */
    @RequestMapping(value = "/flys",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Fly> updateFly(@Valid @RequestBody Fly fly) throws URISyntaxException {
        log.debug("REST request to update Fly : {}", fly);
        if (fly.getId() == null) {
            return createFly(fly);
        }
        Fly result = flyRepository.save(fly);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("fly", fly.getId().toString()))
            .body(result);
    }

    /**
     * GET  /flys -> get all the flys.
     */
    @RequestMapping(value = "/flys",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Fly>> getAllFlys(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Flys");
        Page<Fly> page = flyRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/flys");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /flys/:id -> get the "id" fly.
     */
    @RequestMapping(value = "/flys/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Fly> getFly(@PathVariable Long id) {
        log.debug("REST request to get Fly : {}", id);
        Fly fly = flyRepository.findOne(id);
        return Optional.ofNullable(fly)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /flys/:id -> delete the "id" fly.
     */
    @RequestMapping(value = "/flys/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteFly(@PathVariable Long id) {
        log.debug("REST request to delete Fly : {}", id);
        flyRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("fly", id.toString())).build();
    }
}
