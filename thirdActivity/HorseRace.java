import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class HorseRace {

    // filter only healthy horses
    public List<Horse> getHealthyHorses(List<Horse> horses) {
        return horses.stream().filter(h -> h.getCondition().equals("Healthy")).collect(Collectors.toList());
    }

   
    public int getRaceDistance(Scanner scanner) {
        System.out.print("Race Distance: ");
        int raceDistance = scanner.nextInt();
        scanner.nextLine();  // Consume newline
        return raceDistance;
    }

  
    public void raceCountdown() throws InterruptedException {
        System.out.println("Race Starting in...");
        for (int i = 3; i > 0; i--) {
            System.out.println(i);
            Thread.sleep(1000);
        }
    }

     // group based on age
    public void assignGroup(List<Horse> horses) {
       

        // calculate the most frequent age for "Advanced" group

        //creates a map of key as the age, value as the count of that age 
        Map<Integer, Long> ageCount = horses.stream()
                .collect(Collectors.groupingBy(Horse::getAge, Collectors.counting()));
        //compares individual values of pairs, finds highest. then extracts the key (age) of the highest value (highest count)
        int mostFrequentAge = ageCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElseThrow(() -> new IllegalArgumentException("No horses to classify"));

        // calculate average age for rest of group. maps horses to its age, then calculates average, returns 0,0 if empty list
        double averageAge = horses.stream().mapToInt(Horse::getAge).average().orElse(0.0);

        // use enum class instead of string in the assignment of group (kinda like .env pero di nakaprivate)
        // Assign groups based on conditions
	    for (Horse horse : horses) {
	        if (horse.getAge() == mostFrequentAge) {
	            horse.setGroup("Advanced"); // Set "Advanced" group using setter
	        } else if (horse.getAge() > averageAge) {
	            horse.setGroup("Intermediate"); // Set "Intermediate" group using setter
	        } else {
	            horse.setGroup("Beginner"); // Set "Beginner" group using setter
	        }
	    }
    }

    public List<Horse> startRace(List<Horse> healthyHorses, int raceDistance) throws InterruptedException, ExecutionException {
	    // thread pool for running the races concurrently
	    ExecutorService executor = Executors.newFixedThreadPool(healthyHorses.size());

	    //each task represents the action of a single horse racing
	    List<Callable<String>> tasks = new ArrayList<>();

	    List<Horse> raceOrder = new ArrayList<>();  // To store horses in the order they finish

	    // start racing
	    for (Horse horse : healthyHorses) {
	        tasks.add(() -> {
	            int generation = 0;
	            boolean finished = false;
	            while (!horse.isFinished(raceDistance)) {
	                generation++;
	                horse.race(raceDistance, generation);

	                // prevent printing negative distance
	                int remainingDistance = raceDistance - horse.getTotalDistance();
	                if (remainingDistance < 0) {
	                    remainingDistance = 0;
	                }

	                // if the horse finished the race
	                if (remainingDistance == 0 && !finished) {
	                    finished = true;
	                    synchronized (raceOrder) { //synchronized block to avoid thread interference
	                        raceOrder.add(horse);  // add the horse to the finish order list

	                    }
	                    return "";
	                }

	                // wait for 1 second before next speed generation
	                //Thread.sleep(1000);
	            }
	            return "";
	        });
	    }

	    // Await results, not applicable
	    executor.invokeAll(tasks);
	    executor.shutdown();

	    // After all horses finish, output their placement in order
	    System.out.println("RACE FINISHED!");

	    return raceOrder;
	}


    // print horses based on their order of finish
	public void rankHorses(List<Horse> raceOrder) {
	    int rank = 1;
	    // Iterate over the list from the start to the end
	    for (Horse horse : raceOrder) {
	        System.out.println(rank++ + " - " + horse.getName());
	    }
	}
}
