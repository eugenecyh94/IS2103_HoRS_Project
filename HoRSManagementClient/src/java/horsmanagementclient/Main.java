package horsmanagementclient;

import ejb.session.stateless.EjbHorsTimerSessionBeanRemote;
import ejb.session.stateless.GuestEntitySessionBeanRemote;
import javax.ejb.EJB;
import ejb.session.stateless.EmployeeEntitySessionBeanRemote;
import ejb.session.stateless.PartnerEntitySessionBeanRemote;
import ejb.session.stateless.ReservationEntitySessionBeanRemote;
import ejb.session.stateless.RoomEntitySessionBeanRemote;
import ejb.session.stateless.RoomRateSessionBeanRemote;
import ejb.session.stateless.RoomTypeEntitySessionBeanRemote;
import ejb.session.stateless.SearchSessionBeanRemote;

/**
 *
 * @author yunus
 */
public class Main {

    @EJB
    private static EjbHorsTimerSessionBeanRemote ejbHorsTimerSessionBean;

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

    @EJB
    private static RoomRateSessionBeanRemote roomRateSessionBeanRemote;
    
    @EJB 
    private static ReservationEntitySessionBeanRemote reservationEntitySessionBeanRemote;
    
    @EJB
    private static SearchSessionBeanRemote searchSessionBeanRemote;

    public static void main(String[] args) {

        ManagementMainApp managementMainApp = new ManagementMainApp(roomEntitySessionBeanRemote, employeeEntitySessionBeanRemote, partnerEntitySessionBeanRemote, guestEntitySessionBeanRemote, roomTypeEntitySessionBeanRemote, roomRateSessionBeanRemote, reservationEntitySessionBeanRemote, searchSessionBeanRemote, ejbHorsTimerSessionBean);

        managementMainApp.runApp();
    }

}
