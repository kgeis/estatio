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

import org.estatio.dom.charge.Charge;
import org.estatio.dom.lease.Lease;
import org.estatio.dom.lease.LeaseItem;
import org.estatio.dom.lease.LeaseItemType;
import org.estatio.fixture.charge.ChargeRefData;
import org.estatio.fixture.security.tenancy.ApplicationTenancyForGbOxfDefault;

public class LeaseItemLinkForDepositForOxfTopModel001Gb extends LeaseItemLinkAbstract {

    public static final String LEASE_REF = LeaseForOxfTopModel001Gb.REF;
    public static final String AT_PATH = ApplicationTenancyForGbOxfDefault.PATH;

    @Override
    protected void execute(final ExecutionContext fixtureResults) {
        createLeaseLeaseItemLinkForTopModel001(fixtureResults);
    }

    private void createLeaseLeaseItemLinkForTopModel001(final ExecutionContext executionContext) {

        // prereqs
        if(isExecutePrereqs()) {
            executionContext.executeChild(this, new LeaseForOxfTopModel001Gb());
            executionContext.executeChild(this, new LeaseItemAndLeaseTermForDepositForOxfTopModel001Gb());
        }

        // exec
        final Charge charge = charges.findByReference(ChargeRefData.GB_DEPOSIT);
        final Lease lease = leases.findLeaseByReference(LEASE_REF);
        final LeaseItem depositItem = lease.findFirstItemOfTypeAndCharge(LeaseItemType.DEPOSIT, charge);
        final LeaseItem rentItem = lease.findFirstItemOfType(LeaseItemType.RENT);

        createLeaseItemLink(
                depositItem,
                rentItem,
                executionContext);

        final Charge chargeDsc = charges.findByReference(ChargeRefData.GB_DEPOSIT_SC);
        final LeaseItem depositItemSc = lease.findFirstItemOfTypeAndCharge(LeaseItemType.DEPOSIT, chargeDsc);
        final LeaseItem scItem = lease.findFirstItemOfType(LeaseItemType.SERVICE_CHARGE);

        createLeaseItemLink(
                depositItemSc,
                scItem,
                executionContext);
    }
}
