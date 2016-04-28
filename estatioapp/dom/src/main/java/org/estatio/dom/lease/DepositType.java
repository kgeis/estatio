package org.estatio.dom.lease;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.joda.time.LocalDate;

import org.estatio.dom.tax.Tax;

public enum DepositType {

    INDEXED_MGR_INCLUDING_VAT {
        @Override
        BigDecimal calculateDepositValue(final LeaseTermForDeposit term, final LocalDate date) {

            BigDecimal currentValue = BigDecimal.ZERO;
            List<LeaseItem> rentItems = term.getLeaseItem().findLinkedLeaseItemsByType(LeaseItemType.RENT);

            for (LeaseItem rentItem : rentItems) {
                Tax itemTax = rentItem.getTax();
                BigDecimal rentItemValueUntilVerificationDate = rentItem.valueForDate(date.minusDays(1));
                BigDecimal vatAmount = itemTax != null ?
                        itemTax.percentageFor(date.minusDays(1)).multiply(rentItemValueUntilVerificationDate).divide(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP)
                        : BigDecimal.ZERO;
                if (rentItemValueUntilVerificationDate != null) {
                    currentValue = currentValue.add(rentItemValueUntilVerificationDate).add(vatAmount);
                }
            }

            return currentValue;
        }

        @Override List<LeaseItemType> applicableTypes() {
            return Arrays.asList(
                    LeaseItemType.RENT);
        }
    },

    BASE_MGR_INCLUDING_VAT {
        @Override
        BigDecimal calculateDepositValue(final LeaseTermForDeposit term, final LocalDate date) {

            BigDecimal currentValue = BigDecimal.ZERO;
            List<LeaseItem> rentItems = term.getLeaseItem().findLinkedLeaseItemsByType(LeaseItemType.RENT);

            for (LeaseItem rentItem : rentItems) {
                LeaseTermForIndexable termForIndexable = (LeaseTermForIndexable)rentItem.currentTerm(date.minusDays(1));
                Tax itemTax = rentItem.getTax();
                BigDecimal rentItemValueUntilVerificationDate = termForIndexable.getBaseValue();
                BigDecimal vatAmount = itemTax != null ?
                        itemTax.percentageFor(date.minusDays(1)).multiply(rentItemValueUntilVerificationDate).divide(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP)
                        : BigDecimal.ZERO;
                if (rentItemValueUntilVerificationDate != null) {
                    currentValue = currentValue.add(rentItemValueUntilVerificationDate).add(vatAmount);
                }
            }

            return currentValue;
        }

        @Override List<LeaseItemType> applicableTypes() {
            return Arrays.asList(
                    LeaseItemType.RENT);
        }
    },

    INDEXED_MGR_EXCLUDING_VAT {
        @Override
        BigDecimal calculateDepositValue(final LeaseTermForDeposit term, final LocalDate date) {

            BigDecimal currentValue = BigDecimal.ZERO;
            List<LeaseItem> rentItems = term.getLeaseItem().findLinkedLeaseItemsByType(LeaseItemType.RENT);

            for (LeaseItem rentItem : rentItems) {
                BigDecimal rentItemValueUntilVerificationDate = rentItem.valueForDate(date.minusDays(1));
                if (rentItemValueUntilVerificationDate != null) {
                    currentValue = currentValue.add(rentItemValueUntilVerificationDate);
                }
            }

            return currentValue;
        }

        @Override List<LeaseItemType> applicableTypes() {
            return Arrays.asList(
                    LeaseItemType.RENT);
        }
    },

    BASE_MGR_EXCLUDING_VAT {
        @Override
        BigDecimal calculateDepositValue(final LeaseTermForDeposit term, final LocalDate date) {

            BigDecimal currentValue = BigDecimal.ZERO;
            List<LeaseItem> rentItems = term.getLeaseItem().findLinkedLeaseItemsByType(LeaseItemType.RENT);

            for (LeaseItem rentItem : rentItems) {
                LeaseTermForIndexable termForIndexable = (LeaseTermForIndexable)rentItem.currentTerm(date.minusDays(1));
                BigDecimal rentItemValueUntilVerificationDate = termForIndexable.getBaseValue();
                if (rentItemValueUntilVerificationDate != null) {
                    currentValue = currentValue.add(rentItemValueUntilVerificationDate);
                }
            }

            return currentValue;
        }

        @Override List<LeaseItemType> applicableTypes() {
            return Arrays.asList(
                    LeaseItemType.RENT);
        }
    },

    SERVICE_CHARGE {
        @Override BigDecimal calculateDepositValue(final LeaseTermForDeposit term, final LocalDate date) {
            BigDecimal currentValue = BigDecimal.ZERO;
            List<LeaseItem> serviceChargeItems = term.getLeaseItem().findLinkedLeaseItemsByType(LeaseItemType.SERVICE_CHARGE);

            for (LeaseItem serviceChargeItem : serviceChargeItems) {
                BigDecimal serviceChargeValueUntilVerificationDate = serviceChargeItem.valueForDate(date.minusDays(1));
                if (serviceChargeValueUntilVerificationDate != null) {
                    currentValue = currentValue.add(serviceChargeValueUntilVerificationDate);
                }
            }

            return currentValue;
        }

        @Override List<LeaseItemType> applicableTypes() {
            return Arrays.asList(
                    LeaseItemType.SERVICE_CHARGE);
        }
    },

    MANUAL {
        @Override
        BigDecimal calculateDepositValue(LeaseTermForDeposit term, LocalDate date) {
            return BigDecimal.ZERO;
        }

        @Override List<LeaseItemType> applicableTypes() {
            return Arrays.asList(
                    LeaseItemType.RENT,
                    LeaseItemType.SERVICE_CHARGE,
                    LeaseItemType.SERVICE_CHARGE_INDEXABLE,
                    LeaseItemType.SERVICE_CHARGE_BUDGETED);
        }

    };

    abstract BigDecimal calculateDepositValue(final LeaseTermForDeposit term, final LocalDate date);
    abstract List<LeaseItemType> applicableTypes();

    boolean appliesTo(final LeaseItemType leaseItemType){
        return applicableTypes().contains(leaseItemType);
    };

}
