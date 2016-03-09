package com.mountyhub.app.service;

import com.mountyhub.app.domain.ScriptCall;
import com.mountyhub.app.domain.Troll;
import com.mountyhub.app.exception.MountyHallScriptException;
import com.mountyhub.app.repository.ScriptCallRepository;
import com.mountyhub.app.service.util.MountyHallScriptUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.io.IOException;
import java.net.URL;
import java.time.ZonedDateTime;

/**
 * Created by Theo on 3/7/16.
 */
@Service
@Transactional
public class ScriptCallService {

    private final Logger log = LoggerFactory.getLogger(ScriptCallService.class);

    @Inject
    private ScriptCallRepository scriptCallRepository;

    public void callScript(ScriptCall scriptCall, Troll troll) throws MountyHallScriptException, IOException {
        String url = MountyHallScriptUtil.getUrl(scriptCall.getName(), troll.getNumber(), troll.getRestrictedPassword());
        scriptCall.setUrl(url);

        checkScriptCallSizeByDay(scriptCall, troll);

        // Call the script and save it
        String response = IOUtils.toString(new URL(url), "Windows-1252");
        scriptCall.setBody(response);
        if (troll.getId() != null) {
            scriptCall.setTroll(troll);
        }
        scriptCallRepository.save(scriptCall);

        // Bad number/password
        if (StringUtils.startsWith(response, "Erreur")) {
            throw new MountyHallScriptException("Le numéro de troll ou le mot de passe restreint sont faux !");
        }
    }

    public void checkScriptCallSizeByDay(ScriptCall scriptCall, Troll troll) throws MountyHallScriptException {
        ZonedDateTime date = scriptCall.getDateCalled().minusDays(1);
        Long count = scriptCallRepository.countByTrollNumberAndTypeAndSuccessfulTrueAndDateCalledAfter(troll.getNumber(), scriptCall.getType(), date);
        if (count >= MountyHallScriptUtil.getScriptLimitByDay(scriptCall.getType())) {
            throw new MountyHallScriptException("Limite d'appel par jour des scripts de type " + scriptCall.getType() + " atteinte !");
        }
    }

}
