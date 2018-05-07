package com.endava.addprojectinternship2018.model.enums;

public enum ContractStatus {
    ACTIVE,
    UNSIGNED,
    SIGNED_BY_CUSTOMER,
    SIGNED_BY_COMPANY,
    DELETED;

    @Override
    public String toString() {
        switch (name()) {
            case "ACTIVE":
                return "Active";
            case "UNSIGNED":
                return "Unsigned";
            case "SIGNED_BY_CUSTOMER":
                return "Signed only by Customer";
            case "SIGNED_BY_COMPANY":
                return "Signed only by Company";
            default:
                return name();
        }
    }

}
