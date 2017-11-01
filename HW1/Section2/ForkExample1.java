// Demonstrating multithreading and thread synchronization in Java
// 3 Threads initialised, of which two are dependednt on thread 1
// one of the waiting threads will never will notified here..

public class ForkExample1 implements Runnable {

	int i; 
	boolean busy; 
	ForkExample1 other; 

	// create the runnable object
	public ForkExample1(int i, ForkExample1 other) {
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
					notify(); 
					busy = false; 
					System.out.println(this.i + " Finished!");
				}
			}
			catch(InterruptedException tie) { tie.printStackTrace(); }
		}
		else {
			while(other.isBusy()) { 
				System.out.println(this.i + " Waiting!");
				
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
		ForkExample1 t1 = new ForkExample1(1, null);
		ForkExample1 t2 = new ForkExample1(2, t1);
		ForkExample1 t3 = new ForkExample1(3, t1);
		(new Thread(t3)).start();
		(new Thread(t2)).start();
		(new Thread(t1)).start();
	}

}
