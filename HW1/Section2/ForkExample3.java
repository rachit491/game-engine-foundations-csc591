
// Demonstrating multithreading and thread synchronization in Java
public class ForkExample3 implements Runnable {

	int i; 
	boolean busy; 
	ForkExample3 other; 

	// create the runnable object
	public ForkExample3(int i, ForkExample3 other) {
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
				
				//Removing this block will bring in a deadlock situation as T2 will be waiting for the busy flag to be false
				//but it wont be notified about this, the program is stuck, even if we set busy = false, thread2 will never be executed
				busy = false;
				/*
				synchronized(this) {
					busy = false; // must synchronize while editing the flag
					notify(); // notify() will only notify threads waiting on *this* object;
				}
				*/
				System.out.println(this.i + " Finished!");
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
			System.out.println(this.i + " Executing!");
			try {
				Thread.sleep(2000);
			} catch(InterruptedException tie) { tie.printStackTrace(); }
			System.out.println(this.i + " Finished!");
		}
	}

	public static void main(String[] args) {
		ForkExample3 t1 = new ForkExample3(1, null);
		ForkExample3 t2 = new ForkExample3(2, t1);
		(new Thread(t2)).start();
		(new Thread(t1)).start();
	}

}
