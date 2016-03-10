package com.mountyhub.app.web.rest;

import com.mountyhub.app.Application;
import com.mountyhub.app.domain.UserOption;
import com.mountyhub.app.repository.UserOptionRepository;

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

import com.mountyhub.app.domain.enumeration.UserOptionName;

/**
 * Test class for the UserOptionResource REST controller.
 *
 * @see UserOptionResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class UserOptionResourceIntTest {

    
    private static final UserOptionName DEFAULT_NAME = UserOptionName.ZONEID;
    private static final UserOptionName UPDATED_NAME = UserOptionName.ZONEID;
    private static final String DEFAULT_VALUE = "AAAAA";
    private static final String UPDATED_VALUE = "BBBBB";

    @Inject
    private UserOptionRepository userOptionRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restUserOptionMockMvc;

    private UserOption userOption;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        UserOptionResource userOptionResource = new UserOptionResource();
        ReflectionTestUtils.setField(userOptionResource, "userOptionRepository", userOptionRepository);
        this.restUserOptionMockMvc = MockMvcBuilders.standaloneSetup(userOptionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        userOption = new UserOption();
        userOption.setName(DEFAULT_NAME);
        userOption.setValue(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    public void createUserOption() throws Exception {
        int databaseSizeBeforeCreate = userOptionRepository.findAll().size();

        // Create the UserOption

        restUserOptionMockMvc.perform(post("/api/userOptions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userOption)))
                .andExpect(status().isCreated());

        // Validate the UserOption in the database
        List<UserOption> userOptions = userOptionRepository.findAll();
        assertThat(userOptions).hasSize(databaseSizeBeforeCreate + 1);
        UserOption testUserOption = userOptions.get(userOptions.size() - 1);
        assertThat(testUserOption.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testUserOption.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = userOptionRepository.findAll().size();
        // set the field null
        userOption.setName(null);

        // Create the UserOption, which fails.

        restUserOptionMockMvc.perform(post("/api/userOptions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userOption)))
                .andExpect(status().isBadRequest());

        List<UserOption> userOptions = userOptionRepository.findAll();
        assertThat(userOptions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = userOptionRepository.findAll().size();
        // set the field null
        userOption.setValue(null);

        // Create the UserOption, which fails.

        restUserOptionMockMvc.perform(post("/api/userOptions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userOption)))
                .andExpect(status().isBadRequest());

        List<UserOption> userOptions = userOptionRepository.findAll();
        assertThat(userOptions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllUserOptions() throws Exception {
        // Initialize the database
        userOptionRepository.saveAndFlush(userOption);

        // Get all the userOptions
        restUserOptionMockMvc.perform(get("/api/userOptions?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(userOption.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())));
    }

    @Test
    @Transactional
    public void getUserOption() throws Exception {
        // Initialize the database
        userOptionRepository.saveAndFlush(userOption);

        // Get the userOption
        restUserOptionMockMvc.perform(get("/api/userOptions/{id}", userOption.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(userOption.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingUserOption() throws Exception {
        // Get the userOption
        restUserOptionMockMvc.perform(get("/api/userOptions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserOption() throws Exception {
        // Initialize the database
        userOptionRepository.saveAndFlush(userOption);

		int databaseSizeBeforeUpdate = userOptionRepository.findAll().size();

        // Update the userOption
        userOption.setName(UPDATED_NAME);
        userOption.setValue(UPDATED_VALUE);

        restUserOptionMockMvc.perform(put("/api/userOptions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userOption)))
                .andExpect(status().isOk());

        // Validate the UserOption in the database
        List<UserOption> userOptions = userOptionRepository.findAll();
        assertThat(userOptions).hasSize(databaseSizeBeforeUpdate);
        UserOption testUserOption = userOptions.get(userOptions.size() - 1);
        assertThat(testUserOption.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUserOption.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void deleteUserOption() throws Exception {
        // Initialize the database
        userOptionRepository.saveAndFlush(userOption);

		int databaseSizeBeforeDelete = userOptionRepository.findAll().size();

        // Get the userOption
        restUserOptionMockMvc.perform(delete("/api/userOptions/{id}", userOption.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<UserOption> userOptions = userOptionRepository.findAll();
        assertThat(userOptions).hasSize(databaseSizeBeforeDelete - 1);
    }
}
