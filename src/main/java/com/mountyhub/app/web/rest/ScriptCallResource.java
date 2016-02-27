package com.mountyhub.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mountyhub.app.domain.ScriptCall;
import com.mountyhub.app.repository.ScriptCallRepository;
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
 * REST controller for managing ScriptCall.
 */
@RestController
@RequestMapping("/api")
public class ScriptCallResource {

    private final Logger log = LoggerFactory.getLogger(ScriptCallResource.class);
        
    @Inject
    private ScriptCallRepository scriptCallRepository;
    
    /**
     * POST  /scriptCalls -> Create a new scriptCall.
     */
    @RequestMapping(value = "/scriptCalls",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ScriptCall> createScriptCall(@Valid @RequestBody ScriptCall scriptCall) throws URISyntaxException {
        log.debug("REST request to save ScriptCall : {}", scriptCall);
        if (scriptCall.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("scriptCall", "idexists", "A new scriptCall cannot already have an ID")).body(null);
        }
        ScriptCall result = scriptCallRepository.save(scriptCall);
        return ResponseEntity.created(new URI("/api/scriptCalls/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("scriptCall", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /scriptCalls -> Updates an existing scriptCall.
     */
    @RequestMapping(value = "/scriptCalls",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ScriptCall> updateScriptCall(@Valid @RequestBody ScriptCall scriptCall) throws URISyntaxException {
        log.debug("REST request to update ScriptCall : {}", scriptCall);
        if (scriptCall.getId() == null) {
            return createScriptCall(scriptCall);
        }
        ScriptCall result = scriptCallRepository.save(scriptCall);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("scriptCall", scriptCall.getId().toString()))
            .body(result);
    }

    /**
     * GET  /scriptCalls -> get all the scriptCalls.
     */
    @RequestMapping(value = "/scriptCalls",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ScriptCall>> getAllScriptCalls(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of ScriptCalls");
        Page<ScriptCall> page = scriptCallRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/scriptCalls");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /scriptCalls/:id -> get the "id" scriptCall.
     */
    @RequestMapping(value = "/scriptCalls/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ScriptCall> getScriptCall(@PathVariable Long id) {
        log.debug("REST request to get ScriptCall : {}", id);
        ScriptCall scriptCall = scriptCallRepository.findOne(id);
        return Optional.ofNullable(scriptCall)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /scriptCalls/:id -> delete the "id" scriptCall.
     */
    @RequestMapping(value = "/scriptCalls/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteScriptCall(@PathVariable Long id) {
        log.debug("REST request to delete ScriptCall : {}", id);
        scriptCallRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("scriptCall", id.toString())).build();
    }
}
