package event;

import entity.Player;

public class RestEvent extends Event {
	
	private int healPercent;

	public RestEvent(EventType type) {
		super(type);
		// TODO Auto-generated constructor stub
	}

	
	public void upgradeAttack(int i) {
		Player.changeAmount(Player.getAttackDice(), i);
	}
	
	public void upgradeHeal(int i) {
		Player.changeAmount(Player.getHealDice(), i);

	}
	
	public void upgradeDefence(int i) {
		Player.changeAmount(Player.getDefendDice(), i);

	}
	
	@Override
	public void execute(Player player) {
		// TODO Auto-generated method stub

	}
}
