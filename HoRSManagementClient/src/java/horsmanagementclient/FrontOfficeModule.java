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
            System.out.println("5: Back\n");
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
