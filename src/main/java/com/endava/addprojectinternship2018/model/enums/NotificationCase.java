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
    }
}
