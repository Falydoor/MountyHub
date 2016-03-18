package com.mountyhub.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mountyhub.app.domain.Spell;
import com.mountyhub.app.repository.SpellRepository;
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
 * REST controller for managing Spell.
 */
@RestController
@RequestMapping("/api")
public class SpellResource {

    private final Logger log = LoggerFactory.getLogger(SpellResource.class);
        
    @Inject
    private SpellRepository spellRepository;
    
    /**
     * POST  /spells -> Create a new spell.
     */
    @RequestMapping(value = "/spells",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Spell> createSpell(@Valid @RequestBody Spell spell) throws URISyntaxException {
        log.debug("REST request to save Spell : {}", spell);
        if (spell.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("spell", "idexists", "A new spell cannot already have an ID")).body(null);
        }
        Spell result = spellRepository.save(spell);
        return ResponseEntity.created(new URI("/api/spells/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("spell", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /spells -> Updates an existing spell.
     */
    @RequestMapping(value = "/spells",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Spell> updateSpell(@Valid @RequestBody Spell spell) throws URISyntaxException {
        log.debug("REST request to update Spell : {}", spell);
        if (spell.getId() == null) {
            return createSpell(spell);
        }
        Spell result = spellRepository.save(spell);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("spell", spell.getId().toString()))
            .body(result);
    }

    /**
     * GET  /spells -> get all the spells.
     */
    @RequestMapping(value = "/spells",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Spell>> getAllSpells(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Spells");
        Page<Spell> page = spellRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/spells");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /spells/:id -> get the "id" spell.
     */
    @RequestMapping(value = "/spells/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Spell> getSpell(@PathVariable Long id) {
        log.debug("REST request to get Spell : {}", id);
        Spell spell = spellRepository.findOne(id);
        return Optional.ofNullable(spell)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /spells/:id -> delete the "id" spell.
     */
    @RequestMapping(value = "/spells/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSpell(@PathVariable Long id) {
        log.debug("REST request to delete Spell : {}", id);
        spellRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("spell", id.toString())).build();
    }
}
