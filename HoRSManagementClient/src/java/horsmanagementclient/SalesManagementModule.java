package horsmanagementclient;

import ejb.session.stateless.EmployeeEntitySessionBeanRemote;
import entity.EmployeeEntity;
import java.util.Scanner;
import util.enumeration.EmployeeAccessRightEnum;
import util.exception.InvalidAccessRightException;
import ejb.session.stateless.RoomEntitySessionBeanRemote;

public class SalesManagementModule {

    private EmployeeEntitySessionBeanRemote employeeEntitySessionBeanRemote;

    private EmployeeEntity currentEmployeeEntity;

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
            System.out.println("4: Delete Room Rate");
            System.out.println("5: Update Room Rate");
            System.out.println("6: Back\n");
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
                    doDeleteRoomRate();
                } else if (response == 5) {
                    doUpdateRoomRate();
                } else if (response == 6) {
                    break;
                }else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 6) {
                break;
            }
        }
    }

    private void doCreateNewRoomRate() {

    }

    private void doViewRoomRate() {

    }

    private void doViewAllRoomRates() {

    }

    private void doDeleteRoomRate() {

    }

    private void doUpdateRoomRate() {

    }
}