package event;

import java.util.List;

import entity.Enemy;
import entity.Player;

public class EliteEvent extends Event {
	
	private List<Enemy>[] elite;

	public EliteEvent(EventType type) {
		super(type);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(Player player) {
		// TODO Auto-generated method stub
		
	}

}
