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
import util.exception.NoAllocationExceptionReportException;
import util.exception.NoRoomAllocationException;
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
    @EJB
    RoomEntitySessionBeanLocal roomEntitySessionBeanLocal;

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    @Schedule(dayOfWeek = "*", hour = "2", info = "Daily2amRoomAllocationTimer")
    //for testing, triggered every 5 seconds
    @Override
    //@Schedule(hour = "*", minute = "*/1", info = "TestRoomAllocationTimer")
    public void automatedRoomAllocation() {

        System.out.println("********** Automated Allocation Started");

        LocalDate today = LocalDate.now();

        List<DailyExceptionReportEntity> exceptionReports = em
                .createQuery("SELECT er FROM DailyExceptionReportEntity er").getResultList();

        DailyExceptionReportEntity exceptionReport = null;

        if (exceptionReports.isEmpty()) {
            exceptionReport = new DailyExceptionReportEntity(today);
            em.persist(exceptionReport);
            em.flush();
        } else {
            for (DailyExceptionReportEntity er : exceptionReports) {
                if (er.getDate().equals(today)) {
                    exceptionReport = er;
                    break;
                }
            }
        }

        if (exceptionReport == null) {
            exceptionReport = new DailyExceptionReportEntity(today);
            em.persist(exceptionReport);
            em.flush();
        }

        List<ReservationEntity> reservations = reservationEntitySessionBeanLocal.retrieveAllReservationsByCheckInDate(today);

        List<ReservationEntity> notAllocatedReservations = new ArrayList<>();

        for (ReservationEntity rs : reservations) {
            if (!rs.isRoomAllocated()) {
                notAllocatedReservations.add(rs);
            }
        }

        for (ReservationEntity rs : notAllocatedReservations) {
            try {
                allocationSessionBeanLocal.allocateRoom(rs.getReservationId());
                rs.setRoomAllocated(true);
            } catch (RoomAllocationUpgradedException | NoRoomAllocationException ex) {
                String exceptionString = "Reservation ID: " + rs.getReservationId() + " " + "\n" + ex.getMessage();
                exceptionReport.getExceptionDetails().add(exceptionString);
            }
        }
    }

    @Override
    public void manualRoomAllocation(LocalDate date) {

        List<DailyExceptionReportEntity> exceptionReports = em
                .createQuery("SELECT er FROM DailyExceptionReportEntity er").getResultList();

        DailyExceptionReportEntity exceptionReport = null;

        if (exceptionReports.isEmpty()) {
            exceptionReport = new DailyExceptionReportEntity(date);
            em.persist(exceptionReport);
            em.flush();
        } else {
            for (DailyExceptionReportEntity er : exceptionReports) {
                if (er.getDate().equals(date)) {
                    exceptionReport = er;
                    break;
                }
            }
        }

        if (exceptionReport == null) {
            exceptionReport = new DailyExceptionReportEntity(date);
            em.persist(exceptionReport);
            em.flush();
        }

        List<ReservationEntity> reservations = reservationEntitySessionBeanLocal.retrieveAllReservationsByCheckInDate(date);

        List<ReservationEntity> notAllocatedReservations = new ArrayList<>();

        for (ReservationEntity rs : reservations) {
            if (!rs.isRoomAllocated()) {
                notAllocatedReservations.add(rs);
            }
        }

        for (ReservationEntity rs : notAllocatedReservations) {
            try {
                allocationSessionBeanLocal.allocateRoom(rs.getReservationId());
                rs.setRoomAllocated(true);
            } catch (RoomAllocationUpgradedException | NoRoomAllocationException ex) {
                String exceptionString = "Reservation ID: " + rs.getReservationId() + " " + "\n" + ex.getMessage();
                exceptionReport.getExceptionDetails().add(exceptionString);
            }
        }
    }

    @Override
    public DailyExceptionReportEntity viewCurrentDayAllocationExceptionReport() throws NoAllocationExceptionReportException {

        LocalDate date = LocalDate.now();

        List<DailyExceptionReportEntity> exceptionReports = em
                .createQuery("SELECT er FROM DailyExceptionReportEntity er").getResultList();

        if (exceptionReports.isEmpty()) {
            throw new NoAllocationExceptionReportException("No Room Allocation Report is found!");
        }

        DailyExceptionReportEntity exceptionReport = null;

        for (DailyExceptionReportEntity er : exceptionReports) {
            if (er.getDate().equals(date)) {
                exceptionReport = er;
            }
        }

        if (exceptionReport == null) {
            throw new NoAllocationExceptionReportException("No Room Allocation Report is found!");
        }

        return exceptionReport;
    }

    @Override
    public DailyExceptionReportEntity viewSpecificDayAllocationExceptionReport(LocalDate date) throws NoAllocationExceptionReportException {

        List<DailyExceptionReportEntity> exceptionReports = em
                .createQuery("SELECT er FROM DailyExceptionReportEntity er").getResultList();

        if (exceptionReports.isEmpty()) {
            throw new NoAllocationExceptionReportException("No Room Allocation Report is found!");
        }

        DailyExceptionReportEntity exceptionReport = null;

        for (DailyExceptionReportEntity er : exceptionReports) {
            if (er.getDate().equals(date)) {
                exceptionReport = er;
            }
        }

        if (exceptionReport == null) {
            throw new NoAllocationExceptionReportException("No Room Allocation Report is found!");
        }

        return exceptionReport;
    }

    @Override
    public List<RoomEntity> checkOutGuest(Long reservationId, List<RoomEntity> rooms) {

            for (RoomEntity r : rooms) {
                r.setRoomAllocated(false);
            }

            return rooms;
    }

    @Override
    public void deleteExceptionReport(Long exceptionReportId
    ) {

        DailyExceptionReportEntity exceptionReport = em.find(DailyExceptionReportEntity.class,
                exceptionReportId);
        em.remove(exceptionReport);

    }

}
