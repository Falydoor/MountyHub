package com.mountyhub.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mountyhub.app.domain.CompetenceMH;
import com.mountyhub.app.repository.CompetenceMHRepository;
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
 * REST controller for managing CompetenceMH.
 */
@RestController
@RequestMapping("/api")
public class CompetenceMHResource {

    private final Logger log = LoggerFactory.getLogger(CompetenceMHResource.class);
        
    @Inject
    private CompetenceMHRepository competenceMHRepository;
    
    /**
     * POST  /competenceMHs -> Create a new competenceMH.
     */
    @RequestMapping(value = "/competenceMHs",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CompetenceMH> createCompetenceMH(@Valid @RequestBody CompetenceMH competenceMH) throws URISyntaxException {
        log.debug("REST request to save CompetenceMH : {}", competenceMH);
        if (competenceMH.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("competenceMH", "idexists", "A new competenceMH cannot already have an ID")).body(null);
        }
        CompetenceMH result = competenceMHRepository.save(competenceMH);
        return ResponseEntity.created(new URI("/api/competenceMHs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("competenceMH", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /competenceMHs -> Updates an existing competenceMH.
     */
    @RequestMapping(value = "/competenceMHs",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CompetenceMH> updateCompetenceMH(@Valid @RequestBody CompetenceMH competenceMH) throws URISyntaxException {
        log.debug("REST request to update CompetenceMH : {}", competenceMH);
        if (competenceMH.getId() == null) {
            return createCompetenceMH(competenceMH);
        }
        CompetenceMH result = competenceMHRepository.save(competenceMH);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("competenceMH", competenceMH.getId().toString()))
            .body(result);
    }

    /**
     * GET  /competenceMHs -> get all the competenceMHs.
     */
    @RequestMapping(value = "/competenceMHs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<CompetenceMH>> getAllCompetenceMHs(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of CompetenceMHs");
        Page<CompetenceMH> page = competenceMHRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/competenceMHs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /competenceMHs/:id -> get the "id" competenceMH.
     */
    @RequestMapping(value = "/competenceMHs/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CompetenceMH> getCompetenceMH(@PathVariable Long id) {
        log.debug("REST request to get CompetenceMH : {}", id);
        CompetenceMH competenceMH = competenceMHRepository.findOne(id);
        return Optional.ofNullable(competenceMH)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /competenceMHs/:id -> delete the "id" competenceMH.
     */
    @RequestMapping(value = "/competenceMHs/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCompetenceMH(@PathVariable Long id) {
        log.debug("REST request to delete CompetenceMH : {}", id);
        competenceMHRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("competenceMH", id.toString())).build();
    }
}
