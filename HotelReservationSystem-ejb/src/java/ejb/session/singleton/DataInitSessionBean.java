/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.EmployeeEntitySessionBeanLocal;
import entity.EmployeeEntity;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import util.enumeration.EmployeeAccessRightEnum;
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
   
    public DataInitSessionBean() {
    }

    @PostConstruct
    public void postConstruct() {
        try
        {
            employeeEntitySessionBeanLocal.retrieveEmployeeByUsername("manager");
        }
        catch(EmployeeNotFoundException ex)
        {
            initializeData();
        }
     }

    public void initializeData() {
        employeeEntitySessionBeanLocal.createNewEmployee(new EmployeeEntity("Yunus", "Ali", "admin", "password", EmployeeAccessRightEnum.SYSTEMADMIN));
        employeeEntitySessionBeanLocal.createNewEmployee(new EmployeeEntity("Eugene", "Chua", "opsmanager", "password", EmployeeAccessRightEnum.OPSMANAGER));
        employeeEntitySessionBeanLocal.createNewEmployee(new EmployeeEntity("Eugene", "Chua", "salesmanager", "password", EmployeeAccessRightEnum.SALESMANAGER));
        employeeEntitySessionBeanLocal.createNewEmployee(new EmployeeEntity("Yunus", "Ali", "guestofficer", "password", EmployeeAccessRightEnum.GUESTRELATIONSOFFICER));
       
    }
       
}
