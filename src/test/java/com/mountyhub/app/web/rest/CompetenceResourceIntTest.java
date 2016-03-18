package com.mountyhub.app.web.rest;

import com.mountyhub.app.Application;
import com.mountyhub.app.domain.Competence;
import com.mountyhub.app.repository.CompetenceRepository;

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
 * Test class for the CompetenceResource REST controller.
 *
 * @see CompetenceResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class CompetenceResourceIntTest {


    private static final Integer DEFAULT_PERCENT = 1;
    private static final Integer UPDATED_PERCENT = 2;

    private static final Integer DEFAULT_PERCENT_BONUS = 1;
    private static final Integer UPDATED_PERCENT_BONUS = 2;

    private static final Integer DEFAULT_LEVEL = 1;
    private static final Integer UPDATED_LEVEL = 2;

    @Inject
    private CompetenceRepository competenceRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCompetenceMockMvc;

    private Competence competence;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CompetenceResource competenceResource = new CompetenceResource();
        ReflectionTestUtils.setField(competenceResource, "competenceRepository", competenceRepository);
        this.restCompetenceMockMvc = MockMvcBuilders.standaloneSetup(competenceResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        competence = new Competence();
        competence.setPercent(DEFAULT_PERCENT);
        competence.setPercentBonus(DEFAULT_PERCENT_BONUS);
        competence.setLevel(DEFAULT_LEVEL);
    }

    @Test
    @Transactional
    public void createCompetence() throws Exception {
        int databaseSizeBeforeCreate = competenceRepository.findAll().size();

        // Create the Competence

        restCompetenceMockMvc.perform(post("/api/competences")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(competence)))
                .andExpect(status().isCreated());

        // Validate the Competence in the database
        List<Competence> competences = competenceRepository.findAll();
        assertThat(competences).hasSize(databaseSizeBeforeCreate + 1);
        Competence testCompetence = competences.get(competences.size() - 1);
        assertThat(testCompetence.getPercent()).isEqualTo(DEFAULT_PERCENT);
        assertThat(testCompetence.getPercentBonus()).isEqualTo(DEFAULT_PERCENT_BONUS);
        assertThat(testCompetence.getLevel()).isEqualTo(DEFAULT_LEVEL);
    }

    @Test
    @Transactional
    public void checkPercentIsRequired() throws Exception {
        int databaseSizeBeforeTest = competenceRepository.findAll().size();
        // set the field null
        competence.setPercent(null);

        // Create the Competence, which fails.

        restCompetenceMockMvc.perform(post("/api/competences")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(competence)))
                .andExpect(status().isBadRequest());

        List<Competence> competences = competenceRepository.findAll();
        assertThat(competences).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPercentBonusIsRequired() throws Exception {
        int databaseSizeBeforeTest = competenceRepository.findAll().size();
        // set the field null
        competence.setPercentBonus(null);

        // Create the Competence, which fails.

        restCompetenceMockMvc.perform(post("/api/competences")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(competence)))
                .andExpect(status().isBadRequest());

        List<Competence> competences = competenceRepository.findAll();
        assertThat(competences).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLevelIsRequired() throws Exception {
        int databaseSizeBeforeTest = competenceRepository.findAll().size();
        // set the field null
        competence.setLevel(null);

        // Create the Competence, which fails.

        restCompetenceMockMvc.perform(post("/api/competences")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(competence)))
                .andExpect(status().isBadRequest());

        List<Competence> competences = competenceRepository.findAll();
        assertThat(competences).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCompetences() throws Exception {
        // Initialize the database
        competenceRepository.saveAndFlush(competence);

        // Get all the competences
        restCompetenceMockMvc.perform(get("/api/competences?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(competence.getId().intValue())))
                .andExpect(jsonPath("$.[*].percent").value(hasItem(DEFAULT_PERCENT)))
                .andExpect(jsonPath("$.[*].percentBonus").value(hasItem(DEFAULT_PERCENT_BONUS)))
                .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL)));
    }

    @Test
    @Transactional
    public void getCompetence() throws Exception {
        // Initialize the database
        competenceRepository.saveAndFlush(competence);

        // Get the competence
        restCompetenceMockMvc.perform(get("/api/competences/{id}", competence.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(competence.getId().intValue()))
            .andExpect(jsonPath("$.percent").value(DEFAULT_PERCENT))
            .andExpect(jsonPath("$.percentBonus").value(DEFAULT_PERCENT_BONUS))
            .andExpect(jsonPath("$.level").value(DEFAULT_LEVEL));
    }

    @Test
    @Transactional
    public void getNonExistingCompetence() throws Exception {
        // Get the competence
        restCompetenceMockMvc.perform(get("/api/competences/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCompetence() throws Exception {
        // Initialize the database
        competenceRepository.saveAndFlush(competence);

		int databaseSizeBeforeUpdate = competenceRepository.findAll().size();

        // Update the competence
        competence.setPercent(UPDATED_PERCENT);
        competence.setPercentBonus(UPDATED_PERCENT_BONUS);
        competence.setLevel(UPDATED_LEVEL);

        restCompetenceMockMvc.perform(put("/api/competences")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(competence)))
                .andExpect(status().isOk());

        // Validate the Competence in the database
        List<Competence> competences = competenceRepository.findAll();
        assertThat(competences).hasSize(databaseSizeBeforeUpdate);
        Competence testCompetence = competences.get(competences.size() - 1);
        assertThat(testCompetence.getPercent()).isEqualTo(UPDATED_PERCENT);
        assertThat(testCompetence.getPercentBonus()).isEqualTo(UPDATED_PERCENT_BONUS);
        assertThat(testCompetence.getLevel()).isEqualTo(UPDATED_LEVEL);
    }

    @Test
    @Transactional
    public void deleteCompetence() throws Exception {
        // Initialize the database
        competenceRepository.saveAndFlush(competence);

		int databaseSizeBeforeDelete = competenceRepository.findAll().size();

        // Get the competence
        restCompetenceMockMvc.perform(delete("/api/competences/{id}", competence.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Competence> competences = competenceRepository.findAll();
        assertThat(competences).hasSize(databaseSizeBeforeDelete - 1);
    }
}
