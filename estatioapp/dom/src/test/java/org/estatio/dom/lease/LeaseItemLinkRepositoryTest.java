package org.estatio.dom.lease;

import java.util.Arrays;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

public class LeaseItemLinkRepositoryTest {

    LeaseItemLinkRepository leaseItemLinkRepository;

    LeaseItem depositItem;
    LeaseItem rentItem;
    LeaseItem serviceChargeItem;
    LeaseItem notSupportedTaxItem;

    Lease lease;

    public static class CreateLinkTests extends LeaseItemLinkRepositoryTest {

        @Before
        public void setup() {
            leaseItemLinkRepository = new LeaseItemLinkRepository() {
                @Override
                public List<LeaseItemLink> findBySourceItem(final LeaseItem sourceItem) {
                    LeaseItemLink link = new LeaseItemLink();
                    link.setSourceItem(depositItem);
                    link.setLinkedItem(serviceChargeItem);
                    return Arrays.asList(link);
                }
            };

            depositItem = new LeaseItem();
            depositItem.setType(LeaseItemType.DEPOSIT);

            rentItem = new LeaseItem();
            rentItem.setType(LeaseItemType.RENT);

            serviceChargeItem = new LeaseItem();
            serviceChargeItem.setType(LeaseItemType.SERVICE_CHARGE);

            notSupportedTaxItem = new LeaseItem();
            notSupportedTaxItem.setType(LeaseItemType.TAX);

            lease = new Lease();
            SortedSet<LeaseItem> leaseItems = new TreeSet<>();
            leaseItems.add(depositItem);
            leaseItems.add(rentItem);
            leaseItems.add(serviceChargeItem);
            leaseItems.add(notSupportedTaxItem);
            lease.setItems(leaseItems);

            depositItem.setLease(lease);
        }

        @Test
        public void testHideCreateLink() throws Exception {
            Assertions.assertThat(leaseItemLinkRepository.hideCreateLink(depositItem)).isEqualTo(false);
            Assertions.assertThat(leaseItemLinkRepository.hideCreateLink(rentItem)).isEqualTo(true);
        }

        @Test
        public void testValidateCreateLink() throws Exception {
            Assertions.assertThat(leaseItemLinkRepository.validateCreateLink(depositItem, rentItem)).isEqualTo(null);
            Assertions.assertThat(leaseItemLinkRepository.validateCreateLink(depositItem, serviceChargeItem)).isEqualTo("This item is already linked");
            Assertions.assertThat(leaseItemLinkRepository.validateCreateLink(depositItem, notSupportedTaxItem)).isEqualTo("A deposit cannot be linked to an Item of type Tax");
            Assertions.assertThat(leaseItemLinkRepository.validateCreateLink(depositItem, depositItem)).isEqualTo("A deposit cannot be linked to an Item of type Deposit");
        }

        @Test
        public void testChoicesCreateLink() throws Exception {
            Assertions.assertThat(leaseItemLinkRepository.choicesCreateLink(depositItem).size()).isEqualTo(2);
            Assertions.assertThat(leaseItemLinkRepository.choicesCreateLink(depositItem)).isEqualTo(Arrays.asList(rentItem, serviceChargeItem));
        }

    }

    public static class FindLinkedItemsTest extends LeaseItemLinkRepositoryTest {

        LeaseItem serviceChargeItem2;
        LeaseItem serviceChargeItem3;

        @Before
        public void setup() {
            leaseItemLinkRepository = new LeaseItemLinkRepository() {
                @Override
                public List<LeaseItemLink> findBySourceItem(final LeaseItem sourceItem) {
                    LeaseItemLink link = new LeaseItemLink();
                    link.setSourceItem(depositItem);
                    link.setLinkedItem(serviceChargeItem);
                    LeaseItemLink link2 = new LeaseItemLink();
                    link2.setSourceItem(depositItem);
                    link2.setLinkedItem(serviceChargeItem2);
                    LeaseItemLink link3 = new LeaseItemLink();
                    link3.setSourceItem(depositItem);
                    link3.setLinkedItem(serviceChargeItem3);
                    return Arrays.asList(link, link2, link3);
                }
            };

            depositItem = new LeaseItem();
            depositItem.setType(LeaseItemType.DEPOSIT);

            serviceChargeItem = new LeaseItem();
            serviceChargeItem.setType(LeaseItemType.SERVICE_CHARGE);

            serviceChargeItem2 = new LeaseItem();
            serviceChargeItem2.setType(LeaseItemType.SERVICE_CHARGE);

            serviceChargeItem3 = new LeaseItem();
            serviceChargeItem3.setType(LeaseItemType.SERVICE_CHARGE_BUDGETED);

        }

        @Test
        public void testFindLinkedItemsBySourceItemAndType() throws Exception {
            Assertions.assertThat(leaseItemLinkRepository.findLinkedItemsBySourceItemAndType(depositItem, LeaseItemType.SERVICE_CHARGE).size()).isEqualTo(2);
            Assertions.assertThat(leaseItemLinkRepository.findLinkedItemsBySourceItemAndType(depositItem, LeaseItemType.SERVICE_CHARGE_BUDGETED).size()).isEqualTo(1);
            Assertions.assertThat(leaseItemLinkRepository.findLinkedItemsBySourceItemAndType(depositItem, LeaseItemType.SERVICE_CHARGE_BUDGETED)).isEqualTo(Arrays.asList(serviceChargeItem3));
        }

    }

}

