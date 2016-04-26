
package org.estatio.dom.lease;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;

import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.annotation.Where;

import org.isisaddons.module.security.dom.tenancy.ApplicationTenancy;

import org.estatio.dom.EstatioDomainObject;
import org.estatio.dom.apptenancy.WithApplicationTenancyPropertyLocal;
import org.estatio.dom.utils.TitleBuilder;

import lombok.Getter;
import lombok.Setter;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(
        strategy = IdGeneratorStrategy.NATIVE,
        column = "id")
@javax.jdo.annotations.Queries({
        @javax.jdo.annotations.Query(
                name = "findBySourceItem", language = "JDOQL",
                value = "SELECT "
                        + "FROM org.estatio.dom.lease.LeaseItemLink "
                        + "WHERE sourceItem == :sourceItem")
})
@DomainObject()
public class LeaseItemLink
        extends EstatioDomainObject<LeaseItemLink>
        implements WithApplicationTenancyPropertyLocal {

    public LeaseItemLink() {
        super("sourceItem, linkedItem");
    }

    public String title() {
        return TitleBuilder.start()
                .withName(getSourceItem())
                .withName(getLinkedItem())
                .toString();
    }

    @Getter @Setter
    @Column(name = "sourceItemId", allowsNull = "false")
    @PropertyLayout(hidden = Where.REFERENCES_PARENT)
    private LeaseItem sourceItem;

    @Getter @Setter
    @Column(name = "linkedItemId", allowsNull = "false")
    private LeaseItem linkedItem;

    @PropertyLayout(hidden = Where.EVERYWHERE)
    @Override
    public ApplicationTenancy getApplicationTenancy() {
        return sourceItem.getApplicationTenancy();
    }
}
