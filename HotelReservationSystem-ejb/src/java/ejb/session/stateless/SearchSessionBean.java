package ejb.session.stateless;

import entity.ReservationEntity;
import entity.RoomEntity;
import entity.RoomTypeEntity;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.NoRoomTypeAvailableException;
import util.exception.RoomCannotBeFoundException;
import util.exception.RoomRateCannotBeFoundException;
import util.exception.RoomTypeCannotBeFoundException;

@Stateless
public class SearchSessionBean implements SearchSessionBeanRemote, SearchSessionBeanLocal {

    @EJB
    RoomEntitySessionBeanLocal roomEntitySessionBeanLocal;
    @EJB
    RoomTypeEntitySessionBeanLocal roomTypeEntitySessionBeanLocal;
    @EJB
    ReservationEntitySessionBeanLocal reservationEntitySessionBeanLocal;
    @EJB
    RoomRateSessionBeanLocal roomRateSessionBeanLocal;

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    public SearchSessionBean() {
    }

    @Override
    public List<String> searchAvailableRoomTypesWalkIn(LocalDate checkInDate, LocalDate checkOutDate, int guestNumberOfRooms) throws NoRoomTypeAvailableException, RoomTypeCannotBeFoundException {

        List<String> availableRoomTypeAndRatePerNight = new ArrayList<>();
        List<RoomTypeEntity> roomTypes = roomTypeEntitySessionBeanLocal.retrieveAllRoomTypes();
        List<RoomEntity> rooms = roomEntitySessionBeanLocal.retrieveAllAvailableRooms();

        List<ReservationEntity> dateFilteredreservations = reservationEntitySessionBeanLocal.retrieveAllReservationsBySearchDates(checkInDate, checkOutDate);

        System.out.println("For debugging: roomTypes and room check");
        for (RoomTypeEntity rt : roomTypes) {
            System.out.println("" + rt.getName());
            for (RoomEntity r : rooms) {
                if (r.getRoomType().equals(rt)) {
                    System.out.println("" + r.getRoomNumber());
                }
            }
        }

        System.out.println("For debugging: reservation check");
        for (ReservationEntity rs : dateFilteredreservations) {
            System.out.println("" + rs.getReservationId() + ", " + rs.getCheckInDate().toString()
                    + ", " + rs.getCheckOutDate().toString());
        }

        //Main logic to search available rooms and respective rate
        for (RoomTypeEntity rt : roomTypes) {
            int totalRoomsAvailable = 0;
            for (RoomEntity r : rooms) {
                r.getRoomType();
                if (r.getRoomType().equals(rt) && r.isRoomStatusAvail() && !r.isDisabled()) {
                    totalRoomsAvailable++;
                }
            }
            System.out.println("For debugging: totalRoomsAvailable = " + totalRoomsAvailable);
            int periodAvailableRooms = totalRoomsAvailable;
            BigDecimal periodRoomTypeRate = new BigDecimal("0");

            //Looping through every day
            for (LocalDate dailyDate = checkInDate; dailyDate.isBefore(checkOutDate.plusDays(1)); dailyDate = dailyDate.plusDays(1)) {
                System.out.println("For debugging: dailyDate loop: " + dailyDate.toString());
                int dailyAvailableRooms = totalRoomsAvailable;
                //parameter for calculate rate is false, as search is done in front counter - refer to roomratesessionbean
                try {
                    periodRoomTypeRate.add(roomRateSessionBeanLocal.selectDailyRoomRate(dailyDate, rt.getRoomTypeId(), false).getRate());
                    System.out.println("For debugging: Daily Room Rate = " + roomRateSessionBeanLocal.selectDailyRoomRate(dailyDate, rt.getRoomTypeId(), false).getRate());
                } catch (RoomRateCannotBeFoundException ex) {
                    ex.getMessage();
                    //break;
                }
                for (ReservationEntity rs : dateFilteredreservations) {
                    if (rs.getRoomType().equals(rt) && !(dailyDate.isBefore(rs.getCheckInDate()) || dailyDate.isAfter(rs.getCheckOutDate()))) {
                        dailyAvailableRooms -= rs.getNumOfRooms();
                    }
                }

                if (dailyAvailableRooms <= periodAvailableRooms) {
                    periodAvailableRooms = dailyAvailableRooms;
                }
            }
            System.out.println("For debugging: periodRoomsAvailable = " + periodAvailableRooms);
            //if avail rooms > guest required number of rooms
            if (periodAvailableRooms >= guestNumberOfRooms) {
                availableRoomTypeAndRatePerNight.add(rt.getName());
                availableRoomTypeAndRatePerNight.add("SGD " + periodRoomTypeRate.toString()); //can change to per night if neccessary;
            }
        }

        if (availableRoomTypeAndRatePerNight.isEmpty()) {
            throw new NoRoomTypeAvailableException("No Room Type is available for this period!");
        }

        return availableRoomTypeAndRatePerNight;
    }

    @Override
    public List<String> searchAvailableRoomTypesOnline(LocalDate checkInDate, LocalDate checkOutDate, int guestNumberOfRooms) throws NoRoomTypeAvailableException, RoomTypeCannotBeFoundException {

        List<String> availableRoomTypeAndRatePerNight = new ArrayList<>();
        List<RoomTypeEntity> roomTypes = roomTypeEntitySessionBeanLocal.retrieveAllRoomTypes();
        List<RoomEntity> rooms = roomEntitySessionBeanLocal.retrieveAllRooms();
        List<ReservationEntity> dateFilteredreservations = reservationEntitySessionBeanLocal.retrieveAllReservationsBySearchDates(checkInDate, checkOutDate);

        //Main logic to search available rooms and respective rate
        for (RoomTypeEntity rt : roomTypes) {
            int totalRoomsAvailable = 0;
            for (RoomEntity r : rooms) {
                if (r.getRoomType().equals(rt) && r.isRoomStatusAvail() && !r.isDisabled()) {
                    totalRoomsAvailable++;
                }
            }
            int periodAvailableRooms = totalRoomsAvailable;
            BigDecimal periodRoomTypeRates = new BigDecimal("0");

            //Looping through every day
            for (LocalDate dailyDate = checkInDate; dailyDate.isBefore(checkOutDate.plusDays(1)); dailyDate = dailyDate.plusDays(1)) {
                int dailyAvailableRooms = totalRoomsAvailable;
                //parameter for calculate rate is true, as search is done online - refer to roomratesessionbean
                try{
                periodRoomTypeRates.add(roomRateSessionBeanLocal.selectDailyRoomRate(dailyDate, rt.getRoomTypeId(), true).getRate());
                } catch (RoomRateCannotBeFoundException ex){
                    ex.getMessage();
                }
                for (ReservationEntity rs : dateFilteredreservations) {
                    if (rs.getRoomType().equals(rt) && !(dailyDate.isBefore(rs.getCheckInDate()) || dailyDate.isAfter(rs.getCheckOutDate()))) {
                        dailyAvailableRooms -= rs.getNumOfRooms();
                    }
                }

                if (dailyAvailableRooms <= periodAvailableRooms) {
                    periodAvailableRooms = dailyAvailableRooms;
                }
            }

            //if avail rooms > guest required number of rooms
            if ((periodAvailableRooms >= guestNumberOfRooms) && (periodAvailableRooms > 0)) {
                availableRoomTypeAndRatePerNight.add(rt.getName());
                availableRoomTypeAndRatePerNight.add("SGD " + periodRoomTypeRates.toString()); //can change to per night if neccessary;
            }
        }

        if (availableRoomTypeAndRatePerNight.isEmpty()) {
            throw new NoRoomTypeAvailableException("No Room Type is available for this period!");
        }

        return availableRoomTypeAndRatePerNight;
    }

}
