
package horsreservationclient;

import ejb.session.stateless.GuestEntitySessionBeanRemote;
import ejb.session.stateless.ReservationEntitySessionBeanRemote;
import ejb.session.stateless.RoomTypeEntitySessionBeanRemote;
import ejb.session.stateless.SearchSessionBeanRemote;
import javax.ejb.EJB;


public class Main {

    @EJB
    private static ReservationEntitySessionBeanRemote reservationEntitySessionBean;

    @EJB
    private static SearchSessionBeanRemote searchSessionBean;

    @EJB
    private static RoomTypeEntitySessionBeanRemote roomTypeEntitySessionBean;

    @EJB
    private static GuestEntitySessionBeanRemote guestEntitySessionBean;
    
    
    public static void main(String[] args) {
        ReservationMainApp reservationMainApp = new ReservationMainApp(reservationEntitySessionBean, searchSessionBean, roomTypeEntitySessionBean, guestEntitySessionBean);
        reservationMainApp.runApp();
    }
    
}
