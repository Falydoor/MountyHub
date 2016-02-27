package com.mountyhub.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mountyhub.app.domain.Troll;
import com.mountyhub.app.repository.TrollRepository;
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
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * REST controller for managing Troll.
 */
@RestController
@RequestMapping("/api")
public class TrollResource {

    private final Logger log = LoggerFactory.getLogger(TrollResource.class);
        
    @Inject
    private TrollRepository trollRepository;
    
    /**
     * POST  /trolls -> Create a new troll.
     */
    @RequestMapping(value = "/trolls",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Troll> createTroll(@Valid @RequestBody Troll troll) throws URISyntaxException {
        log.debug("REST request to save Troll : {}", troll);
        if (troll.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("troll", "idexists", "A new troll cannot already have an ID")).body(null);
        }
        Troll result = trollRepository.save(troll);
        return ResponseEntity.created(new URI("/api/trolls/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("troll", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /trolls -> Updates an existing troll.
     */
    @RequestMapping(value = "/trolls",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Troll> updateTroll(@Valid @RequestBody Troll troll) throws URISyntaxException {
        log.debug("REST request to update Troll : {}", troll);
        if (troll.getId() == null) {
            return createTroll(troll);
        }
        Troll result = trollRepository.save(troll);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("troll", troll.getId().toString()))
            .body(result);
    }

    /**
     * GET  /trolls -> get all the trolls.
     */
    @RequestMapping(value = "/trolls",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Troll>> getAllTrolls(Pageable pageable, @RequestParam(required = false) String filter)
        throws URISyntaxException {
        if ("user-is-null".equals(filter)) {
            log.debug("REST request to get all Trolls where user is null");
            return new ResponseEntity<>(StreamSupport
                .stream(trollRepository.findAll().spliterator(), false)
                .filter(troll -> troll.getUser() == null)
                .collect(Collectors.toList()), HttpStatus.OK);
        }
        log.debug("REST request to get a page of Trolls");
        Page<Troll> page = trollRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/trolls");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /trolls/:id -> get the "id" troll.
     */
    @RequestMapping(value = "/trolls/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Troll> getTroll(@PathVariable Long id) {
        log.debug("REST request to get Troll : {}", id);
        Troll troll = trollRepository.findOne(id);
        return Optional.ofNullable(troll)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /trolls/:id -> delete the "id" troll.
     */
    @RequestMapping(value = "/trolls/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTroll(@PathVariable Long id) {
        log.debug("REST request to delete Troll : {}", id);
        trollRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("troll", id.toString())).build();
    }
}
