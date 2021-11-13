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
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
    
    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    //@Schedule(dayOfWeek = "*", hour = "2", info = "Daily2amRoomAllocationTimer")    
    //for testing, triggered every 5 seconds
    @Override
    @Schedule(hour = "*", minute = "*", second = "*/5", info = "TestRoomAllocationTimer")
    public void automatedRoomAllocation() {

        System.out.println("********** Automated Allocation Started");

        LocalDate today = LocalDate.now();
        DailyExceptionReportEntity exceptionReport = new DailyExceptionReportEntity(today);
        List<ReservationEntity> reservations = reservationEntitySessionBeanLocal.retrieveAllReservationsByCheckInDate(today);

        em.persist(exceptionReport);
        em.flush();

        List<ReservationEntity> notAllocatedReservations = new ArrayList<>();
        
        for (ReservationEntity rs : reservations) {
            if(!rs.isRoomAllocated()){
                notAllocatedReservations.add(rs);
            }
        }
        
        for (ReservationEntity rs : notAllocatedReservations) {
            try {
                allocationSessionBeanLocal.allocateRoom(rs.getReservationId());
                rs.setRoomAllocated(true);
            } catch (RoomAllocationUpgradedException | RoomAllocationNotUpgradedException ex) {
                String exceptionString = "Reservation ID: " + rs.getReservationId() + " " + "\n" + ex.getMessage();
                exceptionReport.getExceptionDetails().add(exceptionString);
            }
        }
    }

    @Override
    public void manualRoomAllocation(LocalDate date) {

        DailyExceptionReportEntity exceptionReport = new DailyExceptionReportEntity(date);
        List<ReservationEntity> reservations = reservationEntitySessionBeanLocal.retrieveAllReservationsByCheckInDate(date);

        em.persist(exceptionReport);
        em.flush();
        
        List<ReservationEntity> notAllocatedReservations = new ArrayList<>();
        
        for (ReservationEntity rs : reservations) {
            if(!rs.isRoomAllocated()){
                notAllocatedReservations.add(rs);
            }
        }
        
        for (ReservationEntity rs : notAllocatedReservations) {
            try {
                allocationSessionBeanLocal.allocateRoom(rs.getReservationId());
            } catch (RoomAllocationUpgradedException | RoomAllocationNotUpgradedException ex) {
                String exceptionString = "Reservation ID: " + rs.getReservationId() + " " + "\n" + ex.getMessage();
                exceptionReport.getExceptionDetails().add(exceptionString);
            }
        }
    }

    @Override
    public void deleteExceptionReport(Long exceptionReportId) {
        
        DailyExceptionReportEntity exceptionReport = em.find(DailyExceptionReportEntity.class, exceptionReportId);
        em.remove(exceptionReport);
        
    }
    
}
