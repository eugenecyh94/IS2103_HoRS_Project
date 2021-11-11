package ejb.session.stateless;

import entity.GuestEntity;
import entity.ReservationEntity;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.GuestNotFoundException;
import util.exception.ReservationCannotBeFoundException;

@Stateless
public class ReservationEntitySessionBean implements ReservationEntitySessionBeanRemote, ReservationEntitySessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    public ReservationEntitySessionBean() {
    }

    @Override // to amend to only input reservationtype object for para later
    public ReservationEntity createNewReservation(ReservationEntity reservation) {

        em.persist(reservation);
        em.flush();

        return reservation;

    }

    @Override
    public ReservationEntity retrieveReservationById(Long reservationId) throws ReservationCannotBeFoundException {

        ReservationEntity reservation = em.find(ReservationEntity.class, reservationId);

        if (reservation == null) {
            throw new ReservationCannotBeFoundException("Reservation does not exist for the entered ID!");
        }

        reservation.getRoomType();
        reservation.getGuest();

        return reservation;

    }

    //how about retrieving reservation detail by passport number and guest id?
    @Override
    public List<ReservationEntity> retrieveAllReservationsByGuestId(Long guestId) throws GuestNotFoundException {

        GuestEntity guest = em.find(GuestEntity.class, guestId);

        if (guest == null) {
            throw new GuestNotFoundException("Guest does not exists for the entered ID!");
        }

        Query query = em.createQuery("SELECT rs FROM ReservationEntity rs WHERE rs.guest := inGuest");
        query.setParameter("inGuest", guest);
        List<ReservationEntity> reservations = query.getResultList();
        reservations.size();

        return reservations;

    }

    //used for room allocation session bean, only check in date queried
    @Override
    public List<ReservationEntity> retrieveAllReservationsByCheckInDate(LocalDate reservationCheckInDate) {
        
        Query query = em.createQuery("SELECT rs FROM ReservationEntity rs");
        List<ReservationEntity> reservations = query.getResultList();
        List<ReservationEntity> dateFilteredReservations = new ArrayList<>();
        
        for (ReservationEntity rs : reservations) {
            if(rs.getCheckInDate().isEqual(reservationCheckInDate)){
                dateFilteredReservations.add(rs);
            }
        }
        
        return dateFilteredReservations;
        
    }
    
    //used for room searching in search session bean, check in and check out date queried
    @Override
    public List<ReservationEntity> retrieveAllReservationsBySearchDates(LocalDate guestCheckInDate, LocalDate guestCheckOutDate) {

        Query query = em.createQuery("SELECT rs FROM ReservationEntity rs");
        List<ReservationEntity> reservations = query.getResultList();
        List<ReservationEntity> dateFilteredReservations = new ArrayList<>();

        for (LocalDate dailyDate = guestCheckInDate; dailyDate.isEqual(guestCheckOutDate); dailyDate.plusDays(1)) {
            for (ReservationEntity rs : reservations) {
                if (!(dailyDate.isBefore(rs.getCheckInDate()) || dailyDate.isAfter(rs.getCheckOutDate()))) {
                    if (!dateFilteredReservations.contains(rs)) {
                        rs.getRoomType();
                        rs.getGuest();
                        dateFilteredReservations.add(rs);
                    }
                }
            }
        }
        return dateFilteredReservations;
    }

}
