package com.mountyhub.app.web.rest;

import com.mountyhub.app.Application;
import com.mountyhub.app.domain.ScriptCall;
import com.mountyhub.app.repository.ScriptCallRepository;

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

import com.mountyhub.app.domain.enumeration.ScriptName;
import com.mountyhub.app.domain.enumeration.ScriptType;

/**
 * Test class for the ScriptCallResource REST controller.
 *
 * @see ScriptCallResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ScriptCallResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Z"));

    
    private static final ScriptName DEFAULT_NAME = ScriptName.SP_Caract;
    private static final ScriptName UPDATED_NAME = ScriptName.SP_Profil2;
    
    private static final ScriptType DEFAULT_TYPE = ScriptType.DYNAMIQUE;
    private static final ScriptType UPDATED_TYPE = ScriptType.STATIQUE;

    private static final ZonedDateTime DEFAULT_DATE_CALLED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_DATE_CALLED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_DATE_CALLED_STR = dateTimeFormatter.format(DEFAULT_DATE_CALLED);
    private static final String DEFAULT_URL = "AAAAA";
    private static final String UPDATED_URL = "BBBBB";

    private static final Boolean DEFAULT_SUCCESSFULLY_CALLED = false;
    private static final Boolean UPDATED_SUCCESSFULLY_CALLED = true;
    private static final String DEFAULT_BODY = "AAAAA";
    private static final String UPDATED_BODY = "BBBBB";

    private static final Boolean DEFAULT_SUCCESSFULLY_PARSED = false;
    private static final Boolean UPDATED_SUCCESSFULLY_PARSED = true;

    @Inject
    private ScriptCallRepository scriptCallRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restScriptCallMockMvc;

    private ScriptCall scriptCall;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ScriptCallResource scriptCallResource = new ScriptCallResource();
        ReflectionTestUtils.setField(scriptCallResource, "scriptCallRepository", scriptCallRepository);
        this.restScriptCallMockMvc = MockMvcBuilders.standaloneSetup(scriptCallResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        scriptCall = new ScriptCall();
        scriptCall.setName(DEFAULT_NAME);
        scriptCall.setType(DEFAULT_TYPE);
        scriptCall.setDateCalled(DEFAULT_DATE_CALLED);
        scriptCall.setUrl(DEFAULT_URL);
        scriptCall.setSuccessfullyCalled(DEFAULT_SUCCESSFULLY_CALLED);
        scriptCall.setBody(DEFAULT_BODY);
        scriptCall.setSuccessfullyParsed(DEFAULT_SUCCESSFULLY_PARSED);
    }

    @Test
    @Transactional
    public void createScriptCall() throws Exception {
        int databaseSizeBeforeCreate = scriptCallRepository.findAll().size();

        // Create the ScriptCall

        restScriptCallMockMvc.perform(post("/api/scriptCalls")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(scriptCall)))
                .andExpect(status().isCreated());

        // Validate the ScriptCall in the database
        List<ScriptCall> scriptCalls = scriptCallRepository.findAll();
        assertThat(scriptCalls).hasSize(databaseSizeBeforeCreate + 1);
        ScriptCall testScriptCall = scriptCalls.get(scriptCalls.size() - 1);
        assertThat(testScriptCall.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testScriptCall.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testScriptCall.getDateCalled()).isEqualTo(DEFAULT_DATE_CALLED);
        assertThat(testScriptCall.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testScriptCall.getSuccessfullyCalled()).isEqualTo(DEFAULT_SUCCESSFULLY_CALLED);
        assertThat(testScriptCall.getBody()).isEqualTo(DEFAULT_BODY);
        assertThat(testScriptCall.getSuccessfullyParsed()).isEqualTo(DEFAULT_SUCCESSFULLY_PARSED);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = scriptCallRepository.findAll().size();
        // set the field null
        scriptCall.setName(null);

        // Create the ScriptCall, which fails.

        restScriptCallMockMvc.perform(post("/api/scriptCalls")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(scriptCall)))
                .andExpect(status().isBadRequest());

        List<ScriptCall> scriptCalls = scriptCallRepository.findAll();
        assertThat(scriptCalls).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = scriptCallRepository.findAll().size();
        // set the field null
        scriptCall.setType(null);

        // Create the ScriptCall, which fails.

        restScriptCallMockMvc.perform(post("/api/scriptCalls")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(scriptCall)))
                .andExpect(status().isBadRequest());

        List<ScriptCall> scriptCalls = scriptCallRepository.findAll();
        assertThat(scriptCalls).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateCalledIsRequired() throws Exception {
        int databaseSizeBeforeTest = scriptCallRepository.findAll().size();
        // set the field null
        scriptCall.setDateCalled(null);

        // Create the ScriptCall, which fails.

        restScriptCallMockMvc.perform(post("/api/scriptCalls")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(scriptCall)))
                .andExpect(status().isBadRequest());

        List<ScriptCall> scriptCalls = scriptCallRepository.findAll();
        assertThat(scriptCalls).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUrlIsRequired() throws Exception {
        int databaseSizeBeforeTest = scriptCallRepository.findAll().size();
        // set the field null
        scriptCall.setUrl(null);

        // Create the ScriptCall, which fails.

        restScriptCallMockMvc.perform(post("/api/scriptCalls")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(scriptCall)))
                .andExpect(status().isBadRequest());

        List<ScriptCall> scriptCalls = scriptCallRepository.findAll();
        assertThat(scriptCalls).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSuccessfullyCalledIsRequired() throws Exception {
        int databaseSizeBeforeTest = scriptCallRepository.findAll().size();
        // set the field null
        scriptCall.setSuccessfullyCalled(null);

        // Create the ScriptCall, which fails.

        restScriptCallMockMvc.perform(post("/api/scriptCalls")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(scriptCall)))
                .andExpect(status().isBadRequest());

        List<ScriptCall> scriptCalls = scriptCallRepository.findAll();
        assertThat(scriptCalls).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBodyIsRequired() throws Exception {
        int databaseSizeBeforeTest = scriptCallRepository.findAll().size();
        // set the field null
        scriptCall.setBody(null);

        // Create the ScriptCall, which fails.

        restScriptCallMockMvc.perform(post("/api/scriptCalls")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(scriptCall)))
                .andExpect(status().isBadRequest());

        List<ScriptCall> scriptCalls = scriptCallRepository.findAll();
        assertThat(scriptCalls).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSuccessfullyParsedIsRequired() throws Exception {
        int databaseSizeBeforeTest = scriptCallRepository.findAll().size();
        // set the field null
        scriptCall.setSuccessfullyParsed(null);

        // Create the ScriptCall, which fails.

        restScriptCallMockMvc.perform(post("/api/scriptCalls")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(scriptCall)))
                .andExpect(status().isBadRequest());

        List<ScriptCall> scriptCalls = scriptCallRepository.findAll();
        assertThat(scriptCalls).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllScriptCalls() throws Exception {
        // Initialize the database
        scriptCallRepository.saveAndFlush(scriptCall);

        // Get all the scriptCalls
        restScriptCallMockMvc.perform(get("/api/scriptCalls?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(scriptCall.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
                .andExpect(jsonPath("$.[*].dateCalled").value(hasItem(DEFAULT_DATE_CALLED_STR)))
                .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
                .andExpect(jsonPath("$.[*].successfullyCalled").value(hasItem(DEFAULT_SUCCESSFULLY_CALLED.booleanValue())))
                .andExpect(jsonPath("$.[*].body").value(hasItem(DEFAULT_BODY.toString())))
                .andExpect(jsonPath("$.[*].successfullyParsed").value(hasItem(DEFAULT_SUCCESSFULLY_PARSED.booleanValue())));
    }

    @Test
    @Transactional
    public void getScriptCall() throws Exception {
        // Initialize the database
        scriptCallRepository.saveAndFlush(scriptCall);

        // Get the scriptCall
        restScriptCallMockMvc.perform(get("/api/scriptCalls/{id}", scriptCall.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(scriptCall.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.dateCalled").value(DEFAULT_DATE_CALLED_STR))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()))
            .andExpect(jsonPath("$.successfullyCalled").value(DEFAULT_SUCCESSFULLY_CALLED.booleanValue()))
            .andExpect(jsonPath("$.body").value(DEFAULT_BODY.toString()))
            .andExpect(jsonPath("$.successfullyParsed").value(DEFAULT_SUCCESSFULLY_PARSED.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingScriptCall() throws Exception {
        // Get the scriptCall
        restScriptCallMockMvc.perform(get("/api/scriptCalls/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateScriptCall() throws Exception {
        // Initialize the database
        scriptCallRepository.saveAndFlush(scriptCall);

		int databaseSizeBeforeUpdate = scriptCallRepository.findAll().size();

        // Update the scriptCall
        scriptCall.setName(UPDATED_NAME);
        scriptCall.setType(UPDATED_TYPE);
        scriptCall.setDateCalled(UPDATED_DATE_CALLED);
        scriptCall.setUrl(UPDATED_URL);
        scriptCall.setSuccessfullyCalled(UPDATED_SUCCESSFULLY_CALLED);
        scriptCall.setBody(UPDATED_BODY);
        scriptCall.setSuccessfullyParsed(UPDATED_SUCCESSFULLY_PARSED);

        restScriptCallMockMvc.perform(put("/api/scriptCalls")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(scriptCall)))
                .andExpect(status().isOk());

        // Validate the ScriptCall in the database
        List<ScriptCall> scriptCalls = scriptCallRepository.findAll();
        assertThat(scriptCalls).hasSize(databaseSizeBeforeUpdate);
        ScriptCall testScriptCall = scriptCalls.get(scriptCalls.size() - 1);
        assertThat(testScriptCall.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testScriptCall.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testScriptCall.getDateCalled()).isEqualTo(UPDATED_DATE_CALLED);
        assertThat(testScriptCall.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testScriptCall.getSuccessfullyCalled()).isEqualTo(UPDATED_SUCCESSFULLY_CALLED);
        assertThat(testScriptCall.getBody()).isEqualTo(UPDATED_BODY);
        assertThat(testScriptCall.getSuccessfullyParsed()).isEqualTo(UPDATED_SUCCESSFULLY_PARSED);
    }

    @Test
    @Transactional
    public void deleteScriptCall() throws Exception {
        // Initialize the database
        scriptCallRepository.saveAndFlush(scriptCall);

		int databaseSizeBeforeDelete = scriptCallRepository.findAll().size();

        // Get the scriptCall
        restScriptCallMockMvc.perform(delete("/api/scriptCalls/{id}", scriptCall.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ScriptCall> scriptCalls = scriptCallRepository.findAll();
        assertThat(scriptCalls).hasSize(databaseSizeBeforeDelete - 1);
    }
}
