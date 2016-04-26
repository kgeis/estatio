
package org.estatio.dom.lease.contributed;

import java.util.List;

import javax.inject.Inject;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.CollectionLayout;
import org.apache.isis.applib.annotation.Contributed;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.SemanticsOf;

import org.estatio.dom.lease.LeaseItem;
import org.estatio.dom.lease.LeaseItemLink;
import org.estatio.dom.lease.LeaseItemLinkRepository;
import org.estatio.dom.lease.LeaseItemType;

@DomainService(nature = NatureOfService.VIEW_CONTRIBUTIONS_ONLY)
public class LeaseItemLinkContributions {


    @ActionLayout(contributed = Contributed.AS_ACTION)
    public LeaseItem createLink(final LeaseItem sourceItem, final LeaseItem linkedItem){
        leaseItemLinkRepository.newLeaseItemLink(sourceItem, linkedItem);
        return sourceItem;
    }

    public List<LeaseItem> choices1CreateLink(final LeaseItem sourceItem) {
        return leaseItemLinkRepository.choicesCreateLink(sourceItem);
    }

    public boolean hideCreateLink(final LeaseItem sourceItem, final LeaseItem linkedItem){
        return leaseItemLinkRepository.hideCreateLink(sourceItem);
    }

    public String validateCreateLink(final LeaseItem sourceItem, final LeaseItem linkedItem){
        return leaseItemLinkRepository.validateCreateLink(sourceItem, linkedItem);
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(contributed = Contributed.AS_ASSOCIATION)
    @CollectionLayout(defaultView = "table")
    public List<LeaseItemLink> linkedItems(final LeaseItem sourceItem) {
        return leaseItemLinkRepository.findBySourceItem(sourceItem);
    }

    public boolean hideLinkedItems(final LeaseItem sourceItem){
        if (sourceItem.getType()== LeaseItemType.DEPOSIT){
            return false;
        }
        return true;
    }

    @Inject
    private LeaseItemLinkRepository leaseItemLinkRepository;
}
