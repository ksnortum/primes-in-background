package net.snortum.javafx.background;

import javafx.concurrent.Task;

class PrimeNumbersTask extends Task<Void> { 
	private int numbersToFind;
	private boolean stopTask = false;

	PrimeNumbersTask(int numbersToFind) {
		this.numbersToFind = numbersToFind;
	}

	@Override
	protected Void call() throws Exception {
		long number = 0;
		
		for (int i = 0; i < numbersToFind; i++) {
			if (stopTask) {
				updateProgress(0, numbersToFind);
				break;
			}
			
			number = nextPrimeNumber(number);
			// You get the value of number from the new value of the listener 
			// of a message property in the calling class
			updateMessage(String.valueOf(number));
			// You get the progress from binding the task's and progressbar's progress properties
			updateProgress(i + 1, numbersToFind);
			// slow down creation of primes so the display can keep up
			// (probably not needed if you are binding to task's message property)
			Thread.sleep(10); 
		}
		
		return null; 
	}
	
	public void setStopTask(boolean stopTask) {
		this.stopTask = stopTask;
	}

	private long nextPrimeNumber(long thisPrime) {
		if (thisPrime < 2) {
			return 2;
		} else if (thisPrime == 2) {
			return 3;
		} else if (thisPrime == 3) {
			return 5;
		}
		
		long candidate = thisPrime;
		candidate += (thisPrime % 2 == 0) ? 1 : 2;
		
		while (true) {
			boolean noDivisor = true;
			
			for (int i = 3; i < Math.sqrt(candidate); i += 2) {
				if (candidate % i == 0) {
					noDivisor = false;
					break;
				}
			}
			
			if (noDivisor) {
				return candidate;
			}
			
			candidate += 2;
		}
	}
}
