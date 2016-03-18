package com.mountyhub.app.web.rest;

import com.mountyhub.app.Application;
import com.mountyhub.app.domain.Spell;
import com.mountyhub.app.repository.SpellRepository;

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
 * Test class for the SpellResource REST controller.
 *
 * @see SpellResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class SpellResourceIntTest {


    private static final Integer DEFAULT_PERCENT = 1;
    private static final Integer UPDATED_PERCENT = 2;

    private static final Integer DEFAULT_PERCENT_BONUS = 1;
    private static final Integer UPDATED_PERCENT_BONUS = 2;

    private static final Integer DEFAULT_LEVEL = 1;
    private static final Integer UPDATED_LEVEL = 2;

    @Inject
    private SpellRepository spellRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restSpellMockMvc;

    private Spell spell;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SpellResource spellResource = new SpellResource();
        ReflectionTestUtils.setField(spellResource, "spellRepository", spellRepository);
        this.restSpellMockMvc = MockMvcBuilders.standaloneSetup(spellResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        spell = new Spell();
        spell.setPercent(DEFAULT_PERCENT);
        spell.setPercentBonus(DEFAULT_PERCENT_BONUS);
        spell.setLevel(DEFAULT_LEVEL);
    }

    @Test
    @Transactional
    public void createSpell() throws Exception {
        int databaseSizeBeforeCreate = spellRepository.findAll().size();

        // Create the Spell

        restSpellMockMvc.perform(post("/api/spells")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(spell)))
                .andExpect(status().isCreated());

        // Validate the Spell in the database
        List<Spell> spells = spellRepository.findAll();
        assertThat(spells).hasSize(databaseSizeBeforeCreate + 1);
        Spell testSpell = spells.get(spells.size() - 1);
        assertThat(testSpell.getPercent()).isEqualTo(DEFAULT_PERCENT);
        assertThat(testSpell.getPercentBonus()).isEqualTo(DEFAULT_PERCENT_BONUS);
        assertThat(testSpell.getLevel()).isEqualTo(DEFAULT_LEVEL);
    }

    @Test
    @Transactional
    public void checkPercentIsRequired() throws Exception {
        int databaseSizeBeforeTest = spellRepository.findAll().size();
        // set the field null
        spell.setPercent(null);

        // Create the Spell, which fails.

        restSpellMockMvc.perform(post("/api/spells")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(spell)))
                .andExpect(status().isBadRequest());

        List<Spell> spells = spellRepository.findAll();
        assertThat(spells).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPercentBonusIsRequired() throws Exception {
        int databaseSizeBeforeTest = spellRepository.findAll().size();
        // set the field null
        spell.setPercentBonus(null);

        // Create the Spell, which fails.

        restSpellMockMvc.perform(post("/api/spells")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(spell)))
                .andExpect(status().isBadRequest());

        List<Spell> spells = spellRepository.findAll();
        assertThat(spells).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLevelIsRequired() throws Exception {
        int databaseSizeBeforeTest = spellRepository.findAll().size();
        // set the field null
        spell.setLevel(null);

        // Create the Spell, which fails.

        restSpellMockMvc.perform(post("/api/spells")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(spell)))
                .andExpect(status().isBadRequest());

        List<Spell> spells = spellRepository.findAll();
        assertThat(spells).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSpells() throws Exception {
        // Initialize the database
        spellRepository.saveAndFlush(spell);

        // Get all the spells
        restSpellMockMvc.perform(get("/api/spells?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(spell.getId().intValue())))
                .andExpect(jsonPath("$.[*].percent").value(hasItem(DEFAULT_PERCENT)))
                .andExpect(jsonPath("$.[*].percentBonus").value(hasItem(DEFAULT_PERCENT_BONUS)))
                .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL)));
    }

    @Test
    @Transactional
    public void getSpell() throws Exception {
        // Initialize the database
        spellRepository.saveAndFlush(spell);

        // Get the spell
        restSpellMockMvc.perform(get("/api/spells/{id}", spell.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(spell.getId().intValue()))
            .andExpect(jsonPath("$.percent").value(DEFAULT_PERCENT))
            .andExpect(jsonPath("$.percentBonus").value(DEFAULT_PERCENT_BONUS))
            .andExpect(jsonPath("$.level").value(DEFAULT_LEVEL));
    }

    @Test
    @Transactional
    public void getNonExistingSpell() throws Exception {
        // Get the spell
        restSpellMockMvc.perform(get("/api/spells/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSpell() throws Exception {
        // Initialize the database
        spellRepository.saveAndFlush(spell);

		int databaseSizeBeforeUpdate = spellRepository.findAll().size();

        // Update the spell
        spell.setPercent(UPDATED_PERCENT);
        spell.setPercentBonus(UPDATED_PERCENT_BONUS);
        spell.setLevel(UPDATED_LEVEL);

        restSpellMockMvc.perform(put("/api/spells")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(spell)))
                .andExpect(status().isOk());

        // Validate the Spell in the database
        List<Spell> spells = spellRepository.findAll();
        assertThat(spells).hasSize(databaseSizeBeforeUpdate);
        Spell testSpell = spells.get(spells.size() - 1);
        assertThat(testSpell.getPercent()).isEqualTo(UPDATED_PERCENT);
        assertThat(testSpell.getPercentBonus()).isEqualTo(UPDATED_PERCENT_BONUS);
        assertThat(testSpell.getLevel()).isEqualTo(UPDATED_LEVEL);
    }

    @Test
    @Transactional
    public void deleteSpell() throws Exception {
        // Initialize the database
        spellRepository.saveAndFlush(spell);

		int databaseSizeBeforeDelete = spellRepository.findAll().size();

        // Get the spell
        restSpellMockMvc.perform(delete("/api/spells/{id}", spell.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Spell> spells = spellRepository.findAll();
        assertThat(spells).hasSize(databaseSizeBeforeDelete - 1);
    }
}
