/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.syos.system.utils;

/**
 *
 * @author User
 */

import java.util.Arrays;
import java.util.List;

public class ValidationUtils {
    public static final List<String> VALID_ROLES = Arrays.asList("manager", "cashier", "storekeeper", "supervisor", "admin", "customer");


    public static boolean isValidEmail(String email) {
        return email.contains("@") && email.endsWith("syos.com");
    }
       public static boolean isValidCustomerEmail(String email) {
        return email.contains("@") && email.contains(".") && email.indexOf('@') < email.lastIndexOf('.');
    }
}
