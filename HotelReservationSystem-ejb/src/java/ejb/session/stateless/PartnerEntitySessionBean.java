package ejb.session.stateless;

import entity.PartnerEntity;
import entity.ReservationEntity;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.PartnerNotFoundException;
import util.exception.InvalidLoginCredentialException;
import util.exception.ReservationCannotBeFoundException;


@Stateless
public class PartnerEntitySessionBean implements PartnerEntitySessionBeanRemote, PartnerEntitySessionBeanLocal {

    @EJB
    private ReservationEntitySessionBeanLocal reservationEntitySessionBeanLocal;

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;
    

    public PartnerEntitySessionBean() {
    }

    @Override
    public Long createNewPartner(PartnerEntity newPartnerEntity)
    {
        em.persist(newPartnerEntity);
        em.flush();
        
        return newPartnerEntity.getPartnerId();
    }
    
    
    @Override
    public List<PartnerEntity> retrieveAllPartners()
    {
        Query query = em.createQuery("SELECT s FROM PartnerEntity s");
        
        return query.getResultList();
    }
    
    
    
    @Override
    public PartnerEntity retrievePartnerByPartnerId(Long partnerId) throws PartnerNotFoundException
    {
        PartnerEntity partnerEntity = em.find(PartnerEntity.class, partnerId);
        
        if(partnerEntity != null)
        {
            return partnerEntity;
        }
        else
        {
            throw new PartnerNotFoundException("Partner ID " + partnerId + " does not exist!");
        }
    }
    
    
    @Override
    public PartnerEntity retrievePartnerByUsername(String username) throws PartnerNotFoundException {
        Query query = em.createQuery("SELECT s FROM PartnerEntity s WHERE s.username = :inUsername");
        query.setParameter("inUsername", username);
        
        try
        {
            return (PartnerEntity)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new PartnerNotFoundException("Partner Username " + username + " does not exist!");
        }
    }
    
    
    @Override
    public PartnerEntity partnerLogin (String username, String password) throws InvalidLoginCredentialException{
        try
        {
            PartnerEntity partnerEntity = retrievePartnerByUsername(username);
            
            if(partnerEntity.getPassword().equals(password))
            {
                return partnerEntity;
            }
            else
            {
                throw new InvalidLoginCredentialException("Invalid password!");
            }
        }
        catch(PartnerNotFoundException ex)
        {
            throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
        }
    }
    
    @Override
    public PartnerEntity addParnterReservation(Long reservationEntityId, Long partnerId) throws ReservationCannotBeFoundException,PartnerNotFoundException{
        
        ReservationEntity reservationEntity = reservationEntitySessionBeanLocal.retrieveReservationById(reservationEntityId);
        PartnerEntity partnerEntity = retrievePartnerByPartnerId(partnerId);
        partnerEntity.getPartnerReservations().add(reservationEntity);
        
        return partnerEntity;
    }
}
