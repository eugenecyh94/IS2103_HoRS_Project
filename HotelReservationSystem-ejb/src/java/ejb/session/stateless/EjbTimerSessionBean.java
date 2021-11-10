/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.ejb.Schedule;
import javax.ejb.Stateless;

/**
 *
 * @author Eugene Chua
 */
@Stateless
public class EjbTimerSessionBean implements EjbTimerSessionBeanRemote, EjbTimerSessionBeanLocal {

     //@Schedule(hour = "*", minute = "*", second = "*/5", info = "productEntityReorderQuantityCheckTimer")    
    // For testing purpose, we are allowing the timer to trigger every 5 seconds instead of every 5 minutes
    // To trigger the timer once every 5 minutes instead, use the following the @Schedule annotation
    // @Schedule(hour = "*", minute = "*/5", info = "productEntityReorderQuantityCheckTimer")
/*    public void productEntityReorderQuantityCheckTimer()
    {
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        System.out.println("********** EjbTimerSessionBean.productEntityReorderQuantityCheckTimer(): Timeout at " + timeStamp);
        
        List<ProductEntity> productEntities = productEntitySessionBeanLocal.retrieveAllProducts();
        
        for(ProductEntity productEntity:productEntities)
        {
            if(productEntity.getQuantityOnHand().compareTo(productEntity.getReorderQuantity()) <= 0)
            {
                System.out.println("********** Product " + productEntity.getSkuCode() + " requires reordering: QOH = " + productEntity.getQuantityOnHand() + "; RQ = " + productEntity.getReorderQuantity());
            }
        }
    }
*/
}