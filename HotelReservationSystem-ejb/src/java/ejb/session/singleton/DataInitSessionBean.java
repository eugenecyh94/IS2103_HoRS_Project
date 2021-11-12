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
import entity.RoomRateEntity;
import entity.RoomTypeEntity; 
import java.math.BigDecimal;
import java.util.ArrayList; 
import javax.annotation.PostConstruct; 
import javax.ejb.EJB; 
import javax.ejb.Singleton; 
import javax.ejb.LocalBean; 
import javax.ejb.Startup; 
import util.enumeration.BedSizeEnum; 
import util.enumeration.EmployeeAccessRightEnum; 
import util.enumeration.RateTypeEnum;
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
        
        RoomTypeEntity deluxeRoom = roomTypeEntitySessionBeanLocal.createNewRoomType(new RoomTypeEntity("Deluxe Room", "Deluxue Room Description", 2, "20sqm" , BedSizeEnum.SINGLE, new ArrayList<String>(), "Premier Rooom")); 
        RoomTypeEntity premierRoom = roomTypeEntitySessionBeanLocal.createNewRoomType(new RoomTypeEntity("Premier Room", "Premier Room Description", 2, "22sqm" , BedSizeEnum.SUPERSINGLE, new ArrayList<String>(), "Family Room")); 
        RoomTypeEntity familyRoom = roomTypeEntitySessionBeanLocal.createNewRoomType(new RoomTypeEntity("Family Room", "Family Room Description", 4, "24sqm" , BedSizeEnum.QUEEN, new ArrayList<String>(), "Junior Suite")); 
        RoomTypeEntity juniorSuite = roomTypeEntitySessionBeanLocal.createNewRoomType(new RoomTypeEntity("Junior Suite", "Junior Suite Description", 4, "26sqm" , BedSizeEnum.KING, new ArrayList<String>(), "Grand Suite")); 
        RoomTypeEntity grandSuite = roomTypeEntitySessionBeanLocal.createNewRoomType(new RoomTypeEntity("Grand Suite", "Grand Suite Description", 4, "26sqm" , BedSizeEnum.KING, new ArrayList<String>(), "None")); 
        
        roomRateSessionBeanLocal.createNewRoomRate(new RoomRateEntity("Deluxe Room Published",RateTypeEnum.PUBLISHED,new BigDecimal("100"),deluxeRoom));
        roomRateSessionBeanLocal.createNewRoomRate(new RoomRateEntity("Deluxe Room Normal",RateTypeEnum.NORMAL,new BigDecimal("50"),deluxeRoom));
        roomRateSessionBeanLocal.createNewRoomRate(new RoomRateEntity("Premier Room Published",RateTypeEnum.PUBLISHED,new BigDecimal("200"),premierRoom));
        roomRateSessionBeanLocal.createNewRoomRate(new RoomRateEntity("Premier Room Normal",RateTypeEnum.NORMAL,new BigDecimal("100"),premierRoom));
        roomRateSessionBeanLocal.createNewRoomRate(new RoomRateEntity("Family Room Published",RateTypeEnum.PUBLISHED,new BigDecimal("300"),familyRoom));
        roomRateSessionBeanLocal.createNewRoomRate(new RoomRateEntity("Family Room Normal",RateTypeEnum.NORMAL,new BigDecimal("150"),familyRoom));
        roomRateSessionBeanLocal.createNewRoomRate(new RoomRateEntity("Junior Suite Published",RateTypeEnum.PUBLISHED,new BigDecimal("400"),juniorSuite));
        roomRateSessionBeanLocal.createNewRoomRate(new RoomRateEntity("Junior Suite Published",RateTypeEnum.NORMAL,new BigDecimal("400"),juniorSuite));
        roomRateSessionBeanLocal.createNewRoomRate(new RoomRateEntity("Grand Suite Published",RateTypeEnum.PUBLISHED,new BigDecimal("500"),grandSuite));
        roomRateSessionBeanLocal.createNewRoomRate(new RoomRateEntity("Grand Suite Published",RateTypeEnum.NORMAL,new BigDecimal("250"),grandSuite));
        
        roomEntitySessionBeanLocal.createNewRoom(new RoomEntity("0101"), deluxeRoom.getRoomTypeId());
        roomEntitySessionBeanLocal.createNewRoom(new RoomEntity("0201"), deluxeRoom.getRoomTypeId());
        roomEntitySessionBeanLocal.createNewRoom(new RoomEntity("0301"), deluxeRoom.getRoomTypeId());
        roomEntitySessionBeanLocal.createNewRoom(new RoomEntity("0401"), deluxeRoom.getRoomTypeId());
        roomEntitySessionBeanLocal.createNewRoom(new RoomEntity("0501"), deluxeRoom.getRoomTypeId());
        roomEntitySessionBeanLocal.createNewRoom(new RoomEntity("0102"), premierRoom.getRoomTypeId());
        roomEntitySessionBeanLocal.createNewRoom(new RoomEntity("0202"), premierRoom.getRoomTypeId());
        roomEntitySessionBeanLocal.createNewRoom(new RoomEntity("0302"), premierRoom.getRoomTypeId());
        roomEntitySessionBeanLocal.createNewRoom(new RoomEntity("0402"), premierRoom.getRoomTypeId());
        roomEntitySessionBeanLocal.createNewRoom(new RoomEntity("0502"), premierRoom.getRoomTypeId());
        roomEntitySessionBeanLocal.createNewRoom(new RoomEntity("0103"), familyRoom.getRoomTypeId());
        roomEntitySessionBeanLocal.createNewRoom(new RoomEntity("0203"), familyRoom.getRoomTypeId());
        roomEntitySessionBeanLocal.createNewRoom(new RoomEntity("0303"), familyRoom.getRoomTypeId());
        roomEntitySessionBeanLocal.createNewRoom(new RoomEntity("0403"), familyRoom.getRoomTypeId());
        roomEntitySessionBeanLocal.createNewRoom(new RoomEntity("0503"), familyRoom.getRoomTypeId());
        roomEntitySessionBeanLocal.createNewRoom(new RoomEntity("0104"), juniorSuite.getRoomTypeId());
        roomEntitySessionBeanLocal.createNewRoom(new RoomEntity("0204"), juniorSuite.getRoomTypeId());
        roomEntitySessionBeanLocal.createNewRoom(new RoomEntity("0304"), juniorSuite.getRoomTypeId());
        roomEntitySessionBeanLocal.createNewRoom(new RoomEntity("0404"), juniorSuite.getRoomTypeId());
        roomEntitySessionBeanLocal.createNewRoom(new RoomEntity("0504"), juniorSuite.getRoomTypeId());
        roomEntitySessionBeanLocal.createNewRoom(new RoomEntity("0105"), grandSuite.getRoomTypeId());
        roomEntitySessionBeanLocal.createNewRoom(new RoomEntity("0205"), grandSuite.getRoomTypeId());
        roomEntitySessionBeanLocal.createNewRoom(new RoomEntity("0305"), grandSuite.getRoomTypeId());
        roomEntitySessionBeanLocal.createNewRoom(new RoomEntity("0405"), grandSuite.getRoomTypeId());
        roomEntitySessionBeanLocal.createNewRoom(new RoomEntity("0505"), grandSuite.getRoomTypeId());
        
    } 
        
}
