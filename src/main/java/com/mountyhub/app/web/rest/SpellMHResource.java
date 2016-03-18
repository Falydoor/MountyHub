package com.mountyhub.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mountyhub.app.domain.SpellMH;
import com.mountyhub.app.repository.SpellMHRepository;
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
 * REST controller for managing SpellMH.
 */
@RestController
@RequestMapping("/api")
public class SpellMHResource {

    private final Logger log = LoggerFactory.getLogger(SpellMHResource.class);
        
    @Inject
    private SpellMHRepository spellMHRepository;
    
    /**
     * POST  /spellMHs -> Create a new spellMH.
     */
    @RequestMapping(value = "/spellMHs",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SpellMH> createSpellMH(@Valid @RequestBody SpellMH spellMH) throws URISyntaxException {
        log.debug("REST request to save SpellMH : {}", spellMH);
        if (spellMH.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("spellMH", "idexists", "A new spellMH cannot already have an ID")).body(null);
        }
        SpellMH result = spellMHRepository.save(spellMH);
        return ResponseEntity.created(new URI("/api/spellMHs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("spellMH", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /spellMHs -> Updates an existing spellMH.
     */
    @RequestMapping(value = "/spellMHs",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SpellMH> updateSpellMH(@Valid @RequestBody SpellMH spellMH) throws URISyntaxException {
        log.debug("REST request to update SpellMH : {}", spellMH);
        if (spellMH.getId() == null) {
            return createSpellMH(spellMH);
        }
        SpellMH result = spellMHRepository.save(spellMH);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("spellMH", spellMH.getId().toString()))
            .body(result);
    }

    /**
     * GET  /spellMHs -> get all the spellMHs.
     */
    @RequestMapping(value = "/spellMHs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<SpellMH>> getAllSpellMHs(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of SpellMHs");
        Page<SpellMH> page = spellMHRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/spellMHs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /spellMHs/:id -> get the "id" spellMH.
     */
    @RequestMapping(value = "/spellMHs/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SpellMH> getSpellMH(@PathVariable Long id) {
        log.debug("REST request to get SpellMH : {}", id);
        SpellMH spellMH = spellMHRepository.findOne(id);
        return Optional.ofNullable(spellMH)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /spellMHs/:id -> delete the "id" spellMH.
     */
    @RequestMapping(value = "/spellMHs/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSpellMH(@PathVariable Long id) {
        log.debug("REST request to delete SpellMH : {}", id);
        spellMHRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("spellMH", id.toString())).build();
    }
}
