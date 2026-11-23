package event;

import java.util.List;

import entity.Enemy;
import entity.Player;

public class BossEvent extends Event {
	
	private List<Enemy>[] boss;

	public BossEvent(EventType type) {
		super(type);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(Player player) {
		// TODO Auto-generated method stub

	}

}
