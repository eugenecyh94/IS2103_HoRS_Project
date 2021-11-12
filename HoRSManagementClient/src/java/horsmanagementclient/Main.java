package horsmanagementclient;

import ejb.session.stateless.GuestEntitySessionBeanRemote;
import javax.ejb.EJB;
import ejb.session.stateless.EmployeeEntitySessionBeanRemote;
import ejb.session.stateless.PartnerEntitySessionBeanRemote;
import ejb.session.stateless.RoomEntitySessionBeanRemote;
import ejb.session.stateless.RoomTypeEntitySessionBeanRemote;

/**
 *
 * @author yunus
 */
public class Main {

    @EJB
    private static PartnerEntitySessionBeanRemote partnerEntitySessionBeanRemote;

    @EJB
    private static RoomEntitySessionBeanRemote roomEntitySessionBeanRemote;

    @EJB
    private static EmployeeEntitySessionBeanRemote employeeEntitySessionBeanRemote;

    @EJB
    private static GuestEntitySessionBeanRemote guestEntitySessionBeanRemote;
    
    @EJB
    private static RoomTypeEntitySessionBeanRemote roomTypeEntitySessionBeanRemote;

    
    public static void main(String[] args) {
        
        ManagementMainApp managementMainApp = new ManagementMainApp(roomTypeEntitySessionBeanRemote, roomEntitySessionBeanRemote, employeeEntitySessionBeanRemote, guestEntitySessionBeanRemote, partnerEntitySessionBeanRemote);

        managementMainApp.runApp();
    }
    
}
