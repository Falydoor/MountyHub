package com.mountyhub.app.web.rest;

import com.mountyhub.app.Application;
import com.mountyhub.app.domain.BonusMalus;
import com.mountyhub.app.repository.BonusMalusRepository;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the BonusMalusResource REST controller.
 *
 * @see BonusMalusResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class BonusMalusResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_TYPE = "AAAAA";
    private static final String UPDATED_TYPE = "BBBBB";
    private static final String DEFAULT_EFFECT = "AAAAA";
    private static final String UPDATED_EFFECT = "BBBBB";
    private static final String DEFAULT_REAL_EFFECT = "AAAAA";
    private static final String UPDATED_REAL_EFFECT = "BBBBB";

    private static final Integer DEFAULT_DURATION = 1;
    private static final Integer UPDATED_DURATION = 2;

    private static final Integer DEFAULT_ATTACK = 1;
    private static final Integer UPDATED_ATTACK = 2;

    private static final Integer DEFAULT_ATTACK_M = 1;
    private static final Integer UPDATED_ATTACK_M = 2;

    private static final Integer DEFAULT_DODGE = 1;
    private static final Integer UPDATED_DODGE = 2;

    private static final Integer DEFAULT_DODGE_M = 1;
    private static final Integer UPDATED_DODGE_M = 2;

    private static final Integer DEFAULT_DAMAGE = 1;
    private static final Integer UPDATED_DAMAGE = 2;

    private static final Integer DEFAULT_DAMAGE_M = 1;
    private static final Integer UPDATED_DAMAGE_M = 2;

    private static final Integer DEFAULT_REGENERATION = 1;
    private static final Integer UPDATED_REGENERATION = 2;

    private static final Integer DEFAULT_HIT_POINT = 1;
    private static final Integer UPDATED_HIT_POINT = 2;

    private static final Integer DEFAULT_VIEW = 1;
    private static final Integer UPDATED_VIEW = 2;

    private static final Integer DEFAULT_RM = 1;
    private static final Integer UPDATED_RM = 2;

    private static final Integer DEFAULT_MM = 1;
    private static final Integer UPDATED_MM = 2;

    private static final Integer DEFAULT_ARMOR = 1;
    private static final Integer UPDATED_ARMOR = 2;

    private static final Integer DEFAULT_ARMOR_M = 1;
    private static final Integer UPDATED_ARMOR_M = 2;

    private static final Integer DEFAULT_TURN = 1;
    private static final Integer UPDATED_TURN = 2;

    @Inject
    private BonusMalusRepository bonusMalusRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restBonusMalusMockMvc;

    private BonusMalus bonusMalus;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BonusMalusResource bonusMalusResource = new BonusMalusResource();
        ReflectionTestUtils.setField(bonusMalusResource, "bonusMalusRepository", bonusMalusRepository);
        this.restBonusMalusMockMvc = MockMvcBuilders.standaloneSetup(bonusMalusResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        bonusMalus = new BonusMalus();
        bonusMalus.setName(DEFAULT_NAME);
        bonusMalus.setType(DEFAULT_TYPE);
        bonusMalus.setEffect(DEFAULT_EFFECT);
        bonusMalus.setRealEffect(DEFAULT_REAL_EFFECT);
        bonusMalus.setDuration(DEFAULT_DURATION);
        bonusMalus.setAttack(DEFAULT_ATTACK);
        bonusMalus.setAttackM(DEFAULT_ATTACK_M);
        bonusMalus.setDodge(DEFAULT_DODGE);
        bonusMalus.setDodgeM(DEFAULT_DODGE_M);
        bonusMalus.setDamage(DEFAULT_DAMAGE);
        bonusMalus.setDamageM(DEFAULT_DAMAGE_M);
        bonusMalus.setRegeneration(DEFAULT_REGENERATION);
        bonusMalus.setHitPoint(DEFAULT_HIT_POINT);
        bonusMalus.setView(DEFAULT_VIEW);
        bonusMalus.setRm(DEFAULT_RM);
        bonusMalus.setMm(DEFAULT_MM);
        bonusMalus.setArmor(DEFAULT_ARMOR);
        bonusMalus.setArmorM(DEFAULT_ARMOR_M);
        bonusMalus.setTurn(DEFAULT_TURN);
    }

    @Test
    @Transactional
    public void createBonusMalus() throws Exception {
        int databaseSizeBeforeCreate = bonusMalusRepository.findAll().size();

        // Create the BonusMalus

        restBonusMalusMockMvc.perform(post("/api/bonusMaluss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bonusMalus)))
                .andExpect(status().isCreated());

        // Validate the BonusMalus in the database
        List<BonusMalus> bonusMaluss = bonusMalusRepository.findAll();
        assertThat(bonusMaluss).hasSize(databaseSizeBeforeCreate + 1);
        BonusMalus testBonusMalus = bonusMaluss.get(bonusMaluss.size() - 1);
        assertThat(testBonusMalus.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testBonusMalus.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testBonusMalus.getEffect()).isEqualTo(DEFAULT_EFFECT);
        assertThat(testBonusMalus.getRealEffect()).isEqualTo(DEFAULT_REAL_EFFECT);
        assertThat(testBonusMalus.getDuration()).isEqualTo(DEFAULT_DURATION);
        assertThat(testBonusMalus.getAttack()).isEqualTo(DEFAULT_ATTACK);
        assertThat(testBonusMalus.getAttackM()).isEqualTo(DEFAULT_ATTACK_M);
        assertThat(testBonusMalus.getDodge()).isEqualTo(DEFAULT_DODGE);
        assertThat(testBonusMalus.getDodgeM()).isEqualTo(DEFAULT_DODGE_M);
        assertThat(testBonusMalus.getDamage()).isEqualTo(DEFAULT_DAMAGE);
        assertThat(testBonusMalus.getDamageM()).isEqualTo(DEFAULT_DAMAGE_M);
        assertThat(testBonusMalus.getRegeneration()).isEqualTo(DEFAULT_REGENERATION);
        assertThat(testBonusMalus.getHitPoint()).isEqualTo(DEFAULT_HIT_POINT);
        assertThat(testBonusMalus.getView()).isEqualTo(DEFAULT_VIEW);
        assertThat(testBonusMalus.getRm()).isEqualTo(DEFAULT_RM);
        assertThat(testBonusMalus.getMm()).isEqualTo(DEFAULT_MM);
        assertThat(testBonusMalus.getArmor()).isEqualTo(DEFAULT_ARMOR);
        assertThat(testBonusMalus.getArmorM()).isEqualTo(DEFAULT_ARMOR_M);
        assertThat(testBonusMalus.getTurn()).isEqualTo(DEFAULT_TURN);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = bonusMalusRepository.findAll().size();
        // set the field null
        bonusMalus.setName(null);

        // Create the BonusMalus, which fails.

        restBonusMalusMockMvc.perform(post("/api/bonusMaluss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bonusMalus)))
                .andExpect(status().isBadRequest());

        List<BonusMalus> bonusMaluss = bonusMalusRepository.findAll();
        assertThat(bonusMaluss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = bonusMalusRepository.findAll().size();
        // set the field null
        bonusMalus.setType(null);

        // Create the BonusMalus, which fails.

        restBonusMalusMockMvc.perform(post("/api/bonusMaluss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bonusMalus)))
                .andExpect(status().isBadRequest());

        List<BonusMalus> bonusMaluss = bonusMalusRepository.findAll();
        assertThat(bonusMaluss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEffectIsRequired() throws Exception {
        int databaseSizeBeforeTest = bonusMalusRepository.findAll().size();
        // set the field null
        bonusMalus.setEffect(null);

        // Create the BonusMalus, which fails.

        restBonusMalusMockMvc.perform(post("/api/bonusMaluss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bonusMalus)))
                .andExpect(status().isBadRequest());

        List<BonusMalus> bonusMaluss = bonusMalusRepository.findAll();
        assertThat(bonusMaluss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRealEffectIsRequired() throws Exception {
        int databaseSizeBeforeTest = bonusMalusRepository.findAll().size();
        // set the field null
        bonusMalus.setRealEffect(null);

        // Create the BonusMalus, which fails.

        restBonusMalusMockMvc.perform(post("/api/bonusMaluss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bonusMalus)))
                .andExpect(status().isBadRequest());

        List<BonusMalus> bonusMaluss = bonusMalusRepository.findAll();
        assertThat(bonusMaluss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDurationIsRequired() throws Exception {
        int databaseSizeBeforeTest = bonusMalusRepository.findAll().size();
        // set the field null
        bonusMalus.setDuration(null);

        // Create the BonusMalus, which fails.

        restBonusMalusMockMvc.perform(post("/api/bonusMaluss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bonusMalus)))
                .andExpect(status().isBadRequest());

        List<BonusMalus> bonusMaluss = bonusMalusRepository.findAll();
        assertThat(bonusMaluss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAttackIsRequired() throws Exception {
        int databaseSizeBeforeTest = bonusMalusRepository.findAll().size();
        // set the field null
        bonusMalus.setAttack(null);

        // Create the BonusMalus, which fails.

        restBonusMalusMockMvc.perform(post("/api/bonusMaluss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bonusMalus)))
                .andExpect(status().isBadRequest());

        List<BonusMalus> bonusMaluss = bonusMalusRepository.findAll();
        assertThat(bonusMaluss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAttackMIsRequired() throws Exception {
        int databaseSizeBeforeTest = bonusMalusRepository.findAll().size();
        // set the field null
        bonusMalus.setAttackM(null);

        // Create the BonusMalus, which fails.

        restBonusMalusMockMvc.perform(post("/api/bonusMaluss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bonusMalus)))
                .andExpect(status().isBadRequest());

        List<BonusMalus> bonusMaluss = bonusMalusRepository.findAll();
        assertThat(bonusMaluss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDodgeIsRequired() throws Exception {
        int databaseSizeBeforeTest = bonusMalusRepository.findAll().size();
        // set the field null
        bonusMalus.setDodge(null);

        // Create the BonusMalus, which fails.

        restBonusMalusMockMvc.perform(post("/api/bonusMaluss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bonusMalus)))
                .andExpect(status().isBadRequest());

        List<BonusMalus> bonusMaluss = bonusMalusRepository.findAll();
        assertThat(bonusMaluss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDodgeMIsRequired() throws Exception {
        int databaseSizeBeforeTest = bonusMalusRepository.findAll().size();
        // set the field null
        bonusMalus.setDodgeM(null);

        // Create the BonusMalus, which fails.

        restBonusMalusMockMvc.perform(post("/api/bonusMaluss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bonusMalus)))
                .andExpect(status().isBadRequest());

        List<BonusMalus> bonusMaluss = bonusMalusRepository.findAll();
        assertThat(bonusMaluss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDamageIsRequired() throws Exception {
        int databaseSizeBeforeTest = bonusMalusRepository.findAll().size();
        // set the field null
        bonusMalus.setDamage(null);

        // Create the BonusMalus, which fails.

        restBonusMalusMockMvc.perform(post("/api/bonusMaluss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bonusMalus)))
                .andExpect(status().isBadRequest());

        List<BonusMalus> bonusMaluss = bonusMalusRepository.findAll();
        assertThat(bonusMaluss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDamageMIsRequired() throws Exception {
        int databaseSizeBeforeTest = bonusMalusRepository.findAll().size();
        // set the field null
        bonusMalus.setDamageM(null);

        // Create the BonusMalus, which fails.

        restBonusMalusMockMvc.perform(post("/api/bonusMaluss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bonusMalus)))
                .andExpect(status().isBadRequest());

        List<BonusMalus> bonusMaluss = bonusMalusRepository.findAll();
        assertThat(bonusMaluss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRegenerationIsRequired() throws Exception {
        int databaseSizeBeforeTest = bonusMalusRepository.findAll().size();
        // set the field null
        bonusMalus.setRegeneration(null);

        // Create the BonusMalus, which fails.

        restBonusMalusMockMvc.perform(post("/api/bonusMaluss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bonusMalus)))
                .andExpect(status().isBadRequest());

        List<BonusMalus> bonusMaluss = bonusMalusRepository.findAll();
        assertThat(bonusMaluss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkHitPointIsRequired() throws Exception {
        int databaseSizeBeforeTest = bonusMalusRepository.findAll().size();
        // set the field null
        bonusMalus.setHitPoint(null);

        // Create the BonusMalus, which fails.

        restBonusMalusMockMvc.perform(post("/api/bonusMaluss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bonusMalus)))
                .andExpect(status().isBadRequest());

        List<BonusMalus> bonusMaluss = bonusMalusRepository.findAll();
        assertThat(bonusMaluss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkViewIsRequired() throws Exception {
        int databaseSizeBeforeTest = bonusMalusRepository.findAll().size();
        // set the field null
        bonusMalus.setView(null);

        // Create the BonusMalus, which fails.

        restBonusMalusMockMvc.perform(post("/api/bonusMaluss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bonusMalus)))
                .andExpect(status().isBadRequest());

        List<BonusMalus> bonusMaluss = bonusMalusRepository.findAll();
        assertThat(bonusMaluss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRmIsRequired() throws Exception {
        int databaseSizeBeforeTest = bonusMalusRepository.findAll().size();
        // set the field null
        bonusMalus.setRm(null);

        // Create the BonusMalus, which fails.

        restBonusMalusMockMvc.perform(post("/api/bonusMaluss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bonusMalus)))
                .andExpect(status().isBadRequest());

        List<BonusMalus> bonusMaluss = bonusMalusRepository.findAll();
        assertThat(bonusMaluss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMmIsRequired() throws Exception {
        int databaseSizeBeforeTest = bonusMalusRepository.findAll().size();
        // set the field null
        bonusMalus.setMm(null);

        // Create the BonusMalus, which fails.

        restBonusMalusMockMvc.perform(post("/api/bonusMaluss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bonusMalus)))
                .andExpect(status().isBadRequest());

        List<BonusMalus> bonusMaluss = bonusMalusRepository.findAll();
        assertThat(bonusMaluss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkArmorIsRequired() throws Exception {
        int databaseSizeBeforeTest = bonusMalusRepository.findAll().size();
        // set the field null
        bonusMalus.setArmor(null);

        // Create the BonusMalus, which fails.

        restBonusMalusMockMvc.perform(post("/api/bonusMaluss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bonusMalus)))
                .andExpect(status().isBadRequest());

        List<BonusMalus> bonusMaluss = bonusMalusRepository.findAll();
        assertThat(bonusMaluss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkArmorMIsRequired() throws Exception {
        int databaseSizeBeforeTest = bonusMalusRepository.findAll().size();
        // set the field null
        bonusMalus.setArmorM(null);

        // Create the BonusMalus, which fails.

        restBonusMalusMockMvc.perform(post("/api/bonusMaluss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bonusMalus)))
                .andExpect(status().isBadRequest());

        List<BonusMalus> bonusMaluss = bonusMalusRepository.findAll();
        assertThat(bonusMaluss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTurnIsRequired() throws Exception {
        int databaseSizeBeforeTest = bonusMalusRepository.findAll().size();
        // set the field null
        bonusMalus.setTurn(null);

        // Create the BonusMalus, which fails.

        restBonusMalusMockMvc.perform(post("/api/bonusMaluss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bonusMalus)))
                .andExpect(status().isBadRequest());

        List<BonusMalus> bonusMaluss = bonusMalusRepository.findAll();
        assertThat(bonusMaluss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBonusMaluss() throws Exception {
        // Initialize the database
        bonusMalusRepository.saveAndFlush(bonusMalus);

        // Get all the bonusMaluss
        restBonusMalusMockMvc.perform(get("/api/bonusMaluss?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(bonusMalus.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
                .andExpect(jsonPath("$.[*].effect").value(hasItem(DEFAULT_EFFECT.toString())))
                .andExpect(jsonPath("$.[*].realEffect").value(hasItem(DEFAULT_REAL_EFFECT.toString())))
                .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION)))
                .andExpect(jsonPath("$.[*].attack").value(hasItem(DEFAULT_ATTACK)))
                .andExpect(jsonPath("$.[*].attackM").value(hasItem(DEFAULT_ATTACK_M)))
                .andExpect(jsonPath("$.[*].dodge").value(hasItem(DEFAULT_DODGE)))
                .andExpect(jsonPath("$.[*].dodgeM").value(hasItem(DEFAULT_DODGE_M)))
                .andExpect(jsonPath("$.[*].damage").value(hasItem(DEFAULT_DAMAGE)))
                .andExpect(jsonPath("$.[*].damageM").value(hasItem(DEFAULT_DAMAGE_M)))
                .andExpect(jsonPath("$.[*].regeneration").value(hasItem(DEFAULT_REGENERATION)))
                .andExpect(jsonPath("$.[*].hitPoint").value(hasItem(DEFAULT_HIT_POINT)))
                .andExpect(jsonPath("$.[*].view").value(hasItem(DEFAULT_VIEW)))
                .andExpect(jsonPath("$.[*].rm").value(hasItem(DEFAULT_RM)))
                .andExpect(jsonPath("$.[*].mm").value(hasItem(DEFAULT_MM)))
                .andExpect(jsonPath("$.[*].armor").value(hasItem(DEFAULT_ARMOR)))
                .andExpect(jsonPath("$.[*].armorM").value(hasItem(DEFAULT_ARMOR_M)))
                .andExpect(jsonPath("$.[*].turn").value(hasItem(DEFAULT_TURN)));
    }

    @Test
    @Transactional
    public void getBonusMalus() throws Exception {
        // Initialize the database
        bonusMalusRepository.saveAndFlush(bonusMalus);

        // Get the bonusMalus
        restBonusMalusMockMvc.perform(get("/api/bonusMaluss/{id}", bonusMalus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(bonusMalus.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.effect").value(DEFAULT_EFFECT.toString()))
            .andExpect(jsonPath("$.realEffect").value(DEFAULT_REAL_EFFECT.toString()))
            .andExpect(jsonPath("$.duration").value(DEFAULT_DURATION))
            .andExpect(jsonPath("$.attack").value(DEFAULT_ATTACK))
            .andExpect(jsonPath("$.attackM").value(DEFAULT_ATTACK_M))
            .andExpect(jsonPath("$.dodge").value(DEFAULT_DODGE))
            .andExpect(jsonPath("$.dodgeM").value(DEFAULT_DODGE_M))
            .andExpect(jsonPath("$.damage").value(DEFAULT_DAMAGE))
            .andExpect(jsonPath("$.damageM").value(DEFAULT_DAMAGE_M))
            .andExpect(jsonPath("$.regeneration").value(DEFAULT_REGENERATION))
            .andExpect(jsonPath("$.hitPoint").value(DEFAULT_HIT_POINT))
            .andExpect(jsonPath("$.view").value(DEFAULT_VIEW))
            .andExpect(jsonPath("$.rm").value(DEFAULT_RM))
            .andExpect(jsonPath("$.mm").value(DEFAULT_MM))
            .andExpect(jsonPath("$.armor").value(DEFAULT_ARMOR))
            .andExpect(jsonPath("$.armorM").value(DEFAULT_ARMOR_M))
            .andExpect(jsonPath("$.turn").value(DEFAULT_TURN));
    }

    @Test
    @Transactional
    public void getNonExistingBonusMalus() throws Exception {
        // Get the bonusMalus
        restBonusMalusMockMvc.perform(get("/api/bonusMaluss/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBonusMalus() throws Exception {
        // Initialize the database
        bonusMalusRepository.saveAndFlush(bonusMalus);

		int databaseSizeBeforeUpdate = bonusMalusRepository.findAll().size();

        // Update the bonusMalus
        bonusMalus.setName(UPDATED_NAME);
        bonusMalus.setType(UPDATED_TYPE);
        bonusMalus.setEffect(UPDATED_EFFECT);
        bonusMalus.setRealEffect(UPDATED_REAL_EFFECT);
        bonusMalus.setDuration(UPDATED_DURATION);
        bonusMalus.setAttack(UPDATED_ATTACK);
        bonusMalus.setAttackM(UPDATED_ATTACK_M);
        bonusMalus.setDodge(UPDATED_DODGE);
        bonusMalus.setDodgeM(UPDATED_DODGE_M);
        bonusMalus.setDamage(UPDATED_DAMAGE);
        bonusMalus.setDamageM(UPDATED_DAMAGE_M);
        bonusMalus.setRegeneration(UPDATED_REGENERATION);
        bonusMalus.setHitPoint(UPDATED_HIT_POINT);
        bonusMalus.setView(UPDATED_VIEW);
        bonusMalus.setRm(UPDATED_RM);
        bonusMalus.setMm(UPDATED_MM);
        bonusMalus.setArmor(UPDATED_ARMOR);
        bonusMalus.setArmorM(UPDATED_ARMOR_M);
        bonusMalus.setTurn(UPDATED_TURN);

        restBonusMalusMockMvc.perform(put("/api/bonusMaluss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bonusMalus)))
                .andExpect(status().isOk());

        // Validate the BonusMalus in the database
        List<BonusMalus> bonusMaluss = bonusMalusRepository.findAll();
        assertThat(bonusMaluss).hasSize(databaseSizeBeforeUpdate);
        BonusMalus testBonusMalus = bonusMaluss.get(bonusMaluss.size() - 1);
        assertThat(testBonusMalus.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBonusMalus.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testBonusMalus.getEffect()).isEqualTo(UPDATED_EFFECT);
        assertThat(testBonusMalus.getRealEffect()).isEqualTo(UPDATED_REAL_EFFECT);
        assertThat(testBonusMalus.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testBonusMalus.getAttack()).isEqualTo(UPDATED_ATTACK);
        assertThat(testBonusMalus.getAttackM()).isEqualTo(UPDATED_ATTACK_M);
        assertThat(testBonusMalus.getDodge()).isEqualTo(UPDATED_DODGE);
        assertThat(testBonusMalus.getDodgeM()).isEqualTo(UPDATED_DODGE_M);
        assertThat(testBonusMalus.getDamage()).isEqualTo(UPDATED_DAMAGE);
        assertThat(testBonusMalus.getDamageM()).isEqualTo(UPDATED_DAMAGE_M);
        assertThat(testBonusMalus.getRegeneration()).isEqualTo(UPDATED_REGENERATION);
        assertThat(testBonusMalus.getHitPoint()).isEqualTo(UPDATED_HIT_POINT);
        assertThat(testBonusMalus.getView()).isEqualTo(UPDATED_VIEW);
        assertThat(testBonusMalus.getRm()).isEqualTo(UPDATED_RM);
        assertThat(testBonusMalus.getMm()).isEqualTo(UPDATED_MM);
        assertThat(testBonusMalus.getArmor()).isEqualTo(UPDATED_ARMOR);
        assertThat(testBonusMalus.getArmorM()).isEqualTo(UPDATED_ARMOR_M);
        assertThat(testBonusMalus.getTurn()).isEqualTo(UPDATED_TURN);
    }

    @Test
    @Transactional
    public void deleteBonusMalus() throws Exception {
        // Initialize the database
        bonusMalusRepository.saveAndFlush(bonusMalus);

		int databaseSizeBeforeDelete = bonusMalusRepository.findAll().size();

        // Get the bonusMalus
        restBonusMalusMockMvc.perform(delete("/api/bonusMaluss/{id}", bonusMalus.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<BonusMalus> bonusMaluss = bonusMalusRepository.findAll();
        assertThat(bonusMaluss).hasSize(databaseSizeBeforeDelete - 1);
    }
}
