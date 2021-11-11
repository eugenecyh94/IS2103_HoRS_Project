/* 
 * To change this license header, choose License Headers in Project Properties. 
 * To change this template file, choose Tools | Templates 
 * and open the template in the editor. 
 */ 
package ejb.session.singleton; 
 
import ejb.session.stateless.EmployeeEntitySessionBeanLocal; 
import ejb.session.stateless.RoomEntitySessionBeanLocal; 
import ejb.session.stateless.RoomRateSessionBeanLocal; 
import ejb.session.stateless.RoomTypeEntitySessionBeanLocal; 
import entity.EmployeeEntity; 
import entity.RoomEntity; 
import entity.RoomTypeEntity; 
import java.util.ArrayList; 
import javax.annotation.PostConstruct; 
import javax.ejb.EJB; 
import javax.ejb.Singleton; 
import javax.ejb.LocalBean; 
import javax.ejb.Startup; 
import util.enumeration.BedSizeEnum; 
import util.enumeration.EmployeeAccessRightEnum; 
import util.enumeration.RoomAmenitiesEnum; 
import util.exception.EmployeeNotFoundException; 
 
/** 
 * 
 * @author yunus 
 */ 
@Singleton 
@LocalBean 
@Startup 
public class DataInitSessionBean { 
 
    @EJB 
    private EmployeeEntitySessionBeanLocal employeeEntitySessionBeanLocal; 
    @EJB 
    RoomTypeEntitySessionBeanLocal roomTypeEntitySessionBeanLocal; 
    @EJB 
    RoomRateSessionBeanLocal roomRateSessionBeanLocal; 
    @EJB 
    RoomEntitySessionBeanLocal roomEntitySessionBeanLocal; 
     
    
    public DataInitSessionBean() { 
    } 
 
    @PostConstruct 
    public void postConstruct() { 
        try 
        { 
            employeeEntitySessionBeanLocal.retrieveEmployeeByUsername("sysadmin"); 
        } 
        catch(EmployeeNotFoundException ex) 
        { 
            initializeData(); 
        } 
     } 
 
    public void initializeData() { 
        employeeEntitySessionBeanLocal.createNewEmployee(new EmployeeEntity("Yunus", "Ali", "sysadmin", "password", EmployeeAccessRightEnum.SYSTEMADMIN)); 
        employeeEntitySessionBeanLocal.createNewEmployee(new EmployeeEntity("Eugene", "Chua", "opmanager", "password", EmployeeAccessRightEnum.OPSMANAGER)); 
        employeeEntitySessionBeanLocal.createNewEmployee(new EmployeeEntity("Eugene", "Chua", "salesmanager", "password", EmployeeAccessRightEnum.SALESMANAGER)); 
        employeeEntitySessionBeanLocal.createNewEmployee(new EmployeeEntity("Yunus", "Ali", "guestrelo", "password", EmployeeAccessRightEnum.GUESTRELATIONSOFFICER)); 
        
        RoomTypeEntity deluxeRoom = roomTypeEntitySessionBeanLocal.createNewRoomType(new RoomTypeEntity("Deluxe Room", "Deluxue Room Description", 2, "20sqm" , BedSizeEnum.SINGLE, new ArrayList<RoomAmenitiesEnum>(), "Premier Rooom")); 
        RoomTypeEntity premierRoom = roomTypeEntitySessionBeanLocal.createNewRoomType(new RoomTypeEntity("Premier Room", "Premier Room Description", 2, "22sqm" , BedSizeEnum.SUPERSINGLE, new ArrayList<RoomAmenitiesEnum>(), "Family Room")); 
        RoomTypeEntity familyRoom = roomTypeEntitySessionBeanLocal.createNewRoomType(new RoomTypeEntity("Family Room", "Family Room Description", 4, "24sqm" , BedSizeEnum.QUEEN, new ArrayList<RoomAmenitiesEnum>(), "Junior Suite")); 
        RoomTypeEntity gradeSuite = roomTypeEntitySessionBeanLocal.createNewRoomType(new RoomTypeEntity("Junior Suite", "Junior Suite Description", 4, "26sqm" , BedSizeEnum.KING, new ArrayList<RoomAmenitiesEnum>(), "Premier Room")); 
    } 
        
}