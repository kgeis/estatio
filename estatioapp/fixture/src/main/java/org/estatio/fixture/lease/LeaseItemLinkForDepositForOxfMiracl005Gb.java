/*
 *
 *  Copyright 2012-2014 Eurocommercial Properties NV
 *
 *
 *  Licensed under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.estatio.fixture.lease;

import org.estatio.dom.lease.Lease;
import org.estatio.dom.lease.LeaseItem;
import org.estatio.dom.lease.LeaseItemType;
import org.estatio.fixture.security.tenancy.ApplicationTenancyForGbOxfDefault;

public class LeaseItemLinkForDepositForOxfMiracl005Gb extends LeaseItemLinkAbstract {

    public static final String LEASE_REF = LeaseForOxfMiracl005Gb.REF;
    public static final String AT_PATH = ApplicationTenancyForGbOxfDefault.PATH;

    @Override
    protected void execute(final ExecutionContext fixtureResults) {
        createLeaseLeaseItemLinkForTopModel001(fixtureResults);
    }

    private void createLeaseLeaseItemLinkForTopModel001(final ExecutionContext executionContext) {

        // prereqs
        if(isExecutePrereqs()) {
            executionContext.executeChild(this, new LeaseForOxfMiracl005Gb());
            executionContext.executeChild(this, new LeaseItemAndLeaseTermForDepositForOxfMiracl005Gb());
        }

        // exec
        final Lease lease = leases.findLeaseByReference(LEASE_REF);
        final LeaseItem depositItem = lease.findFirstItemOfType(LeaseItemType.DEPOSIT);
        final LeaseItem rentItem = lease.findFirstItemOfType(LeaseItemType.RENT);

        createLeaseItemLink(
                depositItem,
                rentItem,
                executionContext);
    }
}
