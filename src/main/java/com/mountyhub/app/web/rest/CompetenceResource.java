package com.mountyhub.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mountyhub.app.domain.Competence;
import com.mountyhub.app.repository.CompetenceRepository;
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
 * REST controller for managing Competence.
 */
@RestController
@RequestMapping("/api")
public class CompetenceResource {

    private final Logger log = LoggerFactory.getLogger(CompetenceResource.class);
        
    @Inject
    private CompetenceRepository competenceRepository;
    
    /**
     * POST  /competences -> Create a new competence.
     */
    @RequestMapping(value = "/competences",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Competence> createCompetence(@Valid @RequestBody Competence competence) throws URISyntaxException {
        log.debug("REST request to save Competence : {}", competence);
        if (competence.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("competence", "idexists", "A new competence cannot already have an ID")).body(null);
        }
        Competence result = competenceRepository.save(competence);
        return ResponseEntity.created(new URI("/api/competences/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("competence", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /competences -> Updates an existing competence.
     */
    @RequestMapping(value = "/competences",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Competence> updateCompetence(@Valid @RequestBody Competence competence) throws URISyntaxException {
        log.debug("REST request to update Competence : {}", competence);
        if (competence.getId() == null) {
            return createCompetence(competence);
        }
        Competence result = competenceRepository.save(competence);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("competence", competence.getId().toString()))
            .body(result);
    }

    /**
     * GET  /competences -> get all the competences.
     */
    @RequestMapping(value = "/competences",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Competence>> getAllCompetences(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Competences");
        Page<Competence> page = competenceRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/competences");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /competences/:id -> get the "id" competence.
     */
    @RequestMapping(value = "/competences/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Competence> getCompetence(@PathVariable Long id) {
        log.debug("REST request to get Competence : {}", id);
        Competence competence = competenceRepository.findOne(id);
        return Optional.ofNullable(competence)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /competences/:id -> delete the "id" competence.
     */
    @RequestMapping(value = "/competences/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCompetence(@PathVariable Long id) {
        log.debug("REST request to delete Competence : {}", id);
        competenceRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("competence", id.toString())).build();
    }
}
