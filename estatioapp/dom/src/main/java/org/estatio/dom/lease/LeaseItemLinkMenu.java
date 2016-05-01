
package org.estatio.dom.lease;

import java.util.List;

import javax.inject.Inject;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.RestrictTo;

@DomainService(nature = NatureOfService.VIEW_MENU_ONLY)
@DomainServiceLayout(
        named = "Leases",
        menuBar = DomainServiceLayout.MenuBar.PRIMARY,
        menuOrder = "40.1"
)
public class LeaseItemLinkMenu {

    @Action(restrictTo = RestrictTo.PROTOTYPING)
    public List<LeaseItemLink> allLeaseItemLinks(){
        return leaseItemLinkRepository.allLeaseItemLinks();
    }

    @Inject
    private LeaseItemLinkRepository leaseItemLinkRepository;
}
