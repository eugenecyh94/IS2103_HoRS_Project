/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.ReservationEntity;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import util.exception.GuestNotFoundException;
import util.exception.ReservationCannotBeFoundException;

/**
 *
 * @author Eugene Chua
 */
@Local
public interface ReservationEntitySessionBeanLocal {

    public ReservationEntity createNewReservation(ReservationEntity reservation);

    public ReservationEntity retrieveReservationById(Long reservationId) throws ReservationCannotBeFoundException;

    public List<ReservationEntity> retrieveAllReservationsByGuestId(Long guestId) throws GuestNotFoundException;

    public List<ReservationEntity> retrieveAllReservationsByDates(LocalDate guestCheckInDate, LocalDate guestCheckOutDate);

}
