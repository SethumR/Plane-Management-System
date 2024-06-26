import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

class w2052112_PlaneManagement {
    public static void main(String[] args) {
        PlaneManagement planeManagement = new PlaneManagement();
        planeManagement.start();
    }
}

class PlaneManagement {
    private static final int TOTAL_ROWS = 4; // Total number of rows
    private static final int SEATS_PER_ROW = 15; // Number of seats per row

    private static int[][] seats; // 2D array representing the availability of seats
    private static Scanner scanner; // Scanner object used for user input
    private static Ticket tickets;
    private static Ticket[] ticketsArray = {}; // Array to store all purchased tickets
    private static int totalTicketprice = 0; // Total price of all purchased tickets
    private static char rowLetter;
    private static int seatNumber;
    private static int price; // Price of a single ticket

    public PlaneManagement() {
        seats = new int[TOTAL_ROWS][SEATS_PER_ROW];
        for (int i = 0; i < TOTAL_ROWS; i++) {
            for (int j = 0; j < SEATS_PER_ROW; j++) {
                seats[i][j] = 0; // Initialize all seats as available
            }
        }
        scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("Welcome to the Plane Management application");
        int choice = 0;
        printMenuOptions();
        do {
            try {
                while (true) {
                    System.out.print("Please select an option: ");
                    if (scanner.hasNextInt()) {
                        choice = scanner.nextInt();
                        switch (choice) {
                            case 1:
                                buySeat();
                                break;
                            case 2:
                                cancelSeat();
                                break;
                            case 3:
                                findFirstAvailable();
                                break;
                            case 4:
                                showSeatingPlan();
                                break;
                            case 5:
                                printTicketInfo();
                                break;
                            case 6:
                                searchTicket();
                                break;
                            case 0:
                                System.out.println("Thank you for using our service!");
                                return;
                            default:
                                System.out.println("Invalid choice. Please enter valid option");
                        }
                    } else {
                        String input = scanner.next();
                        System.out.println("Invalid choice. Please enter valid option");
                    }
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid choice. Please enter valid option");
                scanner.next();
            }
        } while (true);
    }


    //Menu option
    private void printMenuOptions() {
        System.out.println("****************************************************************");
        System.out.println("*                          MENU OPTIONS                        *");
        System.out.println("****************************************************************");
        System.out.println("1. Buy a seat");
        System.out.println("2. Cancel a seat");
        System.out.println("3. Find first available seat");
        System.out.println("4. Show seating plan");
        System.out.println("5. Print ticket information and total sales");
        System.out.println("6. Search tickets ");
        System.out.println("0. Quit");
        System.out.println("****************************************************************");
    }

    //buy seat method
    private void buySeat() {
        while (true) {
            System.out.print("Enter person's name: ");
            String name = scanner.next();
            System.out.print("Enter person's surname: ");
            String surname = scanner.next();
            System.out.print("Enter person's email: ");
            String email = scanner.next();

            // Create a new Person object
            Person person = new Person(name, surname, email);

            char rowLetter;
            int seatNumber;

            // Input row letter
            while (true) {
                System.out.print("Enter the row letter (A-D): ");
                rowLetter = Character.toUpperCase(scanner.next().charAt(0));
                if (rowLetter >= 'A' && rowLetter <= 'D') {
                    break; // Break the loop if valid input
                } else {
                    System.out.println("Invalid row letter. Please enter a letter from A to D.");
                }
            }
            //Convert the row letter character to a string
            String rowString = Character.toString(rowLetter);

            while (true) {
                try {
                    System.out.print("Enter the seat number (1-14): ");
                    seatNumber = scanner.nextInt() - 1;
                    if (seatNumber >= 0 && seatNumber <= 13) {
                        if (!((rowLetter == 'B' || rowLetter == 'C') && (seatNumber == 12 || seatNumber == 13))) {
                            break;
                        } else {
                            System.out.println("Invalid seat number. Seats 13 and 14 are not available in rows B and C.");
                        }
                    } else {
                        System.out.println("Invalid seat number. Please enter a number from 1 to 14.");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a valid number.");
                    scanner.next();
                }
            }

            if (seats[rowLetter - 'A'][seatNumber] == 0) {
                seats[rowLetter - 'A'][seatNumber] = 1; // Mark as sold
                seatNumber += 1;

                System.out.println("Seat " + rowLetter + seatNumber + " purchased successfully.");
            } else {
                System.out.println("Seat " + rowLetter + (seatNumber + 1) + " is already booked.");
                continue;
            }

            //making the tickets prices
            if (seatNumber < 6) {
                price = 200;
                totalTicketprice += 200;
            } else if (seatNumber < 10) {
                price = 150;
                totalTicketprice += 150;
            } else {
                price = 180;
                totalTicketprice += 180;
            }

            // storing  the details in a array
            tickets = new Ticket(rowString, seatNumber, price, person);
            Ticket[] Array = Arrays.copyOf(ticketsArray, ticketsArray.length + 1);
            Array[ticketsArray.length] = tickets;
            ticketsArray = Arrays.copyOf(Array, Array.length);

            save(rowString, seatNumber, price, person);
            break;
        }
    }

    //cancel seat method
    private void cancelSeat() {
        while (true) {
            System.out.print("Enter the row letter (A-D): ");
            rowLetter = Character.toUpperCase(scanner.next().charAt(0));
            if (rowLetter >= 'A' && rowLetter <= 'D') {
                break;
            } else {
                System.out.println("Invalid row letter. Please enter a letter from A to D.");
            }
        }

        while (true) {
            try {
                System.out.print("Enter the seat number (1-14): ");
                seatNumber = scanner.nextInt() - 1;
                if (seatNumber >= 0 && seatNumber <= 13) {
                    if (!((rowLetter == 'B' || rowLetter == 'C') && (seatNumber == 12 || seatNumber == 13))) {
                        break;
                    } else {
                        System.out.println("Invalid seat number. Seats 13 and 14 are not available in rows B and C.");
                    }
                } else {
                    System.out.println("Invalid seat number. Please enter a number from 1 to 14.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.next();
            }
        }

        int row = rowLetter - 'A'; // Calculate row index based on row letter
        if (seats[row][seatNumber] == 1) {
            seats[row][seatNumber] = 0; // Mark seat as available
            System.out.println("Seat " + rowLetter + (seatNumber + 1) + " cancelled successfully.");

            String rowLetterString = Character.toString(rowLetter);
            String selectedSeat = rowLetterString + Integer.toString(seatNumber + 1);

            // Find the index to remove
            int indexToRemove = -1;
            for (int i = 0; i < ticketsArray.length; i++) {
                if (ticketsArray[i] != null && ticketsArray[i].getRow().equals(rowLetterString) && (ticketsArray[i].getSeat() == (seatNumber + 1))) {
                    indexToRemove = i;
                    break;
                }
            }

            if (indexToRemove != -1) {
                Ticket[] newArray = new Ticket[ticketsArray.length - 1];
                for (int i = 0, j = 0; i < ticketsArray.length; i++) {
                    if (i != indexToRemove) {
                        newArray[j++] = ticketsArray[i];
                    }
                }
                ticketsArray = newArray;
            }

            if (seatNumber < 6) {
                totalTicketprice -= 200;
            } else if (seatNumber < 10) {
                totalTicketprice -= 150;
            } else {
                totalTicketprice -= 180;
            }
        } else {
            System.out.println("Seat " + rowLetter + (seatNumber + 1) + " is not booked.");
        }
    }


    //find first available method
    private void findFirstAvailable() {
        for (int i = 0; i < TOTAL_ROWS; i++) {
            for (int j = 0; j < SEATS_PER_ROW; j++) {
                if (seats[i][j] == 0) {
                    System.out.println("First available seat: " + (char) ('A' + i) + (j + 1));
                    return;
                }
            }
        }
        System.out.println("No available seats.");
    }

    //show seating plan method
    private void showSeatingPlan() {
        System.out.println("Seating Plan:");
        for (int i = 0; i < TOTAL_ROWS; i++) {
            char rowLetter = (char) ('A' + i);
            System.out.print(rowLetter + " ");
            int seatsPerRow = (i == 1 || i == 2) ? 12 : 14;
            for (int j = 0; j < seatsPerRow; j++) {
                System.out.print(seats[i][j] == 0 ? "O " : "X "); // O for available, X for sold
            }
            System.out.println();
        }
    }

    //print ticket info method
    private void printTicketInfo() {
        System.out.println("Sold Tickets are:");
        for (int i = 0; i < ticketsArray.length; i++) {
            System.out.println(ticketsArray[i].getRow() + ticketsArray[i].getSeat() + " ");
        }
        System.out.println("Total Tickets Values are:£" + totalTicketprice);
    }

    //search ticket method
    private void searchTicket() {
        while (true) {
            System.out.print("Enter the row letter (A-D): ");
            rowLetter = Character.toUpperCase(scanner.next().charAt(0));
            if (rowLetter >= 'A' && rowLetter <= 'D') {
                break; // Break the loop if valid input
            } else {
                System.out.println("Invalid row letter. Please enter a letter from A to D.");
            }
        }

        // Convert the row letter character to a string
        String rowString = Character.toString(rowLetter);

        while (true) {
            try {
                System.out.print("Enter the seat number (1-14): ");
                seatNumber = scanner.nextInt() - 1;
                if (seatNumber >= 0 && seatNumber <= 13) {
                    if (!((rowLetter == 'C' || rowLetter == 'C') && (seatNumber == 12 || seatNumber == 13))) {
                        break;
                    } else {
                        System.out.println("Invalid seat number. Seats 13 and 14 are not available in rows B and C.");
                    }
                } else {
                    System.out.println("Invalid seat number. Please enter a number from 1 to 14.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.next();
            }
        }

        int row = rowLetter - 'A'; // Calculate row index based on row letter

        if (seats[row][seatNumber] == 1) {
            boolean ticketFound = false;
            for (int i = 0; i < ticketsArray.length; i++) {
                if (rowString.equals(ticketsArray[i].getRow()) && (seatNumber + 1) == ticketsArray[i].getSeat()) {
                    ticketFound = true;
                    System.out.println("Ticket Information:");
                    System.out.println("Ticket is " + ticketsArray[i].getRow() + ticketsArray[i].getSeat());
                    System.out.println("Price is " + ticketsArray[i].getPrice() + "\n");
                    System.out.println("Customer Information:");
                    ticketsArray[i].getPerson().printInfo();
                    break;
                }
            }
            if (!ticketFound) {
                System.out.println("Ticket not found for the specified seat.");
            }
        } else {
            System.out.println("This seat is available");
        }
    }


    //saving the file
    public static void save(String row, int seatNumber, int price, Person person) {
        try {
            FileWriter myfile = new FileWriter(row + seatNumber + ".txt");
            myfile.write(
                    "Your Ticket is:" + row + seatNumber + "\r\r" + "Ticket price is:" + price + "\r\r" + "Customer name is:" + person.getName() + "\r\r" +
                            "Customer Surname is: " + person.getSurname() + "\r\r" + "Customer Email is: " + person.getEmail()
            );
            myfile.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}



