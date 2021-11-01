package ejb.session.stateless;

import entity.EmployeeEntity;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.EmployeeNotFoundException;
import util.exception.InvalidLoginCredentialException;

@Stateless
public class EmployeeEntitySessionBean implements EmployeeEntitySessionBeanRemote, EmployeeEntitySessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    public EmployeeEntitySessionBean() {
    }
    
    @Override
    public Long createNewEmployee(EmployeeEntity newEmployeeEntity)
    {
        em.persist(newEmployeeEntity);
        em.flush();
        
        return newEmployeeEntity.getEmployeeId();
    }
    
    
    
    @Override
    public List<EmployeeEntity> retrieveAllEmployees()
    {
        Query query = em.createQuery("SELECT s FROM EmployeeEntity s");
        
        return query.getResultList();
    }
    
    
    
    @Override
    public EmployeeEntity retrieveEmployeeByEmployeeId(Long employeeId) throws EmployeeNotFoundException
    {
        EmployeeEntity employeeEntity = em.find(EmployeeEntity.class, employeeId);
        
        if(employeeEntity != null)
        {
            return employeeEntity;
        }
        else
        {
            throw new EmployeeNotFoundException("Employee ID " + employeeId + " does not exist!");
        }
    }
    
    
    @Override
    public EmployeeEntity retrieveEmployeeByUsername(String username) throws EmployeeNotFoundException {
        Query query = em.createQuery("SELECT s FROM EmployeeEntity s WHERE s.username = :inUsername");
        query.setParameter("inUsername", username);
        
        try
        {
            return (EmployeeEntity)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new EmployeeNotFoundException("Employee Username " + username + " does not exist!");
        }
    }
    
    
    @Override
    public EmployeeEntity employeeLogin (String username, String password) throws InvalidLoginCredentialException{
        try
        {
            EmployeeEntity employeeEntity = retrieveEmployeeByUsername(username);
            
            if(employeeEntity.getPassword().equals(password))
            {
                return employeeEntity;
            }
            else
            {
                throw new InvalidLoginCredentialException("Invalid password!");
            }
        }
        catch(EmployeeNotFoundException ex)
        {
            throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
        }
    }
}
