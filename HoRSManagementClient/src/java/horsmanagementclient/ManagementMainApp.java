package horsmanagementclient;

import ejb.session.stateless.GuestEntitySessionBeanRemote;
import entity.EmployeeEntity;
import java.util.Scanner;
import util.exception.InvalidLoginCredentialException;
import ejb.session.stateless.EmployeeEntitySessionBeanRemote;
import ejb.session.stateless.PartnerEntitySessionBeanRemote;
import util.exception.InvalidAccessRightException;
import ejb.session.stateless.RoomEntitySessionBeanRemote;
import ejb.session.stateless.RoomTypeEntitySessionBeanRemote;
import util.enumeration.EmployeeAccessRightEnum;


public class ManagementMainApp {

    private RoomEntitySessionBeanRemote roomEntitySessionBeanRemote;
    private EmployeeEntitySessionBeanRemote employeeEntitySessionBeanRemote;
    private PartnerEntitySessionBeanRemote partnerEntitySessionBeanRemote;
    private GuestEntitySessionBeanRemote guestEntitySessionBeanRemote;
    private RoomTypeEntitySessionBeanRemote roomTypeEntitySessionBeanRemote;
    
    private SystemAdministrationModule systemAdministrationModule;
    private OperationsManagementModule operationsManagementModule;
    private SalesManagementModule salesManagementModule;
    private FrontOfficeModule frontOfficeModule; 
    private EmployeeEntity currentEmployeeEntity;

    public ManagementMainApp() {
    }

    public ManagementMainApp(RoomTypeEntitySessionBeanRemote roomTypeEntitySessionBeanRemote,RoomEntitySessionBeanRemote roomEntitySessionBeanRemote, EmployeeEntitySessionBeanRemote employeeEntitySessionBeanRemote, GuestEntitySessionBeanRemote guestEntitySessionBeanRemote, PartnerEntitySessionBeanRemote partnerEntitySessionBeanRemote) {
        this.roomTypeEntitySessionBeanRemote = roomTypeEntitySessionBeanRemote;
        this.roomEntitySessionBeanRemote = roomEntitySessionBeanRemote;
        this.employeeEntitySessionBeanRemote = employeeEntitySessionBeanRemote;
        this.guestEntitySessionBeanRemote = guestEntitySessionBeanRemote;
        this.partnerEntitySessionBeanRemote = partnerEntitySessionBeanRemote;
    }
    
     public void runApp()
    {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** Welcome to Merlion Hotel Management System ***\n");
            System.out.println("1: Login");
            System.out.println("2: Exit\n");
            response = 0;
            
            while(response < 1 || response > 2)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    try
                    {
                        doLogin();
                        System.out.println("Login successful!\n");
                        
                        salesManagementModule = new SalesManagementModule(employeeEntitySessionBeanRemote, currentEmployeeEntity);
                        operationsManagementModule = new OperationsManagementModule(roomTypeEntitySessionBeanRemote, roomEntitySessionBeanRemote, currentEmployeeEntity);
                        systemAdministrationModule = new SystemAdministrationModule(employeeEntitySessionBeanRemote, currentEmployeeEntity, partnerEntitySessionBeanRemote);
                        frontOfficeModule = new FrontOfficeModule(currentEmployeeEntity);
                        
                        if(currentEmployeeEntity.getAccessRightEnum().equals(EmployeeAccessRightEnum.OPSMANAGER)) {
                            operationsManagementModule.menuOperationsManagement();
                        } else if(currentEmployeeEntity.getAccessRightEnum().equals(EmployeeAccessRightEnum.SYSTEMADMIN)) {
                            systemAdministrationModule.menuSystemAdministration();
                        } else if(currentEmployeeEntity.getAccessRightEnum().equals(EmployeeAccessRightEnum.SALESMANAGER)) {
                            salesManagementModule.menuSalesManagement();
                        } else if(currentEmployeeEntity.getAccessRightEnum().equals(EmployeeAccessRightEnum.GUESTRELATIONSOFFICER)) {
                            frontOfficeModule.menuFrontOffice();
                        }
                    }
                    catch(InvalidLoginCredentialException | InvalidAccessRightException ex) 
                    {
                        System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                    }
                }
                else if (response == 2)
                {
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            
            if(response == 2)
            {
                break;
            }
        }
    }
    
    
    
    private void doLogin() throws InvalidLoginCredentialException 
    {
        Scanner scanner = new Scanner(System.in);
        String username = "";
        String password = "";
        
        System.out.println("*** Merlion Management System :: Login ***\n");
        System.out.print("Enter username> ");
        username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();
        
        if(username.length() > 0 && password.length() > 0)
        {
            currentEmployeeEntity = employeeEntitySessionBeanRemote.employeeLogin(username, password);      
        }
        else
        {
            throw new InvalidLoginCredentialException("Missing login credential!");
        }
    }
}
