package org.estatio.dom.lease;

import org.apache.isis.applib.ApplicationException;
import org.apache.isis.applib.DomainObjectContainer;


public enum LeaseItemType {

    RENT("Rent", LeaseTermForIndexableRent.class), 
    TURNOVER_RENT("Turnover Rent", LeaseTermForTurnoverRent.class),
    DISCOUNT("Discount", LeaseTerm.class), 
    SERVICE_CHARGE("Service Charge", LeaseTerm.class), 
    OTHER("Other", LeaseTerm.class);

    private final String title;
    private final Class<? extends LeaseTerm> clss;

    private LeaseItemType(String title, Class<? extends LeaseTerm> clss) {
        this.title = title;
        this.clss = clss;
    }

    public String title() {
        return title;
    }
        
    public LeaseTerm createLeaseTerm(DomainObjectContainer container){ 
        try {
            LeaseTerm term = container.newTransientInstance(clss);
            return term;
        } catch (Exception ex) {
            throw new ApplicationException(ex);
        }
    }

    
    
    
    
}
