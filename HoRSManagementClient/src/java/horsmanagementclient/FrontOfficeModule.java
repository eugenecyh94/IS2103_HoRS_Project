package horsmanagementclient;

import ejb.session.stateless.EjbHorsTimerSessionBeanRemote;
import ejb.session.stateless.GuestEntitySessionBeanRemote;
import ejb.session.stateless.ReservationEntitySessionBeanRemote;
import ejb.session.stateless.RoomTypeEntitySessionBeanRemote;
import ejb.session.stateless.SearchSessionBeanRemote;
import entity.EmployeeEntity;
import entity.GuestEntity;
import entity.ReservationEntity;
import entity.RoomTypeEntity;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import util.enumeration.EmployeeAccessRightEnum;
import util.exception.GuestNotFoundException;
import util.exception.InvalidAccessRightException;
import util.exception.NoAllocationExceptionReportException;
import util.exception.NoRoomTypeAvailableException;
import util.exception.RoomTypeCannotBeFoundException;

public class FrontOfficeModule {

    private ReservationEntitySessionBeanRemote reservationEntitySessionBeanRemote;
    private SearchSessionBeanRemote searchSessionBeanRemote;
    private RoomTypeEntitySessionBeanRemote roomTypeEntitySessionBeanRemote;
    private GuestEntitySessionBeanRemote guestEntitySessionBeanRemote;
    private EjbHorsTimerSessionBeanRemote ejbHorsTimerSessionBeanRemote;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private EmployeeEntity currentEmployeeEntity;

    public FrontOfficeModule() {
    }

    public FrontOfficeModule(ReservationEntitySessionBeanRemote reservationEntitySessionBeanRemote, SearchSessionBeanRemote searchSessionBeanRemote, RoomTypeEntitySessionBeanRemote roomTypeEntitySessionBeanRemote, GuestEntitySessionBeanRemote guestEntitySessionBeanRemote, EmployeeEntity currentEmployeeEntity, EjbHorsTimerSessionBeanRemote ejbHorsTimerSessionBeanRemote) {
        this.reservationEntitySessionBeanRemote = reservationEntitySessionBeanRemote;
        this.searchSessionBeanRemote = searchSessionBeanRemote;
        this.roomTypeEntitySessionBeanRemote = roomTypeEntitySessionBeanRemote;
        this.guestEntitySessionBeanRemote = guestEntitySessionBeanRemote;
        this.currentEmployeeEntity = currentEmployeeEntity;
        this.ejbHorsTimerSessionBeanRemote = ejbHorsTimerSessionBeanRemote;
    }

    public void menuFrontOffice() throws InvalidAccessRightException {

        if (currentEmployeeEntity.getAccessRightEnum() != EmployeeAccessRightEnum.GUESTRELATIONSOFFICER) {
            throw new InvalidAccessRightException("You don't have GUEST RELATIONS OFFICER rights to access the Front Office Module.");
        }

        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** Merlion Management System :: Front Office Module ***\n");
            System.out.println("1: Walk-In Room Search");
            System.out.println("2: Walk-In Room Reservation");
            System.out.println("3: Check-In Guest");
            System.out.println("4: Check-Out Guest");
            System.out.println("5: Retrieve Current Day Allocation report");
            System.out.println("6: Retrieve Day Allocation report");
            System.out.println("7: Back\n");
            response = 0;

            while (response < 1 || response > 7) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    doWalkInRoomSearch();
                } else if (response == 2) {
                    doWalkInReserveRoom();
                } else if (response == 3) {
                    doCheckInGuest();
                } else if (response == 4) {
                    doCheckOutGuest();
                } else if (response == 5) {
                    doViewCurrentAllocationReport();
                } else if (response == 6) {
                    doViewAllocationReport();
                } else if (response == 7) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 7) {
                break;
            }
        }
    }

     

    private void doWalkInRoomSearch() {
        Scanner sc = new Scanner(System.in);

        System.out.println("*** Merlion Management System :: Front Office Module :: Walk-In Search Room  ***\n");

        System.out.println("Enter the check in Date (dd/mm/yyyy): ");
        String sDate = sc.nextLine().trim();
        LocalDate checkinDate = LocalDate.parse(sDate, formatter);

        LocalDate checkoutDate;

        while (true) {
            System.out.println("Enter the check out Date (dd/MM/yyyy): ");
            sDate = sc.nextLine().trim();
            checkoutDate = LocalDate.parse(sDate, formatter);

            if (checkoutDate.isBefore(checkinDate)) {
                System.out.println("\nCheckout Date entered is wrong! Checkout Date must be after Check In Date!");
            } else {
                break;
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
        try {
            List<String> availableRooms = searchSessionBeanRemote.searchAvailableRoomTypesWalkIn(checkinDate, checkoutDate, numRooms, numAdults);
            System.out.printf("%20s%30s\n", "Room Type", "Room Rate");

            int i = 0;
            while (i < availableRooms.size()) {
                System.out.printf("%20s%30s\n", availableRooms.get(i++), availableRooms.get(i++));
            }
        } catch (NoRoomTypeAvailableException | RoomTypeCannotBeFoundException ex) {
            System.out.println("Error occured: " + ex.getMessage());
        }
        System.out.println("");
    }

    private void doWalkInReserveRoom() {
        Scanner sc = new Scanner(System.in);

        System.out.println("*** Merlion Management System :: Front Office Module :: Walk-In Reserve Room  ***\n");

        System.out.println("Enter the check in Date (dd/mm/yyyy): ");
        String sDate = sc.nextLine().trim();
        LocalDate checkinDate = LocalDate.parse(sDate, formatter);

        LocalDate checkoutDate;

        while (true) {
            System.out.println("Enter the check out Date (dd/MM/yyyy): ");
            sDate = sc.nextLine().trim();
            checkoutDate = LocalDate.parse(sDate, formatter);

            if (checkoutDate.isBefore(checkinDate)) {
                System.out.println("\nCheckout Date entered is wrong! Checkout Date must be after Check In Date!");
            } else {
                break;
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

        RoomTypeEntity roomTypeEntity;
        try {
            List<String> availableRooms = searchSessionBeanRemote.searchAvailableRoomTypesWalkIn(checkinDate, checkoutDate, numRooms, numAdults);
            System.out.printf("%2s%30s%20s\n", "#", "Room Type", "Room Rate");

            int i = 0, j = 1;
            while (i < availableRooms.size()) {
                System.out.printf("%2s%30s%20s\n", j++, availableRooms.get(i++), availableRooms.get(i++));
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

            GuestEntity guestEntity;

            System.out.println("Enter the Passport number of the guest: ");
            String passportNumber = sc.nextLine();

            try {
                guestEntity = guestEntitySessionBeanRemote.retrieveGuestByPassportNumber(passportNumber);
            } catch (GuestNotFoundException ex) {

                guestEntity = new GuestEntity();
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
            }

            ReservationEntity reservationEntity = new ReservationEntity(checkinDate, checkoutDate, numAdults, numRooms, roomTypeEntity, guestEntity);

            reservationEntity = reservationEntitySessionBeanRemote.createNewGuestReservation(reservationEntity, guestEntity.getGuestId());
            System.out.println("Reservation Created Successfully: Reservation ID: " + reservationEntity.getReservationId());

        } catch (NoRoomTypeAvailableException | RoomTypeCannotBeFoundException | GuestNotFoundException ex) {
            System.out.println("Error occured: " + ex.getMessage());
        }

    }

    private void doCheckInGuest() {
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter the Booking ID: ");
        Long bookingId = sc.nextLong();

        System.out.println("Checked In Successfully: Room number for your booking is: ");
    }

    private void doCheckOutGuest() {
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter the Booking ID: ");
        Long bookingId = sc.nextLong();

        System.out.println("Checked Out Successfully");
    }
}
