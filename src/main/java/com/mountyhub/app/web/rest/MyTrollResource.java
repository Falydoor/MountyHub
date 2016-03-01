package com.mountyhub.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mountyhub.app.domain.Troll;
import com.mountyhub.app.exception.MountyHallScriptException;
import com.mountyhub.app.exception.MountyHubException;
import com.mountyhub.app.service.TrollService;
import com.mountyhub.app.web.rest.dto.ProfilDTO;
import com.mountyhub.app.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by Theo on 2/29/16.
 */
@RestController
@RequestMapping("/api")
public class MyTrollResource {

    private final Logger log = LoggerFactory.getLogger(MyTrollResource.class);

    @Inject
    private TrollService trollService;

    @RequestMapping(value = "/profil",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> addTroll(Long number, String restrictedPassword) throws URISyntaxException {
        log.debug("REST request to add Troll : {} {}", number, restrictedPassword);
        Troll troll = new Troll();
        troll.setNumber(number);
        troll.setRestrictedPassword(restrictedPassword);
        try {
            troll = trollService.createUpdateTroll(troll);
        } catch (IOException e) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("troll", "", "Erreur lors de la récuperation des infos de votre troll " + number.toString() + " !")).build();
        } catch (MountyHubException | MountyHallScriptException e) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("troll", "", e.getMessage())).build();
        }
        return ResponseEntity.created(new URI("/profil"))
            .headers(HeaderUtil.createAlert("Votre troll " + number.toString() + " a bien été lié !", ""))
            .body(troll);
    }

    @RequestMapping(value = "/profil",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> getProfil() throws URISyntaxException {
        log.debug("REST request to get Profil");
        try {
            ProfilDTO profil = trollService.getPrivateProfil();
            return ResponseEntity.created(new URI("/profil"))
                .body(profil);
        } catch (MountyHubException e) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("troll", "", e.getMessage())).build();
        }
    }

}
