package com.mountyhub.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mountyhub.app.domain.BonusMalus;
import com.mountyhub.app.repository.BonusMalusRepository;
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
 * REST controller for managing BonusMalus.
 */
@RestController
@RequestMapping("/api")
public class BonusMalusResource {

    private final Logger log = LoggerFactory.getLogger(BonusMalusResource.class);
        
    @Inject
    private BonusMalusRepository bonusMalusRepository;
    
    /**
     * POST  /bonusMaluss -> Create a new bonusMalus.
     */
    @RequestMapping(value = "/bonusMaluss",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BonusMalus> createBonusMalus(@Valid @RequestBody BonusMalus bonusMalus) throws URISyntaxException {
        log.debug("REST request to save BonusMalus : {}", bonusMalus);
        if (bonusMalus.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("bonusMalus", "idexists", "A new bonusMalus cannot already have an ID")).body(null);
        }
        BonusMalus result = bonusMalusRepository.save(bonusMalus);
        return ResponseEntity.created(new URI("/api/bonusMaluss/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("bonusMalus", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /bonusMaluss -> Updates an existing bonusMalus.
     */
    @RequestMapping(value = "/bonusMaluss",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BonusMalus> updateBonusMalus(@Valid @RequestBody BonusMalus bonusMalus) throws URISyntaxException {
        log.debug("REST request to update BonusMalus : {}", bonusMalus);
        if (bonusMalus.getId() == null) {
            return createBonusMalus(bonusMalus);
        }
        BonusMalus result = bonusMalusRepository.save(bonusMalus);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("bonusMalus", bonusMalus.getId().toString()))
            .body(result);
    }

    /**
     * GET  /bonusMaluss -> get all the bonusMaluss.
     */
    @RequestMapping(value = "/bonusMaluss",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<BonusMalus>> getAllBonusMaluss(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of BonusMaluss");
        Page<BonusMalus> page = bonusMalusRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/bonusMaluss");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /bonusMaluss/:id -> get the "id" bonusMalus.
     */
    @RequestMapping(value = "/bonusMaluss/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BonusMalus> getBonusMalus(@PathVariable Long id) {
        log.debug("REST request to get BonusMalus : {}", id);
        BonusMalus bonusMalus = bonusMalusRepository.findOne(id);
        return Optional.ofNullable(bonusMalus)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /bonusMaluss/:id -> delete the "id" bonusMalus.
     */
    @RequestMapping(value = "/bonusMaluss/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteBonusMalus(@PathVariable Long id) {
        log.debug("REST request to delete BonusMalus : {}", id);
        bonusMalusRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("bonusMalus", id.toString())).build();
    }
}
