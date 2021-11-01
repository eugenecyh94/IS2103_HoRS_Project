package horsmanagementclient;

import ejb.session.stateless.EmployeeEntitySessionBeanRemote;
import entity.EmployeeEntity;
import java.util.Scanner;
import util.enumeration.EmployeeAccessRightEnum;
import util.exception.InvalidAccessRightException;
import ejb.session.stateless.RoomEntitySessionBeanRemote;

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
            throw new InvalidAccessRightException("You don't have OPERATION MANAGER rights to access the system administration module.");
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
    private void doViewAllRooms(){
    }
    
    private void doUpdateRoom () {
        
    }
    
    private void doDeleteRoom() {
    }
    
    private void doViewExceptionReport () {
        
    }
}