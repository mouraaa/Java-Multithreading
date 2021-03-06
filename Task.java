import java.util.*;

public class Task extends Thread {

	public final int MIN_PID = 300;
	public final int MAX_PID = 5000;
	public final int MAX_TIME = 10000;
	public final int MIN_TIME = 1000;
	public LinkedHashMap<Integer,Integer> map = new LinkedHashMap<>();


	public Task() {
		allocate_map();
	}

	//creates a map and itializes all pids to 0
	public int allocate_map() {
		try {
			for(int i = 0; i < 10; i++) {
				Random rnd = new Random();
				int pid = rnd.nextInt(MAX_PID - MIN_PID + 1) + MIN_PID;
				if(map.containsKey(pid)) {
					while(true) {
						int new_pid = rnd.nextInt(MAX_PID - MIN_PID + 1) + MIN_PID;
						if(!map.containsKey(new_pid)) {
							map.put(new_pid,0);
							break;
						}
					}
				}
				else {
					map.put(pid,0);
				}
			}
			return 1;
		}
		catch(Exception e) {
			return -1;
		}
	}

	//the method needed for threads
	//allocate_pid will also activate release_pid after a set of time
	public void run() {
		int answer = allocate_pid();
		if(answer == -1) {
			System.out.println("Allocating pid was unsuccessful");
		}
	}

	//iterators through the map until it finds the first pid that can be allocated
	//otherwise, returns -1
	public synchronized int allocate_pid() {
		Iterator iterator = map.entrySet().iterator(); 
		while(iterator.hasNext()) {
			Map.Entry mapElement = (Map.Entry)iterator.next(); 
			int key = (int)mapElement.getKey();
			int value = (int)mapElement.getValue();
			if(value == 0) {
				System.out.println("pid " + key + " was allocated successfully.");
				map.replace(key,1);
        		try {
        			Random rnd = new Random();
        			int sleep_time = rnd.nextInt(MAX_TIME - MIN_TIME + 1) + MIN_TIME;
          			Thread.sleep(sleep_time);
          			release_pid(key);
        		}
		        catch(Exception e) {
		          System.out.println("error");
		        }
		        // System.out.println(); //add a space for prettier output
				return key;
			}
		}
		return -1;
	}

	//iterators through the map until it finds the first pid that can be released
	//otherwise, prints all in use
	public void release_pid(int int_pid) {
		map.replace(int_pid,0);
		System.out.println("pid " + int_pid +  " was released successfully.");
		
	}
}

