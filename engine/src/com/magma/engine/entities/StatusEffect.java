package com.magma.engine.entities;

public class StatusEffect {	
	public int maxHealth;
	public int maxEnergy;
	public int numbness;
	
	public int attack;
	public int defense;
	public int luck;
	public int maxFear;
	public int speed;
	
	public StatusEffect add(StatusEffect with) {
		maxHealth += with.maxHealth;
		maxEnergy += with.maxEnergy;
		numbness += with.numbness;
		attack += with.attack;
		defense += with.defense;
		luck += with.luck;
		maxFear += with.maxFear;
		speed += with.speed;
		return this;
	}
	
	public StatusEffect subtract(StatusEffect with) {
		maxHealth -= with.maxHealth;
		maxEnergy -= with.maxEnergy;
		numbness -= with.numbness;
		attack -= with.attack;
		defense -= with.defense;
		luck -= with.luck;
		maxFear -= with.maxFear;
		speed -= with.speed;
		return this;
	}
	
	public static StatusEffect invincible() {
		final int MAX = 9999;
		StatusEffect stat = new StatusEffect();
		stat.maxHealth = MAX;
		stat.maxEnergy = MAX;
		stat.numbness = MAX;
		stat.attack = MAX;
		stat.defense = MAX;
		stat.luck = MAX;
		stat.maxFear = MAX;
		stat.speed = 4;
		return stat;
	}

	@Override
	public String toString() {
		return "EN=" + maxHealth + ", CA=" + maxEnergy + ", NU=" + numbness
				+ ", AT=" + attack + ", DEF=" + defense + ", LU=" + luck + ", FO=" + maxFear + ", SP="
				+ speed;
	}
}
