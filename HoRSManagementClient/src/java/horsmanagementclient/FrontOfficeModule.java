package horsmanagementclient;

import ejb.session.stateless.EjbHorsTimerSessionBeanRemote;
import ejb.session.stateless.GuestEntitySessionBeanRemote;
import ejb.session.stateless.ReservationEntitySessionBeanRemote;
import ejb.session.stateless.RoomEntitySessionBeanRemote;
import ejb.session.stateless.RoomTypeEntitySessionBeanRemote;
import ejb.session.stateless.SearchSessionBeanRemote;
import entity.EmployeeEntity;
import entity.GuestEntity;
import entity.ReservationEntity;
import entity.RoomEntity;
import entity.RoomTypeEntity;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import util.enumeration.EmployeeAccessRightEnum;
import util.exception.GuestNotFoundException;
import util.exception.InvalidAccessRightException;
import util.exception.NoAllocationExceptionReportException;
import util.exception.NoRoomAllocationException;
import util.exception.NoRoomTypeAvailableException;
import util.exception.RoomTypeCannotBeFoundException;

public class FrontOfficeModule {

    private ReservationEntitySessionBeanRemote reservationEntitySessionBeanRemote;
    private SearchSessionBeanRemote searchSessionBeanRemote;
    private RoomTypeEntitySessionBeanRemote roomTypeEntitySessionBeanRemote;
    private GuestEntitySessionBeanRemote guestEntitySessionBeanRemote;
    private EjbHorsTimerSessionBeanRemote ejbHorsTimerSessionBeanRemote;
    private RoomEntitySessionBeanRemote roomEntitySessionBeanRemote;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private EmployeeEntity currentEmployeeEntity;

    public FrontOfficeModule() {
    }

    public FrontOfficeModule(ReservationEntitySessionBeanRemote reservationEntitySessionBeanRemote, SearchSessionBeanRemote searchSessionBeanRemote, RoomTypeEntitySessionBeanRemote roomTypeEntitySessionBeanRemote, GuestEntitySessionBeanRemote guestEntitySessionBeanRemote, EmployeeEntity currentEmployeeEntity, EjbHorsTimerSessionBeanRemote ejbHorsTimerSessionBeanRemote, RoomEntitySessionBeanRemote roomEntitySessionBeanRemote) {
        this.reservationEntitySessionBeanRemote = reservationEntitySessionBeanRemote;
        this.searchSessionBeanRemote = searchSessionBeanRemote;
        this.roomTypeEntitySessionBeanRemote = roomTypeEntitySessionBeanRemote;
        this.guestEntitySessionBeanRemote = guestEntitySessionBeanRemote;
        this.currentEmployeeEntity = currentEmployeeEntity;
        this.ejbHorsTimerSessionBeanRemote = ejbHorsTimerSessionBeanRemote;
        this.roomEntitySessionBeanRemote = roomEntitySessionBeanRemote;
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
            System.out.println("-----------------------");
            System.out.println("2: Check-In Guest");
            System.out.println("3: Check-Out Guest");
            System.out.println("4: Manually Allocate Room by Date");
            System.out.println("-----------------------");
            System.out.println("5: Retrieve Current Day Allocation Exception report");
            System.out.println("6: Retrieve Allocation Exception report by Date");
            System.out.println("7: Back\n");
            response = 0;

            while (response < 1 || response > 7) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    doWalkInRoomSearch();
                } else if (response == 2) {
                    doCheckInGuest();
                } else if (response == 3) {
                    doCheckOutGuest();
                } else if (response == 4) {
                    doManualRoomAllocation();
                } else if (response == 5) {
                    doViewCurrentAllocationExceptionReport();
                } else if (response == 6) {
                    doViewAllocationExceptionReport();
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

        int totalDays = 0;

        for (LocalDate dailyDate = checkinDate; dailyDate.isBefore(checkoutDate); dailyDate = dailyDate.plusDays(1)) {
            totalDays += 1;
        }

        String totalDaysInString = new String();
        BigDecimal totalDaysInBd = new BigDecimal(totalDaysInString.valueOf(totalDays));
        System.out.println("\nTotal Nights of Stay = " + totalDaysInBd.toString() + "\n");

        int numRooms;

        while (true) {
            System.out.println("Enter the number of rooms that you want to book: ");
            System.out.print(">");
            numRooms = sc.nextInt();
            if (numRooms > 0) {
                break;
            } else {
                System.out.println("The number of rooms has to be greater than 0!" + "\n");
            }
        }

        int numAdults;

        while (true) {
            System.out.println("Enter the number of adults: ");
            System.out.print(">");
            numAdults = sc.nextInt();
            if (numAdults > 0) {
                break;
            } else {
                System.err.println("The number of adults has to be greater than 0!" + "\n");
            }
        }

        try {
            List<String> availableRooms = searchSessionBeanRemote.searchAvailableRoomTypesWalkIn(checkinDate, checkoutDate, numRooms, numAdults);
            System.out.printf("%2s%20s%20s\n", "#", "Room Type", "Room Rate");

            int i = 0, j = 1;
            while (i < availableRooms.size()) {
                System.out.printf("%2s%20s%20s\n", j++, availableRooms.get(i++), availableRooms.get(i++));
            }

            while (true) {
                int response;
                System.out.println("1. Reserve Room");
                System.out.println("2. Back");
                System.out.print(">");
                response = 0;

                response = sc.nextInt();
                System.err.println("");
                while (response > 0 && response <= 2) {
                    if (response == 1) {
                        doWalkInReserveRoom(availableRooms, checkinDate, checkoutDate, numRooms, numAdults, totalDaysInBd);
                        break;
                    } else if (response == 2) {
                        break;
                    } else {
                        System.out.println("Invalid Choice Please try again!" + "\n");
                    }
                }
                if (response == 2) {
                    break;
                }

            }
        } catch (NoRoomTypeAvailableException | RoomTypeCannotBeFoundException ex) {
            System.out.println("Error occured: " + ex.getMessage());
        }
        System.out.println("");
    }

    private void doWalkInReserveRoom(List<String> availableRooms, LocalDate checkinDate, LocalDate checkoutDate, int numRooms, int numAdults, BigDecimal totalDaysInBd) {

        Scanner sc = new Scanner(System.in);
        RoomTypeEntity roomTypeEntity = new RoomTypeEntity();
        String dailyRateOfChosenRoom = "";
        try {
            while (true) {

                System.out.print("Enter the # of the Room Type you wish to book from the above list: ");
                System.out.print(">");
                int input = sc.nextInt();

                if (input > 0 && input <= availableRooms.size() / 2) {
                    roomTypeEntity = roomTypeEntitySessionBeanRemote.retrieveRoomTypeByName(availableRooms.get((input - 1) * 2));
                    dailyRateOfChosenRoom = availableRooms.get(((input - 1) * 2) + 1);
                    break;
                } else {
                    System.out.println("Invalid choice try again!" + "\n");
                }
            }

            BigDecimal dailyRateofChosenRoomInBd = new BigDecimal(dailyRateOfChosenRoom);
            BigDecimal totalRoomsInBd = new BigDecimal(numRooms);
            BigDecimal totalAmountInBd = dailyRateofChosenRoomInBd.multiply(totalDaysInBd).multiply(totalRoomsInBd);
            System.out.println("Total Amount for " + roomTypeEntity.getName() + " from "
                    + checkinDate.toString() + " to " + checkoutDate.toString() + " is SGD"
                    + totalAmountInBd.toString());

            ReservationEntity reservationEntity = new ReservationEntity(checkinDate, checkoutDate, numAdults, numRooms, roomTypeEntity);

            reservationEntity.setTotalAmount(totalAmountInBd);

            String confirmation = "";
            sc.nextLine();
            System.out.println("Confirm Reservation? (Enter 'Y' to confirm)> ");
            confirmation = sc.nextLine().trim().toUpperCase();
            try {
                if (confirmation.equals("Y")) {
                    GuestEntity guestEntity;
                    sc.nextLine();
                    System.out.println("Enter the Passport number of the guest: ");
                    String passportNumber = sc.nextLine().trim();

                    try {
                        guestEntity = guestEntitySessionBeanRemote.retrieveGuestByPassportNumber(passportNumber);
                    } catch (GuestNotFoundException ex) {

                        guestEntity = new GuestEntity();
                        System.out.println("Enter First Name");
                        guestEntity.setFirstName(sc.nextLine().trim());
                        System.out.println("Enter Last Name");
                        guestEntity.setLastName(sc.nextLine().trim());
                        System.out.println("Enter email");
                        guestEntity.setEmail(sc.nextLine().trim());
                        System.out.println("Enter mobile number");
                        guestEntity.setMobileNumber(sc.nextLine().trim());
                        guestEntity.setPassportNumber(passportNumber);

                        guestEntity = guestEntitySessionBeanRemote.registerAsGuest(guestEntity);
                    }

                    reservationEntity.setGuest(guestEntity);
                    reservationEntity = reservationEntitySessionBeanRemote.createNewGuestReservation(reservationEntity, guestEntity.getGuestId());
                    System.out.println("Reservation Created Successfully: Reservation ID: " + reservationEntity.getReservationId());
                } else {
                    System.out.println("Reservation cancelled!");
                }

            } catch (GuestNotFoundException ex) {
                System.out.println("Error occured: " + ex.getMessage() + "\n");
            }
        } catch (RoomTypeCannotBeFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void doCheckInGuest() {
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter the Reservation ID: ");
        Long reservationId = sc.nextLong();

        try {
            List<RoomEntity> rooms = roomEntitySessionBeanRemote.retrieveRoomsByReservationId(reservationId);

            System.out.println("Checked In Successfully: Room number for your booking is: ");
            for (RoomEntity r : rooms) {
                System.out.println(r.getRoomNumber());
            }
        } catch (NoRoomAllocationException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void doCheckOutGuest() {
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter the Reservation ID: ");
        Long reservationId = sc.nextLong();

        try {
            List<RoomEntity> rooms = roomEntitySessionBeanRemote.retrieveRoomsByReservationId(reservationId);

            System.out.println("Checked Out Successfully from Room Number:");
            for (RoomEntity r : rooms) {
                System.out.println(r.getRoomNumber());
            }
        } catch (NoRoomAllocationException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void doViewCurrentAllocationExceptionReport() {

        try {
            List<String> allocationReport = ejbHorsTimerSessionBeanRemote.viewCurrentDayAllocationExceptionReport().getExceptionDetails();
            System.out.println("Allocation Exception Report for " + LocalDate.now().toString());
            for (String s : allocationReport) {
                System.out.println(s);
            }
        } catch (NoAllocationExceptionReportException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void doViewAllocationExceptionReport() {
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter Date> ");
        String sdate = sc.nextLine().trim();

        try {
            LocalDate date = LocalDate.parse(sdate, formatter);
            List<String> allocationReport = ejbHorsTimerSessionBeanRemote.viewSpecificDayAllocationExceptionReport(date).getExceptionDetails();
            System.out.println("Allocation Exception Report for " + date.toString());
            for (String s : allocationReport) {
                System.out.println(s);
            }
        } catch (NoAllocationExceptionReportException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void doManualRoomAllocation() {

        Scanner sc = new Scanner(System.in);

        System.out.println("*** Merlion Management System :: Front Office Module :: Do manual room allocation  ***\n");

        System.out.println("Enter the Date to allocate (dd/mm/yyyy): ");
        String sDate = sc.nextLine().trim();
        LocalDate date = LocalDate.parse(sDate, formatter);

        ejbHorsTimerSessionBeanRemote.manualRoomAllocation(date);
        System.out.println("Room has been successfully allocated for reservations which check in on " + date.toString());
    }

}
