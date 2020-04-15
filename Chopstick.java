public class Chopstick {
	public boolean pickedUp;
	public int lastPickedUpBy;

	public Chopstick() {
		pickedUp = false;
		lastPickedUpBy = 0;
	}

	/**
	 * Determines whether a chopstick was last picked up by a philosopher
	 * 
	 * @param piTID Thread ID of the philosopher
	 * @return Whether the philosopher last picked by the chopstick
	 */
	public boolean lastPickedUpByMe(final int piTID) {
		return lastPickedUpBy == piTID;
	}

	/**
	 * Determines whether a chopstick is currently picked up by another philosopher
	 * 
	 * @param piTID Thread ID of the philosopher
	 * @return True if the chopstick is picked up and the philosopher ID does not
	 *         match
	 */
	public boolean pickedUpByAnother(final int piTID) {
		return lastPickedUpBy != piTID && pickedUp;
	}

	/**
	 * Pick up a chopstick and set the new philosopher
	 * 
	 * @param piTID Thread ID of the philosopher
	 */
	public void pickUp(final int piTID) {
		pickedUp = true;
		lastPickedUpBy = piTID;
	}

	/**
	 * Put a chopstick down
	 */
	public void putDown() {
		pickedUp = false;
	}
}