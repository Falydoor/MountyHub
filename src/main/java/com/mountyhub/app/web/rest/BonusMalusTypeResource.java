package com.mountyhub.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mountyhub.app.domain.BonusMalusType;
import com.mountyhub.app.repository.BonusMalusTypeRepository;
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
 * REST controller for managing BonusMalusType.
 */
@RestController
@RequestMapping("/api")
public class BonusMalusTypeResource {

    private final Logger log = LoggerFactory.getLogger(BonusMalusTypeResource.class);
        
    @Inject
    private BonusMalusTypeRepository bonusMalusTypeRepository;
    
    /**
     * POST  /bonusMalusTypes -> Create a new bonusMalusType.
     */
    @RequestMapping(value = "/bonusMalusTypes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BonusMalusType> createBonusMalusType(@Valid @RequestBody BonusMalusType bonusMalusType) throws URISyntaxException {
        log.debug("REST request to save BonusMalusType : {}", bonusMalusType);
        if (bonusMalusType.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("bonusMalusType", "idexists", "A new bonusMalusType cannot already have an ID")).body(null);
        }
        BonusMalusType result = bonusMalusTypeRepository.save(bonusMalusType);
        return ResponseEntity.created(new URI("/api/bonusMalusTypes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("bonusMalusType", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /bonusMalusTypes -> Updates an existing bonusMalusType.
     */
    @RequestMapping(value = "/bonusMalusTypes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BonusMalusType> updateBonusMalusType(@Valid @RequestBody BonusMalusType bonusMalusType) throws URISyntaxException {
        log.debug("REST request to update BonusMalusType : {}", bonusMalusType);
        if (bonusMalusType.getId() == null) {
            return createBonusMalusType(bonusMalusType);
        }
        BonusMalusType result = bonusMalusTypeRepository.save(bonusMalusType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("bonusMalusType", bonusMalusType.getId().toString()))
            .body(result);
    }

    /**
     * GET  /bonusMalusTypes -> get all the bonusMalusTypes.
     */
    @RequestMapping(value = "/bonusMalusTypes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<BonusMalusType>> getAllBonusMalusTypes(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of BonusMalusTypes");
        Page<BonusMalusType> page = bonusMalusTypeRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/bonusMalusTypes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /bonusMalusTypes/:id -> get the "id" bonusMalusType.
     */
    @RequestMapping(value = "/bonusMalusTypes/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BonusMalusType> getBonusMalusType(@PathVariable Long id) {
        log.debug("REST request to get BonusMalusType : {}", id);
        BonusMalusType bonusMalusType = bonusMalusTypeRepository.findOne(id);
        return Optional.ofNullable(bonusMalusType)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /bonusMalusTypes/:id -> delete the "id" bonusMalusType.
     */
    @RequestMapping(value = "/bonusMalusTypes/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteBonusMalusType(@PathVariable Long id) {
        log.debug("REST request to delete BonusMalusType : {}", id);
        bonusMalusTypeRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("bonusMalusType", id.toString())).build();
    }
}
