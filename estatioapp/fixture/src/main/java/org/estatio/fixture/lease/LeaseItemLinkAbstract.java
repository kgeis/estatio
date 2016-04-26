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

import javax.inject.Inject;

import org.estatio.dom.lease.LeaseItem;
import org.estatio.dom.lease.LeaseItemLink;
import org.estatio.dom.lease.LeaseItemLinkRepository;
import org.estatio.dom.lease.Leases;
import org.estatio.fixture.EstatioFixtureScript;

public abstract class LeaseItemLinkAbstract extends EstatioFixtureScript {

    protected LeaseItemLink createLeaseItemLink(final LeaseItem sourceItem, final LeaseItem linkedItem, final ExecutionContext executionContext){
        LeaseItemLink link = leaseItemLinkRepository.newLeaseItemLink(sourceItem, linkedItem);
        return executionContext.addResult(this, link);
    }

    // //////////////////////////////////////

    @Inject
    private LeaseItemLinkRepository leaseItemLinkRepository;

    @Inject
    Leases leases;

}
