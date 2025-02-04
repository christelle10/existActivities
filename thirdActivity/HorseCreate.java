import java.util.List;
import java.util.Scanner;
import java.util.Random;
import java.util.InputMismatchException;


public class HorseCreate {
	   public int getNumberOfHorses(Scanner scanner) {
		    int numberOfHorses = 0;
		    boolean validInput = false;

		    do {
		        try {
		            System.out.print("Insert number of horses: ");
		            numberOfHorses = scanner.nextInt();

		            if (numberOfHorses <= 1) {
		                System.out.println("More than 1 horse is required to join the race.");
		            } else if (numberOfHorses < 0) {
		                throw new IllegalArgumentException("The number of horses cannot be negative.");
		            } else {
		                validInput = true; // Input is valid
		            }

		        } catch (InputMismatchException e) {
		            System.out.println("Invalid input! Please enter an integer.");
		            scanner.nextLine(); // Clear the invalid input
		        } catch (IllegalArgumentException e) {
		            System.out.println(e.getMessage());
		        }
		    } while (!validInput);

		    return numberOfHorses;
		}

    
    public void collectHorseDetails(List<Horse> horses, int numberOfHorses, Scanner scanner, Random random) {
        for (int i = 0; i < numberOfHorses; i++) {
            System.out.println("\n Enter details for Horse " + (i + 1) + ":");

            // Validate name
            System.out.print("Name: ");
            String name = scanner.next();

            // Validate warcry
            String warcry = validateWarcry(scanner);

            // Validate age
            int age = validateAge(scanner);

            // Randomly generate health condition
            String condition = random.nextBoolean() ? "Healthy" : "Unhealthy";

            //Initialize trackDistance as 0
          	int trackDistance = 0;

            // Add horse to the list
            horses.add(new Horse(name, warcry, age));
        }
    }


    public String validateWarcry(Scanner scanner) {
        String warcry = "";
        boolean validWarcry = false;
        while (!validWarcry) {
            System.out.print("Warcry: ");
            warcry = scanner.next();

            // Check if warcry contains any digits
            if (warcry.matches(".*\\d.*")) {
                System.out.println("Invalid input! Warcry cannot contain numbers.");
            } else {
                validWarcry = true;
            }
        }
        return warcry;
    }

    
    public int validateAge(Scanner scanner) {
        int age = -1; // Default value to ensure valid age
        boolean validAge = false;
        while (!validAge) {
            try {
                System.out.print("Age: ");
                age = scanner.nextInt();
                scanner.nextLine(); // Consume newline left-over

                if (age < 0) {
                    throw new IllegalArgumentException("Age cannot be negative.");
                }
                validAge = true;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a valid integer for age.");
                scanner.nextLine(); // Clear invalid input
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
        return age;
    }

}


