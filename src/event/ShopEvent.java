package event;

import java.util.List;

import entity.Player;
import entity.RewardItem;

public class ShopEvent extends Event {
	
	private List<RewardItem>[] stock;

	public ShopEvent(EventType type) {
		super(type);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(Player player) {
		// TODO Auto-generated method stub

	}
	
	public boolean buy(Player player, RewardItem item) {
		return false;
		
	}

}
