package com.mountyhub.app.web.rest;

import com.mountyhub.app.Application;
import com.mountyhub.app.domain.Fly;
import com.mountyhub.app.repository.FlyRepository;

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

import com.mountyhub.app.domain.enumeration.FlyType;

/**
 * Test class for the FlyResource REST controller.
 *
 * @see FlyResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class FlyResourceIntTest {


    private static final Long DEFAULT_NUMBER = 1L;
    private static final Long UPDATED_NUMBER = 2L;

    private static final Integer DEFAULT_OLD = 1;
    private static final Integer UPDATED_OLD = 2;

    private static final Boolean DEFAULT_HERE = false;
    private static final Boolean UPDATED_HERE = true;
    
    private static final FlyType DEFAULT_TYPE = FlyType.CARNATION;
    private static final FlyType UPDATED_TYPE = FlyType.POGEE;
    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    @Inject
    private FlyRepository flyRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restFlyMockMvc;

    private Fly fly;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FlyResource flyResource = new FlyResource();
        ReflectionTestUtils.setField(flyResource, "flyRepository", flyRepository);
        this.restFlyMockMvc = MockMvcBuilders.standaloneSetup(flyResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        fly = new Fly();
        fly.setNumber(DEFAULT_NUMBER);
        fly.setOld(DEFAULT_OLD);
        fly.setHere(DEFAULT_HERE);
        fly.setType(DEFAULT_TYPE);
        fly.setName(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createFly() throws Exception {
        int databaseSizeBeforeCreate = flyRepository.findAll().size();

        // Create the Fly

        restFlyMockMvc.perform(post("/api/flys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(fly)))
                .andExpect(status().isCreated());

        // Validate the Fly in the database
        List<Fly> flys = flyRepository.findAll();
        assertThat(flys).hasSize(databaseSizeBeforeCreate + 1);
        Fly testFly = flys.get(flys.size() - 1);
        assertThat(testFly.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testFly.getOld()).isEqualTo(DEFAULT_OLD);
        assertThat(testFly.getHere()).isEqualTo(DEFAULT_HERE);
        assertThat(testFly.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testFly.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void checkNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = flyRepository.findAll().size();
        // set the field null
        fly.setNumber(null);

        // Create the Fly, which fails.

        restFlyMockMvc.perform(post("/api/flys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(fly)))
                .andExpect(status().isBadRequest());

        List<Fly> flys = flyRepository.findAll();
        assertThat(flys).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkOldIsRequired() throws Exception {
        int databaseSizeBeforeTest = flyRepository.findAll().size();
        // set the field null
        fly.setOld(null);

        // Create the Fly, which fails.

        restFlyMockMvc.perform(post("/api/flys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(fly)))
                .andExpect(status().isBadRequest());

        List<Fly> flys = flyRepository.findAll();
        assertThat(flys).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkHereIsRequired() throws Exception {
        int databaseSizeBeforeTest = flyRepository.findAll().size();
        // set the field null
        fly.setHere(null);

        // Create the Fly, which fails.

        restFlyMockMvc.perform(post("/api/flys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(fly)))
                .andExpect(status().isBadRequest());

        List<Fly> flys = flyRepository.findAll();
        assertThat(flys).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = flyRepository.findAll().size();
        // set the field null
        fly.setType(null);

        // Create the Fly, which fails.

        restFlyMockMvc.perform(post("/api/flys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(fly)))
                .andExpect(status().isBadRequest());

        List<Fly> flys = flyRepository.findAll();
        assertThat(flys).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = flyRepository.findAll().size();
        // set the field null
        fly.setName(null);

        // Create the Fly, which fails.

        restFlyMockMvc.perform(post("/api/flys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(fly)))
                .andExpect(status().isBadRequest());

        List<Fly> flys = flyRepository.findAll();
        assertThat(flys).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFlys() throws Exception {
        // Initialize the database
        flyRepository.saveAndFlush(fly);

        // Get all the flys
        restFlyMockMvc.perform(get("/api/flys?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(fly.getId().intValue())))
                .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER.intValue())))
                .andExpect(jsonPath("$.[*].old").value(hasItem(DEFAULT_OLD)))
                .andExpect(jsonPath("$.[*].here").value(hasItem(DEFAULT_HERE.booleanValue())))
                .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getFly() throws Exception {
        // Initialize the database
        flyRepository.saveAndFlush(fly);

        // Get the fly
        restFlyMockMvc.perform(get("/api/flys/{id}", fly.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(fly.getId().intValue()))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER.intValue()))
            .andExpect(jsonPath("$.old").value(DEFAULT_OLD))
            .andExpect(jsonPath("$.here").value(DEFAULT_HERE.booleanValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingFly() throws Exception {
        // Get the fly
        restFlyMockMvc.perform(get("/api/flys/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFly() throws Exception {
        // Initialize the database
        flyRepository.saveAndFlush(fly);

		int databaseSizeBeforeUpdate = flyRepository.findAll().size();

        // Update the fly
        fly.setNumber(UPDATED_NUMBER);
        fly.setOld(UPDATED_OLD);
        fly.setHere(UPDATED_HERE);
        fly.setType(UPDATED_TYPE);
        fly.setName(UPDATED_NAME);

        restFlyMockMvc.perform(put("/api/flys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(fly)))
                .andExpect(status().isOk());

        // Validate the Fly in the database
        List<Fly> flys = flyRepository.findAll();
        assertThat(flys).hasSize(databaseSizeBeforeUpdate);
        Fly testFly = flys.get(flys.size() - 1);
        assertThat(testFly.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testFly.getOld()).isEqualTo(UPDATED_OLD);
        assertThat(testFly.getHere()).isEqualTo(UPDATED_HERE);
        assertThat(testFly.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testFly.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void deleteFly() throws Exception {
        // Initialize the database
        flyRepository.saveAndFlush(fly);

		int databaseSizeBeforeDelete = flyRepository.findAll().size();

        // Get the fly
        restFlyMockMvc.perform(delete("/api/flys/{id}", fly.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Fly> flys = flyRepository.findAll();
        assertThat(flys).hasSize(databaseSizeBeforeDelete - 1);
    }
}
