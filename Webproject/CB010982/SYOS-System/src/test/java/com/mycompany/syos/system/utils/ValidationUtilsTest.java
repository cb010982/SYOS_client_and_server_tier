/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package com.mycompany.syos.system.utils;
/**
 *
 * @author User
 */

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ValidationUtilsTest {

    @Test
    public void should_ReturnTrue_when_ValidSyosEmailIsProvided() {
        assertTrue(ValidationUtils.isValidEmail("user@syos.com"));
    }

    @Test
    public void should_ReturnFalse_when_EmailDoesNotEndWithSyosCom() {
        assertFalse(ValidationUtils.isValidEmail("user@gmail.com"));
    }

    @Test
    public void should_ReturnFalse_when_EmailDoesNotContainAtSymbol() {
        assertFalse(ValidationUtils.isValidEmail("user.syos.com"));
    }

    @Test
    public void should_ReturnFalse_when_EmailContainsWhitespace() {
        assertFalse(ValidationUtils.isValidEmail("user@syos.com "));
    }

    @Test
    public void should_ReturnTrue_when_ValidCustomerEmailIsProvided() {
        assertTrue(ValidationUtils.isValidCustomerEmail("customer@domain.com"));
    }

    @Test
    public void should_ReturnFalse_when_CustomerEmailMissingAtSymbol() {
        assertFalse(ValidationUtils.isValidCustomerEmail("customer.domain.com"));
    }

    @Test
    public void should_ReturnFalse_when_CustomerEmailMissingDot() {
        assertFalse(ValidationUtils.isValidCustomerEmail("customer@domaincom"));
    }

    @Test
    public void should_ReturnFalse_when_AtSymbolAppearsAfterDot() {
        assertFalse(ValidationUtils.isValidCustomerEmail("customer.domain@com"));
    }
}
