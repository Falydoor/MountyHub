package com.mountyhub.app.web.rest;

import com.mountyhub.app.Application;
import com.mountyhub.app.domain.Gear;
import com.mountyhub.app.repository.GearRepository;

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

import com.mountyhub.app.domain.enumeration.GearType;

/**
 * Test class for the GearResource REST controller.
 *
 * @see GearResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class GearResourceIntTest {


    private static final Long DEFAULT_NUMBER = 1L;
    private static final Long UPDATED_NUMBER = 2L;

    private static final Boolean DEFAULT_WEARED = false;
    private static final Boolean UPDATED_WEARED = true;
    
    private static final GearType DEFAULT_TYPE = GearType.ANNEAU;
    private static final GearType UPDATED_TYPE = GearType.BOTTES;

    private static final Boolean DEFAULT_IDENTIFIED = false;
    private static final Boolean UPDATED_IDENTIFIED = true;
    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_TEMPLATE = "AAAAA";
    private static final String UPDATED_TEMPLATE = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final Float DEFAULT_WEIGHT = 1F;
    private static final Float UPDATED_WEIGHT = 2F;

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
    private GearRepository gearRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restGearMockMvc;

    private Gear gear;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        GearResource gearResource = new GearResource();
        ReflectionTestUtils.setField(gearResource, "gearRepository", gearRepository);
        this.restGearMockMvc = MockMvcBuilders.standaloneSetup(gearResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        gear = new Gear();
        gear.setNumber(DEFAULT_NUMBER);
        gear.setWeared(DEFAULT_WEARED);
        gear.setType(DEFAULT_TYPE);
        gear.setIdentified(DEFAULT_IDENTIFIED);
        gear.setName(DEFAULT_NAME);
        gear.setTemplate(DEFAULT_TEMPLATE);
        gear.setDescription(DEFAULT_DESCRIPTION);
        gear.setWeight(DEFAULT_WEIGHT);
        gear.setAttack(DEFAULT_ATTACK);
        gear.setAttackM(DEFAULT_ATTACK_M);
        gear.setDodge(DEFAULT_DODGE);
        gear.setDodgeM(DEFAULT_DODGE_M);
        gear.setDamage(DEFAULT_DAMAGE);
        gear.setDamageM(DEFAULT_DAMAGE_M);
        gear.setRegeneration(DEFAULT_REGENERATION);
        gear.setHitPoint(DEFAULT_HIT_POINT);
        gear.setView(DEFAULT_VIEW);
        gear.setRm(DEFAULT_RM);
        gear.setMm(DEFAULT_MM);
        gear.setArmor(DEFAULT_ARMOR);
        gear.setArmorM(DEFAULT_ARMOR_M);
        gear.setTurn(DEFAULT_TURN);
    }

    @Test
    @Transactional
    public void createGear() throws Exception {
        int databaseSizeBeforeCreate = gearRepository.findAll().size();

        // Create the Gear

        restGearMockMvc.perform(post("/api/gears")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(gear)))
                .andExpect(status().isCreated());

        // Validate the Gear in the database
        List<Gear> gears = gearRepository.findAll();
        assertThat(gears).hasSize(databaseSizeBeforeCreate + 1);
        Gear testGear = gears.get(gears.size() - 1);
        assertThat(testGear.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testGear.getWeared()).isEqualTo(DEFAULT_WEARED);
        assertThat(testGear.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testGear.getIdentified()).isEqualTo(DEFAULT_IDENTIFIED);
        assertThat(testGear.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testGear.getTemplate()).isEqualTo(DEFAULT_TEMPLATE);
        assertThat(testGear.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testGear.getWeight()).isEqualTo(DEFAULT_WEIGHT);
        assertThat(testGear.getAttack()).isEqualTo(DEFAULT_ATTACK);
        assertThat(testGear.getAttackM()).isEqualTo(DEFAULT_ATTACK_M);
        assertThat(testGear.getDodge()).isEqualTo(DEFAULT_DODGE);
        assertThat(testGear.getDodgeM()).isEqualTo(DEFAULT_DODGE_M);
        assertThat(testGear.getDamage()).isEqualTo(DEFAULT_DAMAGE);
        assertThat(testGear.getDamageM()).isEqualTo(DEFAULT_DAMAGE_M);
        assertThat(testGear.getRegeneration()).isEqualTo(DEFAULT_REGENERATION);
        assertThat(testGear.getHitPoint()).isEqualTo(DEFAULT_HIT_POINT);
        assertThat(testGear.getView()).isEqualTo(DEFAULT_VIEW);
        assertThat(testGear.getRm()).isEqualTo(DEFAULT_RM);
        assertThat(testGear.getMm()).isEqualTo(DEFAULT_MM);
        assertThat(testGear.getArmor()).isEqualTo(DEFAULT_ARMOR);
        assertThat(testGear.getArmorM()).isEqualTo(DEFAULT_ARMOR_M);
        assertThat(testGear.getTurn()).isEqualTo(DEFAULT_TURN);
    }

    @Test
    @Transactional
    public void checkNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = gearRepository.findAll().size();
        // set the field null
        gear.setNumber(null);

        // Create the Gear, which fails.

        restGearMockMvc.perform(post("/api/gears")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(gear)))
                .andExpect(status().isBadRequest());

        List<Gear> gears = gearRepository.findAll();
        assertThat(gears).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkWearedIsRequired() throws Exception {
        int databaseSizeBeforeTest = gearRepository.findAll().size();
        // set the field null
        gear.setWeared(null);

        // Create the Gear, which fails.

        restGearMockMvc.perform(post("/api/gears")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(gear)))
                .andExpect(status().isBadRequest());

        List<Gear> gears = gearRepository.findAll();
        assertThat(gears).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = gearRepository.findAll().size();
        // set the field null
        gear.setType(null);

        // Create the Gear, which fails.

        restGearMockMvc.perform(post("/api/gears")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(gear)))
                .andExpect(status().isBadRequest());

        List<Gear> gears = gearRepository.findAll();
        assertThat(gears).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIdentifiedIsRequired() throws Exception {
        int databaseSizeBeforeTest = gearRepository.findAll().size();
        // set the field null
        gear.setIdentified(null);

        // Create the Gear, which fails.

        restGearMockMvc.perform(post("/api/gears")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(gear)))
                .andExpect(status().isBadRequest());

        List<Gear> gears = gearRepository.findAll();
        assertThat(gears).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = gearRepository.findAll().size();
        // set the field null
        gear.setName(null);

        // Create the Gear, which fails.

        restGearMockMvc.perform(post("/api/gears")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(gear)))
                .andExpect(status().isBadRequest());

        List<Gear> gears = gearRepository.findAll();
        assertThat(gears).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTemplateIsRequired() throws Exception {
        int databaseSizeBeforeTest = gearRepository.findAll().size();
        // set the field null
        gear.setTemplate(null);

        // Create the Gear, which fails.

        restGearMockMvc.perform(post("/api/gears")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(gear)))
                .andExpect(status().isBadRequest());

        List<Gear> gears = gearRepository.findAll();
        assertThat(gears).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = gearRepository.findAll().size();
        // set the field null
        gear.setDescription(null);

        // Create the Gear, which fails.

        restGearMockMvc.perform(post("/api/gears")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(gear)))
                .andExpect(status().isBadRequest());

        List<Gear> gears = gearRepository.findAll();
        assertThat(gears).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkWeightIsRequired() throws Exception {
        int databaseSizeBeforeTest = gearRepository.findAll().size();
        // set the field null
        gear.setWeight(null);

        // Create the Gear, which fails.

        restGearMockMvc.perform(post("/api/gears")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(gear)))
                .andExpect(status().isBadRequest());

        List<Gear> gears = gearRepository.findAll();
        assertThat(gears).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAttackIsRequired() throws Exception {
        int databaseSizeBeforeTest = gearRepository.findAll().size();
        // set the field null
        gear.setAttack(null);

        // Create the Gear, which fails.

        restGearMockMvc.perform(post("/api/gears")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(gear)))
                .andExpect(status().isBadRequest());

        List<Gear> gears = gearRepository.findAll();
        assertThat(gears).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAttackMIsRequired() throws Exception {
        int databaseSizeBeforeTest = gearRepository.findAll().size();
        // set the field null
        gear.setAttackM(null);

        // Create the Gear, which fails.

        restGearMockMvc.perform(post("/api/gears")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(gear)))
                .andExpect(status().isBadRequest());

        List<Gear> gears = gearRepository.findAll();
        assertThat(gears).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDodgeIsRequired() throws Exception {
        int databaseSizeBeforeTest = gearRepository.findAll().size();
        // set the field null
        gear.setDodge(null);

        // Create the Gear, which fails.

        restGearMockMvc.perform(post("/api/gears")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(gear)))
                .andExpect(status().isBadRequest());

        List<Gear> gears = gearRepository.findAll();
        assertThat(gears).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDodgeMIsRequired() throws Exception {
        int databaseSizeBeforeTest = gearRepository.findAll().size();
        // set the field null
        gear.setDodgeM(null);

        // Create the Gear, which fails.

        restGearMockMvc.perform(post("/api/gears")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(gear)))
                .andExpect(status().isBadRequest());

        List<Gear> gears = gearRepository.findAll();
        assertThat(gears).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDamageIsRequired() throws Exception {
        int databaseSizeBeforeTest = gearRepository.findAll().size();
        // set the field null
        gear.setDamage(null);

        // Create the Gear, which fails.

        restGearMockMvc.perform(post("/api/gears")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(gear)))
                .andExpect(status().isBadRequest());

        List<Gear> gears = gearRepository.findAll();
        assertThat(gears).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDamageMIsRequired() throws Exception {
        int databaseSizeBeforeTest = gearRepository.findAll().size();
        // set the field null
        gear.setDamageM(null);

        // Create the Gear, which fails.

        restGearMockMvc.perform(post("/api/gears")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(gear)))
                .andExpect(status().isBadRequest());

        List<Gear> gears = gearRepository.findAll();
        assertThat(gears).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRegenerationIsRequired() throws Exception {
        int databaseSizeBeforeTest = gearRepository.findAll().size();
        // set the field null
        gear.setRegeneration(null);

        // Create the Gear, which fails.

        restGearMockMvc.perform(post("/api/gears")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(gear)))
                .andExpect(status().isBadRequest());

        List<Gear> gears = gearRepository.findAll();
        assertThat(gears).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkHitPointIsRequired() throws Exception {
        int databaseSizeBeforeTest = gearRepository.findAll().size();
        // set the field null
        gear.setHitPoint(null);

        // Create the Gear, which fails.

        restGearMockMvc.perform(post("/api/gears")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(gear)))
                .andExpect(status().isBadRequest());

        List<Gear> gears = gearRepository.findAll();
        assertThat(gears).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkViewIsRequired() throws Exception {
        int databaseSizeBeforeTest = gearRepository.findAll().size();
        // set the field null
        gear.setView(null);

        // Create the Gear, which fails.

        restGearMockMvc.perform(post("/api/gears")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(gear)))
                .andExpect(status().isBadRequest());

        List<Gear> gears = gearRepository.findAll();
        assertThat(gears).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRmIsRequired() throws Exception {
        int databaseSizeBeforeTest = gearRepository.findAll().size();
        // set the field null
        gear.setRm(null);

        // Create the Gear, which fails.

        restGearMockMvc.perform(post("/api/gears")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(gear)))
                .andExpect(status().isBadRequest());

        List<Gear> gears = gearRepository.findAll();
        assertThat(gears).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMmIsRequired() throws Exception {
        int databaseSizeBeforeTest = gearRepository.findAll().size();
        // set the field null
        gear.setMm(null);

        // Create the Gear, which fails.

        restGearMockMvc.perform(post("/api/gears")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(gear)))
                .andExpect(status().isBadRequest());

        List<Gear> gears = gearRepository.findAll();
        assertThat(gears).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkArmorIsRequired() throws Exception {
        int databaseSizeBeforeTest = gearRepository.findAll().size();
        // set the field null
        gear.setArmor(null);

        // Create the Gear, which fails.

        restGearMockMvc.perform(post("/api/gears")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(gear)))
                .andExpect(status().isBadRequest());

        List<Gear> gears = gearRepository.findAll();
        assertThat(gears).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkArmorMIsRequired() throws Exception {
        int databaseSizeBeforeTest = gearRepository.findAll().size();
        // set the field null
        gear.setArmorM(null);

        // Create the Gear, which fails.

        restGearMockMvc.perform(post("/api/gears")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(gear)))
                .andExpect(status().isBadRequest());

        List<Gear> gears = gearRepository.findAll();
        assertThat(gears).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTurnIsRequired() throws Exception {
        int databaseSizeBeforeTest = gearRepository.findAll().size();
        // set the field null
        gear.setTurn(null);

        // Create the Gear, which fails.

        restGearMockMvc.perform(post("/api/gears")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(gear)))
                .andExpect(status().isBadRequest());

        List<Gear> gears = gearRepository.findAll();
        assertThat(gears).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllGears() throws Exception {
        // Initialize the database
        gearRepository.saveAndFlush(gear);

        // Get all the gears
        restGearMockMvc.perform(get("/api/gears?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(gear.getId().intValue())))
                .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER.intValue())))
                .andExpect(jsonPath("$.[*].weared").value(hasItem(DEFAULT_WEARED.booleanValue())))
                .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
                .andExpect(jsonPath("$.[*].identified").value(hasItem(DEFAULT_IDENTIFIED.booleanValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].template").value(hasItem(DEFAULT_TEMPLATE.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].weight").value(hasItem(DEFAULT_WEIGHT.doubleValue())))
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
    public void getGear() throws Exception {
        // Initialize the database
        gearRepository.saveAndFlush(gear);

        // Get the gear
        restGearMockMvc.perform(get("/api/gears/{id}", gear.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(gear.getId().intValue()))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER.intValue()))
            .andExpect(jsonPath("$.weared").value(DEFAULT_WEARED.booleanValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.identified").value(DEFAULT_IDENTIFIED.booleanValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.template").value(DEFAULT_TEMPLATE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.weight").value(DEFAULT_WEIGHT.doubleValue()))
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
    public void getNonExistingGear() throws Exception {
        // Get the gear
        restGearMockMvc.perform(get("/api/gears/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGear() throws Exception {
        // Initialize the database
        gearRepository.saveAndFlush(gear);

		int databaseSizeBeforeUpdate = gearRepository.findAll().size();

        // Update the gear
        gear.setNumber(UPDATED_NUMBER);
        gear.setWeared(UPDATED_WEARED);
        gear.setType(UPDATED_TYPE);
        gear.setIdentified(UPDATED_IDENTIFIED);
        gear.setName(UPDATED_NAME);
        gear.setTemplate(UPDATED_TEMPLATE);
        gear.setDescription(UPDATED_DESCRIPTION);
        gear.setWeight(UPDATED_WEIGHT);
        gear.setAttack(UPDATED_ATTACK);
        gear.setAttackM(UPDATED_ATTACK_M);
        gear.setDodge(UPDATED_DODGE);
        gear.setDodgeM(UPDATED_DODGE_M);
        gear.setDamage(UPDATED_DAMAGE);
        gear.setDamageM(UPDATED_DAMAGE_M);
        gear.setRegeneration(UPDATED_REGENERATION);
        gear.setHitPoint(UPDATED_HIT_POINT);
        gear.setView(UPDATED_VIEW);
        gear.setRm(UPDATED_RM);
        gear.setMm(UPDATED_MM);
        gear.setArmor(UPDATED_ARMOR);
        gear.setArmorM(UPDATED_ARMOR_M);
        gear.setTurn(UPDATED_TURN);

        restGearMockMvc.perform(put("/api/gears")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(gear)))
                .andExpect(status().isOk());

        // Validate the Gear in the database
        List<Gear> gears = gearRepository.findAll();
        assertThat(gears).hasSize(databaseSizeBeforeUpdate);
        Gear testGear = gears.get(gears.size() - 1);
        assertThat(testGear.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testGear.getWeared()).isEqualTo(UPDATED_WEARED);
        assertThat(testGear.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testGear.getIdentified()).isEqualTo(UPDATED_IDENTIFIED);
        assertThat(testGear.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testGear.getTemplate()).isEqualTo(UPDATED_TEMPLATE);
        assertThat(testGear.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testGear.getWeight()).isEqualTo(UPDATED_WEIGHT);
        assertThat(testGear.getAttack()).isEqualTo(UPDATED_ATTACK);
        assertThat(testGear.getAttackM()).isEqualTo(UPDATED_ATTACK_M);
        assertThat(testGear.getDodge()).isEqualTo(UPDATED_DODGE);
        assertThat(testGear.getDodgeM()).isEqualTo(UPDATED_DODGE_M);
        assertThat(testGear.getDamage()).isEqualTo(UPDATED_DAMAGE);
        assertThat(testGear.getDamageM()).isEqualTo(UPDATED_DAMAGE_M);
        assertThat(testGear.getRegeneration()).isEqualTo(UPDATED_REGENERATION);
        assertThat(testGear.getHitPoint()).isEqualTo(UPDATED_HIT_POINT);
        assertThat(testGear.getView()).isEqualTo(UPDATED_VIEW);
        assertThat(testGear.getRm()).isEqualTo(UPDATED_RM);
        assertThat(testGear.getMm()).isEqualTo(UPDATED_MM);
        assertThat(testGear.getArmor()).isEqualTo(UPDATED_ARMOR);
        assertThat(testGear.getArmorM()).isEqualTo(UPDATED_ARMOR_M);
        assertThat(testGear.getTurn()).isEqualTo(UPDATED_TURN);
    }

    @Test
    @Transactional
    public void deleteGear() throws Exception {
        // Initialize the database
        gearRepository.saveAndFlush(gear);

		int databaseSizeBeforeDelete = gearRepository.findAll().size();

        // Get the gear
        restGearMockMvc.perform(delete("/api/gears/{id}", gear.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Gear> gears = gearRepository.findAll();
        assertThat(gears).hasSize(databaseSizeBeforeDelete - 1);
    }
}
