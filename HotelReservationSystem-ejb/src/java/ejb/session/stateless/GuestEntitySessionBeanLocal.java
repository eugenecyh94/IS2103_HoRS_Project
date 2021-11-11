/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.GuestEntity;
import entity.ReservationEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.GuestNotFoundException;
import util.exception.InvalidLoginCredentialException;
import util.exception.ReservationCannotBeFoundException;

/**
 *
 * @author Eugene Chua
 */
@Local
public interface GuestEntitySessionBeanLocal {

    public GuestEntity retrieveGuestByGuestId(Long guestId) throws GuestNotFoundException;

    public GuestEntity retrieveGuestByUsername(String username) throws GuestNotFoundException;

    public GuestEntity guestLogin(String username, String password) throws InvalidLoginCredentialException;

    public List<ReservationEntity> viewAllReservations(Long guestId) throws GuestNotFoundException;

    public ReservationEntity viewMyReservation(Long bookingId) throws ReservationCannotBeFoundException;

    public GuestEntity registerAsGuest(GuestEntity newGuestEntity);

    public GuestEntity retrieveGuestByPassportNumber(String passportNumber) throws GuestNotFoundException;
}
