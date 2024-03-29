package ejb.session.stateless;

import entity.GuestEntity;
import entity.ReservationEntity;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.GuestNotFoundException;
import util.exception.InvalidLoginCredentialException;
import util.exception.ReservationCannotBeFoundException;

@Stateless
public class GuestEntitySessionBean implements GuestEntitySessionBeanRemote, GuestEntitySessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    private ReservationEntitySessionBeanLocal reservationEntitySessionBeanLocal;
    public GuestEntitySessionBean() {
    }

       
    @Override
    public GuestEntity registerAsGuest (GuestEntity newGuestEntity)
    {
        em.persist(newGuestEntity);
        em.flush();
        
        return newGuestEntity;
    }
    
    @Override
    public GuestEntity retrieveGuestByGuestId(Long guestId) throws GuestNotFoundException
    {
        GuestEntity guestEntity = em.find(GuestEntity.class, guestId);
        
        if(guestEntity != null)
        {
            return guestEntity;
        }
        else
        {
            throw new GuestNotFoundException("Guest ID " + guestId + " does not exist!");
        }
    }
    
    
    @Override
    public GuestEntity retrieveGuestByPassportNumber(String passportNumber) throws GuestNotFoundException {
        Query query = em.createQuery("SELECT s FROM GuestEntity s WHERE s.passportNumber = :inPassportNumber");
        query.setParameter("inPassportNumber", passportNumber);
        
        try
        {
            return (GuestEntity)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new GuestNotFoundException("Guest with passport number :  " + passportNumber + " does not exist!");
        }
    }
    
    @Override
    public GuestEntity retrieveGuestByUsername(String username) throws GuestNotFoundException {
        Query query = em.createQuery("SELECT s FROM GuestEntity s WHERE s.username = :inUsername");
        query.setParameter("inUsername", username);
        
        try
        {
            return (GuestEntity)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new GuestNotFoundException("Guest Username " + username + " does not exist!");
        }
    }
    
    
    @Override
    public GuestEntity guestLogin (String username, String password) throws InvalidLoginCredentialException{
        try
        {
            GuestEntity guestEntity = retrieveGuestByUsername(username);
            
            if(guestEntity.getPassword().equals(password))
            {
                return guestEntity;
            }
            else
            {
                throw new InvalidLoginCredentialException("Invalid password!");
            }
        }
        catch(GuestNotFoundException ex)
        {
            throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
        }
    }
    
    public List<ReservationEntity> viewAllReservations(Long guestId) throws GuestNotFoundException {
        List<ReservationEntity> reservationEntities = reservationEntitySessionBeanLocal.retrieveAllReservationsByGuestId(guestId);
        return reservationEntities;
    }
    
    public ReservationEntity viewMyReservation (Long bookingId) throws ReservationCannotBeFoundException {
        ReservationEntity reservationEntity = reservationEntitySessionBeanLocal.retrieveReservationById(bookingId);
        return reservationEntity;
    }
    
    
}
