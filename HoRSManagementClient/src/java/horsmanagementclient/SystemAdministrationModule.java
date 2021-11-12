package horsmanagementclient;

import ejb.session.stateless.EmployeeEntitySessionBeanRemote;
import ejb.session.stateless.PartnerEntitySessionBeanRemote;
import entity.EmployeeEntity;
import entity.PartnerEntity;
import java.util.List;
import java.util.Scanner;
import util.enumeration.EmployeeAccessRightEnum;
import util.exception.EmployeeNotFoundException;
import util.exception.InvalidAccessRightException;

public class SystemAdministrationModule {

    private EmployeeEntitySessionBeanRemote employeeEntitySessionBeanRemote;
    private PartnerEntitySessionBeanRemote partnerEntitySessionBeanRemote;
    private EmployeeEntity currentEmployeeEntity;

    public SystemAdministrationModule() {
    }

    public SystemAdministrationModule(EmployeeEntitySessionBeanRemote employeeEntitySessionBeanRemote, EmployeeEntity currentEmployeeEntity, PartnerEntitySessionBeanRemote partnerEntitySessionBeanRemote) {
        this();
        this.employeeEntitySessionBeanRemote = employeeEntitySessionBeanRemote;
        this.partnerEntitySessionBeanRemote = partnerEntitySessionBeanRemote;
        this.currentEmployeeEntity = currentEmployeeEntity;
    }

    public void menuSystemAdministration() throws InvalidAccessRightException {
        
        if (currentEmployeeEntity.getAccessRightEnum() != EmployeeAccessRightEnum.SYSTEMADMIN) {
            throw new InvalidAccessRightException("You don't have SYSTEM ADMIN rights to access the System Administration Module.");
        }

        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** Merlion Management System :: System Administration ***\n");
            System.out.println("1: Create New Employee");
            System.out.println("2: View All Employees");
            System.out.println("3: Create New Partner");
            System.out.println("4: View All Partners");
            System.out.println("-----------------------");
            System.out.println("5: Back\n");
            response = 0;

            while (response < 1 || response > 7) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    doCreateNewEmployee();
                } else if (response == 2) {
                    doViewAllEmployees();
                } else if (response == 3) {
                    doCreateNewPartner();
                } else if (response == 4) {
                    doViewAllPartners();
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

    private void doCreateNewEmployee() {
        Scanner scanner = new Scanner(System.in);
        EmployeeEntity newEmployeeEntity = new EmployeeEntity();

        System.out.println("*** Merlion Management System :: System Administration :: Create New Employee ***\n");
        System.out.print("Enter First Name> ");
        newEmployeeEntity.setFirstName(scanner.nextLine().trim());
        System.out.print("Enter Last Name> ");
        newEmployeeEntity.setLastName(scanner.nextLine().trim());

        while (true) {
            System.out.print("Select Access Right (1: Sales Manager, 2: Operations Manager, 3: System Admin, 4: Guest Relations Officer)> ");
            Integer accessRightInt = scanner.nextInt();

            if (accessRightInt > 1 && accessRightInt < 4) {
                newEmployeeEntity.setAccessRightEnum(EmployeeAccessRightEnum.values()[accessRightInt - 1]);
                break;
            } else {
                System.out.println("Invalid option, please try again!\n");
            }
        }

        scanner.nextLine();
        System.out.print("Enter Username> ");
        newEmployeeEntity.setUsername(scanner.nextLine().trim());
        System.out.print("Enter Password> ");
        newEmployeeEntity.setPassword(scanner.nextLine().trim());

        Long newEmployeeId = employeeEntitySessionBeanRemote.createNewEmployee(newEmployeeEntity);
        System.out.println("New employee created successfully!: " + newEmployeeId + "\n");
    }

    private void doViewAllEmployees() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("*** Merlion Management System :: System Administration :: View All Employees ***\n");

        List<EmployeeEntity> employeeEntities = employeeEntitySessionBeanRemote.retrieveAllEmployees();
        System.out.printf("%8s%20s%20s%15s%20s%20s\n", "Employee ID", "First Name", "Last Name", "Access Right", "Username", "Password");

        for (EmployeeEntity employeeEntity : employeeEntities) {
            System.out.printf("%8s%10s%10s%20s%15s%20s\n", employeeEntity.getEmployeeId().toString(), employeeEntity.getFirstName(), employeeEntity.getLastName(), employeeEntity.getAccessRightEnum().toString(), employeeEntity.getUsername(), employeeEntity.getPassword());
        }

        System.out.print("\nPress any key to continue...> ");
        scanner.nextLine();
    }

    private void doCreateNewPartner() {
        Scanner scanner = new Scanner(System.in);
        PartnerEntity newPartnerEntity = new PartnerEntity();

        System.out.println("*** Merlion Management System :: System Administration :: Create New Partner ***\n");
        System.out.print("Enter First Name> ");
        newPartnerEntity.setFirstName(scanner.nextLine().trim());
        System.out.print("Enter Last Name> ");
        newPartnerEntity.setLastName(scanner.nextLine().trim());
        
        scanner.nextLine();
        System.out.print("Enter Username> ");
        newPartnerEntity.setUsername(scanner.nextLine().trim());
        System.out.print("Enter Password> ");
        newPartnerEntity.setPassword(scanner.nextLine().trim());

        Long newPartnerId = partnerEntitySessionBeanRemote.createNewPartner(newPartnerEntity);
        System.out.println("New Partner created successfully!: " + newPartnerId + "\n");
    }

    private void doViewAllPartners() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("*** Merlion Management System :: System Administration :: View All Partners ***\n");

        List<PartnerEntity> partnerEntities = partnerEntitySessionBeanRemote.retrieveAllPartners();
        System.out.printf("%8s%20s%20s%20s\n", "Partner ID", "First Name", "Username", "Password");

        for (PartnerEntity partnerEntity : partnerEntities) {
            System.out.printf("%8s%20s%20s%20s\n", partnerEntity.getPartnerId().toString(), partnerEntity.getFirstName(), partnerEntity.getUsername(), partnerEntity.getPassword());
        }

        System.out.print("\nPress any key to continue...> ");
        scanner.nextLine();
    }
}

