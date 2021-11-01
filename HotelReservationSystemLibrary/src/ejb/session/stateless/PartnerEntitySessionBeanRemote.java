/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.PartnerEntity;
import java.util.List;
import javax.ejb.Remote;
import util.exception.InvalidLoginCredentialException;
import util.exception.PartnerNotFoundException;

/**
 *
 * @author Eugene Chua
 */
@Remote
public interface PartnerEntitySessionBeanRemote {

    public Long createNewPartner(PartnerEntity newPartnerEntity);

    public List<PartnerEntity> retrieveAllPartners();

    public PartnerEntity retrievePartnerByPartnerId(Long partnerId) throws PartnerNotFoundException;

    public PartnerEntity partnerLogin(String username, String password) throws InvalidLoginCredentialException;

    public PartnerEntity retrievePartnerByUsername(String username) throws PartnerNotFoundException;
    
}
