package com.mountyhub.app.service;

import com.mountyhub.app.domain.CompetenceMH;
import com.mountyhub.app.domain.SpellMH;
import com.mountyhub.app.exception.MountyHallScriptException;
import com.mountyhub.app.exception.MountyHubException;
import com.mountyhub.app.repository.CompetenceMHRepository;
import com.mountyhub.app.repository.SpellMHRepository;
import com.mountyhub.app.repository.TrollRepository;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

/**
 * Created by Theo on 3/18/16.
 */
@Service
public class CronService {

    private final Logger log = LoggerFactory.getLogger(TrollService.class);

    private final String zone = "Europe/Paris";

    @Inject
    private TrollRepository trollRepository;

    @Inject
    private TrollService trollService;

    @Inject
    private CompetenceMHRepository competenceMHRepository;

    @Inject
    private SpellMHRepository spellMHRepository;

    /**
     * Update every troll's informations one time per day at midnight.
     */
    @Scheduled(cron = "0 0 0 * * ?", zone = zone)
    public void dailyTrollsUpdate() {
        log.debug("START UPDATING TROLLS");
        trollRepository.findAll().stream().forEach(troll -> {
            try {
                trollService.createUpdateTroll(troll);
            } catch (IOException | MountyHubException | MountyHallScriptException e) {
                log.debug("FAIL TO UPDATE TROLL " + troll.getId() + " : " + e.getMessage());
            }
        });
        log.debug("END UPDATING TROLLS");
    }

    /**
     * Update competences one time per day at half past midnight.
     */
    @Scheduled(cron = "0 30 0 * * ?", zone = zone)
    public void dailyCompetencesUpdate() {
        log.debug("START UPDATING COMPETENCES");
        try {
            String response = IOUtils.toString(new URI("ftp://ftp.mountyhall.com/Public_Competences.txt"), "Windows-1252");
            String[] lines = StringUtils.split(response, "\n");

            for (String line : lines) {
                String[] values = StringUtils.splitPreserveAllTokens(line, ";");
                Long number = Long.valueOf(values[0]);
                Optional<CompetenceMH> competenceResult = competenceMHRepository.findByNumber(number);
                CompetenceMH competence = competenceResult.isPresent() ? competenceResult.get() : new CompetenceMH();
                competence.setNumber(number);
                competence.setName(values[1]);
                competence.setPa(Integer.valueOf(values[3]));
                competenceMHRepository.save(competence);
            }
        } catch (IOException | URISyntaxException e) {
            log.debug("FAIL TO UPDATE COMPETENCES : " + e.getMessage());
        }
        log.debug("END UPDATING COMPETENCES");
    }

    /**
     * Update spells one time per day at 35 to one.
     */
    @Scheduled(cron = "0 35 0 * * ?", zone = zone)
    public void dailySpellsUpdate() {
        log.debug("START UPDATING SPELLS");
        try {
            String response = IOUtils.toString(new URI("ftp://ftp.mountyhall.com/Public_Sortileges.txt"), "Windows-1252");
            // Race's spells
            response += "1;Projectile Magique;0;4\n";
            response += "2;Hypnotisme;0;4\n";
            response += "3;Vampirisme;0;4\n";
            response += "4;Rafale Psychique;0;4\n";
            response += "14;Siphon des Ames;0;4\n";
            // Sublifusion Magesque
            response += "30;Sublifusion Magesque Minor;0;1\n";
            response += "31;Sublifusion Magesque Medius;0;1\n";
            response += "32;Sublifusion Magesque Maexus;0;1";
            String[] lines = StringUtils.split(response, "\n");

            for (String line : lines) {
                String[] values = StringUtils.splitPreserveAllTokens(line, ";");
                Long number = Long.valueOf(values[0]);
                Optional<SpellMH> spellResult = spellMHRepository.findByNumber(number);
                SpellMH spell = spellResult.isPresent() ? spellResult.get() : new SpellMH();
                spell.setNumber(number);
                spell.setName(values[1]);
                spell.setPa(Integer.valueOf(values[3]));
                spellMHRepository.save(spell);
            }
        } catch (IOException | URISyntaxException e) {
            log.debug("FAIL TO UPDATE SPELLS : " + e.getMessage());
        }
        log.debug("END UPDATING SPELLS");
    }
}
