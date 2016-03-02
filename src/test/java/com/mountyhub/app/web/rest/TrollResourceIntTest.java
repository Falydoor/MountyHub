package com.mountyhub.app.web.rest;

import com.mountyhub.app.Application;
import com.mountyhub.app.domain.Troll;
import com.mountyhub.app.repository.TrollRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mountyhub.app.domain.enumeration.TrollRace;

/**
 * Test class for the TrollResource REST controller.
 *
 * @see TrollResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class TrollResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Z"));


    private static final Long DEFAULT_NUMBER = 1L;
    private static final Long UPDATED_NUMBER = 2L;
    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    
    private static final TrollRace DEFAULT_RACE = TrollRace.Tomawak;
    private static final TrollRace UPDATED_RACE = TrollRace.Durakuir;

    private static final ZonedDateTime DEFAULT_BIRTH_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_BIRTH_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_BIRTH_DATE_STR = dateTimeFormatter.format(DEFAULT_BIRTH_DATE);

    private static final Integer DEFAULT_X = 1;
    private static final Integer UPDATED_X = 2;

    private static final Integer DEFAULT_Y = 1;
    private static final Integer UPDATED_Y = 2;

    private static final Integer DEFAULT_Z = 1;
    private static final Integer UPDATED_Z = 2;

    private static final Integer DEFAULT_ATTACK = 1;
    private static final Integer UPDATED_ATTACK = 2;

    private static final Integer DEFAULT_DODGE = 1;
    private static final Integer UPDATED_DODGE = 2;

    private static final Integer DEFAULT_DAMAGE = 1;
    private static final Integer UPDATED_DAMAGE = 2;

    private static final Integer DEFAULT_REGENERATION = 1;
    private static final Integer UPDATED_REGENERATION = 2;

    private static final Integer DEFAULT_HIT_POINT = 1;
    private static final Integer UPDATED_HIT_POINT = 2;

    private static final Integer DEFAULT_CURRENT_HIT_POINT = 1;
    private static final Integer UPDATED_CURRENT_HIT_POINT = 2;

    private static final Integer DEFAULT_VIEW = 1;
    private static final Integer UPDATED_VIEW = 2;

    private static final Integer DEFAULT_RM = 1;
    private static final Integer UPDATED_RM = 2;

    private static final Integer DEFAULT_MM = 1;
    private static final Integer UPDATED_MM = 2;

    private static final Integer DEFAULT_ARMOR = 1;
    private static final Integer UPDATED_ARMOR = 2;

    private static final Float DEFAULT_TURN = 1F;
    private static final Float UPDATED_TURN = 2F;

    private static final Float DEFAULT_WEIGHT = 1F;
    private static final Float UPDATED_WEIGHT = 2F;

    private static final Integer DEFAULT_FOCUS = 1;
    private static final Integer UPDATED_FOCUS = 2;

    private static final Integer DEFAULT_ATTACK_P = 1;
    private static final Integer UPDATED_ATTACK_P = 2;

    private static final Integer DEFAULT_DODGE_P = 1;
    private static final Integer UPDATED_DODGE_P = 2;

    private static final Integer DEFAULT_DAMAGE_P = 1;
    private static final Integer UPDATED_DAMAGE_P = 2;

    private static final Integer DEFAULT_REGENERATION_P = 1;
    private static final Integer UPDATED_REGENERATION_P = 2;

    private static final Integer DEFAULT_HIT_POINT_P = 1;
    private static final Integer UPDATED_HIT_POINT_P = 2;

    private static final Integer DEFAULT_ATTACK_M = 1;
    private static final Integer UPDATED_ATTACK_M = 2;

    private static final Integer DEFAULT_DODGE_M = 1;
    private static final Integer UPDATED_DODGE_M = 2;

    private static final Integer DEFAULT_DAMAGE_M = 1;
    private static final Integer UPDATED_DAMAGE_M = 2;

    private static final Integer DEFAULT_REGENERATION_M = 1;
    private static final Integer UPDATED_REGENERATION_M = 2;

    private static final Integer DEFAULT_HIT_POINT_M = 1;
    private static final Integer UPDATED_HIT_POINT_M = 2;

    private static final Integer DEFAULT_VIEW_P = 1;
    private static final Integer UPDATED_VIEW_P = 2;

    private static final Integer DEFAULT_RM_P = 1;
    private static final Integer UPDATED_RM_P = 2;

    private static final Integer DEFAULT_MM_P = 1;
    private static final Integer UPDATED_MM_P = 2;

    private static final Integer DEFAULT_ARMOR_P = 1;
    private static final Integer UPDATED_ARMOR_P = 2;

    private static final Float DEFAULT_WEIGHT_P = 1F;
    private static final Float UPDATED_WEIGHT_P = 2F;

    private static final Integer DEFAULT_VIEW_M = 1;
    private static final Integer UPDATED_VIEW_M = 2;

    private static final Integer DEFAULT_RM_M = 1;
    private static final Integer UPDATED_RM_M = 2;

    private static final Integer DEFAULT_MM_M = 1;
    private static final Integer UPDATED_MM_M = 2;

    private static final Integer DEFAULT_ARMOR_M = 1;
    private static final Integer UPDATED_ARMOR_M = 2;

    private static final Float DEFAULT_WEIGHT_M = 1F;
    private static final Float UPDATED_WEIGHT_M = 2F;

    private static final Integer DEFAULT_LEVEL = 1;
    private static final Integer UPDATED_LEVEL = 2;

    private static final Integer DEFAULT_KILL = 1;
    private static final Integer UPDATED_KILL = 2;

    private static final Integer DEFAULT_DEATH = 1;
    private static final Integer UPDATED_DEATH = 2;
    private static final String DEFAULT_RESTRICTED_PASSWORD = "AAAAA";
    private static final String UPDATED_RESTRICTED_PASSWORD = "BBBBB";

    @Inject
    private TrollRepository trollRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restTrollMockMvc;

    private Troll troll;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TrollResource trollResource = new TrollResource();
        ReflectionTestUtils.setField(trollResource, "trollRepository", trollRepository);
        this.restTrollMockMvc = MockMvcBuilders.standaloneSetup(trollResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        troll = new Troll();
        troll.setNumber(DEFAULT_NUMBER);
        troll.setName(DEFAULT_NAME);
        troll.setRace(DEFAULT_RACE);
        troll.setBirthDate(DEFAULT_BIRTH_DATE);
        troll.setX(DEFAULT_X);
        troll.setY(DEFAULT_Y);
        troll.setZ(DEFAULT_Z);
        troll.setAttack(DEFAULT_ATTACK);
        troll.setDodge(DEFAULT_DODGE);
        troll.setDamage(DEFAULT_DAMAGE);
        troll.setRegeneration(DEFAULT_REGENERATION);
        troll.setHitPoint(DEFAULT_HIT_POINT);
        troll.setCurrentHitPoint(DEFAULT_CURRENT_HIT_POINT);
        troll.setView(DEFAULT_VIEW);
        troll.setRm(DEFAULT_RM);
        troll.setMm(DEFAULT_MM);
        troll.setArmor(DEFAULT_ARMOR);
        troll.setTurn(DEFAULT_TURN);
        troll.setWeight(DEFAULT_WEIGHT);
        troll.setFocus(DEFAULT_FOCUS);
        troll.setAttackP(DEFAULT_ATTACK_P);
        troll.setDodgeP(DEFAULT_DODGE_P);
        troll.setDamageP(DEFAULT_DAMAGE_P);
        troll.setRegenerationP(DEFAULT_REGENERATION_P);
        troll.setHitPointP(DEFAULT_HIT_POINT_P);
        troll.setAttackM(DEFAULT_ATTACK_M);
        troll.setDodgeM(DEFAULT_DODGE_M);
        troll.setDamageM(DEFAULT_DAMAGE_M);
        troll.setRegenerationM(DEFAULT_REGENERATION_M);
        troll.setHitPointM(DEFAULT_HIT_POINT_M);
        troll.setViewP(DEFAULT_VIEW_P);
        troll.setRmP(DEFAULT_RM_P);
        troll.setMmP(DEFAULT_MM_P);
        troll.setArmorP(DEFAULT_ARMOR_P);
        troll.setWeightP(DEFAULT_WEIGHT_P);
        troll.setViewM(DEFAULT_VIEW_M);
        troll.setRmM(DEFAULT_RM_M);
        troll.setMmM(DEFAULT_MM_M);
        troll.setArmorM(DEFAULT_ARMOR_M);
        troll.setWeightM(DEFAULT_WEIGHT_M);
        troll.setLevel(DEFAULT_LEVEL);
        troll.setKill(DEFAULT_KILL);
        troll.setDeath(DEFAULT_DEATH);
        troll.setRestrictedPassword(DEFAULT_RESTRICTED_PASSWORD);
    }

    @Test
    @Transactional
    public void createTroll() throws Exception {
        int databaseSizeBeforeCreate = trollRepository.findAll().size();

        // Create the Troll

        restTrollMockMvc.perform(post("/api/trolls")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(troll)))
                .andExpect(status().isCreated());

        // Validate the Troll in the database
        List<Troll> trolls = trollRepository.findAll();
        assertThat(trolls).hasSize(databaseSizeBeforeCreate + 1);
        Troll testTroll = trolls.get(trolls.size() - 1);
        assertThat(testTroll.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testTroll.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTroll.getRace()).isEqualTo(DEFAULT_RACE);
        assertThat(testTroll.getBirthDate()).isEqualTo(DEFAULT_BIRTH_DATE);
        assertThat(testTroll.getX()).isEqualTo(DEFAULT_X);
        assertThat(testTroll.getY()).isEqualTo(DEFAULT_Y);
        assertThat(testTroll.getZ()).isEqualTo(DEFAULT_Z);
        assertThat(testTroll.getAttack()).isEqualTo(DEFAULT_ATTACK);
        assertThat(testTroll.getDodge()).isEqualTo(DEFAULT_DODGE);
        assertThat(testTroll.getDamage()).isEqualTo(DEFAULT_DAMAGE);
        assertThat(testTroll.getRegeneration()).isEqualTo(DEFAULT_REGENERATION);
        assertThat(testTroll.getHitPoint()).isEqualTo(DEFAULT_HIT_POINT);
        assertThat(testTroll.getCurrentHitPoint()).isEqualTo(DEFAULT_CURRENT_HIT_POINT);
        assertThat(testTroll.getView()).isEqualTo(DEFAULT_VIEW);
        assertThat(testTroll.getRm()).isEqualTo(DEFAULT_RM);
        assertThat(testTroll.getMm()).isEqualTo(DEFAULT_MM);
        assertThat(testTroll.getArmor()).isEqualTo(DEFAULT_ARMOR);
        assertThat(testTroll.getTurn()).isEqualTo(DEFAULT_TURN);
        assertThat(testTroll.getWeight()).isEqualTo(DEFAULT_WEIGHT);
        assertThat(testTroll.getFocus()).isEqualTo(DEFAULT_FOCUS);
        assertThat(testTroll.getAttackP()).isEqualTo(DEFAULT_ATTACK_P);
        assertThat(testTroll.getDodgeP()).isEqualTo(DEFAULT_DODGE_P);
        assertThat(testTroll.getDamageP()).isEqualTo(DEFAULT_DAMAGE_P);
        assertThat(testTroll.getRegenerationP()).isEqualTo(DEFAULT_REGENERATION_P);
        assertThat(testTroll.getHitPointP()).isEqualTo(DEFAULT_HIT_POINT_P);
        assertThat(testTroll.getAttackM()).isEqualTo(DEFAULT_ATTACK_M);
        assertThat(testTroll.getDodgeM()).isEqualTo(DEFAULT_DODGE_M);
        assertThat(testTroll.getDamageM()).isEqualTo(DEFAULT_DAMAGE_M);
        assertThat(testTroll.getRegenerationM()).isEqualTo(DEFAULT_REGENERATION_M);
        assertThat(testTroll.getHitPointM()).isEqualTo(DEFAULT_HIT_POINT_M);
        assertThat(testTroll.getViewP()).isEqualTo(DEFAULT_VIEW_P);
        assertThat(testTroll.getRmP()).isEqualTo(DEFAULT_RM_P);
        assertThat(testTroll.getMmP()).isEqualTo(DEFAULT_MM_P);
        assertThat(testTroll.getArmorP()).isEqualTo(DEFAULT_ARMOR_P);
        assertThat(testTroll.getWeightP()).isEqualTo(DEFAULT_WEIGHT_P);
        assertThat(testTroll.getViewM()).isEqualTo(DEFAULT_VIEW_M);
        assertThat(testTroll.getRmM()).isEqualTo(DEFAULT_RM_M);
        assertThat(testTroll.getMmM()).isEqualTo(DEFAULT_MM_M);
        assertThat(testTroll.getArmorM()).isEqualTo(DEFAULT_ARMOR_M);
        assertThat(testTroll.getWeightM()).isEqualTo(DEFAULT_WEIGHT_M);
        assertThat(testTroll.getLevel()).isEqualTo(DEFAULT_LEVEL);
        assertThat(testTroll.getKill()).isEqualTo(DEFAULT_KILL);
        assertThat(testTroll.getDeath()).isEqualTo(DEFAULT_DEATH);
        assertThat(testTroll.getRestrictedPassword()).isEqualTo(DEFAULT_RESTRICTED_PASSWORD);
    }

    @Test
    @Transactional
    public void checkNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = trollRepository.findAll().size();
        // set the field null
        troll.setNumber(null);

        // Create the Troll, which fails.

        restTrollMockMvc.perform(post("/api/trolls")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(troll)))
                .andExpect(status().isBadRequest());

        List<Troll> trolls = trollRepository.findAll();
        assertThat(trolls).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = trollRepository.findAll().size();
        // set the field null
        troll.setName(null);

        // Create the Troll, which fails.

        restTrollMockMvc.perform(post("/api/trolls")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(troll)))
                .andExpect(status().isBadRequest());

        List<Troll> trolls = trollRepository.findAll();
        assertThat(trolls).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRaceIsRequired() throws Exception {
        int databaseSizeBeforeTest = trollRepository.findAll().size();
        // set the field null
        troll.setRace(null);

        // Create the Troll, which fails.

        restTrollMockMvc.perform(post("/api/trolls")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(troll)))
                .andExpect(status().isBadRequest());

        List<Troll> trolls = trollRepository.findAll();
        assertThat(trolls).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBirthDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = trollRepository.findAll().size();
        // set the field null
        troll.setBirthDate(null);

        // Create the Troll, which fails.

        restTrollMockMvc.perform(post("/api/trolls")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(troll)))
                .andExpect(status().isBadRequest());

        List<Troll> trolls = trollRepository.findAll();
        assertThat(trolls).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkXIsRequired() throws Exception {
        int databaseSizeBeforeTest = trollRepository.findAll().size();
        // set the field null
        troll.setX(null);

        // Create the Troll, which fails.

        restTrollMockMvc.perform(post("/api/trolls")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(troll)))
                .andExpect(status().isBadRequest());

        List<Troll> trolls = trollRepository.findAll();
        assertThat(trolls).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkYIsRequired() throws Exception {
        int databaseSizeBeforeTest = trollRepository.findAll().size();
        // set the field null
        troll.setY(null);

        // Create the Troll, which fails.

        restTrollMockMvc.perform(post("/api/trolls")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(troll)))
                .andExpect(status().isBadRequest());

        List<Troll> trolls = trollRepository.findAll();
        assertThat(trolls).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkZIsRequired() throws Exception {
        int databaseSizeBeforeTest = trollRepository.findAll().size();
        // set the field null
        troll.setZ(null);

        // Create the Troll, which fails.

        restTrollMockMvc.perform(post("/api/trolls")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(troll)))
                .andExpect(status().isBadRequest());

        List<Troll> trolls = trollRepository.findAll();
        assertThat(trolls).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAttackIsRequired() throws Exception {
        int databaseSizeBeforeTest = trollRepository.findAll().size();
        // set the field null
        troll.setAttack(null);

        // Create the Troll, which fails.

        restTrollMockMvc.perform(post("/api/trolls")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(troll)))
                .andExpect(status().isBadRequest());

        List<Troll> trolls = trollRepository.findAll();
        assertThat(trolls).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDodgeIsRequired() throws Exception {
        int databaseSizeBeforeTest = trollRepository.findAll().size();
        // set the field null
        troll.setDodge(null);

        // Create the Troll, which fails.

        restTrollMockMvc.perform(post("/api/trolls")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(troll)))
                .andExpect(status().isBadRequest());

        List<Troll> trolls = trollRepository.findAll();
        assertThat(trolls).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDamageIsRequired() throws Exception {
        int databaseSizeBeforeTest = trollRepository.findAll().size();
        // set the field null
        troll.setDamage(null);

        // Create the Troll, which fails.

        restTrollMockMvc.perform(post("/api/trolls")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(troll)))
                .andExpect(status().isBadRequest());

        List<Troll> trolls = trollRepository.findAll();
        assertThat(trolls).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRegenerationIsRequired() throws Exception {
        int databaseSizeBeforeTest = trollRepository.findAll().size();
        // set the field null
        troll.setRegeneration(null);

        // Create the Troll, which fails.

        restTrollMockMvc.perform(post("/api/trolls")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(troll)))
                .andExpect(status().isBadRequest());

        List<Troll> trolls = trollRepository.findAll();
        assertThat(trolls).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkHitPointIsRequired() throws Exception {
        int databaseSizeBeforeTest = trollRepository.findAll().size();
        // set the field null
        troll.setHitPoint(null);

        // Create the Troll, which fails.

        restTrollMockMvc.perform(post("/api/trolls")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(troll)))
                .andExpect(status().isBadRequest());

        List<Troll> trolls = trollRepository.findAll();
        assertThat(trolls).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCurrentHitPointIsRequired() throws Exception {
        int databaseSizeBeforeTest = trollRepository.findAll().size();
        // set the field null
        troll.setCurrentHitPoint(null);

        // Create the Troll, which fails.

        restTrollMockMvc.perform(post("/api/trolls")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(troll)))
                .andExpect(status().isBadRequest());

        List<Troll> trolls = trollRepository.findAll();
        assertThat(trolls).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkViewIsRequired() throws Exception {
        int databaseSizeBeforeTest = trollRepository.findAll().size();
        // set the field null
        troll.setView(null);

        // Create the Troll, which fails.

        restTrollMockMvc.perform(post("/api/trolls")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(troll)))
                .andExpect(status().isBadRequest());

        List<Troll> trolls = trollRepository.findAll();
        assertThat(trolls).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRmIsRequired() throws Exception {
        int databaseSizeBeforeTest = trollRepository.findAll().size();
        // set the field null
        troll.setRm(null);

        // Create the Troll, which fails.

        restTrollMockMvc.perform(post("/api/trolls")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(troll)))
                .andExpect(status().isBadRequest());

        List<Troll> trolls = trollRepository.findAll();
        assertThat(trolls).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMmIsRequired() throws Exception {
        int databaseSizeBeforeTest = trollRepository.findAll().size();
        // set the field null
        troll.setMm(null);

        // Create the Troll, which fails.

        restTrollMockMvc.perform(post("/api/trolls")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(troll)))
                .andExpect(status().isBadRequest());

        List<Troll> trolls = trollRepository.findAll();
        assertThat(trolls).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkArmorIsRequired() throws Exception {
        int databaseSizeBeforeTest = trollRepository.findAll().size();
        // set the field null
        troll.setArmor(null);

        // Create the Troll, which fails.

        restTrollMockMvc.perform(post("/api/trolls")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(troll)))
                .andExpect(status().isBadRequest());

        List<Troll> trolls = trollRepository.findAll();
        assertThat(trolls).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTurnIsRequired() throws Exception {
        int databaseSizeBeforeTest = trollRepository.findAll().size();
        // set the field null
        troll.setTurn(null);

        // Create the Troll, which fails.

        restTrollMockMvc.perform(post("/api/trolls")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(troll)))
                .andExpect(status().isBadRequest());

        List<Troll> trolls = trollRepository.findAll();
        assertThat(trolls).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkWeightIsRequired() throws Exception {
        int databaseSizeBeforeTest = trollRepository.findAll().size();
        // set the field null
        troll.setWeight(null);

        // Create the Troll, which fails.

        restTrollMockMvc.perform(post("/api/trolls")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(troll)))
                .andExpect(status().isBadRequest());

        List<Troll> trolls = trollRepository.findAll();
        assertThat(trolls).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFocusIsRequired() throws Exception {
        int databaseSizeBeforeTest = trollRepository.findAll().size();
        // set the field null
        troll.setFocus(null);

        // Create the Troll, which fails.

        restTrollMockMvc.perform(post("/api/trolls")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(troll)))
                .andExpect(status().isBadRequest());

        List<Troll> trolls = trollRepository.findAll();
        assertThat(trolls).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAttackPIsRequired() throws Exception {
        int databaseSizeBeforeTest = trollRepository.findAll().size();
        // set the field null
        troll.setAttackP(null);

        // Create the Troll, which fails.

        restTrollMockMvc.perform(post("/api/trolls")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(troll)))
                .andExpect(status().isBadRequest());

        List<Troll> trolls = trollRepository.findAll();
        assertThat(trolls).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDodgePIsRequired() throws Exception {
        int databaseSizeBeforeTest = trollRepository.findAll().size();
        // set the field null
        troll.setDodgeP(null);

        // Create the Troll, which fails.

        restTrollMockMvc.perform(post("/api/trolls")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(troll)))
                .andExpect(status().isBadRequest());

        List<Troll> trolls = trollRepository.findAll();
        assertThat(trolls).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDamagePIsRequired() throws Exception {
        int databaseSizeBeforeTest = trollRepository.findAll().size();
        // set the field null
        troll.setDamageP(null);

        // Create the Troll, which fails.

        restTrollMockMvc.perform(post("/api/trolls")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(troll)))
                .andExpect(status().isBadRequest());

        List<Troll> trolls = trollRepository.findAll();
        assertThat(trolls).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRegenerationPIsRequired() throws Exception {
        int databaseSizeBeforeTest = trollRepository.findAll().size();
        // set the field null
        troll.setRegenerationP(null);

        // Create the Troll, which fails.

        restTrollMockMvc.perform(post("/api/trolls")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(troll)))
                .andExpect(status().isBadRequest());

        List<Troll> trolls = trollRepository.findAll();
        assertThat(trolls).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkHitPointPIsRequired() throws Exception {
        int databaseSizeBeforeTest = trollRepository.findAll().size();
        // set the field null
        troll.setHitPointP(null);

        // Create the Troll, which fails.

        restTrollMockMvc.perform(post("/api/trolls")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(troll)))
                .andExpect(status().isBadRequest());

        List<Troll> trolls = trollRepository.findAll();
        assertThat(trolls).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAttackMIsRequired() throws Exception {
        int databaseSizeBeforeTest = trollRepository.findAll().size();
        // set the field null
        troll.setAttackM(null);

        // Create the Troll, which fails.

        restTrollMockMvc.perform(post("/api/trolls")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(troll)))
                .andExpect(status().isBadRequest());

        List<Troll> trolls = trollRepository.findAll();
        assertThat(trolls).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDodgeMIsRequired() throws Exception {
        int databaseSizeBeforeTest = trollRepository.findAll().size();
        // set the field null
        troll.setDodgeM(null);

        // Create the Troll, which fails.

        restTrollMockMvc.perform(post("/api/trolls")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(troll)))
                .andExpect(status().isBadRequest());

        List<Troll> trolls = trollRepository.findAll();
        assertThat(trolls).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDamageMIsRequired() throws Exception {
        int databaseSizeBeforeTest = trollRepository.findAll().size();
        // set the field null
        troll.setDamageM(null);

        // Create the Troll, which fails.

        restTrollMockMvc.perform(post("/api/trolls")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(troll)))
                .andExpect(status().isBadRequest());

        List<Troll> trolls = trollRepository.findAll();
        assertThat(trolls).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRegenerationMIsRequired() throws Exception {
        int databaseSizeBeforeTest = trollRepository.findAll().size();
        // set the field null
        troll.setRegenerationM(null);

        // Create the Troll, which fails.

        restTrollMockMvc.perform(post("/api/trolls")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(troll)))
                .andExpect(status().isBadRequest());

        List<Troll> trolls = trollRepository.findAll();
        assertThat(trolls).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkHitPointMIsRequired() throws Exception {
        int databaseSizeBeforeTest = trollRepository.findAll().size();
        // set the field null
        troll.setHitPointM(null);

        // Create the Troll, which fails.

        restTrollMockMvc.perform(post("/api/trolls")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(troll)))
                .andExpect(status().isBadRequest());

        List<Troll> trolls = trollRepository.findAll();
        assertThat(trolls).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkViewPIsRequired() throws Exception {
        int databaseSizeBeforeTest = trollRepository.findAll().size();
        // set the field null
        troll.setViewP(null);

        // Create the Troll, which fails.

        restTrollMockMvc.perform(post("/api/trolls")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(troll)))
                .andExpect(status().isBadRequest());

        List<Troll> trolls = trollRepository.findAll();
        assertThat(trolls).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRmPIsRequired() throws Exception {
        int databaseSizeBeforeTest = trollRepository.findAll().size();
        // set the field null
        troll.setRmP(null);

        // Create the Troll, which fails.

        restTrollMockMvc.perform(post("/api/trolls")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(troll)))
                .andExpect(status().isBadRequest());

        List<Troll> trolls = trollRepository.findAll();
        assertThat(trolls).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMmPIsRequired() throws Exception {
        int databaseSizeBeforeTest = trollRepository.findAll().size();
        // set the field null
        troll.setMmP(null);

        // Create the Troll, which fails.

        restTrollMockMvc.perform(post("/api/trolls")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(troll)))
                .andExpect(status().isBadRequest());

        List<Troll> trolls = trollRepository.findAll();
        assertThat(trolls).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkArmorPIsRequired() throws Exception {
        int databaseSizeBeforeTest = trollRepository.findAll().size();
        // set the field null
        troll.setArmorP(null);

        // Create the Troll, which fails.

        restTrollMockMvc.perform(post("/api/trolls")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(troll)))
                .andExpect(status().isBadRequest());

        List<Troll> trolls = trollRepository.findAll();
        assertThat(trolls).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkWeightPIsRequired() throws Exception {
        int databaseSizeBeforeTest = trollRepository.findAll().size();
        // set the field null
        troll.setWeightP(null);

        // Create the Troll, which fails.

        restTrollMockMvc.perform(post("/api/trolls")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(troll)))
                .andExpect(status().isBadRequest());

        List<Troll> trolls = trollRepository.findAll();
        assertThat(trolls).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkViewMIsRequired() throws Exception {
        int databaseSizeBeforeTest = trollRepository.findAll().size();
        // set the field null
        troll.setViewM(null);

        // Create the Troll, which fails.

        restTrollMockMvc.perform(post("/api/trolls")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(troll)))
                .andExpect(status().isBadRequest());

        List<Troll> trolls = trollRepository.findAll();
        assertThat(trolls).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRmMIsRequired() throws Exception {
        int databaseSizeBeforeTest = trollRepository.findAll().size();
        // set the field null
        troll.setRmM(null);

        // Create the Troll, which fails.

        restTrollMockMvc.perform(post("/api/trolls")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(troll)))
                .andExpect(status().isBadRequest());

        List<Troll> trolls = trollRepository.findAll();
        assertThat(trolls).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMmMIsRequired() throws Exception {
        int databaseSizeBeforeTest = trollRepository.findAll().size();
        // set the field null
        troll.setMmM(null);

        // Create the Troll, which fails.

        restTrollMockMvc.perform(post("/api/trolls")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(troll)))
                .andExpect(status().isBadRequest());

        List<Troll> trolls = trollRepository.findAll();
        assertThat(trolls).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkArmorMIsRequired() throws Exception {
        int databaseSizeBeforeTest = trollRepository.findAll().size();
        // set the field null
        troll.setArmorM(null);

        // Create the Troll, which fails.

        restTrollMockMvc.perform(post("/api/trolls")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(troll)))
                .andExpect(status().isBadRequest());

        List<Troll> trolls = trollRepository.findAll();
        assertThat(trolls).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkWeightMIsRequired() throws Exception {
        int databaseSizeBeforeTest = trollRepository.findAll().size();
        // set the field null
        troll.setWeightM(null);

        // Create the Troll, which fails.

        restTrollMockMvc.perform(post("/api/trolls")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(troll)))
                .andExpect(status().isBadRequest());

        List<Troll> trolls = trollRepository.findAll();
        assertThat(trolls).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLevelIsRequired() throws Exception {
        int databaseSizeBeforeTest = trollRepository.findAll().size();
        // set the field null
        troll.setLevel(null);

        // Create the Troll, which fails.

        restTrollMockMvc.perform(post("/api/trolls")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(troll)))
                .andExpect(status().isBadRequest());

        List<Troll> trolls = trollRepository.findAll();
        assertThat(trolls).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkKillIsRequired() throws Exception {
        int databaseSizeBeforeTest = trollRepository.findAll().size();
        // set the field null
        troll.setKill(null);

        // Create the Troll, which fails.

        restTrollMockMvc.perform(post("/api/trolls")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(troll)))
                .andExpect(status().isBadRequest());

        List<Troll> trolls = trollRepository.findAll();
        assertThat(trolls).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDeathIsRequired() throws Exception {
        int databaseSizeBeforeTest = trollRepository.findAll().size();
        // set the field null
        troll.setDeath(null);

        // Create the Troll, which fails.

        restTrollMockMvc.perform(post("/api/trolls")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(troll)))
                .andExpect(status().isBadRequest());

        List<Troll> trolls = trollRepository.findAll();
        assertThat(trolls).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRestrictedPasswordIsRequired() throws Exception {
        int databaseSizeBeforeTest = trollRepository.findAll().size();
        // set the field null
        troll.setRestrictedPassword(null);

        // Create the Troll, which fails.

        restTrollMockMvc.perform(post("/api/trolls")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(troll)))
                .andExpect(status().isBadRequest());

        List<Troll> trolls = trollRepository.findAll();
        assertThat(trolls).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTrolls() throws Exception {
        // Initialize the database
        trollRepository.saveAndFlush(troll);

        // Get all the trolls
        restTrollMockMvc.perform(get("/api/trolls?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(troll.getId().intValue())))
                .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER.intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].race").value(hasItem(DEFAULT_RACE.toString())))
                .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE_STR)))
                .andExpect(jsonPath("$.[*].x").value(hasItem(DEFAULT_X)))
                .andExpect(jsonPath("$.[*].y").value(hasItem(DEFAULT_Y)))
                .andExpect(jsonPath("$.[*].z").value(hasItem(DEFAULT_Z)))
                .andExpect(jsonPath("$.[*].attack").value(hasItem(DEFAULT_ATTACK)))
                .andExpect(jsonPath("$.[*].dodge").value(hasItem(DEFAULT_DODGE)))
                .andExpect(jsonPath("$.[*].damage").value(hasItem(DEFAULT_DAMAGE)))
                .andExpect(jsonPath("$.[*].regeneration").value(hasItem(DEFAULT_REGENERATION)))
                .andExpect(jsonPath("$.[*].hitPoint").value(hasItem(DEFAULT_HIT_POINT)))
                .andExpect(jsonPath("$.[*].currentHitPoint").value(hasItem(DEFAULT_CURRENT_HIT_POINT)))
                .andExpect(jsonPath("$.[*].view").value(hasItem(DEFAULT_VIEW)))
                .andExpect(jsonPath("$.[*].rm").value(hasItem(DEFAULT_RM)))
                .andExpect(jsonPath("$.[*].mm").value(hasItem(DEFAULT_MM)))
                .andExpect(jsonPath("$.[*].armor").value(hasItem(DEFAULT_ARMOR)))
                .andExpect(jsonPath("$.[*].turn").value(hasItem(DEFAULT_TURN.doubleValue())))
                .andExpect(jsonPath("$.[*].weight").value(hasItem(DEFAULT_WEIGHT.doubleValue())))
                .andExpect(jsonPath("$.[*].focus").value(hasItem(DEFAULT_FOCUS)))
                .andExpect(jsonPath("$.[*].attackP").value(hasItem(DEFAULT_ATTACK_P)))
                .andExpect(jsonPath("$.[*].dodgeP").value(hasItem(DEFAULT_DODGE_P)))
                .andExpect(jsonPath("$.[*].damageP").value(hasItem(DEFAULT_DAMAGE_P)))
                .andExpect(jsonPath("$.[*].regenerationP").value(hasItem(DEFAULT_REGENERATION_P)))
                .andExpect(jsonPath("$.[*].hitPointP").value(hasItem(DEFAULT_HIT_POINT_P)))
                .andExpect(jsonPath("$.[*].attackM").value(hasItem(DEFAULT_ATTACK_M)))
                .andExpect(jsonPath("$.[*].dodgeM").value(hasItem(DEFAULT_DODGE_M)))
                .andExpect(jsonPath("$.[*].damageM").value(hasItem(DEFAULT_DAMAGE_M)))
                .andExpect(jsonPath("$.[*].regenerationM").value(hasItem(DEFAULT_REGENERATION_M)))
                .andExpect(jsonPath("$.[*].hitPointM").value(hasItem(DEFAULT_HIT_POINT_M)))
                .andExpect(jsonPath("$.[*].viewP").value(hasItem(DEFAULT_VIEW_P)))
                .andExpect(jsonPath("$.[*].rmP").value(hasItem(DEFAULT_RM_P)))
                .andExpect(jsonPath("$.[*].mmP").value(hasItem(DEFAULT_MM_P)))
                .andExpect(jsonPath("$.[*].armorP").value(hasItem(DEFAULT_ARMOR_P)))
                .andExpect(jsonPath("$.[*].weightP").value(hasItem(DEFAULT_WEIGHT_P.doubleValue())))
                .andExpect(jsonPath("$.[*].viewM").value(hasItem(DEFAULT_VIEW_M)))
                .andExpect(jsonPath("$.[*].rmM").value(hasItem(DEFAULT_RM_M)))
                .andExpect(jsonPath("$.[*].mmM").value(hasItem(DEFAULT_MM_M)))
                .andExpect(jsonPath("$.[*].armorM").value(hasItem(DEFAULT_ARMOR_M)))
                .andExpect(jsonPath("$.[*].weightM").value(hasItem(DEFAULT_WEIGHT_M.doubleValue())))
                .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL)))
                .andExpect(jsonPath("$.[*].kill").value(hasItem(DEFAULT_KILL)))
                .andExpect(jsonPath("$.[*].death").value(hasItem(DEFAULT_DEATH)))
                .andExpect(jsonPath("$.[*].restrictedPassword").value(hasItem(DEFAULT_RESTRICTED_PASSWORD.toString())));
    }

    @Test
    @Transactional
    public void getTroll() throws Exception {
        // Initialize the database
        trollRepository.saveAndFlush(troll);

        // Get the troll
        restTrollMockMvc.perform(get("/api/trolls/{id}", troll.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(troll.getId().intValue()))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER.intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.race").value(DEFAULT_RACE.toString()))
            .andExpect(jsonPath("$.birthDate").value(DEFAULT_BIRTH_DATE_STR))
            .andExpect(jsonPath("$.x").value(DEFAULT_X))
            .andExpect(jsonPath("$.y").value(DEFAULT_Y))
            .andExpect(jsonPath("$.z").value(DEFAULT_Z))
            .andExpect(jsonPath("$.attack").value(DEFAULT_ATTACK))
            .andExpect(jsonPath("$.dodge").value(DEFAULT_DODGE))
            .andExpect(jsonPath("$.damage").value(DEFAULT_DAMAGE))
            .andExpect(jsonPath("$.regeneration").value(DEFAULT_REGENERATION))
            .andExpect(jsonPath("$.hitPoint").value(DEFAULT_HIT_POINT))
            .andExpect(jsonPath("$.currentHitPoint").value(DEFAULT_CURRENT_HIT_POINT))
            .andExpect(jsonPath("$.view").value(DEFAULT_VIEW))
            .andExpect(jsonPath("$.rm").value(DEFAULT_RM))
            .andExpect(jsonPath("$.mm").value(DEFAULT_MM))
            .andExpect(jsonPath("$.armor").value(DEFAULT_ARMOR))
            .andExpect(jsonPath("$.turn").value(DEFAULT_TURN.doubleValue()))
            .andExpect(jsonPath("$.weight").value(DEFAULT_WEIGHT.doubleValue()))
            .andExpect(jsonPath("$.focus").value(DEFAULT_FOCUS))
            .andExpect(jsonPath("$.attackP").value(DEFAULT_ATTACK_P))
            .andExpect(jsonPath("$.dodgeP").value(DEFAULT_DODGE_P))
            .andExpect(jsonPath("$.damageP").value(DEFAULT_DAMAGE_P))
            .andExpect(jsonPath("$.regenerationP").value(DEFAULT_REGENERATION_P))
            .andExpect(jsonPath("$.hitPointP").value(DEFAULT_HIT_POINT_P))
            .andExpect(jsonPath("$.attackM").value(DEFAULT_ATTACK_M))
            .andExpect(jsonPath("$.dodgeM").value(DEFAULT_DODGE_M))
            .andExpect(jsonPath("$.damageM").value(DEFAULT_DAMAGE_M))
            .andExpect(jsonPath("$.regenerationM").value(DEFAULT_REGENERATION_M))
            .andExpect(jsonPath("$.hitPointM").value(DEFAULT_HIT_POINT_M))
            .andExpect(jsonPath("$.viewP").value(DEFAULT_VIEW_P))
            .andExpect(jsonPath("$.rmP").value(DEFAULT_RM_P))
            .andExpect(jsonPath("$.mmP").value(DEFAULT_MM_P))
            .andExpect(jsonPath("$.armorP").value(DEFAULT_ARMOR_P))
            .andExpect(jsonPath("$.weightP").value(DEFAULT_WEIGHT_P.doubleValue()))
            .andExpect(jsonPath("$.viewM").value(DEFAULT_VIEW_M))
            .andExpect(jsonPath("$.rmM").value(DEFAULT_RM_M))
            .andExpect(jsonPath("$.mmM").value(DEFAULT_MM_M))
            .andExpect(jsonPath("$.armorM").value(DEFAULT_ARMOR_M))
            .andExpect(jsonPath("$.weightM").value(DEFAULT_WEIGHT_M.doubleValue()))
            .andExpect(jsonPath("$.level").value(DEFAULT_LEVEL))
            .andExpect(jsonPath("$.kill").value(DEFAULT_KILL))
            .andExpect(jsonPath("$.death").value(DEFAULT_DEATH))
            .andExpect(jsonPath("$.restrictedPassword").value(DEFAULT_RESTRICTED_PASSWORD.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTroll() throws Exception {
        // Get the troll
        restTrollMockMvc.perform(get("/api/trolls/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTroll() throws Exception {
        // Initialize the database
        trollRepository.saveAndFlush(troll);

		int databaseSizeBeforeUpdate = trollRepository.findAll().size();

        // Update the troll
        troll.setNumber(UPDATED_NUMBER);
        troll.setName(UPDATED_NAME);
        troll.setRace(UPDATED_RACE);
        troll.setBirthDate(UPDATED_BIRTH_DATE);
        troll.setX(UPDATED_X);
        troll.setY(UPDATED_Y);
        troll.setZ(UPDATED_Z);
        troll.setAttack(UPDATED_ATTACK);
        troll.setDodge(UPDATED_DODGE);
        troll.setDamage(UPDATED_DAMAGE);
        troll.setRegeneration(UPDATED_REGENERATION);
        troll.setHitPoint(UPDATED_HIT_POINT);
        troll.setCurrentHitPoint(UPDATED_CURRENT_HIT_POINT);
        troll.setView(UPDATED_VIEW);
        troll.setRm(UPDATED_RM);
        troll.setMm(UPDATED_MM);
        troll.setArmor(UPDATED_ARMOR);
        troll.setTurn(UPDATED_TURN);
        troll.setWeight(UPDATED_WEIGHT);
        troll.setFocus(UPDATED_FOCUS);
        troll.setAttackP(UPDATED_ATTACK_P);
        troll.setDodgeP(UPDATED_DODGE_P);
        troll.setDamageP(UPDATED_DAMAGE_P);
        troll.setRegenerationP(UPDATED_REGENERATION_P);
        troll.setHitPointP(UPDATED_HIT_POINT_P);
        troll.setAttackM(UPDATED_ATTACK_M);
        troll.setDodgeM(UPDATED_DODGE_M);
        troll.setDamageM(UPDATED_DAMAGE_M);
        troll.setRegenerationM(UPDATED_REGENERATION_M);
        troll.setHitPointM(UPDATED_HIT_POINT_M);
        troll.setViewP(UPDATED_VIEW_P);
        troll.setRmP(UPDATED_RM_P);
        troll.setMmP(UPDATED_MM_P);
        troll.setArmorP(UPDATED_ARMOR_P);
        troll.setWeightP(UPDATED_WEIGHT_P);
        troll.setViewM(UPDATED_VIEW_M);
        troll.setRmM(UPDATED_RM_M);
        troll.setMmM(UPDATED_MM_M);
        troll.setArmorM(UPDATED_ARMOR_M);
        troll.setWeightM(UPDATED_WEIGHT_M);
        troll.setLevel(UPDATED_LEVEL);
        troll.setKill(UPDATED_KILL);
        troll.setDeath(UPDATED_DEATH);
        troll.setRestrictedPassword(UPDATED_RESTRICTED_PASSWORD);

        restTrollMockMvc.perform(put("/api/trolls")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(troll)))
                .andExpect(status().isOk());

        // Validate the Troll in the database
        List<Troll> trolls = trollRepository.findAll();
        assertThat(trolls).hasSize(databaseSizeBeforeUpdate);
        Troll testTroll = trolls.get(trolls.size() - 1);
        assertThat(testTroll.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testTroll.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTroll.getRace()).isEqualTo(UPDATED_RACE);
        assertThat(testTroll.getBirthDate()).isEqualTo(UPDATED_BIRTH_DATE);
        assertThat(testTroll.getX()).isEqualTo(UPDATED_X);
        assertThat(testTroll.getY()).isEqualTo(UPDATED_Y);
        assertThat(testTroll.getZ()).isEqualTo(UPDATED_Z);
        assertThat(testTroll.getAttack()).isEqualTo(UPDATED_ATTACK);
        assertThat(testTroll.getDodge()).isEqualTo(UPDATED_DODGE);
        assertThat(testTroll.getDamage()).isEqualTo(UPDATED_DAMAGE);
        assertThat(testTroll.getRegeneration()).isEqualTo(UPDATED_REGENERATION);
        assertThat(testTroll.getHitPoint()).isEqualTo(UPDATED_HIT_POINT);
        assertThat(testTroll.getCurrentHitPoint()).isEqualTo(UPDATED_CURRENT_HIT_POINT);
        assertThat(testTroll.getView()).isEqualTo(UPDATED_VIEW);
        assertThat(testTroll.getRm()).isEqualTo(UPDATED_RM);
        assertThat(testTroll.getMm()).isEqualTo(UPDATED_MM);
        assertThat(testTroll.getArmor()).isEqualTo(UPDATED_ARMOR);
        assertThat(testTroll.getTurn()).isEqualTo(UPDATED_TURN);
        assertThat(testTroll.getWeight()).isEqualTo(UPDATED_WEIGHT);
        assertThat(testTroll.getFocus()).isEqualTo(UPDATED_FOCUS);
        assertThat(testTroll.getAttackP()).isEqualTo(UPDATED_ATTACK_P);
        assertThat(testTroll.getDodgeP()).isEqualTo(UPDATED_DODGE_P);
        assertThat(testTroll.getDamageP()).isEqualTo(UPDATED_DAMAGE_P);
        assertThat(testTroll.getRegenerationP()).isEqualTo(UPDATED_REGENERATION_P);
        assertThat(testTroll.getHitPointP()).isEqualTo(UPDATED_HIT_POINT_P);
        assertThat(testTroll.getAttackM()).isEqualTo(UPDATED_ATTACK_M);
        assertThat(testTroll.getDodgeM()).isEqualTo(UPDATED_DODGE_M);
        assertThat(testTroll.getDamageM()).isEqualTo(UPDATED_DAMAGE_M);
        assertThat(testTroll.getRegenerationM()).isEqualTo(UPDATED_REGENERATION_M);
        assertThat(testTroll.getHitPointM()).isEqualTo(UPDATED_HIT_POINT_M);
        assertThat(testTroll.getViewP()).isEqualTo(UPDATED_VIEW_P);
        assertThat(testTroll.getRmP()).isEqualTo(UPDATED_RM_P);
        assertThat(testTroll.getMmP()).isEqualTo(UPDATED_MM_P);
        assertThat(testTroll.getArmorP()).isEqualTo(UPDATED_ARMOR_P);
        assertThat(testTroll.getWeightP()).isEqualTo(UPDATED_WEIGHT_P);
        assertThat(testTroll.getViewM()).isEqualTo(UPDATED_VIEW_M);
        assertThat(testTroll.getRmM()).isEqualTo(UPDATED_RM_M);
        assertThat(testTroll.getMmM()).isEqualTo(UPDATED_MM_M);
        assertThat(testTroll.getArmorM()).isEqualTo(UPDATED_ARMOR_M);
        assertThat(testTroll.getWeightM()).isEqualTo(UPDATED_WEIGHT_M);
        assertThat(testTroll.getLevel()).isEqualTo(UPDATED_LEVEL);
        assertThat(testTroll.getKill()).isEqualTo(UPDATED_KILL);
        assertThat(testTroll.getDeath()).isEqualTo(UPDATED_DEATH);
        assertThat(testTroll.getRestrictedPassword()).isEqualTo(UPDATED_RESTRICTED_PASSWORD);
    }

    @Test
    @Transactional
    public void deleteTroll() throws Exception {
        // Initialize the database
        trollRepository.saveAndFlush(troll);

		int databaseSizeBeforeDelete = trollRepository.findAll().size();

        // Get the troll
        restTrollMockMvc.perform(delete("/api/trolls/{id}", troll.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Troll> trolls = trollRepository.findAll();
        assertThat(trolls).hasSize(databaseSizeBeforeDelete - 1);
    }
}
