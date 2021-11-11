package horsmanagementclient;

import ejb.session.stateless.GuestEntitySessionBeanRemote;
import entity.EmployeeEntity;
import java.util.Scanner;
import javax.ejb.EJB;
import util.exception.InvalidLoginCredentialException;
import ejb.session.stateless.EmployeeEntitySessionBeanRemote;
import ejb.session.stateless.PartnerEntitySessionBeanRemote;
import util.exception.InvalidAccessRightException;
import ejb.session.stateless.RoomEntitySessionBeanRemote;


public class ManagementMainApp {

    private RoomEntitySessionBeanRemote roomSessionBeanRemote;
    private EmployeeEntitySessionBeanRemote employeeEntitySessionBeanRemote;
    private PartnerEntitySessionBeanRemote partnerEntitySessionBeanRemote;
    private GuestEntitySessionBeanRemote guestEntitySessionBeanRemote;
    
    private SystemAdministrationModule systemAdministrationModule;
    private OperationsManagementModule operationsManagementModule;
    private SalesManagementModule salesManagementModule;
    private FrontOfficeModule frontOfficeModule; 
    private EmployeeEntity currentEmployeeEntity;

    public ManagementMainApp() {
    }

    public ManagementMainApp(RoomEntitySessionBeanRemote roomSessionBeanRemote, EmployeeEntitySessionBeanRemote employeeEntitySessionBeanRemote, GuestEntitySessionBeanRemote guestEntitySessionBeanRemote, PartnerEntitySessionBeanRemote partnerEntitySessionBeanRemote) {
        this.roomSessionBeanRemote = roomSessionBeanRemote;
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
                        operationsManagementModule = new OperationsManagementModule(employeeEntitySessionBeanRemote, currentEmployeeEntity);
                        systemAdministrationModule = new SystemAdministrationModule(employeeEntitySessionBeanRemote, currentEmployeeEntity, partnerEntitySessionBeanRemote);
                        frontOfficeModule = new FrontOfficeModule(currentEmployeeEntity);
                        menuMain();
                    }
                    catch(InvalidLoginCredentialException ex) 
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
    
    
    
    private void menuMain()
    {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** Merlion Management System :: Main Menu***\n");
            System.out.println("You are logged in as " + currentEmployeeEntity.getFirstName() + " " + currentEmployeeEntity.getLastName() + " with " + currentEmployeeEntity.getAccessRightEnum().toString() + " rights\n");
            System.out.println("1: Operations Management");
            System.out.println("2: System Administration");
            System.out.println("3: Sales Management");
            System.out.println("4: Front Office");
            System.out.println("5: Logout\n");
            response = 0;
            
            while(response < 1 || response > 4)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    try
                    {
                        operationsManagementModule.menuOperationsManagement();
                    }
                    catch (InvalidAccessRightException ex)
                    {
                        System.out.println("Invalid option, please try again!: " + ex.getMessage() + "\n");
                    }
                    
                }
                else if(response == 2)
                {
                    try
                    {
                        systemAdministrationModule.menuSystemAdministration();
                    }
                    catch (InvalidAccessRightException ex)
                    {
                        System.out.println("Invalid option, please try again!: " + ex.getMessage() + "\n");
                    }
                }
                else if(response == 3)
                {
                    try
                    {
                        salesManagementModule.menuSalesManagement();
                    }
                    catch (InvalidAccessRightException ex)
                    {
                        System.out.println("Invalid option, please try again!: " + ex.getMessage() + "\n");
                    }
                }
                else if(response == 4)
                {
                    try
                    {
                        frontOfficeModule.menuFrontOffice();
                    }
                    catch (InvalidAccessRightException ex)
                    {
                        System.out.println("Invalid option, please try again!: " + ex.getMessage() + "\n");
                    }
                }
                else if (response == 5)
                {
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            
            if(response == 5)
            {
                break;
            }
        }
    }
}
