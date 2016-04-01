package com.mountyhub.app.service;

import com.mountyhub.app.domain.*;
import com.mountyhub.app.domain.enumeration.EffectType;
import com.mountyhub.app.domain.enumeration.FlyType;
import com.mountyhub.app.domain.enumeration.ScriptName;
import com.mountyhub.app.domain.enumeration.ScriptType;
import com.mountyhub.app.exception.MountyHallScriptException;
import com.mountyhub.app.exception.MountyHubException;
import com.mountyhub.app.repository.*;
import com.mountyhub.app.service.util.DateUtil;
import com.mountyhub.app.service.util.MountyHallScriptUtil;
import com.mountyhub.app.service.util.MountyHallUtil;
import com.mountyhub.app.web.rest.dto.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Service class for managing trolls.
 */
@Service
@Transactional
public class TrollService {

    private final Logger log = LoggerFactory.getLogger(TrollService.class);

    @Inject
    private UserRepository userRepository;

    @Inject
    private UserService userService;

    @Inject
    private TrollRepository trollRepository;

    @Inject
    private ScriptCallRepository scriptCallRepository;

    @Inject
    private GearRepository gearRepository;

    @Inject
    private ScriptCallService scriptCallService;

    @Inject
    private FlyRepository flyRepository;

    @Inject
    private CompetenceMHRepository competenceMHRepository;

    @Inject
    private SpellMHRepository spellMHRepository;

    @Inject
    private CompetenceRepository competenceRepository;

    @Inject
    private SpellRepository spellRepository;

    @Inject
    private BonusMalusRepository bonusMalusRepository;

    @Inject
    private BonusMalusTypeRepository bonusMalusTypeRepository;

    public Troll createUpdateTroll(Troll troll) throws IOException, MountyHubException, MountyHallScriptException {
        // Check if the troll isn't already linked to a user for a troll creation
        User currentUser = null;
        if (troll.getUser() == null) {
            currentUser = userService.getUserWithAuthorities();

            // A troll is already linked to the current user
            if (currentUser.getTroll() != null) {
                throw new MountyHubException("Votre utilisateur a déjà un troll lié !");
            }

            // Troll already present with a user linked
            Optional<User> userResult = userRepository.findOneByTrollNumber(troll.getNumber());
            if (userResult.isPresent()) {
                throw new MountyHubException("Le numero de troll " + troll.getNumber() + " est déjà associé à l'utilisateur " + userResult.get().getLogin() + " !");
            }

            // Troll previously deleted
            Optional<Troll> trollResult = trollRepository.findOneByNumberAndDeletedTrue(troll.getNumber());
            if (trollResult.isPresent()) {
                troll = trollResult.get();
                troll.setDeleted(false);
            }
        }

        try {
            // Set characteristic, state and profil
            ScriptCall caractCall = setTrollCharacteristicFromMHScript(troll);
            ScriptCall stateCall = setTrollStateFromMHScript(troll);
            ScriptCall profilCall = setTrollProfilFromMHScript(troll);

            // Save the troll
            trollRepository.save(troll);
            log.debug("Created Information for Troll: {}", troll);

            // Save gear
            ScriptCall gearCall = setTrollGearFromMHScript(troll);

            // Save fly
            ScriptCall flyCall = setTrollFlyFromMHScript(troll);

            // Save aptitude
            ScriptCall aptitudeCall = setTrollAptitudeFromMHScript(troll);

            // Save bonus malus
            ScriptCall bonusMalusCall = setTrollBonusMalusFromMHScript(troll);

            // Save script calls with troll number
            caractCall.setTroll(troll);
            scriptCallRepository.save(caractCall);
            stateCall.setTroll(troll);
            scriptCallRepository.save(stateCall);
            profilCall.setTroll(troll);
            scriptCallRepository.save(profilCall);
            gearCall.setTroll(troll);
            scriptCallRepository.save(gearCall);
            flyCall.setTroll(troll);
            scriptCallRepository.save(flyCall);
            aptitudeCall.setTroll(troll);
            scriptCallRepository.save(aptitudeCall);
            bonusMalusCall.setTroll(troll);
            scriptCallRepository.save(bonusMalusCall);

            // Link troll to current user
            if (currentUser != null) {
                currentUser.setTroll(troll);
                userRepository.save(currentUser);
            }

            return troll;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new MountyHallScriptException("Erreur lors du parsage du script MountyHall !", e);
        }
    }

    public ScriptCall setTrollBonusMalusFromMHScript(Troll troll) throws MountyHallScriptException, IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        // Delete old bonus malus
        bonusMalusRepository.delete(troll.getBonusMalus());

        ScriptCall scriptCall = MountyHallScriptUtil.createScriptCall(ScriptName.SP_Bonusmalus);
        scriptCallService.callScript(scriptCall, troll);

        String[] lines = StringUtils.split(scriptCall.getBody(), "\n");

        List<BonusMalus> bonusMaluses = new ArrayList<>();
        for (String line : lines) {
            line = StringUtils.replace(line, "\\'", "'");
            String[] values = StringUtils.splitPreserveAllTokens(line, ";");

            BonusMalus bonusMalus = new BonusMalus();
            MountyHallScriptUtil.parseBonusMalus(bonusMalus, values);
            bonusMalus.setTroll(troll);
            Optional<BonusMalusType> bonusMalusTypeResult = bonusMalusTypeRepository.findByName(values[1]);
            BonusMalusType bonusMalusType = bonusMalusTypeResult.isPresent() ? bonusMalusTypeResult.get() : new BonusMalusType();
            if (!bonusMalusTypeResult.isPresent()) {
                bonusMalusType.setName(values[1]);
                bonusMalusType.setType(EffectType.INCONNU);
                bonusMalusTypeRepository.save(bonusMalusType);
            }
            bonusMalus.setType(bonusMalusType);
            bonusMaluses.add(bonusMalus);
        }

        // Set real effect by applying decumul on multiple bonus malus type
        Map<BonusMalusType, Integer> bonusMalusCountType = new HashMap<>();
        bonusMaluses.stream()
            .sorted((bm1, bm2) -> bm1.getDuration().compareTo(bm2.getDuration()))
            .forEach(bonusMalus -> {
                bonusMalusCountType.put(bonusMalus.getType(), bonusMalusCountType.getOrDefault(bonusMalus.getType(), 0) + 1);
                String effect = bonusMalus.getEffect();
                // Make the bonus malus magic if it's a magic type
                if (bonusMalus.getType().getType() == EffectType.MAGIQUE) {
                    Pattern physicPattern = Pattern.compile("(\\w+ : [\\-|\\+]\\d+)");
                    Matcher physicMatcher = physicPattern.matcher(bonusMalus.getEffect());
                    StringBuffer magicEffect = new StringBuffer();
                    while (physicMatcher.find()) {
                        String wholeEffect = physicMatcher.group(1);
                        String replacement = MountyHallUtil.effectCanBeMagic(wholeEffect) ? wholeEffect.replace(": ", ": +0\\\\") : wholeEffect;
                        physicMatcher.appendReplacement(magicEffect, replacement);
                    }
                    effect = magicEffect.toString();
                }
                // Apply decumul on the whole effect string
                Pattern numericPattern = Pattern.compile("(\\d+)");
                Matcher numericMatcher = numericPattern.matcher(effect);
                StringBuffer realEffect = new StringBuffer();
                while (numericMatcher.find()) {
                    numericMatcher.appendReplacement(realEffect, MountyHallUtil.applyDecumul(Integer.parseInt(numericMatcher.group(1)), bonusMalusCountType.get(bonusMalus.getType())).toString());
                }
                bonusMalus.setRealEffect(realEffect.toString());
                try {
                    MountyHallUtil.setCharacteristicsFromDescription(bonusMalus, bonusMalus.getRealEffect());
                } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                    log.error("Error setting bonus malus from real effect : " + e.getMessage());
                }
            });

        bonusMalusRepository.save(bonusMaluses);

        scriptCall.setSuccessfullyParsed(true);
        scriptCallService.save(scriptCall);

        return scriptCall;
    }

    public ScriptCall setTrollAptitudeFromMHScript(Troll troll) throws MountyHallScriptException, IOException {
        ScriptCall scriptCall = MountyHallScriptUtil.createScriptCall(ScriptName.SP_Aptitudes2);
        scriptCallService.callScript(scriptCall, troll);

        String[] lines = StringUtils.split(scriptCall.getBody(), "\n");

        for (String line : lines) {
            String[] values = StringUtils.splitPreserveAllTokens(line, ";");

            Map<String, Competence> competences = troll.getCompetences().stream().collect(Collectors.toMap(competence -> competence.getCompetenceMH().getNumber() + "-" + competence.getLevel(), competence -> competence));
            Map<String, Spell> spells = troll.getSpells().stream().collect(Collectors.toMap(spell -> spell.getSpellMH().getNumber() + "-" + spell.getLevel(), spell -> spell));
            long number = Long.parseLong(values[1]);
            int level = Integer.parseInt(values[4]);
            String key = number + "-" + level;
            if ("C".equals(values[0])) {
                Competence competence = competences.get(key);
                if (competence == null) {
                    competence = new Competence();
                    competence.setTroll(troll);
                    Optional<CompetenceMH> competenceMH = competenceMHRepository.findByNumber(number);
                    if (!competenceMH.isPresent()) {
                        throw new MountyHallScriptException("Compétence " + number + " non-présente dans le référentiel !");
                    }
                    competence.setCompetenceMH(competenceMH.get());
                }
                competence.setPercent(Integer.parseInt(values[2]));
                competence.setPercentBonus(Integer.parseInt(values[3]));
                competence.setLevel(level);
                competenceRepository.save(competence);
            } else if ("S".equals(values[0])) {
                Spell spell = spells.get(key);
                if (spell == null) {
                    spell = new Spell();
                    spell.setTroll(troll);
                    Optional<SpellMH> spellMH = spellMHRepository.findByNumber(number);
                    if (!spellMH.isPresent()) {
                        throw new MountyHallScriptException("Sortilège " + number + " non-présent dans le référentiel !");
                    }
                    spell.setSpellMH(spellMH.get());
                }
                spell.setPercent(Integer.parseInt(values[2]));
                spell.setPercentBonus(Integer.parseInt(values[3]));
                spell.setLevel(level);
                spellRepository.save(spell);
            }

            scriptCall.setSuccessfullyParsed(true);
            scriptCallService.save(scriptCall);
        }

        return scriptCall;
    }

    public ScriptCall setTrollFlyFromMHScript(Troll troll) throws MountyHallScriptException, IOException {
        ScriptCall scriptCall = MountyHallScriptUtil.createScriptCall(ScriptName.SP_Mouche);
        scriptCallService.callScript(scriptCall, troll);

        String[] lines = StringUtils.split(scriptCall.getBody(), "\n");

        List<Long> exist = new ArrayList<>();

        for (String line : lines) {
            line = StringUtils.replace(line, "\\'", "'");
            String[] values = StringUtils.splitPreserveAllTokens(line, ";");

            Optional<Fly> flyResult = flyRepository.findByNumber(Long.valueOf(values[0]));
            Fly fly = flyResult.isPresent() ? flyResult.get() : new Fly();
            MountyHallScriptUtil.parseFly(fly, values);
            fly.setTroll(troll);
            flyRepository.save(fly);
            exist.add(fly.getNumber());
        }

        scriptCall.setSuccessfullyParsed(true);
        scriptCallService.save(scriptCall);

        // Delete fly that doesn't exist
        troll.getFlys().stream()
            .filter(fly -> !exist.contains(fly.getNumber()))
            .forEach(fly -> flyRepository.deleteByNumber(fly.getNumber()));

        return scriptCall;
    }

    public ScriptCall setTrollGearFromMHScript(Troll troll) throws IOException, MountyHallScriptException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        ScriptCall scriptCall = MountyHallScriptUtil.createScriptCall(ScriptName.SP_Equipement);
        scriptCallService.callScript(scriptCall, troll);

        String[] lines = StringUtils.split(scriptCall.getBody(), "\n");

        List<Long> wore = new ArrayList<>();

        for (String line : lines) {
            line = StringUtils.replace(line, "\\'", "'");
            String[] values = StringUtils.splitPreserveAllTokens(line, ";");

            Optional<Gear> gearResult = gearRepository.findByNumber(Long.valueOf(values[0]));
            Gear gear = gearResult.isPresent() ? gearResult.get() : new Gear();
            MountyHallScriptUtil.parseGear(gear, values);
            gear.setTroll(troll);
            gearRepository.save(gear);
            wore.add(gear.getNumber());
        }

        scriptCall.setSuccessfullyParsed(true);
        scriptCallService.save(scriptCall);

        // Delete gear that aren't wore
        troll.getGears().stream()
            .filter(gear -> !wore.contains(gear.getNumber()))
            .forEach(gear -> gearRepository.deleteByNumber(gear.getNumber()));

        return scriptCall;
    }

    public ScriptCall setTrollProfilFromMHScript(Troll troll) throws MountyHallScriptException, IOException {
        ScriptCall scriptCall = MountyHallScriptUtil.createScriptCall(ScriptName.SP_ProfilPublic2);
        scriptCallService.callScript(scriptCall, troll);

        String[] lines = StringUtils.split(scriptCall.getBody(), "\n");

        // Bad lines size
        if (lines.length != 1) {
            throw new MountyHallScriptException("Réponse du script MountyHall incorrect !");
        }

        String[] values = StringUtils.splitPreserveAllTokens(lines[0], ";");
        MountyHallScriptUtil.parseProfil(troll, values);

        scriptCall.setSuccessfullyParsed(true);
        scriptCallService.save(scriptCall);

        return scriptCall;
    }

    public ScriptCall setTrollStateFromMHScript(Troll troll) throws MountyHallScriptException, IOException {
        ScriptCall scriptCall = MountyHallScriptUtil.createScriptCall(ScriptName.SP_Profil2);
        scriptCallService.callScript(scriptCall, troll);

        String[] lines = StringUtils.split(scriptCall.getBody(), "\n");

        // Bad lines size
        if (lines.length != 1) {
            throw new MountyHallScriptException("Réponse du script MountyHall incorrect !");
        }

        String[] values = StringUtils.splitPreserveAllTokens(lines[0], ";");
        MountyHallScriptUtil.parseState(troll, values);

        scriptCall.setSuccessfullyParsed(true);
        scriptCallService.save(scriptCall);

        return scriptCall;
    }

    public ScriptCall setTrollCharacteristicFromMHScript(Troll troll) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, MountyHallScriptException, IOException {
        ScriptCall scriptCall = MountyHallScriptUtil.createScriptCall(ScriptName.SP_Caract);
        scriptCallService.callScript(scriptCall, troll);

        String[] lines = StringUtils.split(scriptCall.getBody(), "\n");

        // Bad lines size
        if (lines.length != 3) {
            throw new MountyHallScriptException("Réponse du script MountyHall incorrect !");
        }

        for (String line : lines) {
            String[] values = StringUtils.splitPreserveAllTokens(line, ";");

            // Bad values size
            if (values.length != 14) {
                throw new MountyHallScriptException("Réponse du script MountyHall incorrect !");
            }

            MountyHallScriptUtil.parseCharacteristic(troll, values);
        }

        scriptCall.setSuccessfullyParsed(true);
        scriptCallService.save(scriptCall);

        return scriptCall;
    }

    public ProfilDTO getPrivateProfil() throws MountyHubException {
        User user = userService.getUserWithAuthorities();
        ProfilDTO profil = new ProfilDTO();

        if (user.getTroll() == null) {
            return profil;
        }

        Troll troll = trollRepository.findOne(user.getTroll().getId());
        BeanUtils.copyProperties(troll, profil);

        // Additionnal fields
        profil.setTotalHitPoint(profil.getHitPoint() + profil.getHitPointP() + profil.getHitPointM());
        ZoneId zoneId = DateUtil.getZoneIdFromUserOption(user.getUserOptions());
        profil.setBirthDateFormatted(DateUtil.formatZonedDate(troll.getBirthDate(), zoneId));
        profil.setDla(DateUtil.formatZonedDate(troll.getDla(), zoneId));
        profil.setUserOptions(user.getUserOptions().stream().collect(Collectors.toMap(userOption -> userOption.getName().toString(), userOption1 -> userOption1)));

        // Gears
        List<GearDTO> gears = troll.getGears().stream().sorted((g1, g2) -> g1.getType().toString().compareTo(g2.getType().toString()))
            .map(GearDTO::new).collect(Collectors.toList());
        profil.setGears(gears);
        profil.setGearEffect(MountyHallUtil.formatGlobalEffect(gearRepository.getWoreGlobalEffect(troll.getId())));
        profil.setProtection(troll.getGears().stream().filter(gear -> gear.getWore() && StringUtils.isNotBlank(gear.getProtection()))
            .map(Gear::getProtection)
            .collect(Collectors.joining(" | "))
        );

        // Flies
        profil.setFlies(troll.getFlys().stream().map(FlyDTO::new).collect(Collectors.toList()));
        Map<FlyType, Long> fliesByType = troll.getFlys().stream().filter(Fly::getHere)
            .collect(Collectors.groupingBy(Fly::getType, Collectors.counting()));
        profil.setFlyEffect(MountyHallUtil.formatGlobalEffect(new GlobalEffectDTO(fliesByType)));

        // Aptitudes
        profil.setAptitudes(Stream.concat(troll.getCompetences().stream().map(AptitudeDTO::new), troll.getSpells().stream().map(AptitudeDTO::new))
            .sorted((a1, a2) -> a1.getName().compareTo(a2.getName()))
            .collect(Collectors.toList()));

        // Bonus malus
        List<BonusMalusDTO> bonusMaluses = troll.getBonusMalus().stream()
            .sorted((bm1, bm2) -> bm1.getDuration().compareTo(bm2.getDuration()))
            .map(BonusMalusDTO::new).collect(Collectors.toList());
        profil.setBonusMalus(bonusMaluses);
        profil.setBonusMalusEffect(MountyHallUtil.formatGlobalEffect(bonusMalusRepository.getGlobalEffect(troll.getId())));

        // Turn, bonus/malus, weight, wounds and total turn
        Duration turn = DateUtil.getDurationFromFloatMinutes(profil.getTurn());
        profil.setTurnFormatted(DateUtil.formatDuration(turn));
        Duration bonusMalus = DateUtil.getDurationFromFloatMinutes(profil.getWeightM());
        profil.setBonusMalusTimeFormatted(DateUtil.formatDuration(bonusMalus));
        Duration weight = DateUtil.getDurationFromFloatMinutes(profil.getWeightP());
        profil.setWeightTimeFormatted(DateUtil.formatDuration(weight));
        Duration wounds = Duration.ofMinutes((long) Math.floor(250F / profil.getTotalHitPoint() * (profil.getTotalHitPoint() - profil.getCurrentHitPoint())));
        profil.setWoundsTimeFormatted(DateUtil.formatDuration(wounds));
        Duration totalTurn = turn.plus(weight).plus(bonusMalus).plus(wounds);
        totalTurn = totalTurn.compareTo(turn) < 0 ? turn : totalTurn;
        profil.setTurnTotalFormatted(DateUtil.formatDuration(totalTurn));
        profil.setNextDla(DateUtil.formatZonedDate(troll.getDla().plus(totalTurn), zoneId));

        // Scripts call per day
        ZonedDateTime yesterday = ZonedDateTime.now().minusDays(1);
        Map<ScriptType, Long> scriptCallByDay = troll.getScriptCalls().stream()
            .filter(script -> script.getSuccessfullyCalled() && script.getDateCalled().isAfter(yesterday))
            .collect(Collectors.groupingBy(ScriptCall::getType, Collectors.counting()));
        profil.setScriptCallByDay(scriptCallByDay);

        // Last scripts call by type
        Map<ScriptName, String> scriptCallLastCall = troll.getScriptCalls().stream()
            .filter(ScriptCall::getSuccessfullyParsed)
            .collect(Collectors.groupingBy(ScriptCall::getName))
            .entrySet()
            .stream()
            .collect(Collectors.toMap(Map.Entry::getKey, entry -> DateUtil.formatSinceDate(entry.getValue().get(0).getDateCalled())));
        profil.setScriptCallLastCall(scriptCallLastCall);

        return profil;
    }

    public void deleteTroll() throws MountyHubException {
        User currentUser = userService.getUserWithAuthorities();

        // No troll linked to user
        if (currentUser.getTroll() == null) {
            throw new MountyHubException("Votre utilisateur n'a pas de troll lié !");
        }

        // Set troll deleted and remove it from the user
        currentUser.getTroll().setDeleted(true);
        trollRepository.save(currentUser.getTroll());
        currentUser.setTroll(null);
        userRepository.save(currentUser);
    }

    public void refreshTroll(String refreshType) throws IOException, MountyHallScriptException, MountyHubException {
        Troll troll = userService.getUserWithAuthorities().getTroll();

        if (troll == null) {
            throw new MountyHubException("Vous n'avez pas de Troll !");
        }

        try {
            switch (refreshType) {
                case "state":
                    setTrollStateFromMHScript(troll);
                    break;
                case "fly":
                    setTrollFlyFromMHScript(troll);
                    setTrollCharacteristicFromMHScript(troll);
                    break;
                case "gear":
                    setTrollGearFromMHScript(troll);
                    setTrollCharacteristicFromMHScript(troll);
                    break;
                case "characteristic":
                    setTrollCharacteristicFromMHScript(troll);
                    break;
                case "compAndSpell":
                    setTrollAptitudeFromMHScript(troll);
                    break;
                case "bonusMalus":
                    setTrollBonusMalusFromMHScript(troll);
                    setTrollCharacteristicFromMHScript(troll);
                    break;
                default:
                    throw new MountyHubException("Type de refresh non-implémenté !");
            }
            trollRepository.save(troll);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new MountyHallScriptException("Erreur lors du parsage du script MountyHall !", e);
        }

    }
}
