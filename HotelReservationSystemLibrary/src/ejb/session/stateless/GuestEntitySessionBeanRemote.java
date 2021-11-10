/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.GuestEntity;
import entity.ReservationEntity;
import java.util.List;
import javax.ejb.Remote;
import util.exception.GuestNotFoundException;
import util.exception.InvalidLoginCredentialException;
import util.exception.ReservationCannotBeFoundException;

/**
 *
 * @author Eugene Chua
 */
@Remote
public interface GuestEntitySessionBeanRemote {

    public GuestEntity retrieveGuestByGuestId(Long guestId) throws GuestNotFoundException;

    public GuestEntity retrieveGuestByUsername(String username) throws GuestNotFoundException;

    public GuestEntity guestLogin(String username, String password) throws InvalidLoginCredentialException;

    public List<ReservationEntity> viewAllReservations(Long guestId) throws GuestNotFoundException;

    public ReservationEntity viewMyReservation(Long bookingId) throws ReservationCannotBeFoundException;

    public GuestEntity registerAsGuest(GuestEntity newGuestEntity);
}
