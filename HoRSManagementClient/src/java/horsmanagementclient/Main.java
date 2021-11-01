package horsmanagementclient;

import ejb.session.stateless.GuestEntitySessionBeanRemote;
import javax.ejb.EJB;
import ejb.session.stateless.EmployeeEntitySessionBeanRemote;
import ejb.session.stateless.PartnerEntitySessionBeanRemote;
import ejb.session.stateless.RoomEntitySessionBeanRemote;

/**
 *
 * @author yunus
 */
public class Main {

    @EJB
    private static PartnerEntitySessionBeanRemote partnerEntitySessionBeanRemote;

    @EJB
    private static RoomEntitySessionBeanRemote roomSessionBeanRemote;

    @EJB
    private static EmployeeEntitySessionBeanRemote employeeEntitySessionBeanRemote;

    @EJB
    private static GuestEntitySessionBeanRemote guestEntitySessionBeanRemote;

    
    public static void main(String[] args) {
        
        ManagementMainApp managementMainApp = new ManagementMainApp(roomSessionBeanRemote, employeeEntitySessionBeanRemote, guestEntitySessionBeanRemote, partnerEntitySessionBeanRemote);

        managementMainApp.runApp();
    }
    
}
