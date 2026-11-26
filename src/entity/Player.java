package entity;


import edu.princeton.cs.algs4.StdRandom;   
/**
 * This class generates the all the things needed for the player to progress through the game
 * It stores and tracks information essential for the players journey through the game.
 * 
 * @author Sterling + Brayden
 */
public class Player {
	private String name;
	private int maxHealth;
	private int currentHealth;
	private int gold;
	private int currentNode;
	private static int attackDice;
	private static int healDice;
	private static int defendDice;
	
	public Player(String name, int maxHealth, int currentHealth, int gold, int currentNode, int attackDice,
			int healDice, int defendDice) {
		super();
		this.name = name;
		this.maxHealth = maxHealth;
		this.currentHealth = currentHealth;
		this.gold = gold;
		this.currentNode = currentNode;
		this.attackDice = attackDice;
		this.healDice = healDice;
		this.defendDice = defendDice;
	}
	
	/**
	 * returns a random number between 1 and the size of the dice
	 * as a integer to do damage
	 * 
	 * @return
	 */
	public int attack() {
		return StdRandom.uniformInt(1, getAttackDice());
	}
	
	/**
	 * Heals a an amount equal to i to currentHealth
	 * does not go over max health
	 * @param i
	 */
	public void heal(int i) {
		if(currentHealth + i > maxHealth)
			currentHealth = maxHealth;
		else {
			currentHealth += i;
		}
	}
	
	/**
	 * returns a random number between 1 and the size of the dice
	 * as a integer to heal
	 * 
	 * @return
	 */
	public int heal() {
		return StdRandom.uniformInt(1, getHealDice());
	}
	
	/**
	 * returns a random number between 1 and the size of the dice
	 * as a integer to defend
	 * 
	 * @return
	 */
	public int defend() {
		return StdRandom.uniformInt(1, getDefendDice());
	}
	
	/**
	 * Removes a an amount equal to i from currentHealth
	 * @param i
	 */
	public void takeDamage(int i) {
		currentHealth -= i;
		if(currentHealth <= 0) {
			isDead();
		}
	}
	
	public void changeGold(int i) {
		gold += i;
	}
	
	public void isDead() {
		//TODO
	}

	public void moveTo(int vertex) {
		//TODO
	}

	public static void changeAmount(int dice, int changeAmount) {
		if(dice + changeAmount < 1)
			dice = 1;
		else {
			dice += changeAmount;
		}
	}
	
	/**
	 * Returns which die they have for attacking
	 * @return
	 */
	public static int getAttackDice() {
		return attackDice;
	}
	
	/**
	 * Sets which specific die they have for attacking
	 * @param attackDice
	 */
	public void setAttackDice(int changeAmount) {
		changeAmount(attackDice, changeAmount);

	}

	/**
	 * Returns which specific die they have for healing
	 * @return
	 */
	public static int getHealDice() {
		return healDice;
	}

	/**
	 * Sets which specific die they have for healing
	 * @param healDice
	 */
	public void setHealDice(int changeAmount) {
		changeAmount(attackDice, changeAmount);
	}

	/**
	 * Returns which specific die they have for defending
	 * @return
	 */
	public static int getDefendDice() {
		return defendDice;
	}

	/**
	 * Sets which  specific die they have for defending
	 * @param defendDice
	 */
	public void setDefendDice(int changeAmount) {
		changeAmount(attackDice, changeAmount);	
	}

}
