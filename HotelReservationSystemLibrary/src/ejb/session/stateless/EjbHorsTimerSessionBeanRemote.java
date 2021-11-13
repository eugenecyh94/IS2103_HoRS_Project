/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.DailyExceptionReportEntity;
import java.time.LocalDate;
import javax.ejb.Remote;
import util.exception.NoAllocationExceptionReportException;

/**
 *
 * @author Eugene Chua
 */
@Remote
public interface EjbHorsTimerSessionBeanRemote {

    public void automatedRoomAllocation();

    public void manualRoomAllocation(LocalDate date);

    public void deleteExceptionReport(Long exceptionReportId);

    public DailyExceptionReportEntity viewCurrentDayAllocationExceptionReport() throws NoAllocationExceptionReportException;

    public DailyExceptionReportEntity viewSpecificDayAllocationExceptionReport(LocalDate date) throws NoAllocationExceptionReportException;
    
}
