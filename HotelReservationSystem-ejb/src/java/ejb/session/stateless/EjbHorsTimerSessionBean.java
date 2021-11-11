/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.DailyExceptionReportEntity;
import entity.ReservationEntity;
import entity.RoomEntity;
import java.time.LocalDate;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import util.exception.RoomAllocationNotUpgradedException;
import util.exception.RoomAllocationUpgradedException;

/**
 *
 * @author Eugene Chua
 */
@Stateless
public class EjbHorsTimerSessionBean implements EjbHorsTimerSessionBeanRemote, EjbHorsTimerSessionBeanLocal {

    @EJB
    ReservationEntitySessionBeanLocal reservationEntitySessionBeanLocal;
    @EJB
    AllocationSessionBeanLocal allocationSessionBeanLocal;

    //@Schedule(dayOfWeek = "*", hour = "2", info = "Daily2amRoomAllocationTimer")    
    //for testing, triggered every 5 seconds
    @Schedule(hour = "*", minute = "*", second = "*/5", info = "TestRoomAllocationTimer")
    public void automatedRoomAllocation() {
        
        System.out.println("********** Automated Allocation Started");

        LocalDate today = LocalDate.now();
        DailyExceptionReportEntity exceptionReport = new DailyExceptionReportEntity(today);
        List<ReservationEntity> reservations = reservationEntitySessionBeanLocal.retrieveAllReservationsByCheckInDate(today);

        for (ReservationEntity rs : reservations) {
            try {
                List<RoomEntity> roomsAllocated = allocationSessionBeanLocal.allocateRoom(rs.getReservationId());
            } catch (RoomAllocationUpgradedException | RoomAllocationNotUpgradedException ex) {
                String exceptionString = new String("Reservation ID: " + rs.getReservationId() + " " + "\n" + ex.getMessage());
                exceptionReport.getExceptionDetails().add(exceptionString);
            }
        }
    }
}
