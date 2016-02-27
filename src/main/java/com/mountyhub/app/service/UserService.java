package com.mountyhub.app.service;

import com.mountyhub.app.domain.Authority;
import com.mountyhub.app.domain.ScriptCall;
import com.mountyhub.app.domain.Troll;
import com.mountyhub.app.domain.User;
import com.mountyhub.app.domain.enumeration.ScriptName;
import com.mountyhub.app.domain.enumeration.ScriptType;
import com.mountyhub.app.domain.enumeration.TrollRace;
import com.mountyhub.app.exception.MountyHallScriptException;
import com.mountyhub.app.exception.MountyHubException;
import com.mountyhub.app.repository.*;
import com.mountyhub.app.security.SecurityUtils;
import com.mountyhub.app.service.util.RandomUtil;
import com.mountyhub.app.web.rest.dto.ManagedUserDTO;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    @Inject
    private PasswordEncoder passwordEncoder;

    @Inject
    private UserRepository userRepository;

    @Inject
    private PersistentTokenRepository persistentTokenRepository;

    @Inject
    private AuthorityRepository authorityRepository;

    @Inject
    private TrollRepository trollRepository;

    @Inject
    private ScriptCallRepository scriptCallRepository;

    public Optional<User> activateRegistration(String key) {
        log.debug("Activating user for activation key {}", key);
        userRepository.findOneByActivationKey(key)
            .map(user -> {
                // activate given user for the registration key.
                user.setActivated(true);
                user.setActivationKey(null);
                userRepository.save(user);
                log.debug("Activated user: {}", user);
                return user;
            });
        return Optional.empty();
    }

    public Optional<User> completePasswordReset(String newPassword, String key) {
        log.debug("Reset user password for reset key {}", key);

        return userRepository.findOneByResetKey(key)
            .filter(user -> {
                ZonedDateTime oneDayAgo = ZonedDateTime.now().minusHours(24);
                return user.getResetDate().isAfter(oneDayAgo);
            })
            .map(user -> {
                user.setPassword(passwordEncoder.encode(newPassword));
                user.setResetKey(null);
                user.setResetDate(null);
                userRepository.save(user);
                return user;
            });
    }

    public Optional<User> requestPasswordReset(String mail) {
        return userRepository.findOneByEmail(mail)
            .filter(User::getActivated)
            .map(user -> {
                user.setResetKey(RandomUtil.generateResetKey());
                user.setResetDate(ZonedDateTime.now());
                userRepository.save(user);
                return user;
            });
    }

    public User createUserInformation(String login, String password, String firstName, String lastName, String email,
                                      String langKey) {

        User newUser = new User();
        Authority authority = authorityRepository.findOne("ROLE_USER");
        Set<Authority> authorities = new HashSet<>();
        String encryptedPassword = passwordEncoder.encode(password);
        newUser.setLogin(login);
        // new user gets initially a generated password
        newUser.setPassword(encryptedPassword);
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setEmail(email);
        newUser.setLangKey(langKey);
        // new user is not active
        newUser.setActivated(false);
        // new user gets registration key
        newUser.setActivationKey(RandomUtil.generateActivationKey());
        authorities.add(authority);
        newUser.setAuthorities(authorities);
        userRepository.save(newUser);
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    public User createUser(ManagedUserDTO managedUserDTO) {
        User user = new User();
        user.setLogin(managedUserDTO.getLogin());
        user.setFirstName(managedUserDTO.getFirstName());
        user.setLastName(managedUserDTO.getLastName());
        user.setEmail(managedUserDTO.getEmail());
        if (managedUserDTO.getLangKey() == null) {
            user.setLangKey("en"); // default language is English
        } else {
            user.setLangKey(managedUserDTO.getLangKey());
        }
        if (managedUserDTO.getAuthorities() != null) {
            Set<Authority> authorities = new HashSet<>();
            managedUserDTO.getAuthorities().stream().forEach(
                authority -> authorities.add(authorityRepository.findOne(authority))
            );
            user.setAuthorities(authorities);
        }
        String encryptedPassword = passwordEncoder.encode(RandomUtil.generatePassword());
        user.setPassword(encryptedPassword);
        user.setResetKey(RandomUtil.generateResetKey());
        user.setResetDate(ZonedDateTime.now());
        user.setActivated(true);
        userRepository.save(user);
        log.debug("Created Information for User: {}", user);
        return user;
    }

    public void updateUserInformation(String firstName, String lastName, String email, String langKey) {
        userRepository.findOneByLogin(SecurityUtils.getCurrentUser().getUsername()).ifPresent(u -> {
            u.setFirstName(firstName);
            u.setLastName(lastName);
            u.setEmail(email);
            u.setLangKey(langKey);
            userRepository.save(u);
            log.debug("Changed Information for User: {}", u);
        });
    }

    public void deleteUserInformation(String login) {
        userRepository.findOneByLogin(login).ifPresent(u -> {
            userRepository.delete(u);
            log.debug("Deleted User: {}", u);
        });
    }

    public void changePassword(String password) {
        userRepository.findOneByLogin(SecurityUtils.getCurrentUser().getUsername()).ifPresent(u -> {
            String encryptedPassword = passwordEncoder.encode(password);
            u.setPassword(encryptedPassword);
            userRepository.save(u);
            log.debug("Changed password for User: {}", u);
        });
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthoritiesByLogin(String login) {
        return userRepository.findOneByLogin(login).map(u -> {
            u.getAuthorities().size();
            return u;
        });
    }

    @Transactional(readOnly = true)
    public User getUserWithAuthorities(Long id) {
        User user = userRepository.findOne(id);
        user.getAuthorities().size(); // eagerly load the association
        return user;
    }

    @Transactional(readOnly = true)
    public User getUserWithAuthorities() {
        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUser().getUsername()).get();
        user.getAuthorities().size(); // eagerly load the association
        return user;
    }

    /**
     * Persistent Token are used for providing automatic authentication, they should be automatically deleted after
     * 30 days.
     * <p/>
     * <p>
     * This is scheduled to get fired everyday, at midnight.
     * </p>
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void removeOldPersistentTokens() {
        LocalDate now = LocalDate.now();
        persistentTokenRepository.findByTokenDateBefore(now.minusMonths(1)).stream().forEach(token -> {
            log.debug("Deleting token {}", token.getSeries());
            User user = token.getUser();
            user.getPersistentTokens().remove(token);
            persistentTokenRepository.delete(token);
        });
    }

    /**
     * Not activated users should be automatically deleted after 3 days.
     * <p/>
     * <p>
     * This is scheduled to get fired everyday, at 01:00 (am).
     * </p>
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void removeNotActivatedUsers() {
        ZonedDateTime now = ZonedDateTime.now();
        List<User> users = userRepository.findAllByActivatedIsFalseAndCreatedDateBefore(now.minusDays(3));
        for (User user : users) {
            log.debug("Deleting not activated user {}", user.getLogin());
            userRepository.delete(user);
        }
    }

    public void addTroll(Long number, String restrictedPassword) throws IOException, MountyHubException, MountyHallScriptException {
        User currentUser = getUserWithAuthorities();

        // A troll is already linked to the user
        if (currentUser.getTroll() != null) {
            throw new MountyHubException("Votre utilisateur a déjà un troll lié !");
        }

        Optional<Troll> trollResult = trollRepository.findOneByNumber(number);
        if (trollResult.isPresent()) {
            throw new MountyHubException("Le numero de troll " + number + " est déjà associé à l'utilisteur " + trollResult.get().getUser().getLogin() + " !");
        }

        // ScriptCall creation
        ScriptCall scriptCall = new ScriptCall();
        scriptCall.setDateCalled(ZonedDateTime.now());
        scriptCall.setName(ScriptName.SP_Caract);
        scriptCall.setType(ScriptType.DYNAMIQUE);
        scriptCall.setSuccessful(false);

//        String url = MountyHallScriptUtil.getUrl(ScriptName.SP_Caract, number, restrictedPassword);
        String url = "https://gist.githubusercontent.com/Falydoor/b53eaca2eb09778a943b/raw/432af4f161f78941b101a9bba9b5c640f103dd27/gistfile1.txt";
        scriptCall.setUrl(url);

        try {
            // Get the characteristic of the troll
            String response = IOUtils.toString(new URL(url));
            String[] lines = StringUtils.split(response, "\n");

            // Bad number/password
            if (lines.length != 3) {
                throw new MountyHallScriptException("Le numéro de troll ou le mot de passe restreint sont faux !");
            }

            // Create the troll
            Troll troll = new Troll();
            troll.setNumber(number);

            for (String line : lines) {
                String[] values = StringUtils.split(line, ";");

                // Bad number of values
                if (values.length != 14) {
                    throw new MountyHallScriptException("Réponse du script MountyHall incorrect !");
                }

                scriptCall.setSuccessful(true);

                // Attaque; Esquive; Dégats; Régénération; PVMax; PVActuels; Portée deVue; RM; MM; Armure; Duree du Tour; Poids; Concentration
                switch (values[0]) {
                    case "BMM":
                        troll.setAttackM(Integer.parseInt(values[1]));
                        troll.setDodgeM(Integer.parseInt(values[2]));
                        troll.setDamageM(Integer.parseInt(values[3]));
                        troll.setRegenerationM(Integer.parseInt(values[4]));
                        troll.setHitPointM(Integer.parseInt(values[5]));
                        troll.setViewM(Integer.parseInt(values[7]));
                        troll.setRmM(Integer.parseInt(values[8]));
                        troll.setMmM(Integer.parseInt(values[9]));
                        troll.setArmorM(Integer.parseInt(values[10]));
                        troll.setWeightM(Integer.parseInt(values[12]));
                        break;
                    case "BMP":
                        troll.setAttackP(Integer.parseInt(values[1]));
                        troll.setDodgeP(Integer.parseInt(values[2]));
                        troll.setDamageP(Integer.parseInt(values[3]));
                        troll.setRegenerationP(Integer.parseInt(values[4]));
                        troll.setHitPointP(Integer.parseInt(values[5]));
                        troll.setViewP(Integer.parseInt(values[7]));
                        troll.setRmP(Integer.parseInt(values[8]));
                        troll.setMmP(Integer.parseInt(values[9]));
                        troll.setArmorP(Integer.parseInt(values[10]));
                        troll.setWeightP(Integer.parseInt(values[12]));
                        break;
                    case "CAR":
                        troll.setAttack(Integer.parseInt(values[1]));
                        troll.setDodge(Integer.parseInt(values[2]));
                        troll.setDamage(Integer.parseInt(values[3]));
                        troll.setRegeneration(Integer.parseInt(values[4]));
                        troll.setHitPoint(Integer.parseInt(values[5]));
                        troll.setCurrentHitPoint(Integer.parseInt(values[6]));
                        troll.setView(Integer.parseInt(values[7]));
                        troll.setRm(Integer.parseInt(values[8]));
                        troll.setMm(Integer.parseInt(values[9]));
                        troll.setArmor(Integer.parseInt(values[10]));
                        troll.setTurn(Integer.parseInt(values[11]));
                        troll.setWeight(Integer.parseInt(values[12]));
                        troll.setFocus(Integer.parseInt(values[13]));
                        break;
                    default:
                }
            }

            // Save the troll
            troll.setX(0);
            troll.setY(0);
            troll.setZ(0);
            troll.setRace(TrollRace.TOMAWAK);
            troll.setBirthDate(ZonedDateTime.now());
            troll.setName("Test");
            currentUser.setTroll(troll);
            trollRepository.save(troll);
            scriptCall.setTroll(troll);
        } finally {
            scriptCallRepository.save(scriptCall);
        }
    }
}
