package event;

import java.util.List;

import entity.Enemy;
import entity.Player;

public class MinionEvent extends Event{
	
	private List<Enemy>[] enemies;

	public MinionEvent(EventType type) {
		super(type);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(Player player) {
		// TODO Auto-generated method stub
		
	}

}
