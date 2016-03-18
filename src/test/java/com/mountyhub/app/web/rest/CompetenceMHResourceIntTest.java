package com.mountyhub.app.web.rest;

import com.mountyhub.app.Application;
import com.mountyhub.app.domain.CompetenceMH;
import com.mountyhub.app.repository.CompetenceMHRepository;

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
 * Test class for the CompetenceMHResource REST controller.
 *
 * @see CompetenceMHResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class CompetenceMHResourceIntTest {


    private static final Long DEFAULT_NUMBER = 1L;
    private static final Long UPDATED_NUMBER = 2L;
    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final Integer DEFAULT_PA = 1;
    private static final Integer UPDATED_PA = 2;

    @Inject
    private CompetenceMHRepository competenceMHRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCompetenceMHMockMvc;

    private CompetenceMH competenceMH;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CompetenceMHResource competenceMHResource = new CompetenceMHResource();
        ReflectionTestUtils.setField(competenceMHResource, "competenceMHRepository", competenceMHRepository);
        this.restCompetenceMHMockMvc = MockMvcBuilders.standaloneSetup(competenceMHResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        competenceMH = new CompetenceMH();
        competenceMH.setNumber(DEFAULT_NUMBER);
        competenceMH.setName(DEFAULT_NAME);
        competenceMH.setPa(DEFAULT_PA);
    }

    @Test
    @Transactional
    public void createCompetenceMH() throws Exception {
        int databaseSizeBeforeCreate = competenceMHRepository.findAll().size();

        // Create the CompetenceMH

        restCompetenceMHMockMvc.perform(post("/api/competenceMHs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(competenceMH)))
                .andExpect(status().isCreated());

        // Validate the CompetenceMH in the database
        List<CompetenceMH> competenceMHs = competenceMHRepository.findAll();
        assertThat(competenceMHs).hasSize(databaseSizeBeforeCreate + 1);
        CompetenceMH testCompetenceMH = competenceMHs.get(competenceMHs.size() - 1);
        assertThat(testCompetenceMH.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testCompetenceMH.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCompetenceMH.getPa()).isEqualTo(DEFAULT_PA);
    }

    @Test
    @Transactional
    public void checkNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = competenceMHRepository.findAll().size();
        // set the field null
        competenceMH.setNumber(null);

        // Create the CompetenceMH, which fails.

        restCompetenceMHMockMvc.perform(post("/api/competenceMHs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(competenceMH)))
                .andExpect(status().isBadRequest());

        List<CompetenceMH> competenceMHs = competenceMHRepository.findAll();
        assertThat(competenceMHs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = competenceMHRepository.findAll().size();
        // set the field null
        competenceMH.setName(null);

        // Create the CompetenceMH, which fails.

        restCompetenceMHMockMvc.perform(post("/api/competenceMHs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(competenceMH)))
                .andExpect(status().isBadRequest());

        List<CompetenceMH> competenceMHs = competenceMHRepository.findAll();
        assertThat(competenceMHs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPaIsRequired() throws Exception {
        int databaseSizeBeforeTest = competenceMHRepository.findAll().size();
        // set the field null
        competenceMH.setPa(null);

        // Create the CompetenceMH, which fails.

        restCompetenceMHMockMvc.perform(post("/api/competenceMHs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(competenceMH)))
                .andExpect(status().isBadRequest());

        List<CompetenceMH> competenceMHs = competenceMHRepository.findAll();
        assertThat(competenceMHs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCompetenceMHs() throws Exception {
        // Initialize the database
        competenceMHRepository.saveAndFlush(competenceMH);

        // Get all the competenceMHs
        restCompetenceMHMockMvc.perform(get("/api/competenceMHs?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(competenceMH.getId().intValue())))
                .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER.intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].pa").value(hasItem(DEFAULT_PA)));
    }

    @Test
    @Transactional
    public void getCompetenceMH() throws Exception {
        // Initialize the database
        competenceMHRepository.saveAndFlush(competenceMH);

        // Get the competenceMH
        restCompetenceMHMockMvc.perform(get("/api/competenceMHs/{id}", competenceMH.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(competenceMH.getId().intValue()))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER.intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.pa").value(DEFAULT_PA));
    }

    @Test
    @Transactional
    public void getNonExistingCompetenceMH() throws Exception {
        // Get the competenceMH
        restCompetenceMHMockMvc.perform(get("/api/competenceMHs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCompetenceMH() throws Exception {
        // Initialize the database
        competenceMHRepository.saveAndFlush(competenceMH);

		int databaseSizeBeforeUpdate = competenceMHRepository.findAll().size();

        // Update the competenceMH
        competenceMH.setNumber(UPDATED_NUMBER);
        competenceMH.setName(UPDATED_NAME);
        competenceMH.setPa(UPDATED_PA);

        restCompetenceMHMockMvc.perform(put("/api/competenceMHs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(competenceMH)))
                .andExpect(status().isOk());

        // Validate the CompetenceMH in the database
        List<CompetenceMH> competenceMHs = competenceMHRepository.findAll();
        assertThat(competenceMHs).hasSize(databaseSizeBeforeUpdate);
        CompetenceMH testCompetenceMH = competenceMHs.get(competenceMHs.size() - 1);
        assertThat(testCompetenceMH.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testCompetenceMH.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCompetenceMH.getPa()).isEqualTo(UPDATED_PA);
    }

    @Test
    @Transactional
    public void deleteCompetenceMH() throws Exception {
        // Initialize the database
        competenceMHRepository.saveAndFlush(competenceMH);

		int databaseSizeBeforeDelete = competenceMHRepository.findAll().size();

        // Get the competenceMH
        restCompetenceMHMockMvc.perform(delete("/api/competenceMHs/{id}", competenceMH.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<CompetenceMH> competenceMHs = competenceMHRepository.findAll();
        assertThat(competenceMHs).hasSize(databaseSizeBeforeDelete - 1);
    }
}
