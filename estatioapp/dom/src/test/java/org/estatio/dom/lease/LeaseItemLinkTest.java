package org.estatio.dom.lease;

import org.junit.Test;

import org.estatio.dom.AbstractBeanPropertiesTest;

public class LeaseItemLinkTest {

    public static class BeanProperties extends AbstractBeanPropertiesTest {

        @Test
        public void test() {
            final LeaseItemLink pojo = new LeaseItemLink();
            newPojoTester()
                    .withFixture(pojos(LeaseItem.class, LeaseItem.class))
                    .exercise(pojo);
        }

    }

}
