package com.mountyhub.app.web.rest.dto;

import com.mountyhub.app.domain.UserOption;
import com.mountyhub.app.domain.enumeration.ScriptName;
import com.mountyhub.app.domain.enumeration.ScriptType;
import com.mountyhub.app.domain.enumeration.TrollRace;

import java.util.List;
import java.util.Map;

/**
 * Created by Theo on 2/29/16.
 */
public class ProfilDTO {
    private Long number;

    private String name;

    private TrollRace race;

    private String birthDateFormatted;

    private Integer x;

    private Integer y;

    private Integer z;

    private Integer attack;

    private Integer dodge;

    private Integer damage;

    private Integer regeneration;

    private Integer hitPoint;

    private Integer currentHitPoint;

    private Integer view;

    private Integer rm;

    private Integer mm;

    private Integer armor;

    private Float turn;

    private Float weight;

    private Integer focus;

    private Integer attackP;

    private Integer dodgeP;

    private Integer damageP;

    private Integer regenerationP;

    private Integer hitPointP;

    private Integer attackM;

    private Integer dodgeM;

    private Integer damageM;

    private Integer regenerationM;

    private Integer hitPointM;

    private Integer viewP;

    private Integer rmP;

    private Integer mmP;

    private Integer armorP;

    private Float weightP;

    private Integer viewM;

    private Integer rmM;

    private Integer mmM;

    private Integer armorM;

    private Float weightM;

    private Integer level;

    private Integer kill;

    private Integer death;

    private String restrictedPassword;

    private Integer percentHitPoint;

    private String turnFormatted;

    private String turnTotalFormatted;

    private String weightTimeFormatted;

    private List<GearDTO> gears;

    private String bonusMalusTimeFormatted;

    private String woundsTimeFormatted;

    private Integer totalHitPoint;

    private Map<ScriptType, Long> scriptCallByDay;

    private Map<ScriptName, String> scriptCallLastCall;

    private String dla;

    private Boolean hidden;

    private Boolean invisible;

    private Boolean intangible;

    private Integer strain;

    private Integer pa;

    private Map<String, UserOption> userOptions;

    private String nextDla;

    private String gearEffect;

    private List<FlyDTO> flies;

    private String flyEffect;

    public List<FlyDTO> getFlies() {
        return flies;
    }

    public void setFlies(List<FlyDTO> flies) {
        this.flies = flies;
    }

    public String getFlyEffect() {
        return flyEffect;
    }

    public void setFlyEffect(String flyEffect) {
        this.flyEffect = flyEffect;
    }

    public String getGearEffect() {
        return gearEffect;
    }

    public void setGearEffect(String gearEffect) {
        this.gearEffect = gearEffect;
    }

    public String getNextDla() {
        return nextDla;
    }

    public void setNextDla(String nextDla) {
        this.nextDla = nextDla;
    }

    public Map<String, UserOption> getUserOptions() {
        return userOptions;
    }

    public void setUserOptions(Map<String, UserOption> userOptions) {
        this.userOptions = userOptions;
    }

    public Integer getPa() {
        return pa;
    }

    public void setPa(Integer pa) {
        this.pa = pa;
    }

    public String getDla() {
        return dla;
    }

    public void setDla(String dla) {
        this.dla = dla;
    }

    public Boolean getHidden() {
        return hidden;
    }

    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
    }

    public Boolean getInvisible() {
        return invisible;
    }

    public void setInvisible(Boolean invisible) {
        this.invisible = invisible;
    }

    public Boolean getIntangible() {
        return intangible;
    }

    public void setIntangible(Boolean intangible) {
        this.intangible = intangible;
    }

    public Integer getStrain() {
        return strain;
    }

    public void setStrain(Integer strain) {
        this.strain = strain;
    }

    public Map<ScriptName, String> getScriptCallLastCall() {
        return scriptCallLastCall;
    }

    public void setScriptCallLastCall(Map<ScriptName, String> scriptCallLastCall) {
        this.scriptCallLastCall = scriptCallLastCall;
    }

    public Map<ScriptType, Long> getScriptCallByDay() {
        return scriptCallByDay;
    }

    public void setScriptCallByDay(Map<ScriptType, Long> scriptCallByDay) {
        this.scriptCallByDay = scriptCallByDay;
    }

    public Integer getTotalHitPoint() {
        return totalHitPoint;
    }

    public void setTotalHitPoint(Integer totalHitPoint) {
        this.totalHitPoint = totalHitPoint;
    }

    public String getWeightTimeFormatted() {
        return weightTimeFormatted;
    }

    public void setWeightTimeFormatted(String weightTimeFormatted) {
        this.weightTimeFormatted = weightTimeFormatted;
    }

    public String getBonusMalusTimeFormatted() {
        return bonusMalusTimeFormatted;
    }

    public void setBonusMalusTimeFormatted(String bonusMalusTimeFormatted) {
        this.bonusMalusTimeFormatted = bonusMalusTimeFormatted;
    }

    public String getWoundsTimeFormatted() {
        return woundsTimeFormatted;
    }

    public void setWoundsTimeFormatted(String woundsTimeFormatted) {
        this.woundsTimeFormatted = woundsTimeFormatted;
    }

    public List<GearDTO> getGears() {
        return gears;
    }

    public void setGears(List<GearDTO> gears) {
        this.gears = gears;
    }

    public String getTurnFormatted() {
        return turnFormatted;
    }

    public void setTurnFormatted(String turnFormatted) {
        this.turnFormatted = turnFormatted;
    }

    public String getTurnTotalFormatted() {
        return turnTotalFormatted;
    }

    public void setTurnTotalFormatted(String turnTotalFormatted) {
        this.turnTotalFormatted = turnTotalFormatted;
    }

    public Integer getPercentHitPoint() {
        return percentHitPoint;
    }

    public void setPercentHitPoint(Integer percentHitPoint) {
        this.percentHitPoint = percentHitPoint;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TrollRace getRace() {
        return race;
    }

    public void setRace(TrollRace race) {
        this.race = race;
    }

    public String getBirthDateFormatted() {
        return birthDateFormatted;
    }

    public void setBirthDateFormatted(String birthDateFormatted) {
        this.birthDateFormatted = birthDateFormatted;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Integer getZ() {
        return z;
    }

    public void setZ(Integer z) {
        this.z = z;
    }

    public Integer getAttack() {
        return attack;
    }

    public void setAttack(Integer attack) {
        this.attack = attack;
    }

    public Integer getDodge() {
        return dodge;
    }

    public void setDodge(Integer dodge) {
        this.dodge = dodge;
    }

    public Integer getDamage() {
        return damage;
    }

    public void setDamage(Integer damage) {
        this.damage = damage;
    }

    public Integer getRegeneration() {
        return regeneration;
    }

    public void setRegeneration(Integer regeneration) {
        this.regeneration = regeneration;
    }

    public Integer getHitPoint() {
        return hitPoint;
    }

    public void setHitPoint(Integer hitPoint) {
        this.hitPoint = hitPoint;
    }

    public Integer getCurrentHitPoint() {
        return currentHitPoint;
    }

    public void setCurrentHitPoint(Integer currentHitPoint) {
        this.currentHitPoint = currentHitPoint;
    }

    public Integer getView() {
        return view;
    }

    public void setView(Integer view) {
        this.view = view;
    }

    public Integer getRm() {
        return rm;
    }

    public void setRm(Integer rm) {
        this.rm = rm;
    }

    public Integer getMm() {
        return mm;
    }

    public void setMm(Integer mm) {
        this.mm = mm;
    }

    public Integer getArmor() {
        return armor;
    }

    public void setArmor(Integer armor) {
        this.armor = armor;
    }

    public Integer getFocus() {
        return focus;
    }

    public void setFocus(Integer focus) {
        this.focus = focus;
    }

    public Integer getAttackP() {
        return attackP;
    }

    public void setAttackP(Integer attackP) {
        this.attackP = attackP;
    }

    public Integer getDodgeP() {
        return dodgeP;
    }

    public void setDodgeP(Integer dodgeP) {
        this.dodgeP = dodgeP;
    }

    public Integer getDamageP() {
        return damageP;
    }

    public void setDamageP(Integer damageP) {
        this.damageP = damageP;
    }

    public Integer getRegenerationP() {
        return regenerationP;
    }

    public void setRegenerationP(Integer regenerationP) {
        this.regenerationP = regenerationP;
    }

    public Integer getHitPointP() {
        return hitPointP;
    }

    public void setHitPointP(Integer hitPointP) {
        this.hitPointP = hitPointP;
    }

    public Integer getAttackM() {
        return attackM;
    }

    public void setAttackM(Integer attackM) {
        this.attackM = attackM;
    }

    public Integer getDodgeM() {
        return dodgeM;
    }

    public void setDodgeM(Integer dodgeM) {
        this.dodgeM = dodgeM;
    }

    public Integer getDamageM() {
        return damageM;
    }

    public void setDamageM(Integer damageM) {
        this.damageM = damageM;
    }

    public Integer getRegenerationM() {
        return regenerationM;
    }

    public void setRegenerationM(Integer regenerationM) {
        this.regenerationM = regenerationM;
    }

    public Integer getHitPointM() {
        return hitPointM;
    }

    public void setHitPointM(Integer hitPointM) {
        this.hitPointM = hitPointM;
    }

    public Integer getViewP() {
        return viewP;
    }

    public void setViewP(Integer viewP) {
        this.viewP = viewP;
    }

    public Integer getRmP() {
        return rmP;
    }

    public void setRmP(Integer rmP) {
        this.rmP = rmP;
    }

    public Integer getMmP() {
        return mmP;
    }

    public void setMmP(Integer mmP) {
        this.mmP = mmP;
    }

    public Integer getArmorP() {
        return armorP;
    }

    public void setArmorP(Integer armorP) {
        this.armorP = armorP;
    }

    public Integer getViewM() {
        return viewM;
    }

    public void setViewM(Integer viewM) {
        this.viewM = viewM;
    }

    public Integer getRmM() {
        return rmM;
    }

    public void setRmM(Integer rmM) {
        this.rmM = rmM;
    }

    public Integer getMmM() {
        return mmM;
    }

    public void setMmM(Integer mmM) {
        this.mmM = mmM;
    }

    public Integer getArmorM() {
        return armorM;
    }

    public void setArmorM(Integer armorM) {
        this.armorM = armorM;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getKill() {
        return kill;
    }

    public void setKill(Integer kill) {
        this.kill = kill;
    }

    public Integer getDeath() {
        return death;
    }

    public void setDeath(Integer death) {
        this.death = death;
    }

    public String getRestrictedPassword() {
        return restrictedPassword;
    }

    public void setRestrictedPassword(String restrictedPassword) {
        this.restrictedPassword = restrictedPassword;
    }

    public Float getTurn() {
        return turn;
    }

    public void setTurn(Float turn) {
        this.turn = turn;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public Float getWeightP() {
        return weightP;
    }

    public void setWeightP(Float weightP) {
        this.weightP = weightP;
    }

    public Float getWeightM() {
        return weightM;
    }

    public void setWeightM(Float weightM) {
        this.weightM = weightM;
    }
}
