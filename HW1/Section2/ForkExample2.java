// Demonstrating multithreading and thread synchronization in Java

// 3 Threads initialised, of which two are dependednt on thread 1
// Notify in second synchronzied blck is changed to notifyAll() so that both 
// 2 and 3 thread are notified about thread 1 being free

public class ForkExample2 implements Runnable {

	int i; 
	boolean busy; 
	ForkExample2 other; 

	// create the runnable object
	public ForkExample2(int i, ForkExample2 other) {
		this.i = i; 
		if(i==1) { busy = true; } 
		else { this.other = other; }
	}

	// synchronized method to test if thread is busy or not
	public synchronized boolean isBusy() { return busy; } 

	// run method needed by runnable interface
	public void run() {
		if(i==1) { 
			try {
				System.out.println(this.i + " Executing!");
				Thread.sleep(4000); 
				synchronized(this) {
					notify(); 
				}
				Thread.sleep(4000); 
				synchronized(this) {
					notifyAll(); 
					busy = false; 
					System.out.println(this.i + " Finished!");
				}
			}
			catch(InterruptedException tie) { tie.printStackTrace(); }
		}
		else {
			while(other.isBusy()) { 
				System.out.println(this.i + " Waiting!");
				// must sychnronize to wait on other object
				try { synchronized(other) { other.wait(); } } 
				catch(InterruptedException tie) { tie.printStackTrace(); }
			}
			try {
				System.out.println(this.i + " Executing!");
				Thread.sleep(3000);
				System.out.println(this.i + " Finished!");
			}
			catch(InterruptedException tie) { tie.printStackTrace(); }
		}
	}

	public static void main(String[] args) {
		ForkExample2 t1 = new ForkExample2(1, null);
		ForkExample2 t2 = new ForkExample2(2, t1);
		ForkExample2 t3 = new ForkExample2(3, t1);
		(new Thread(t3)).start();
		(new Thread(t2)).start();
		(new Thread(t1)).start();
	}

}
