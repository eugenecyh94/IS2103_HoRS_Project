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
import util.enumeration.GuestNotFoundException;
import util.exception.InvalidLoginCredentialException;

@Stateless
public class GuestEntitySessionBean implements GuestEntitySessionBeanRemote, GuestEntitySessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    public GuestEntitySessionBean() {
    }

       
    @Override
    public Long registerAsGuest (GuestEntity newGuestEntity)
    {
        em.persist(newGuestEntity);
        em.flush();
        
        return newGuestEntity.getGuestId();
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
    
    public List<ReservationEntity> viewAllReservations(Long guestId) {
        
        
    }
    
    public ReservationEntity viewMyReservation (Long guesId, Long bookingId) {
        
    }
    
    
}
