package ejb.session.stateless;

import entity.ReservationEntity;
import entity.RoomEntity;
import entity.RoomTypeEntity;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
    public List<String> searchAvailableRoomTypesWalkIn(LocalDate checkInDate, LocalDate checkOutDate, int guestNumberOfRooms, int numberOfAdults) throws NoRoomTypeAvailableException, RoomTypeCannotBeFoundException {

        List<String> availableRoomTypeAndRatePerNight = new ArrayList<>();
        List<RoomTypeEntity> roomTypes = roomTypeEntitySessionBeanLocal.retrieveAllRoomTypes();
        List<RoomEntity> rooms = roomEntitySessionBeanLocal.retrieveAllAvailableRooms();

        List<ReservationEntity> dateFilteredreservations = reservationEntitySessionBeanLocal.retrieveAllReservationsBySearchDates(checkInDate, checkOutDate);

        //for debug
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
        List<RoomTypeEntity> capacityFilteredRoomTypes = new ArrayList<>();

        for (RoomTypeEntity rt : roomTypes) {
            if (rt.getCapacity() >= numberOfAdults) {
                capacityFilteredRoomTypes.add(rt);
            }
        }

        for (RoomTypeEntity rt : capacityFilteredRoomTypes) {
            int totalRoomsAvailable = 0;
            for (RoomEntity r : rooms) {
                r.getRoomType();
                if (r.getRoomType().equals(rt) && r.isRoomStatusAvail() && !r.isDisabled()) {
                    totalRoomsAvailable++;
                }
            }
            System.out.println("For debugging: totalRoomsAvailable = " + rt.getName() + " " + totalRoomsAvailable);
            int periodAvailableRooms = totalRoomsAvailable;
            BigDecimal periodRoomTypeRate = new BigDecimal("0");
            int totalDays = 0;

            //Looping through every day
            for (LocalDate dailyDate = checkInDate; dailyDate.isBefore(checkOutDate); dailyDate = dailyDate.plusDays(1)) {
                totalDays += 1;
                System.out.println("For debugging: dailyDate loop: " + dailyDate.toString());
                int dailyAvailableRooms = totalRoomsAvailable;
                //parameter for calculate rate is false, as search is done in front counter - refer to roomratesessionbean
                try {
                    periodRoomTypeRate = periodRoomTypeRate.add(roomRateSessionBeanLocal.selectDailyRoomRate(dailyDate, rt.getRoomTypeId(), false).getRate());
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
            System.out.println("For debugging: periodRoomsAvailable = " + rt.getName() + " " + periodAvailableRooms);
            System.out.println("For debugging: periodRoomRate = " + rt.getName() + " " + periodRoomTypeRate.toString());
            //if avail rooms > guest required number of rooms
            if (periodAvailableRooms >= guestNumberOfRooms && !periodRoomTypeRate.equals(new BigDecimal("0"))) {
                String totalDaysInString = new String();
                BigDecimal totalDaysInBd = new BigDecimal(totalDaysInString.valueOf(totalDays));
                availableRoomTypeAndRatePerNight.add(rt.getName());
                availableRoomTypeAndRatePerNight.add(periodRoomTypeRate.divide(totalDaysInBd, 2, RoundingMode.CEILING).toString()); //can change to per night if neccessary;
            }
        }

        if (availableRoomTypeAndRatePerNight.isEmpty()) {
            throw new NoRoomTypeAvailableException("No Room Type is available for this period!");
        }

        System.out.println("For debugging: List returned");
        for (String s : availableRoomTypeAndRatePerNight) {
            System.out.println(s);
        }

        return availableRoomTypeAndRatePerNight;
    }

    @Override
    public List<String> searchAvailableRoomTypesOnline(LocalDate checkInDate, LocalDate checkOutDate, int guestNumberOfRooms, int numberOfAdults) throws NoRoomTypeAvailableException, RoomTypeCannotBeFoundException {
        List<String> availableRoomTypeAndRatePerNight = new ArrayList<>();
        List<RoomTypeEntity> roomTypes = roomTypeEntitySessionBeanLocal.retrieveAllRoomTypes();
        List<RoomEntity> rooms = roomEntitySessionBeanLocal.retrieveAllAvailableRooms();

        List<ReservationEntity> dateFilteredreservations = reservationEntitySessionBeanLocal.retrieveAllReservationsBySearchDates(checkInDate, checkOutDate);

        //for debug
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
        List<RoomTypeEntity> capacityFilteredRoomTypes = new ArrayList<>();

        for (RoomTypeEntity rt : roomTypes) {
            if (rt.getCapacity() >= numberOfAdults) {
                capacityFilteredRoomTypes.add(rt);
            }
        }

        for (RoomTypeEntity rt : capacityFilteredRoomTypes) {
            int totalRoomsAvailable = 0;
            for (RoomEntity r : rooms) {
                r.getRoomType();
                if (r.getRoomType().equals(rt) && r.isRoomStatusAvail() && !r.isDisabled()) {
                    totalRoomsAvailable++;
                }
            }
            System.out.println("For debugging: totalRoomsAvailable = " + rt.getName() + " " + totalRoomsAvailable);
            int periodAvailableRooms = totalRoomsAvailable;
            BigDecimal periodRoomTypeRate = new BigDecimal("0");
            int totalDays = 0;

            //Looping through every day
            for (LocalDate dailyDate = checkInDate; dailyDate.isBefore(checkOutDate); dailyDate = dailyDate.plusDays(1)) {
                totalDays += 1;
                System.out.println("For debugging: dailyDate loop: " + dailyDate.toString());
                int dailyAvailableRooms = totalRoomsAvailable;
                
                try {
                    periodRoomTypeRate = periodRoomTypeRate.add(roomRateSessionBeanLocal.selectDailyRoomRate(dailyDate, rt.getRoomTypeId(), true).getRate());
                    System.out.println("For debugging: Daily Room Rate = " + roomRateSessionBeanLocal.selectDailyRoomRate(dailyDate, rt.getRoomTypeId(), true).getRate());
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
            System.out.println("For debugging: periodRoomsAvailable = " + rt.getName() + " " + periodAvailableRooms);
            System.out.println("For debugging: periodRoomRate = " + rt.getName() + " " + periodRoomTypeRate.toString());
            //if avail rooms > guest required number of rooms
            if (periodAvailableRooms >= guestNumberOfRooms && !periodRoomTypeRate.equals(new BigDecimal("0"))) {
                String totalDaysInString = new String();
                BigDecimal totalDaysInBd = new BigDecimal(totalDaysInString.valueOf(totalDays));
                availableRoomTypeAndRatePerNight.add(rt.getName());
                availableRoomTypeAndRatePerNight.add(periodRoomTypeRate.divide(totalDaysInBd, 2, RoundingMode.CEILING).toString()); //can change to per night if neccessary;
            }
        }

        if (availableRoomTypeAndRatePerNight.isEmpty()) {
            throw new NoRoomTypeAvailableException("No Room Type is available for this period!");
        }

        System.out.println("For debugging: List returned");
        for (String s : availableRoomTypeAndRatePerNight) {
            System.out.println(s);
        }

        return availableRoomTypeAndRatePerNight;
    }

}
