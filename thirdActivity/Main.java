import java.util.*;
import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
        List<Horse> horses = new ArrayList<>();
        List<Horse> healthyHorses = new ArrayList<>();
        List<Horse> raceOrder = new ArrayList<>();
        HorseCreate horseCreate = new HorseCreate();
        HorseRace horseRace = new HorseRace();
        int numberOfHorses = 0;

        boolean continueRacing = false;  // Flag to control the loop

        while (!continueRacing) {
            horses.clear();  

            //*********** USER INPUT *********** //
            numberOfHorses = horseCreate.getNumberOfHorses(scanner);
            horseCreate.collectHorseDetails(horses, numberOfHorses, scanner, random);

            //*********** RACE *********** //
            healthyHorses = horseRace.getHealthyHorses(horses);

            // assign group based on age
            horseRace.assignGroup(healthyHorses);

            // Handle if only one horse is healthy
            if (healthyHorses.size() == 1) {
                System.out.println("There's only 1 Qualified Horse in the group of horses you entered in the race.");
                System.out.print("Enter a new group of horses to start a new race? (y/n): ");
                String userInput = scanner.nextLine().toLowerCase();
                if (userInput.equals("n")) {
                    System.exit(0);
                }
                continue;  // ask user new set of horses
            }
            else {
                continueRacing = true;
            }
        }

        System.out.println("\n" + healthyHorses.size() + " Qualified Horse(s): ");

        //only join healthy horses
            int index = 1;
            for (Horse horse : healthyHorses) {
                System.out.println(index + ". " + horse.getName());
                index++;
            }

            
            int raceDistance = 0;
            boolean validInput = false;

            //make sure race distance is valid int 
            while (!validInput) {
                try {
                    raceDistance = horseRace.getRaceDistance(scanner);
                    validInput = true;  // If input is valid, exit the loop
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a valid number for the race distance.");
                    scanner.nextLine();  // Clear the invalid input from the scanner
                }
            }

            // start race countdown
            horseRace.raceCountdown();

            // start the race, will return a horse list that has the horse's placements
            raceOrder = horseRace.startRace(healthyHorses, raceDistance);

            //print the rankings 
            horseRace.rankHorses(raceOrder);

        
        System.out.println("Thanks for racing!");
        scanner.close();
    }
}
