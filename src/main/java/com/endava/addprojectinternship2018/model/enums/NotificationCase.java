package com.endava.addprojectinternship2018.model.enums;

public enum NotificationCase {
    NEW_USER {
        @Override
        public String toString() {
            return "NEW USER !";
        }
    },
    NEW_MESSAGE {
        @Override
        public String toString() {
            return "NEW MESSAGE!";
        }
    },
    NEW_CONTRACT {
        @Override
        public String toString() {
            return "NEW CONTRACT!";
        }
    },
    NEW_INVOICE {
        @Override
        public String toString() {
            return "NEW INVOICE!";
        }
    },
    CONTRACT_SIGNED {
        @Override
        public String toString() {
            return "CONTRACT SIGNED!";
        }
    },
    INVOICE_PAID {
        @Override
        public String toString() {
            return "INVOICE PAID!";
        }
    }
}
