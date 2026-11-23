package event;

import entity.Player;

public abstract class Event {
	private EventType type;
	
	public Event(EventType type) {
		super();
		this.type = type;
	}

	/**
	 * Executes the event
	 * Ex: starts the combat phase if on a minion node
	 * @param player
	 */
	public abstract void execute(Player player);

	/**
	 * returns the type of event
	 * @return
	 */
	public EventType getType() {
		return type;
	}
	
}
