package horsmanagementclient;

import ejb.session.stateless.EmployeeEntitySessionBeanRemote;
import entity.EmployeeEntity;
import java.util.Scanner;
import util.enumeration.EmployeeAccessRightEnum;
import util.exception.InvalidAccessRightException;
import ejb.session.stateless.RoomRateSessionBeanRemote;
import ejb.session.stateless.RoomTypeEntitySessionBeanRemote;
import entity.RoomRateEntity;
import entity.RoomTypeEntity;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import util.enumeration.RateTypeEnum;
import util.exception.RoomTypeCannotBeFoundException;

public class SalesManagementModule {

    private EmployeeEntitySessionBeanRemote employeeEntitySessionBeanRemote;
    private RoomTypeEntitySessionBeanRemote roomTypeEntitySessionBeanRemote;
    private RoomRateSessionBeanRemote roomRateSessionBeanRemote;

    private EmployeeEntity currentEmployeeEntity;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public SalesManagementModule() {
    }

    public SalesManagementModule(EmployeeEntitySessionBeanRemote employeeEntitySessionBeanRemote, EmployeeEntity currentEmployeeEntity) {
        this.employeeEntitySessionBeanRemote = employeeEntitySessionBeanRemote;
        this.currentEmployeeEntity = currentEmployeeEntity;
    }

    public void menuSalesManagement() throws InvalidAccessRightException {

        if (currentEmployeeEntity.getAccessRightEnum() != EmployeeAccessRightEnum.SALESMANAGER) {
            throw new InvalidAccessRightException("You don't have SALES MANAGER rights to access the Sales Management Module.");
        }

        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** Merlion Management System :: Sales Management***\n");
            System.out.println("1: Create New Room Rate");
            System.out.println("2: View Room Rate Details");
            System.out.println("3: View All Room Rates");
            System.out.println("-----------------------");
            System.out.println("4: Back\n");
            response = 0;

            while (response < 1 || response > 11) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    doCreateNewRoomRate();
                } else if (response == 2) {
                    doViewRoomRate();
                } else if (response == 3) {
                    doViewAllRoomRates();
                } else if (response == 4) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 6) {
                break;
            }
        }
    }

    private void doCreateNewRoomRate() {
        System.out.println("*** Merlion Management System :: Sales Management :: Create Room Rate ***\n");

        RoomRateEntity roomRateEntity = new RoomRateEntity();
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter the Room Rate Name: ");
        roomRateEntity.setRateName(sc.nextLine().trim());

        System.out.println("*** RoomTypes ***\n");

        List<RoomTypeEntity> roomTypeEntities = roomTypeEntitySessionBeanRemote.retrieveAllRoomTypes();
        System.out.printf("%8s%20s%20s%20s%20s\n", "RoomType ID", "RoomType Name", "RoomType Bed Size", "Room size", "Capacity");

        roomTypeEntities.forEach(roomTypeEntity -> {
            System.out.printf("%8s%20s%20s%20s%20s\n", roomTypeEntity.getRoomTypeId().toString(), roomTypeEntity.getName(), roomTypeEntity.getBedSize().toString(), roomTypeEntity.getRoomSize(), roomTypeEntity.getCapacity());
        });

        System.out.println("");

        System.out.println("Enter the Room Type ID: ");
        while (true) {
            try {
                Long input = sc.nextLong();
                RoomTypeEntity roomTypeEntity = roomTypeEntitySessionBeanRemote.retrieveRoomTypeById(input);
                roomRateEntity.setRoomType(roomTypeEntity);
                break;
            } catch (RoomTypeCannotBeFoundException ex) {
                System.out.println("Invalid Choice: " + ex.getMessage());
            }
        }

        while (true) {
            System.out.print("1: Select Room Rate Type 1: PUBLISHED, 2: NORMAL, 3: PEAK, 4: PROMOTION> ");
            Integer roomRateInt = sc.nextInt();

            if (roomRateInt >= 1 && roomRateInt <= 4) {
                roomRateEntity.setRateType(RateTypeEnum.values()[roomRateInt]);

                break;
            } else {
                System.out.println("Invalid option, please try again!\n");
            }
        }

        System.out.println("Enter the Room Rate per night: ");
        roomRateEntity.setRate(sc.nextBigDecimal());

        if (roomRateEntity.getRateType().equals(RateTypeEnum.PEAK) || roomRateEntity.getRateType().equals(RateTypeEnum.PROMOTION)) {
            while (true) {

                System.out.println("Enter Start Date (dd/MM/yyyy): ");
                String sDate = sc.nextLine().trim();
                roomRateEntity.setStartDate(LocalDate.parse(sDate, formatter));
                System.out.println("Enter End Date (dd/MM/yyyy): ");
                sDate = sc.nextLine().trim();
                roomRateEntity.setStartDate(LocalDate.parse(sDate, formatter));

            }
        }

        roomRateEntity = roomRateSessionBeanRemote.createNewRoomRate(roomRateEntity);
        System.out.println("New Room Rate Created with ID : " + roomRateEntity.getRoomRateId());

    }

    private void doViewRoomRate() {
        Scanner sc = new Scanner(System.in);
        Integer response = 0;

        System.out.println("*** Merlion Management System :: Sales Management :: View Room Rate Details ***\n");
        System.out.print("Enter Room Rate ID> ");
        Long roomRateId = sc.nextLong();

        RoomRateEntity roomRateEntity = roomRateSessionBeanRemote.retrieveRoomRateById(roomRateId);
        if (roomRateEntity != null) {
            System.out.printf("%8s%20s%20s%20s%20s\n", "RoomRate ID", "RoomRate Name", "RoomRate Type", "Per Night", "Start Date", "End Date");
            System.out.printf("%8s%20s%20s%20s%20s\n", roomRateEntity.getRoomRateId(), roomRateEntity.getRateName(), roomRateEntity.getRateType().toString(), roomRateEntity.getRate(), roomRateEntity.getStartDate().toString(), roomRateEntity.getEndDate().toString());
            System.out.println("------------------------");
            System.out.println("1: Update RomRate");
            System.out.println("2: Delete RomRate");
            System.out.println("3: Back\n");
            System.out.print("> ");
            response = sc.nextInt();

            if (response == 1) {
                doUpdateRoomRate(roomRateEntity);
            } else if (response == 2) {
                doDeleteRoomRate(roomRateEntity);
            }
        } else {
            System.out.println("No Room Rate with the ID found!");
        }

    }

    private void doViewAllRoomRates() {
        System.out.println("*** Merlion Management System :: Sales Management :: View All RoomRates ***\n");

        List<RoomRateEntity> roomRateEntities = roomRateSessionBeanRemote.retrieveAllRoomRates();
        System.out.printf("%8s%20s%20s%20s%20s\n", "RoomRate ID", "RoomRate Name", "RoomRate Type", "Per Night", "Start Date", "End Date");

        roomRateEntities.forEach(roomRateEntity -> {
            System.out.printf("%8s%20s%20s%20s%20s\n", roomRateEntity.getRoomRateId(), roomRateEntity.getRateName(), roomRateEntity.getRateType().toString(), roomRateEntity.getRate(), roomRateEntity.getStartDate().toString(), roomRateEntity.getEndDate().toString());
        });

        System.out.println("");
    }

    private void doDeleteRoomRate(RoomRateEntity roomRateEntity) {
        Scanner sc = new Scanner(System.in);
        String input;

        System.out.println("*** Merlion Management System :: Sales Management :: View RoomRate Details :: Delete RoomRate ***\n");
        System.out.printf("Confirm Delete RoomRate %s (RoomRate ID: %d) (Enter 'Y' to Delete)> ", roomRateEntity.getRateName(), roomRateEntity.getRoomRateId());
        input = sc.nextLine().trim();

        if (input.equals("Y")) {

            roomRateSessionBeanRemote.deleteRoomRate(roomRateEntity.getRoomRateId());
            System.out.println("RoomRate deleted successfully!\n");

        }
    }

    private void doUpdateRoomRate(RoomRateEntity roomRateEntity) {

        Scanner sc = new Scanner(System.in);
        System.out.println("*** Merlion Management System :: Sales Management :: View RoomRate Details :: Update RoomRate ***\n");

        System.out.println("Enter the Room Rate Name (blank if no change) : ");
        String newName = sc.nextLine().trim();
        if (newName.length() > 0) {
            roomRateEntity.setRateName(newName);
        }

        System.out.println("*** RoomTypes ***\n");

        List<RoomTypeEntity> roomTypeEntities = roomTypeEntitySessionBeanRemote.retrieveAllRoomTypes();
        System.out.printf("%8s%20s%20s%20s%20s\n", "RoomType ID", "RoomType Name", "RoomType Bed Size", "Room size", "Capacity");

        roomTypeEntities.forEach(roomTypeEntity -> {
            System.out.printf("%8s%20s%20s%20s%20s\n", roomTypeEntity.getRoomTypeId().toString(), roomTypeEntity.getName(), roomTypeEntity.getBedSize().toString(), roomTypeEntity.getRoomSize(), roomTypeEntity.getCapacity());
        });

        System.out.println("");

        System.out.println("Enter the Room Type ID (0 if no change): ");
        Long newRoomTypeId = sc.nextLong();
        if (newRoomTypeId > 0) {
            while (true) {
                try {
                    RoomTypeEntity roomTypeEntity = roomTypeEntitySessionBeanRemote.retrieveRoomTypeById(newRoomTypeId);
                    roomRateEntity.setRoomType(roomTypeEntity);
                    break;
                } catch (RoomTypeCannotBeFoundException ex) {
                    System.out.println("Invalid Choice: " + ex.getMessage());
                }
            }
        }
        System.out.println("Change Room Rate Type? y/n");

        if (sc.nextLine().trim().equals('y')) {
            while (true) {
                System.out.print("1: Select Room Rate Type 1: PUBLISHED, 2: NORMAL, 3: PEAK, 4: PROMOTION> ");
                Integer roomRateInt = sc.nextInt();

                if (roomRateInt >= 1 && roomRateInt <= 4) {
                    roomRateEntity.setRateType(RateTypeEnum.values()[roomRateInt]);

                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
        }
        System.out.println("Change Room Rate per night? y/n");
        if (sc.nextLine().trim().equals('y')) {
            System.out.println("Enter the Room Rate per night : ");
            roomRateEntity.setRate(sc.nextBigDecimal());
        }

        if (roomRateEntity.getRateType().equals(RateTypeEnum.PEAK) || roomRateEntity.getRateType().equals(RateTypeEnum.PROMOTION)) {
            System.out.println("Change Room Rate Dates? y/n");
            if (sc.nextLine().trim().equals('y')) {
                while (true) {

                    System.out.println("Enter Start Date (dd/MM/yyyy): ");
                    String sDate = sc.nextLine().trim();
                    roomRateEntity.setStartDate(LocalDate.parse(sDate, formatter));
                    System.out.println("Enter End Date (dd/MM/yyyy): ");
                    sDate = sc.nextLine().trim();
                    roomRateEntity.setStartDate(LocalDate.parse(sDate, formatter));
                }
            }
        }

        roomRateSessionBeanRemote.updateRoomRate(roomRateEntity);
        System.out.println("Room Rate Updated Successfully!");

    }
}
