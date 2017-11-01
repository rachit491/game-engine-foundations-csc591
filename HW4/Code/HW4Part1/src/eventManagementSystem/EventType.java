package eventManagementSystem;

import java.io.Serializable;

public enum EventType implements Serializable {
	COLLISION_EVENT(0),
	DEATH_EVENT(1),
	SPAWN_EVENT(2),
	UPDATE_POSITION_EVENT(3),
	JUMP_EVENT(4),
	INPUT_EVENT(5),
	INPUT_EVENT_RELEASE(5),
	
	//replay events
	WILDCARD(6),
	REPLAY_RECORD(7),
	REPLAY_STOP(8),
	REPLAY_FF(9),
	REPLAY_NORMAL(10),
	REPLAY_SM(11),
	
	RECORD_SAVE_DATA(12),
	RECORD_RESTORE_DATA(13),
	REPLAY_SAVE_DATA(12),
	REPLAY_RESTORE_DATA(13);
	
	private final int value;

    private EventType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
