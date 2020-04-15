
/**
 * Class Monitor To synchronize dining philosophers.
 *
 * @author Serguei A. Mokhov, mokhov@cs.concordia.ca
 */
public class Monitor {
	/*
	 * ------------ Data members ------------
	 */
	Chopstick[] chopsticks;
	// number of philosophers
	int noP;
	boolean someonetalking;

	/**
	 * Constructor
	 */
	public Monitor(int piNumberOfPhilosophers) {
		// TODO: set appropriate number of chopsticks based on the # of philosophers
		noP = piNumberOfPhilosophers;
		chopsticks = new Chopstick[piNumberOfPhilosophers];

		// initialize all the chopsticks
		for (int i = 0; i < chopsticks.length; i++) {
			chopsticks[i] = new Chopstick();
		}
	}

	/*
	 * ------------------------------- User-defined monitor procedures
	 * -------------------------------
	 */

	/**
	 * Grants request (returns) to eat when both chopsticks/forks are available.
	 * Else forces the philosopher to wait()
	 */
	public synchronized void pickUp(final int piTID) {
		// ...
		// chopstick id of a corresponding philosopher Thread ID
		int pid = piTID - 1;

		while (true) {
			if (chopsticks[pid].pickedUpByAnother(piTID) || chopsticks[(pid + 1) % noP].pickedUpByAnother(piTID)) {
				// atleast one chopstick is picked up by someone else
				// prioritize hungry philosophers by allowing philosophers who were not
				// the last users of available chopsticks to pick them up while waiting
				// for the other chopstick to become available.
				if (!chopsticks[pid].pickedUp && !chopsticks[pid].lastPickedUpByMe(piTID)) {
					// chopstick on the left
					chopsticks[pid].pickUp(piTID);
				} else if (!chopsticks[(pid + 1) % noP].pickedUp
						&& !chopsticks[(pid + 1) % noP].lastPickedUpByMe(piTID)) {
					// chopstick on the right
					chopsticks[(pid + 1) % noP].pickUp(piTID);
				}
			} else {
				// both chopsticks are available to you, pick them up (even though you may
				// already have them) and leave the loop
				chopsticks[pid].pickUp(piTID);
				chopsticks[(pid + 1) % noP].pickUp(piTID);
				break;
			}

			try {
				wait();
			} catch (InterruptedException e) {
				System.err.println("Monitor.pickUp():");
				DiningPhilosophers.reportException(e);
				System.exit(1);
			}
		}
	}

	/**
	 * When a given philosopher's done eating, they put the chopstiks/forks down and
	 * let others know they are available.
	 */
	public synchronized void putDown(final int piTID) {
		// ...
		chopsticks[piTID - 1].putDown();
		chopsticks[(piTID) % noP].putDown();

		notifyAll();
	}

	/**
	 * Only one philopher at a time is allowed to philosophy (while she is not
	 * eating).
	 */
	public synchronized void requestTalk() {
		// ...
		while (someonetalking) {
			try {
				wait();
			} catch (InterruptedException e) {
				System.err.println("Monitor.requestTalk():");
				DiningPhilosophers.reportException(e);
				System.exit(1);
			}
		}

		// requester is now talking
		someonetalking = true;
	}

	/**
	 * When one philosopher is done talking stuff, others can feel free to start
	 * talking.
	 */
	public synchronized void endTalk() {
		// ...
		someonetalking = false;
		notifyAll();
	}
}

// EOF
