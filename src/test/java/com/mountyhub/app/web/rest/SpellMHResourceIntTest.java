package com.mountyhub.app.web.rest;

import com.mountyhub.app.Application;
import com.mountyhub.app.domain.SpellMH;
import com.mountyhub.app.repository.SpellMHRepository;

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
 * Test class for the SpellMHResource REST controller.
 *
 * @see SpellMHResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class SpellMHResourceIntTest {


    private static final Long DEFAULT_NUMBER = 1L;
    private static final Long UPDATED_NUMBER = 2L;
    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final Integer DEFAULT_PA = 1;
    private static final Integer UPDATED_PA = 2;

    @Inject
    private SpellMHRepository spellMHRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restSpellMHMockMvc;

    private SpellMH spellMH;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SpellMHResource spellMHResource = new SpellMHResource();
        ReflectionTestUtils.setField(spellMHResource, "spellMHRepository", spellMHRepository);
        this.restSpellMHMockMvc = MockMvcBuilders.standaloneSetup(spellMHResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        spellMH = new SpellMH();
        spellMH.setNumber(DEFAULT_NUMBER);
        spellMH.setName(DEFAULT_NAME);
        spellMH.setPa(DEFAULT_PA);
    }

    @Test
    @Transactional
    public void createSpellMH() throws Exception {
        int databaseSizeBeforeCreate = spellMHRepository.findAll().size();

        // Create the SpellMH

        restSpellMHMockMvc.perform(post("/api/spellMHs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(spellMH)))
                .andExpect(status().isCreated());

        // Validate the SpellMH in the database
        List<SpellMH> spellMHs = spellMHRepository.findAll();
        assertThat(spellMHs).hasSize(databaseSizeBeforeCreate + 1);
        SpellMH testSpellMH = spellMHs.get(spellMHs.size() - 1);
        assertThat(testSpellMH.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testSpellMH.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSpellMH.getPa()).isEqualTo(DEFAULT_PA);
    }

    @Test
    @Transactional
    public void checkNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = spellMHRepository.findAll().size();
        // set the field null
        spellMH.setNumber(null);

        // Create the SpellMH, which fails.

        restSpellMHMockMvc.perform(post("/api/spellMHs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(spellMH)))
                .andExpect(status().isBadRequest());

        List<SpellMH> spellMHs = spellMHRepository.findAll();
        assertThat(spellMHs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = spellMHRepository.findAll().size();
        // set the field null
        spellMH.setName(null);

        // Create the SpellMH, which fails.

        restSpellMHMockMvc.perform(post("/api/spellMHs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(spellMH)))
                .andExpect(status().isBadRequest());

        List<SpellMH> spellMHs = spellMHRepository.findAll();
        assertThat(spellMHs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPaIsRequired() throws Exception {
        int databaseSizeBeforeTest = spellMHRepository.findAll().size();
        // set the field null
        spellMH.setPa(null);

        // Create the SpellMH, which fails.

        restSpellMHMockMvc.perform(post("/api/spellMHs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(spellMH)))
                .andExpect(status().isBadRequest());

        List<SpellMH> spellMHs = spellMHRepository.findAll();
        assertThat(spellMHs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSpellMHs() throws Exception {
        // Initialize the database
        spellMHRepository.saveAndFlush(spellMH);

        // Get all the spellMHs
        restSpellMHMockMvc.perform(get("/api/spellMHs?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(spellMH.getId().intValue())))
                .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER.intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].pa").value(hasItem(DEFAULT_PA)));
    }

    @Test
    @Transactional
    public void getSpellMH() throws Exception {
        // Initialize the database
        spellMHRepository.saveAndFlush(spellMH);

        // Get the spellMH
        restSpellMHMockMvc.perform(get("/api/spellMHs/{id}", spellMH.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(spellMH.getId().intValue()))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER.intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.pa").value(DEFAULT_PA));
    }

    @Test
    @Transactional
    public void getNonExistingSpellMH() throws Exception {
        // Get the spellMH
        restSpellMHMockMvc.perform(get("/api/spellMHs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSpellMH() throws Exception {
        // Initialize the database
        spellMHRepository.saveAndFlush(spellMH);

		int databaseSizeBeforeUpdate = spellMHRepository.findAll().size();

        // Update the spellMH
        spellMH.setNumber(UPDATED_NUMBER);
        spellMH.setName(UPDATED_NAME);
        spellMH.setPa(UPDATED_PA);

        restSpellMHMockMvc.perform(put("/api/spellMHs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(spellMH)))
                .andExpect(status().isOk());

        // Validate the SpellMH in the database
        List<SpellMH> spellMHs = spellMHRepository.findAll();
        assertThat(spellMHs).hasSize(databaseSizeBeforeUpdate);
        SpellMH testSpellMH = spellMHs.get(spellMHs.size() - 1);
        assertThat(testSpellMH.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testSpellMH.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSpellMH.getPa()).isEqualTo(UPDATED_PA);
    }

    @Test
    @Transactional
    public void deleteSpellMH() throws Exception {
        // Initialize the database
        spellMHRepository.saveAndFlush(spellMH);

		int databaseSizeBeforeDelete = spellMHRepository.findAll().size();

        // Get the spellMH
        restSpellMHMockMvc.perform(delete("/api/spellMHs/{id}", spellMH.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<SpellMH> spellMHs = spellMHRepository.findAll();
        assertThat(spellMHs).hasSize(databaseSizeBeforeDelete - 1);
    }
}
