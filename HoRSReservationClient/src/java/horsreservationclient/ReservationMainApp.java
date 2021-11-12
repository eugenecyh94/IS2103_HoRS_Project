package horsreservationclient;

import ejb.session.stateless.GuestEntitySessionBeanRemote;
import ejb.session.stateless.ReservationEntitySessionBeanRemote;
import ejb.session.stateless.RoomTypeEntitySessionBeanRemote;
import ejb.session.stateless.SearchSessionBeanRemote;
import entity.GuestEntity;
import entity.ReservationEntity;
import entity.RoomTypeEntity;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import util.exception.GuestNotFoundException;
import util.exception.InvalidLoginCredentialException;
import util.exception.NoRoomTypeAvailableException;
import util.exception.ReservationCannotBeFoundException;
import util.exception.RoomTypeCannotBeFoundException;

public class ReservationMainApp {

    private ReservationEntitySessionBeanRemote reservationEntitySessionBeanRemote;
    private SearchSessionBeanRemote searchSessionBeanRemote;
    private RoomTypeEntitySessionBeanRemote roomTypeEntitySessionBeanRemote;
    private GuestEntitySessionBeanRemote guestEntitySessionBeanRemote;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private GuestEntity currentGuestEntity;

    public ReservationMainApp() {
    }

    public ReservationMainApp(ReservationEntitySessionBeanRemote reservationEntitySessionBean, SearchSessionBeanRemote searchSessionBean, RoomTypeEntitySessionBeanRemote roomTypeEntitySessionBean, GuestEntitySessionBeanRemote guestEntitySessionBean) {
        this.reservationEntitySessionBeanRemote = reservationEntitySessionBean;
        this.searchSessionBeanRemote = searchSessionBean;
        this.roomTypeEntitySessionBeanRemote = roomTypeEntitySessionBean;
        this.guestEntitySessionBeanRemote = guestEntitySessionBean;
    }

    public void runApp() {

        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** Merlion Management System ***\n");
            if (currentGuestEntity != null) {
                System.out.println("You are logged in as " + currentGuestEntity.getFirstName() + " " + currentGuestEntity.getLastName());
                System.out.println("1: Search Hotel Room");
                System.out.println("2: Reserve Hotel Room");
                System.out.println("3: View My Reservation Details");
                System.out.println("4: View All My Reservations");
                System.out.println("5: Logout ");
            } else {
                System.out.println("1: Guest Login");
                System.out.println("2: Register as Guest");
                System.out.println("3: Search Hotel");
                System.out.println("4: Exit ");
            }
            response = 0;

            while (response < 1 || response > 5) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (currentGuestEntity == null && response == 1) {
                    doGuestLogin();
                } else if (currentGuestEntity == null && response == 2) {
                    doRegisterAsGuest();
                } else if (currentGuestEntity == null && response == 3) {
                    doSearchHotelRoom();
                } else if (currentGuestEntity == null && response == 4) {
                    break;
                } else if (currentGuestEntity != null && response == 1) {
                    doSearchHotelRoom();
                } else if (currentGuestEntity != null && response == 2) {
                    doReserveHotelRoom();
                } else if (currentGuestEntity != null && response == 3) {
                    doViewMyReservationDetails();
                } else if (currentGuestEntity != null && response == 4) {
                    doViewAllMyReservations();
                } else if (currentGuestEntity != null && response == 5) {
                    currentGuestEntity = null;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (currentGuestEntity != null && response == 5) {
                break;
            } else if (currentGuestEntity == null && response == 4) {
                break;
            }
        }
    }

    private void doGuestLogin() {
        Scanner scanner = new Scanner(System.in);
        String username = "";
        String password = "";

        System.out.println("*** Merlion Reservation System :: Login ***\n");
        System.out.print("Enter username> ");
        username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();

        try {
            currentGuestEntity = guestEntitySessionBeanRemote.guestLogin(username, password);
        } catch (InvalidLoginCredentialException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private void doRegisterAsGuest() {
        GuestEntity guestEntity = new GuestEntity();
        Scanner sc = new Scanner(System.in);

        System.out.println("*** Merlion Reservation System :: Register As Guest ***\n");

        System.out.println("Enter First Name");
        guestEntity.setFirstName(sc.nextLine().trim());

        System.out.println("Enter Last Name");
        guestEntity.setLastName(sc.nextLine().trim());

        System.out.println("Enter Passport Number");
        guestEntity.setPassportNumber(sc.nextLine().trim());

        System.out.println("Enter email");
        guestEntity.setEmail(sc.nextLine().trim());

        System.out.println("Enter mobile number");
        guestEntity.setMobileNumber(sc.nextLine().trim());

        String username;

        System.out.println("Enter Username: ");
        username = sc.nextLine().trim();

        try {
            guestEntitySessionBeanRemote.retrieveGuestByUsername(username);
            System.out.println("Username Already Exists, choose Another Username!");
        } catch (GuestNotFoundException ex) {
            System.out.println("Welcome to Merlion Reservation System!");
        }
        guestEntity.setUsername(username);

        System.out.println("Enter Password: ");
        String password = sc.nextLine().trim();
        guestEntity.setPassword(password);
        guestEntity.setRegistered(Boolean.TRUE);

        currentGuestEntity = guestEntitySessionBeanRemote.registerAsGuest(guestEntity);
        System.out.println("New Guest Registered with ID: " + currentGuestEntity.getGuestId());

    }

    private void doSearchHotelRoom() {
        Scanner sc = new Scanner(System.in);

        System.out.println("*** Merlion Reservation System :: Search Room  ***\n");

        System.out.println("Enter the check in Date (dd/mm/yyyy): ");
        String sDate = sc.nextLine().trim();
        LocalDate checkinDate = LocalDate.parse(sDate, formatter);

        System.out.println("Enter the check out Date (dd/MM/yyyy): ");
        sDate = sc.nextLine().trim();
        LocalDate checkoutDate = LocalDate.parse(sDate, formatter);

        int numRooms;

        while (true) {
            System.out.println("Enter the number of rooms that you want to book: ");
            numRooms = sc.nextInt();
            if (numRooms > 0) {
                break;
            } else {
                System.err.println("The number of rooms has to be greater than 0!");
            }
        }

        try {
            List<String> availableRooms = searchSessionBeanRemote.searchAvailableRoomTypesWalkIn(checkinDate, checkoutDate, numRooms);
            System.out.printf("%20s%20s\n", "Room Type", "Room Rate");

            int i = 0;
            while (i < availableRooms.size()) {
                System.out.printf("%20s%20s\n", availableRooms.get(i++), availableRooms.get(i++));
            }
        } catch (NoRoomTypeAvailableException | RoomTypeCannotBeFoundException ex) {
            System.out.println("Error occured: " + ex.getMessage());
        }
        System.out.println("");
    }

    private void doReserveHotelRoom() {
        Scanner sc = new Scanner(System.in);

        System.out.println("*** Merlion Reservation System :: Reserve Room  ***\n");

        System.out.println("Enter the check in Date (dd/mm/yyyy): ");
        String sDate = sc.nextLine().trim();
        LocalDate checkinDate = LocalDate.parse(sDate, formatter);

        System.out.println("Enter the check out Date (dd/MM/yyyy): ");
        sDate = sc.nextLine().trim();
        LocalDate checkoutDate = LocalDate.parse(sDate, formatter);

        int numRooms;

        while (true) {
            System.out.println("Enter the number of rooms that you want to book: ");
            numRooms = sc.nextInt();
            if (numRooms > 0) {
                break;
            } else {
                System.out.println("The number of rooms has to be greater than 0!");
            }
        }

        RoomTypeEntity roomTypeEntity;
        try {
            List<String> availableRooms = searchSessionBeanRemote.searchAvailableRoomTypesWalkIn(checkinDate, checkoutDate, numRooms);
            System.out.printf("%2s%20s%20s\n", "#", "Room Type", "Room Rate");

            int i = 0, j = 1;
            while (i < availableRooms.size()) {
                System.out.printf("%2s%20s%20s\n", j++, availableRooms.get(i++), availableRooms.get(i++));
            }

            System.out.println("Enter the # of the Room Type you wish to book: ");
            int input = sc.nextInt();
            while (true) {
                if (input > 0 && input < j) {
                    roomTypeEntity = roomTypeEntitySessionBeanRemote.retrieveRoomTypeByName(availableRooms.get((input - 1 * 2)));
                    break;
                } else {
                    System.out.println("Invalid choice try again!");
                }
            }

            int numAdults;
            while (true) {
                System.out.println("Enter the number of Adults: ");
                numAdults = sc.nextInt();
                if (numAdults > 0) {
                    break;
                } else {
                    System.out.println("The number of Adults has to be greater than 0!");
                }
            }

            ReservationEntity reservationEntity = new ReservationEntity(checkinDate, checkoutDate, numAdults, numRooms, roomTypeEntity, currentGuestEntity);

            reservationEntity = reservationEntitySessionBeanRemote.createNewGuestReservation(reservationEntity, currentGuestEntity.getGuestId());
            System.out.println("Reservation Created Successfully: Reservation ID: " + reservationEntity.getReservationId());

        } catch (NoRoomTypeAvailableException | RoomTypeCannotBeFoundException | GuestNotFoundException ex) {
            System.out.println("Error Occured: " + ex.getMessage());
        }
    }

    private void doViewMyReservationDetails() {
        Scanner sc = new Scanner(System.in);

        System.out.println("*** Merlion Reservation System :: View My Reservation Details***\n");

        System.out.println("Enter the Reservation ID: ");
        Long reservationId = sc.nextLong();

        try {
            ReservationEntity reservationEntity = reservationEntitySessionBeanRemote.retrieveReservationById(reservationId);
            System.out.printf("%8s%20s%20s%20s%20s\n", "Reservation ID", "RoomType Name", "Checkin Date", "Checkout Date", "# of Rooms");
            System.out.printf("%8s%20s%20s%20s%20s\n", reservationEntity.getReservationId(), reservationEntity.getRoomType().getName(), reservationEntity.getCheckInDate().toString(), reservationEntity.getCheckOutDate().toString(), reservationEntity.getNumOfRooms());
            System.out.println("");
        } catch (ReservationCannotBeFoundException ex) {
            System.out.println("Error Occured: " + ex.getMessage());
        }
    }

    private void doViewAllMyReservations() {

        System.out.println("*** Merlion Reservation System :: View All My Reservations  ***\n");

        try {
            List<ReservationEntity> reservationEntities = reservationEntitySessionBeanRemote.retrieveAllReservationsByGuestId(currentGuestEntity.getGuestId());
            System.out.printf("%8s%20s%20s%20s%20s\n", "Reservation ID", "RoomType Name", "Checkin Date", "Checkout Date", "# of Rooms");
            reservationEntities.forEach(reservationEntity -> {
                System.out.printf("%8s%20s%20s%20s%20s\n", reservationEntity.getReservationId(), reservationEntity.getRoomType().getName(), reservationEntity.getCheckInDate().toString(), reservationEntity.getCheckOutDate().toString(), reservationEntity.getNumOfRooms());
            });
            System.out.println("");
        } catch (GuestNotFoundException ex) {
            System.out.println("Error Occured: " + ex.getMessage());
        }
    }
}
