package com.mountyhub.app.service;

import com.mountyhub.app.domain.CompetenceMH;
import com.mountyhub.app.exception.MountyHallScriptException;
import com.mountyhub.app.exception.MountyHubException;
import com.mountyhub.app.repository.CompetenceMHRepository;
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
                String[] values = StringUtils.split(line, ";");
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
}
