package org.estatio.integtests.lease;

import javax.inject.Inject;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import org.apache.isis.applib.fixturescripts.FixtureScript;

import org.estatio.dom.lease.Lease;
import org.estatio.dom.lease.LeaseItem;
import org.estatio.dom.lease.LeaseItemLink;
import org.estatio.dom.lease.LeaseItemLinkRepository;
import org.estatio.dom.lease.LeaseItemType;
import org.estatio.dom.lease.Leases;
import org.estatio.fixture.EstatioBaseLineFixture;
import org.estatio.fixture.lease.LeaseForOxfMiracl005Gb;
import org.estatio.fixture.lease.LeaseItemAndTermsForOxfMiracl005Gb;
import org.estatio.integtests.EstatioIntegrationTest;

public class LeaseLinkRepositoryTest extends EstatioIntegrationTest {

    @Before
    public void setupData() {
        runFixtureScript(new FixtureScript() {
            @Override
            protected void execute(ExecutionContext executionContext) {
                executionContext.executeChild(this, new EstatioBaseLineFixture());
                executionContext.executeChild(this, new LeaseItemAndTermsForOxfMiracl005Gb());
            }
        });
    }

    @Inject
    Leases leases;

    @Inject
    LeaseItemLinkRepository leaseItemLinkRepository;

    Lease lease;

    @Before
    public void setUp() throws Exception {
        lease = leases.findLeaseByReference(LeaseForOxfMiracl005Gb.REF);
    }

    public static class NewLeaseItemLink extends LeaseLinkRepositoryTest{

        @Test
        public void newLeaseItemLink() throws Exception {
            // given
            LeaseItem depositItem = lease.findFirstItemOfType(LeaseItemType.DEPOSIT);
            LeaseItem rentItem = lease.findFirstItemOfType(LeaseItemType.RENT);

            // when
            LeaseItemLink newLink = leaseItemLinkRepository.newLeaseItemLink(depositItem, rentItem);

            // then
            Assertions.assertThat(newLink.getSourceItem()).isEqualTo(depositItem);
            Assertions.assertThat(newLink.getLinkedItem()).isEqualTo(rentItem);

            // finder works
            Assertions.assertThat(leaseItemLinkRepository.findBySourceItem(depositItem).size()).isEqualTo(1);
            Assertions.assertThat(leaseItemLinkRepository.findBySourceItem(depositItem).get(0)).isEqualTo(newLink);

        }

    }


}
