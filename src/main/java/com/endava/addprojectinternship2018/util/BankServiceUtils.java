package com.endava.addprojectinternship2018.util;

import java.util.UUID;

public class BankServiceUtils {

    public static String generateAccountNumber() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 14).toUpperCase();
    }
}
