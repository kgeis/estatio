
package org.estatio.dom.lease;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.SemanticsOf;

import org.estatio.dom.UdoDomainRepositoryAndFactory;

@DomainService(repositoryFor = LeaseItemLink.class, nature = NatureOfService.DOMAIN)
public class LeaseItemLinkRepository extends UdoDomainRepositoryAndFactory<LeaseItemLink> {

    public LeaseItemLinkRepository() {
        super(LeaseItemLinkRepository.class, LeaseItemLink.class);
    }

    // //////////////////////////////////////

    @Action(semantics = SemanticsOf.NON_IDEMPOTENT)
    public LeaseItemLink createLeaseItemLink(
            final LeaseItem sourceItem,
            final LeaseItem linkedItem) {
        LeaseItemLink leaseItemLink = newTransientInstance();
        leaseItemLink.setSourceItem(sourceItem);
        leaseItemLink.setLinkedItem(linkedItem);
        persistIfNotAlready(leaseItemLink);
        return leaseItemLink;
    }

    @Action(semantics = SemanticsOf.SAFE)
    public List<LeaseItemLink> findBySourceItem(final LeaseItem sourceItem) {
        return allMatches("findBySourceItem", "sourceItem", sourceItem);
    }

    @Programmatic
    public boolean hideCreateLink(final LeaseItem sourceItem){
        return sourceItem.getType() != LeaseItemType.DEPOSIT;
    }

    @Programmatic
    public String validateCreateLink(final LeaseItem sourceItem, final LeaseItem linkedItem){

        if (sourceItem.getType()!=LeaseItemType.DEPOSIT){
            return "Only items of type deposit can be linked";
        }

        if (!validChoices.contains(linkedItem.getType())){
            return "A deposit cannot be linked to an Item of type ".concat(linkedItem.getType().title());
        }

        List<LeaseItemLink> existingLinks = findBySourceItem(sourceItem);
        for (LeaseItemLink link : existingLinks){
            if (link.getLinkedItem()==linkedItem){
                return "This item is already linked";
            }
        }

        return null;
    }

    @Programmatic
    public List<LeaseItem> choicesCreateLink(final LeaseItem sourceItem) {
        List<LeaseItem> choices = new ArrayList<>();
        for (LeaseItem item : sourceItem.getLease().getItems()){
            if (validChoices.contains(item.getType())){
                choices.add(item);
            }
        }
        return choices;
    }

    private static List<LeaseItemType> validChoices = Arrays.asList(LeaseItemType.RENT, LeaseItemType.SERVICE_CHARGE, LeaseItemType.SERVICE_CHARGE_INDEXABLE, LeaseItemType.SERVICE_CHARGE_BUDGETED);

    @Programmatic
    public List<LeaseItem> findLinkedItemsBySourceItem(final LeaseItem leaseItem) {
        List<LeaseItem> result = new ArrayList<>();
        for (LeaseItemLink link : findBySourceItem(leaseItem)){
                result.add(link.getLinkedItem());
        }
        return result;
    }

    @Programmatic
    public List<LeaseItem> findLinkedItemsBySourceItemAndType(final LeaseItem leaseItem, final LeaseItemType leaseItemType) {
        List<LeaseItem> result = new ArrayList<>();
        for (LeaseItem item : findLinkedItemsBySourceItem(leaseItem)){
            if (item.getType()==leaseItemType){
                result.add(item);
            }
        }
        return result;
    }

    @Programmatic
    public LeaseItemLink findBySourceItemAndLinkedItem(final LeaseItem sourceItem, final LeaseItem linkedItem) {
        return uniqueMatch("findBySourceItemAndLinkedItem", "sourceItem", sourceItem, "linkedItem", linkedItem);
    }

    @Programmatic
    public LeaseItemLink findOrCreateLeaseItemLink(final LeaseItem sourceItem, final LeaseItem linkedItem) {
        LeaseItemLink link = findBySourceItemAndLinkedItem(sourceItem, linkedItem);
        if (link == null) {
            link = createLeaseItemLink(sourceItem, linkedItem);
        }
        return link;
    }

    @Programmatic
    public List<LeaseItemLink> allLeaseItemLinks(){
        return allInstances(LeaseItemLink.class);
    }
}

