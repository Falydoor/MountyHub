package com.mountyhub.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mountyhub.app.domain.UserOption;
import com.mountyhub.app.repository.UserOptionRepository;
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
 * REST controller for managing UserOption.
 */
@RestController
@RequestMapping("/api")
public class UserOptionResource {

    private final Logger log = LoggerFactory.getLogger(UserOptionResource.class);
        
    @Inject
    private UserOptionRepository userOptionRepository;
    
    /**
     * POST  /userOptions -> Create a new userOption.
     */
    @RequestMapping(value = "/userOptions",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserOption> createUserOption(@Valid @RequestBody UserOption userOption) throws URISyntaxException {
        log.debug("REST request to save UserOption : {}", userOption);
        if (userOption.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("userOption", "idexists", "A new userOption cannot already have an ID")).body(null);
        }
        UserOption result = userOptionRepository.save(userOption);
        return ResponseEntity.created(new URI("/api/userOptions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("userOption", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /userOptions -> Updates an existing userOption.
     */
    @RequestMapping(value = "/userOptions",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserOption> updateUserOption(@Valid @RequestBody UserOption userOption) throws URISyntaxException {
        log.debug("REST request to update UserOption : {}", userOption);
        if (userOption.getId() == null) {
            return createUserOption(userOption);
        }
        UserOption result = userOptionRepository.save(userOption);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("userOption", userOption.getId().toString()))
            .body(result);
    }

    /**
     * GET  /userOptions -> get all the userOptions.
     */
    @RequestMapping(value = "/userOptions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<UserOption>> getAllUserOptions(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of UserOptions");
        Page<UserOption> page = userOptionRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/userOptions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /userOptions/:id -> get the "id" userOption.
     */
    @RequestMapping(value = "/userOptions/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserOption> getUserOption(@PathVariable Long id) {
        log.debug("REST request to get UserOption : {}", id);
        UserOption userOption = userOptionRepository.findOne(id);
        return Optional.ofNullable(userOption)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /userOptions/:id -> delete the "id" userOption.
     */
    @RequestMapping(value = "/userOptions/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteUserOption(@PathVariable Long id) {
        log.debug("REST request to delete UserOption : {}", id);
        userOptionRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("userOption", id.toString())).build();
    }
}
