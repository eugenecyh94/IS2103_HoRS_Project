package horsmanagementclient;

import entity.EmployeeEntity;
import java.util.Scanner;
import util.enumeration.EmployeeAccessRightEnum;
import util.exception.InvalidAccessRightException;
import ejb.session.stateless.RoomEntitySessionBeanRemote;
import ejb.session.stateless.RoomTypeEntitySessionBeanRemote;
import entity.RoomEntity;
import entity.RoomTypeEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import util.enumeration.BedSizeEnum;
import util.exception.RoomCannotBeDeletedException;
import util.exception.RoomCannotBeFoundException;
import util.exception.RoomTypeCannotBeDeletedException;
import util.exception.RoomTypeCannotBeFoundException;

public class OperationsManagementModule {

    private RoomTypeEntitySessionBeanRemote roomTypeEntitySessionBeanRemote;
    private RoomEntitySessionBeanRemote roomEntitySessionBeanRemote;

    private EmployeeEntity currentEmployeeEntity;

    public OperationsManagementModule() {
    }

    public OperationsManagementModule(RoomTypeEntitySessionBeanRemote roomTypeEntitySessionBeanRemote, RoomEntitySessionBeanRemote roomEntitySessionBeanRemote, EmployeeEntity currentEmployeeEntity) {
        this.roomTypeEntitySessionBeanRemote = roomTypeEntitySessionBeanRemote;
        this.roomEntitySessionBeanRemote = roomEntitySessionBeanRemote;
        this.currentEmployeeEntity = currentEmployeeEntity;
    }

    public void menuOperationsManagement() throws InvalidAccessRightException {

        if (currentEmployeeEntity.getAccessRightEnum() != EmployeeAccessRightEnum.OPSMANAGER) {
            throw new InvalidAccessRightException("You don't have OPERATION MANAGER rights to access the Operations Management Module.");
        }

        Scanner sc = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** Merlion Management System :: Operations Management ***\n");
            System.out.println("1: Create New Room Type");
            System.out.println("2: View Room Type Details");
            System.out.println("3: View All Room Types");
            System.out.println("-----------------------");
            System.out.println("4: Create New Room");
            System.out.println("5: View All Rooms");
            System.out.println("6: View Room Details");
            System.out.println("-----------------------");
            System.out.println("7: View Room Allocation Exception Report");
            System.out.println("8: Back\n");
            response = 0;

            while (response < 1 || response > 8) {
                System.out.print("> ");

                response = sc.nextInt();

                if (response == 1) {
                    doCreateNewRoomType();
                } else if (response == 2) {
                    doViewRoomType();
                } else if (response == 3) {
                    doViewAllRoomTypes();
                } else if (response == 4) {
                    doCreateNewRoomEntity();
                } else if (response == 5) {
                    doViewAllRooms();
                } else if (response == 6) {
                    doViewRoomDetails();
                } else if (response == 7) {
                    doViewExceptionReport();
                } else if (response == 8) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 8) {
                break;
            }
        }
    }

    private void doCreateNewRoomType() {

        RoomTypeEntity roomTypeEntity = new RoomTypeEntity();
        Scanner sc = new Scanner(System.in);

        String name = "";
        while (name.length() < 1) {
            System.out.println("Enter the Room Type Name: ");
            name = sc.nextLine().trim();
            if (name.length() > 0) {
                roomTypeEntity.setName(name);
            } else {
                System.out.println("Invalid option, please try again!\n");
            }
        }

        String description = "";
        while (description.length() < 1) {
            System.out.println("Enter the Room Type Description: ");
            description = sc.nextLine().trim();
            if (description.length() > 0) {
                roomTypeEntity.setDescription(description);
            } else {
                System.out.println("Invalid option, please try again!\n");
            }
        }

        String size = "";
        while (size.length() < 1) {
            System.out.println("Enter the Room Type Size: ");
            size = sc.nextLine().trim();
            if (size.length() > 0) {
                roomTypeEntity.setRoomSize(size);
            } else {
                System.out.println("Invalid option, please try again!\n");
            }
        }

        while (true) {
            System.out.print("1: Select Bed Size 1: SINGLE, 2: SUPERSINGLE, 3: QUEEN, 4: KING > ");
            Integer bedSizeInt = sc.nextInt();
            sc.nextLine();

            if (bedSizeInt >= 1 && bedSizeInt <= 4) {
                roomTypeEntity.setBedSize(BedSizeEnum.values()[bedSizeInt - 1]);
                break;
            } else {
                System.out.println("Invalid option, please try again!\n");
            }
        }

        System.out.println("Enter the Room Type Capacity: ");
        roomTypeEntity.setCapacity(sc.nextInt());
        sc.nextLine();

        List<String> roomAmenities = new ArrayList<>();

        while (true) {
            System.out.println("Enter Room Type Amenities: ");
            System.out.println("1: WIFI");
            System.out.println("2: BATHTUB");
            System.out.println("3: BATHROOMAMENITIES");
            System.out.println("4: HAIRDRYER");
            System.out.println("5: LAUNDRYSERVICE");
            System.out.println("6: MINIBAR");
            System.out.println("7: LCDTV");
            System.out.println("8: TVSPEAKER");
            System.out.println("9: Done");

            Integer amenityInteger = sc.nextInt();

            if (amenityInteger >= 1 && amenityInteger <= 8) {
                switch (amenityInteger) {
                    case 1:
                        roomAmenities.add("WIFI");
                    case 2:
                        roomAmenities.add("BATHTUB");
                    case 3:
                        roomAmenities.add("BATHROOMAMENITIES");
                    case 4:
                        roomAmenities.add("HAIRDRYER");
                    case 5:
                        roomAmenities.add("LAUNDRY SERVICE");
                    case 6:
                        roomAmenities.add("MINIBAR");
                    case 7:
                        roomAmenities.add("LCDTV");
                    case 8:
                        roomAmenities.add("TVSPEAKER");
                    case 9:
                        break;
                }
            } else if (amenityInteger == 9) {
                break;
            } else {
                System.out.println("Invalid option, please try again!\n");
            }
        }

        sc.nextLine();
        roomTypeEntity.setRoomAmenities(roomAmenities);

        String higherRoom = "";
        System.out.print("Enter next higher room type (leave blank and enter if no next higher room type)> ");
        higherRoom = sc.nextLine().trim();
        if (higherRoom.length() > 0) {
            roomTypeEntity.setNextHigherRoomType(higherRoom);
        } else {
            roomTypeEntity.setNextHigherRoomType("NONE");
        }

        RoomTypeEntity newRoomTypeEntity = roomTypeEntitySessionBeanRemote.createNewRoomType(roomTypeEntity);
        System.out.println("New Room Type Entity created Successfully with ID: " + newRoomTypeEntity.getRoomTypeId());
    }

    private void doViewRoomType() {
        Scanner sc = new Scanner(System.in);
        Integer response = 0;

        System.out.println("*** Merlion Management System :: Operations Management :: View RoomType Details ***\n");
        System.out.print("Enter RoomType ID: ");
        //String roomTypeName = sc.nextLine().trim();
        Long roomTypeId = sc.nextLong();
        try {
            RoomTypeEntity roomTypeEntity = roomTypeEntitySessionBeanRemote.retrieveRoomTypeById(roomTypeId);
            System.out.printf("%8s%30s%20s%20s%20s\n", "RoomType ID", "RoomType Name", "RoomType Bed Size", "Room size", "Capacity");
            System.out.printf("%8s%30s%20s%20s%20s\n", roomTypeEntity.getRoomTypeId().toString(), roomTypeEntity.getName(), roomTypeEntity.getBedSize().toString(), roomTypeEntity.getRoomSize(), roomTypeEntity.getCapacity());
            System.out.println("------------------------");
            System.out.println("1: Update RoomType");
            System.out.println("2: Delete RoomType");
            System.out.println("3: Back\n");
            System.out.print("> ");
            response = sc.nextInt();

            if (response == 1) {
                doUpdateRoomType(roomTypeEntity);
            } else if (response == 2) {
                doDeleteRoomType(roomTypeEntity);
            }
        } catch (RoomTypeCannotBeFoundException ex) {
            System.out.println("An error has occurred while retrieving RoomType: " + ex.getMessage() + "\n");
        }
    }

    private void doUpdateRoomType(RoomTypeEntity roomTypeEntity) {
        Scanner sc = new Scanner(System.in);
        String input;

        System.out.println("*** Merlion Management System :: Operations Management :: View RoomType Details :: Update RoomType ***\n");
        System.out.print("Enter RoomType Name (blank if no change)> ");
        input = sc.nextLine().trim();
        if (input.length() > 0) {
            roomTypeEntity.setName(input);
        }

        System.out.print("Enter Room Description (blank if no change)> ");
        input = sc.nextLine().trim();
        if (input.length() > 0) {
            roomTypeEntity.setDescription(input);
        }

        while (true) {
            System.out.print("Enter Room Capacity (enter 0 if no changes)> ");
            int capacity = sc.nextInt();
            if (capacity > 0) {
                roomTypeEntity.setCapacity(capacity);
                break;
            } else if (capacity == 0) {
                break;
            } else {
                System.out.println("Invalid option, please enter 0 or positive number!");
            }
        }

        List<String> roomAmenities = new ArrayList<>();

        while (true) {
            System.out.println("Enter Room Type Amenities: ");
            System.out.println("0: NO CHANGE");
            System.out.println("1: WIFI");
            System.out.println("2: BATHTUB");
            System.out.println("3: BATHROOMAMENITIES");
            System.out.println("4: HAIRDRYER");
            System.out.println("5: LAUNDRYSERVICE");
            System.out.println("6: MINIBAR");
            System.out.println("7: LCDTV");
            System.out.println("8: TVSPEAKER");
            System.out.println("9: Done");

            Integer amenityInteger = sc.nextInt();

            if (amenityInteger >= 1 && amenityInteger <= 8) {
                switch (amenityInteger) {
                    case 1:
                        roomAmenities.add("WIFI");
                    case 2:
                        roomAmenities.add("BATHTUB");
                    case 3:
                        roomAmenities.add("BATHROOMAMENITIES");
                    case 4:
                        roomAmenities.add("HAIRDRYER");
                    case 5:
                        roomAmenities.add("LAUNDRY SERVICE");
                    case 6:
                        roomAmenities.add("MINIBAR");
                    case 7:
                        roomAmenities.add("LCDTV");
                    case 8:
                        roomAmenities.add("TVSPEAKER");
                    case 9:
                        break;
                }
            } else if (amenityInteger == 9) {
                roomTypeEntity.setRoomAmenities(roomAmenities);
                break;
            } else if (amenityInteger == 0) {
                break;
            } else {
                System.out.println("Invalid option, please try again!\n");
            }
        }

        while (true) {
            System.out.print("Select Bed Size (0: No Change, 1: SINGLE, 2: SUPERSINGLE, 3: QUEEN, 4: KING)> ");
            Integer bedSizeInteger = sc.nextInt();

            if (bedSizeInteger >= 1 && bedSizeInteger <= 4) {
                roomTypeEntity.setBedSize(BedSizeEnum.values()[bedSizeInteger - 1]);
                break;
            } else if (bedSizeInteger == 0) {
                break;
            } else {
                System.out.println("Invalid option, please try again!\n");
            }
        }

        sc.nextLine();

        System.out.print("Enter next higher room Type (blank if no change)> ");
        input = sc.nextLine().trim();
        if (input.length() > 0) {
            roomTypeEntity.setNextHigherRoomType(input);
        }

        roomTypeEntitySessionBeanRemote.updateRoomTypeDetails(roomTypeEntity);
        System.out.println("RoomType updated successfully!\n");
    }

    private void doDeleteRoomType(RoomTypeEntity roomTypeEntity) {
        Scanner sc = new Scanner(System.in);
        String input;

        System.out.println("*** Merlion Management System :: Operations Management :: View RoomType Details :: Delete RoomType ***\n");
        System.out.printf("Confirm Delete RoomType %s (RoomType ID: %d) (Enter 'Y' to Delete)> ", roomTypeEntity.getName(), roomTypeEntity.getRoomTypeId());
        input = sc.nextLine().trim().toUpperCase();

        if (input.equals("Y")) {
            try {
                roomTypeEntitySessionBeanRemote.deleteRoomTypebyID(roomTypeEntity.getRoomTypeId());
                System.out.println("RoomType deleted successfully!\n");
            } catch (RoomTypeCannotBeDeletedException ex) {
                System.out.println("Room cannot be deleted: " + ex.getMessage() + "\n");
            }
        } else {
            System.out.println("RoomType NOT deleted!\n");
        }
    }

    private void doViewAllRoomTypes() {
        System.out.println("*** Merlion Management System :: Operations Management:: View All RoomTypes ***\n");

        List<RoomTypeEntity> roomTypeEntities = roomTypeEntitySessionBeanRemote.retrieveAllRoomTypes();
        System.out.printf("%8s%30s%20s%20s%20s%20s\n", "RoomType ID", "RoomType Name", "RoomType Bed Size", "Room size", "Capacity", "Enabled");
        roomTypeEntities.forEach(roomTypeEntity -> {
            System.out.printf("%8s%30s%20s%20s%20s%20s\n", roomTypeEntity.getRoomTypeId().toString(), roomTypeEntity.getName(), roomTypeEntity.getBedSize().toString(), roomTypeEntity.getRoomSize(), roomTypeEntity.getCapacity(), roomTypeEntity.isRoomTypeEnabled());
        });
        System.out.println("");

    }

    private void doCreateNewRoomEntity() {
        Scanner sc = new Scanner(System.in);

        System.out.println("*** Merlion Management System :: Operations Management :: Create New RoomEntity ***\n");

        List<RoomTypeEntity> roomTypeEntities = roomTypeEntitySessionBeanRemote.retrieveAllRoomTypes();
        System.out.printf("%8s%30s%20s%20s%20s%20s\n", "RoomType ID", "RoomType Name", "RoomType Bed Size", "Room size", "Capacity", "Enabled");
        roomTypeEntities.forEach(roomTypeEntity -> {
            System.out.printf("%8s%30s%20s%20s%20s%20s\n", roomTypeEntity.getRoomTypeId().toString(), roomTypeEntity.getName(), roomTypeEntity.getBedSize().toString(), roomTypeEntity.getRoomSize(), roomTypeEntity.getCapacity(), roomTypeEntity.isRoomTypeEnabled());
        });

        Long roomTypeId;

        while (true) {
            System.out.println("\nEnter Room Type ID: ");
            roomTypeId = sc.nextLong();
            try {
                roomTypeEntitySessionBeanRemote.retrieveRoomTypeById(roomTypeId);
                break;
            } catch (RoomTypeCannotBeFoundException ex) {
                System.out.println("An error has occured: " + ex.getMessage() + " Try Again!\n");
            }
        }

        RoomEntity newRoomEntity = new RoomEntity();

        sc.nextLine();
        System.out.println("Enter Room Number");
        newRoomEntity.setRoomNumber(sc.nextLine().trim());

        newRoomEntity = roomEntitySessionBeanRemote.createNewRoom(newRoomEntity, roomTypeId);
        System.out.println("New " + newRoomEntity.getRoomType().getName() + " " + newRoomEntity.getRoomNumber()
                + " created Successfully with ID:" + newRoomEntity.getRoomId() + "\n");

    }

    private void doViewRoomDetails() {
        Scanner sc = new Scanner(System.in);
        Integer response = 0;

        System.out.println("*** Merlion Management System :: Operations Management :: View Room Details ***\n");
        System.out.print("Enter Room Number> ");
        String roomNumber = sc.nextLine().trim();

        try {
            RoomEntity roomEntity = roomEntitySessionBeanRemote.retrieveRoomByRoomNumber(roomNumber);
            System.out.printf("%8s%30s%30s%20s%20s\n", "Room ID", "Room Number", "Room Type", "Room Status", "Room Allocated");
            System.out.printf("%8s%30s%30s%20s%20s\n", roomEntity.getRoomId(), roomEntity.getRoomNumber(), roomEntity.getRoomType().getName(), roomEntity.isRoomStatusAvail(), roomEntity.isRoomAllocated());
            System.out.println("------------------------");
            System.out.println("1: Update Room");
            System.out.println("2: Delete Room");
            System.out.println("3: Back\n");
            System.out.print("> ");
            response = sc.nextInt();

            if (response == 1) {
                doUpdateRoom(roomEntity);
            } else if (response == 2) {
                doDeleteRoom(roomEntity);
            }
        } catch (RoomCannotBeFoundException ex) {
            System.out.println("An error has occurred while retrieving Room: " + ex.getMessage() + "\n");
        }
    }

    private void doUpdateRoom(RoomEntity roomEntity) {
        Scanner sc = new Scanner(System.in);
        String input;

        System.out.println("*** Merlion Management System :: Operations Management :: View Room Details :: Update Room ***\n");
        System.out.print("Change Room Type? y/n > ");
        if (sc.nextLine().equals('y')) {
            doViewAllRoomTypes();

            while (true) {
                System.out.println("Enter RoomType Id > ");
                Long roomTypeId = sc.nextLong();
                sc.nextLine();
                try {
                    RoomTypeEntity roomTypeEntity = roomTypeEntitySessionBeanRemote.retrieveRoomTypeById(roomTypeId);
                    roomEntity.setRoomType(roomTypeEntity);
                    System.out.println("Room Type Changed to " + roomEntity.getRoomType().getName());
                    break;
                } catch (RoomTypeCannotBeFoundException ex) {
                    System.out.println("An error has occured: " + ex.getMessage() + " Try Again!\n");
                }
            }
        }

        System.out.print("Change Room Number? y/n > ");
        if (sc.nextLine().trim().toUpperCase().equals('Y')) {
            while (true) {
                System.out.println("Enter Room Number > ");
                String newNumber = sc.nextLine().trim();
                try {
                    RoomEntity roomEntity1 = roomEntitySessionBeanRemote.retrieveRoomByRoomNumber(newNumber);
                    System.out.println("Room Number already exits! Try Again\n");
                } catch (RoomCannotBeFoundException ex) {
                    roomEntity.setRoomNumber(newNumber);
                    break;
                }
            }

        }

        System.out.print("Enter Room Status: Current Status is " + roomEntity.isRoomStatusAvail() + "! Change to " + !roomEntity.isRoomStatusAvail() + "? (y/n)");
        if (sc.nextLine().trim().toUpperCase().equals('Y')) {
            roomEntity.setRoomStatusAvail(!roomEntity.isRoomStatusAvail());
            System.out.println("Room Status changed to " + roomEntity.isRoomStatusAvail());
        }

        roomEntitySessionBeanRemote.updateRoomDetails(roomEntity);
        System.out.println("Room updated successfully!\n");
    }

    private RoomTypeEntitySessionBeanRemote lookupRoomTypeEntitySessionBeanRemote() {
        try {
            Context c = new InitialContext();
            return (RoomTypeEntitySessionBeanRemote) c.lookup("java:comp/env/RoomTypeEntitySessionBean");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    private void doDeleteRoom(RoomEntity roomEntity) {
        Scanner sc = new Scanner(System.in);
        String input;

        System.out.println("*** Merlion Management System :: Operations Management :: View Room Details :: Delete Room ***\n");
        System.out.printf("Confirm Delete Room %s (Room ID: %d) (Enter 'y' to Delete)> ", roomEntity.getRoomNumber(), roomEntity.getRoomId());
        input = sc.nextLine().toUpperCase().trim();

        if (input.equals("Y")) {
            try {
                roomEntitySessionBeanRemote.deleteRoombyID(roomEntity.getRoomId());
                System.out.println("Room deleted successfully!\n");
            } catch (RoomCannotBeFoundException | RoomCannotBeDeletedException ex) {
                System.out.println("An error has occurred while deleting the Room: " + ex.getMessage() + "\n");
            }
        } else {
            System.out.println("Room NOT deleted!\n");
        }
    }

    private void doViewAllRooms() {
        System.out.println("*** Merlion Management System :: Operations Management :: View All Rooms ***\n");

        List<RoomEntity> roomEntities = roomEntitySessionBeanRemote.retrieveAllRooms();
        System.out.printf("%8s%30s%20s%20s\n", "Room ID", "Room Number", "Room Status", "Allocated");

        roomEntities.forEach(roomEntity -> {
            System.out.printf("%8s%30s%20s%20s\n", roomEntity.getRoomId().toString(), roomEntity.getRoomNumber(), roomEntity.isRoomStatusAvail(), roomEntity.isRoomAllocated());
        });

        System.out.println("");
    }

    private void doViewExceptionReport() {

    }
}
