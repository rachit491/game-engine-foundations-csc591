// Demonstrating multithreading and thread synchronization in Java
// Synchronization goes in order 1-2-3 of execution
// 3 waits for 2, 2 waits for 1, as 1 is done, 2 starts, when 2 is done 3 starts

public class ForkExample4 implements Runnable {

	int i; 
	boolean busy; 
	ForkExample4 other; 

	// create the runnable object
	public ForkExample4(int i, ForkExample4 other) {
		this.i = i; 
		if(i==1||i==2) { busy = true; } 
		this.other = other; 
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
					busy = false; 
					notify(); 
					System.out.println(this.i + " Finished!");
				}
			}
			catch(InterruptedException tie) { tie.printStackTrace(); }
		}
		else {
			while(other.isBusy()) { 
				synchronized(this) {
					notify();	// to notify depend thread that this one is still waiting
				}
				System.out.println(this.i + " Waiting!");
				// must sychnronize to wait on other object
				try { synchronized(other) { other.wait(); } } 
				catch(InterruptedException tie) { tie.printStackTrace(); }
			}
			try {
				synchronized(this) {
					busy = true;
					notify();
				}
				System.out.println(this.i + " Executing!");
				Thread.sleep(4000);
				synchronized(this) {
					notify();
					busy = false;
					System.out.println(this.i + " Finished!");
				}
			} catch(InterruptedException tie) { tie.printStackTrace(); }
			
		}
	}

	public static void main(String[] args) {
		ForkExample4 t1 = new ForkExample4(1, null);
		ForkExample4 t2 = new ForkExample4(2, t1);
		ForkExample4 t3 = new ForkExample4(3, t2);
		(new Thread(t2)).start();
		(new Thread(t3)).start();
		(new Thread(t1)).start();
	}

}
