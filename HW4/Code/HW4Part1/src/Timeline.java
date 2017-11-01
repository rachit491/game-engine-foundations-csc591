import java.io.Serializable;

/**
 * Timeline Class, tried to use the code from book, modified to major extent
 * of updating with integer values so timeCycles can be used as GVT as well.
 * @author rachit
 *
 */
public class Timeline implements Serializable {	
	long timeCycles, startTime;
	boolean isPaused;
	double tickSize, factor;
	
	
	public Timeline(long startTime) {
		this.isPaused = false;
		this.tickSize = 60;
		this.factor = 120;
		this.startTime = startTime;
		
		this.setTickSize(this.tickSize);
		
		this.timeCycles = this.secondsToCycles(startTime);
	}
	
	public void setTickSize(double d) {
		this.tickSize = d;
	}
	
	public double getTickSize() {
		return this.tickSize;
	}
	
	public long secondsToCycles(long timeSeconds) {
		return (long) ((timeSeconds) * this.tickSize);
	}
	
	/*public void update(long realTime) {
		if(!this.isPaused) {
			long delta = this.secondsToCycles(realTime);
			this.timeCycles += delta;
		}
	}*/
	
	public long getTimeCycles() {
		return this.timeCycles;
	}
	
	public boolean getIsPaused() {
		return this.isPaused;
	}
	
	public void setIsPaused(boolean isPaused) {
		this.isPaused = isPaused;
	}

	private long cyclesToSeconds(double tick) {
		return (long) (factor/tick);
	}
	
	public void singleStep() {
		if (!this.isPaused) {
			// Add one ideal frame interval; don't forget
			// to scale it by our current time scale!
			long dtScaledCycles = cyclesToSeconds((this.tickSize));
			this.timeCycles += dtScaledCycles;
		}
	}
	
}
