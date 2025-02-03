import java.util.*;
import java.util.stream.Collectors;

public class Horse {
    private String name;
    private String warcry;
    private int age;
    private String condition;
    private int speed;
    private int totalDistance = 0;
    private String group;


//shud only have getters & setters 
    public Horse(String name, String warcry, int age) {
        this.name = name;
        this.warcry = warcry;
        this.age = age;
        this.condition = Math.random() > 0.2 ? "Healthy" : "Unhealthy"; // 80% chance to be healthy
        this.speed = (int) (Math.random() * 10) + 1; // Initial speed between 1-10
        
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWarcry() {
        return warcry;
    }

    public void setWarcry(String warcry) {
        this.warcry = warcry;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(int totalDistance) {
        this.totalDistance = totalDistance;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

   

    public void race(int distance, int generation) { //put on services, model shud only be representation of the object
	    if ("Advanced".equals(this.group) && generation >= 3) {
	         Random random = new Random();
    		// change speed to 5-10 after 3 generations
	        System.out.println("change of speed for advanced starts now.");
    		this.speed = 5 + random.nextInt(6); 
	    }
	    if ("Intermediate".equals(this.group) && generation >= 5) {
	        this.speed = (int) (this.speed * 1.1); // 10% increase after 5 generations
	    }

	    this.totalDistance += this.speed;
	    int remainingDistance = distance - this.totalDistance;

	    // prevent negative distance
	    if (remainingDistance < 0) {
	        remainingDistance = 0;
	    }

	    // print the running status if not finished yet
	    if (remainingDistance == 0) {
	        System.out.printf("%s finished the race! %s\n", this.name, this.warcry);
	    } else {
	        System.out.printf("%s ran %d, remaining %d\n", this.name, this.speed, remainingDistance);
	    }
	}

	


    // Check if horse has finished the race
    public boolean isFinished(int distance) {
        return this.totalDistance >= distance;
    }
}
