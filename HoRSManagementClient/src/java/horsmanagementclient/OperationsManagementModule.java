package horsmanagementclient;

import ejb.session.stateless.EmployeeEntitySessionBeanRemote;
import entity.EmployeeEntity;
import java.util.Scanner;
import util.enumeration.EmployeeAccessRightEnum;
import util.exception.InvalidAccessRightException;
import ejb.session.stateless.RoomEntitySessionBeanRemote;
import entity.RoomTypeEntity;
import java.util.ArrayList;
import java.util.List;
import util.enumeration.BedSizeEnum;
import util.enumeration.RoomAmenitiesEnum;

public class OperationsManagementModule {

    private RoomEntitySessionBeanRemote roomSessionBeanRemote;
    private EmployeeEntitySessionBeanRemote employeeEntitySessionBeanRemote;

    private EmployeeEntity currentEmployeeEntity;

    public OperationsManagementModule() {
    }

    public OperationsManagementModule(EmployeeEntitySessionBeanRemote employeeEntitySessionBeanRemote, EmployeeEntity currentEmployeeEntity) {
        this.employeeEntitySessionBeanRemote = employeeEntitySessionBeanRemote;
        this.currentEmployeeEntity = currentEmployeeEntity;
    }

    public void menuOperationsManagement() throws InvalidAccessRightException {

        if (currentEmployeeEntity.getAccessRightEnum() != EmployeeAccessRightEnum.OPSMANAGER) {
            throw new InvalidAccessRightException("You don't have OPERATION MANAGER rights to access the Operations Management Module.");
        }

        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** Merlion Management System :: System Administration ***\n");
            System.out.println("1: Create New Room Type");
            System.out.println("2: View Room Type Details");
            System.out.println("3: View All Room Types");
            System.out.println("4: Delete Room Type");
            System.out.println("5: Update Room Type");
            System.out.println("-----------------------");
            System.out.println("6: Create New Room");
            System.out.println("7: View All Rooms");
            System.out.println("8: Update Room");
            System.out.println("9: Delete Room");
            System.out.println("-----------------------");
            System.out.println("10: View Room Allocationh Exception Report");
            System.out.println("11: Back\n");
            response = 0;

            while (response < 1 || response > 11) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    doCreateNewRoomType();
                } else if (response == 2) {
                    doViewRoomType();
                } else if (response == 3) {
                    doViewAllRoomTypes();
                } else if (response == 4) {
                    doDeleteRoomType();
                } else if (response == 5) {
                    doUpdateRoomType();
                } else if (response == 6) {
                    doCreateRoom();
                } else if (response == 7) {
                    doViewAllRooms();
                } else if (response == 8) {
                    doUpdateRoom();
                } else if (response == 9) {
                    doDeleteRoom();
                } else if (response == 10) {
                    doViewExceptionReport();
                } else if (response == 11) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 11) {
                break;
            }
        }
    }

    private void doCreateNewRoomType() {
        System.out.println("*** Merlion Management System :: Operations Management :: Create New Room Type ***\n");

        RoomTypeEntity roomTypeEntity = new RoomTypeEntity();
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter the Room Type Name: ");
        roomTypeEntity.setName(sc.nextLine().trim());

        System.out.println("Enter the Room Type Description: ");
        roomTypeEntity.setDescription(sc.nextLine().trim());

        System.out.println("Enter the Room Type Size: ");
        roomTypeEntity.setRoomSize(sc.nextLine().trim());

        while (true) {
            System.out.print("Select Access Right (1: Sales Manager, 2: Operations Manager, 3: System Admin, 4: Guest Relations Officer)> ");
            Integer bedSizeInt = sc.nextInt();

            if (bedSizeInt >= 1 && bedSizeInt <= 4) {
                roomTypeEntity.setBedSize(BedSizeEnum.values()[bedSizeInt]);

                break;
            } else {
                System.out.println("Invalid option, please try again!\n");
            }
        }

        System.out.println("Enter the Room Type Capacity: ");
        roomTypeEntity.setCapacity(sc.nextInt());
        
        List<RoomAmenitiesEnum> roomAmenities = new ArrayList<>();

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
            System.err.println("9: Done");

            Integer amenityInteger = sc.nextInt();

            if (amenityInteger >= 1 && amenityInteger <= 8) {
                roomAmenities.add(RoomAmenitiesEnum.values()[amenityInteger]);
            } else if(amenityInteger == 9) {
                break;
            } else {
                System.out.println("Invalid option, please try again!\n");
            }
        }
        
        roomTypeEntity.setRoomAmenities(roomAmenities);
        
        //call session bean
    }

    private void doViewRoomType() {
        
    }

    private void doViewAllRoomTypes() {
        
    }

    private void doDeleteRoomType() {

    }

    private void doUpdateRoomType() {

    }

    private void doCreateRoom() {

    }

    private void doViewAllRooms() {
    }

    private void doUpdateRoom() {

    }

    private void doDeleteRoom() {
    }

    private void doViewExceptionReport() {

    }
}
