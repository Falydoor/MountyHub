package com.mountyhub.app.web.rest;

import com.mountyhub.app.Application;
import com.mountyhub.app.domain.BonusMalusType;
import com.mountyhub.app.repository.BonusMalusTypeRepository;

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

import com.mountyhub.app.domain.enumeration.EffectType;

/**
 * Test class for the BonusMalusTypeResource REST controller.
 *
 * @see BonusMalusTypeResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class BonusMalusTypeResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    
    private static final EffectType DEFAULT_TYPE = EffectType.PHYSIQUE;
    private static final EffectType UPDATED_TYPE = EffectType.MAGIQUE;

    @Inject
    private BonusMalusTypeRepository bonusMalusTypeRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restBonusMalusTypeMockMvc;

    private BonusMalusType bonusMalusType;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BonusMalusTypeResource bonusMalusTypeResource = new BonusMalusTypeResource();
        ReflectionTestUtils.setField(bonusMalusTypeResource, "bonusMalusTypeRepository", bonusMalusTypeRepository);
        this.restBonusMalusTypeMockMvc = MockMvcBuilders.standaloneSetup(bonusMalusTypeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        bonusMalusType = new BonusMalusType();
        bonusMalusType.setName(DEFAULT_NAME);
        bonusMalusType.setType(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    public void createBonusMalusType() throws Exception {
        int databaseSizeBeforeCreate = bonusMalusTypeRepository.findAll().size();

        // Create the BonusMalusType

        restBonusMalusTypeMockMvc.perform(post("/api/bonusMalusTypes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bonusMalusType)))
                .andExpect(status().isCreated());

        // Validate the BonusMalusType in the database
        List<BonusMalusType> bonusMalusTypes = bonusMalusTypeRepository.findAll();
        assertThat(bonusMalusTypes).hasSize(databaseSizeBeforeCreate + 1);
        BonusMalusType testBonusMalusType = bonusMalusTypes.get(bonusMalusTypes.size() - 1);
        assertThat(testBonusMalusType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testBonusMalusType.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = bonusMalusTypeRepository.findAll().size();
        // set the field null
        bonusMalusType.setName(null);

        // Create the BonusMalusType, which fails.

        restBonusMalusTypeMockMvc.perform(post("/api/bonusMalusTypes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bonusMalusType)))
                .andExpect(status().isBadRequest());

        List<BonusMalusType> bonusMalusTypes = bonusMalusTypeRepository.findAll();
        assertThat(bonusMalusTypes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = bonusMalusTypeRepository.findAll().size();
        // set the field null
        bonusMalusType.setType(null);

        // Create the BonusMalusType, which fails.

        restBonusMalusTypeMockMvc.perform(post("/api/bonusMalusTypes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bonusMalusType)))
                .andExpect(status().isBadRequest());

        List<BonusMalusType> bonusMalusTypes = bonusMalusTypeRepository.findAll();
        assertThat(bonusMalusTypes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBonusMalusTypes() throws Exception {
        // Initialize the database
        bonusMalusTypeRepository.saveAndFlush(bonusMalusType);

        // Get all the bonusMalusTypes
        restBonusMalusTypeMockMvc.perform(get("/api/bonusMalusTypes?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(bonusMalusType.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    @Test
    @Transactional
    public void getBonusMalusType() throws Exception {
        // Initialize the database
        bonusMalusTypeRepository.saveAndFlush(bonusMalusType);

        // Get the bonusMalusType
        restBonusMalusTypeMockMvc.perform(get("/api/bonusMalusTypes/{id}", bonusMalusType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(bonusMalusType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBonusMalusType() throws Exception {
        // Get the bonusMalusType
        restBonusMalusTypeMockMvc.perform(get("/api/bonusMalusTypes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBonusMalusType() throws Exception {
        // Initialize the database
        bonusMalusTypeRepository.saveAndFlush(bonusMalusType);

		int databaseSizeBeforeUpdate = bonusMalusTypeRepository.findAll().size();

        // Update the bonusMalusType
        bonusMalusType.setName(UPDATED_NAME);
        bonusMalusType.setType(UPDATED_TYPE);

        restBonusMalusTypeMockMvc.perform(put("/api/bonusMalusTypes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bonusMalusType)))
                .andExpect(status().isOk());

        // Validate the BonusMalusType in the database
        List<BonusMalusType> bonusMalusTypes = bonusMalusTypeRepository.findAll();
        assertThat(bonusMalusTypes).hasSize(databaseSizeBeforeUpdate);
        BonusMalusType testBonusMalusType = bonusMalusTypes.get(bonusMalusTypes.size() - 1);
        assertThat(testBonusMalusType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBonusMalusType.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void deleteBonusMalusType() throws Exception {
        // Initialize the database
        bonusMalusTypeRepository.saveAndFlush(bonusMalusType);

		int databaseSizeBeforeDelete = bonusMalusTypeRepository.findAll().size();

        // Get the bonusMalusType
        restBonusMalusTypeMockMvc.perform(delete("/api/bonusMalusTypes/{id}", bonusMalusType.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<BonusMalusType> bonusMalusTypes = bonusMalusTypeRepository.findAll();
        assertThat(bonusMalusTypes).hasSize(databaseSizeBeforeDelete - 1);
    }
}
