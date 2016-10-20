import java.util.Random;

public class ThreadsAndStuff {
	private static int x,y,z;
	private volatile static int testing = 0;
	public static void f() {
		x = x + 1;
		y = y + 1;
		z = z + x - y;
	}
	
	static Runnable codestuffhalfashardbutsynchronized = new Runnable(){
		public synchronized void run(){
			for (int i = 0; i <1000000000; i++){
				f();
			}			
		}
	};

	static Runnable codestuffhalfashard = new Runnable(){
		public void run(){
			for (int i = 0; i <1000000000; i++){
				f();
			}			
		}
	};

	static Runnable codestuff = new Runnable(){
		public void run(){
			for (int i = 0; i <2000000000; i++){
				f();
			}			
		}
	};

	public static void printValues() {
	    System.out.println("x = " + x);
	    System.out.println("y = " + y);
	    System.out.println("z = " + z);
	}
	

	public static void main(String[] args) throws InterruptedException {
		//Part 1
		/*
		Thread SoAlone = new Thread(codestuff);
		long startTime = System.nanoTime();
		// put computation you want to measure here
		SoAlone.start();

		SoAlone.join();
		long endTime = System.nanoTime();
		System.out.println("Computation took " + ((endTime - startTime) / 1000000) + " milliseconds");
		printValues();
		*/
		
		// Part 2
		/*
		Thread BadThread = new Thread(codestuffhalfashard);
		Thread WorseThread = new Thread(codestuffhalfashard);
		long startTime = System.nanoTime();
		BadThread.start();
		WorseThread.start();
		
		BadThread.join();
		WorseThread.join();
		
		long endTime = System.nanoTime();
		System.out.println("Computation took " + ((endTime - startTime) / 1000000) + " milliseconds");
		printValues();
		*/
		
		// Part 3
		/*
		Thread BadThread = new Thread(codestuffhalfashardbutsynchronized);
		Thread WorseThread = new Thread(codestuffhalfashardbutsynchronized);
		long startTime = System.nanoTime();
		BadThread.start();
		WorseThread.start();
		
		BadThread.join();
		WorseThread.join();
		
		long endTime = System.nanoTime();
		System.out.println("Computation took " + ((endTime - startTime) / 1000000) + " milliseconds");
		printValues();
		*/
		
		// Part 4
		
		int[][] matrix = new int[10][10000000]; // if you run out of memory during execution, reduce the second dimension
		int[] summs = new int[matrix.length];
		Random rand = new Random();

		// Initialize matrix with random numbers
		for (int x = 0; x < matrix.length; x++) {
		    for (int y = 0; y < matrix[x].length; y++) {
		        int randomNum = rand.nextInt(200); // 0 - 199
		        matrix[x][y] = randomNum;
		    }
		}
		// one thread
		long startTime = System.nanoTime();
		int sum = 0;
		for (int x = 0; x < matrix.length; x++) {
		    for (int y = 0; y < matrix[x].length; y++) {
		        sum = sum + (int)Math.log(matrix[x][y]);
		    }
		}
		long endTime = System.nanoTime();
		System.out.println("One thread sum:  " + sum);
		System.out.println("Computation took " + ((endTime - startTime) / 1000000) + " milliseconds");
		// multiple threads
		Thread arrayofThreads[] = new Thread[matrix.length];
		Thread arrayofThreads2[] = new Thread[matrix.length];
	    class workfaster implements Runnable{
	    	private int x;
	    	workfaster(int xx){
	    		x = xx;
	    	}
	    	
	    	public void run(){
	    		int sec = 0;
	    		for (int y = 0; y < matrix[x].length; y++) {
	    			sec = sec + (int)Math.log(matrix[x][y]);
	    		}
	    		
	    		addit(x,sec);
	    	}
	    	public void addit(int x, int sec){
	    		summs[x] = sec;
	    	}
	    };
	    
	    class workfaster2 implements Runnable{
	    	private int x;
	    	workfaster2(int xx){
	    		x = xx;
	    	}
	    	
	    	public void run(){
	    		int sec = 0;
	    		for (int y = 0; y < matrix[x].length; y++) {
	    			sec = sec + (int)Math.log(matrix[x][y]);
	    		}
	    		
	    		addit(sec);
	    	}
	    	public synchronized void addit(int x){
	    		testing+=x;
	    	}
	    };
	    
		startTime = System.nanoTime();
		sum = 0;
		for (int x = 0; x < matrix.length; x++) {
			arrayofThreads[x] = new Thread(new workfaster(x));
			arrayofThreads[x].start();
		}
		
		Thread current;
		for (int i = 0; i < matrix.length; i++){
		    current = arrayofThreads[i];
		    current.join();
		}
		int secondcount = 0;
		for (int i = 0; i < matrix.length; i++){
			secondcount = secondcount + summs[i];
		}
		
		endTime = System.nanoTime();
		System.out.println("Multiple thread sum:  " + secondcount);
		System.out.println("Computation took " + ((endTime - startTime) / 1000000) + " milliseconds");
		
		startTime = System.nanoTime();
		sum = 0;
		for (int x = 0; x < matrix.length; x++) {
			arrayofThreads2[x] = new Thread(new workfaster2(x));
			arrayofThreads2[x].start();
		}
		
		for (int i = 0; i < matrix.length; i++){
		    current = arrayofThreads2[i];
		    current.join();
		}
		
		endTime = System.nanoTime();
		System.out.println("Multiple thread2 sum:  " + testing);
		System.out.println("Computation took " + ((endTime - startTime) / 1000000) + " milliseconds");
	}

}
