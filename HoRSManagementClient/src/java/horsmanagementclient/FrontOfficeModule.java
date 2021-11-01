package horsmanagementclient;

import ejb.session.stateless.EmployeeEntitySessionBeanRemote;
import entity.EmployeeEntity;
import java.util.Scanner;
import util.enumeration.EmployeeAccessRightEnum;
import util.exception.InvalidAccessRightException;

public class FrontOfficeModule {

    private EmployeeEntitySessionBeanRemote employeeEntitySessionBeanRemote;

    private EmployeeEntity currentEmployeeEntity;

    public FrontOfficeModule() {
    }

    public FrontOfficeModule(EmployeeEntitySessionBeanRemote employeeEntitySessionBeanRemote, EmployeeEntity currentEmployeeEntity) {
        this.employeeEntitySessionBeanRemote = employeeEntitySessionBeanRemote;
        this.currentEmployeeEntity = currentEmployeeEntity;
    }

    public void menuSalesManagement() throws InvalidAccessRightException {

        if (currentEmployeeEntity.getAccessRightEnum() != EmployeeAccessRightEnum.SALESMANAGER) {
            throw new InvalidAccessRightException("You don't have SALES MANAGER rights to access the system administration module.");
        }

        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** Merlion Management System :: Sales Management***\n");
            System.out.println("1: Create New Room Rate");
            System.out.println("2: View Room Rate Details");
            System.out.println("3: View All Room Rates");
            System.out.println("4: Delete Room Rate");
            System.out.println("5: Update Room Rate");
            System.out.println("6: Back\n");
            response = 0;

            while (response < 1 || response > 11) {
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
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 5) {
                break;
            }
        }
    }

    private void doWalkInRoomSearch() {

    }

    private void doWalkInReserveRoom() {

    }

    private void doCheckInGuest() {

    }

    private void doCheckOutGuest() {

    }
}
