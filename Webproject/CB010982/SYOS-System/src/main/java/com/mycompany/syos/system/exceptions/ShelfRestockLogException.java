/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.syos.system.exceptions;

/**
 *
 * @author User
 */
public class ShelfRestockLogException extends Exception {
   
    public ShelfRestockLogException(String message) {
        super(message);
    }

    public ShelfRestockLogException(String message, Throwable cause) {
        super(message, cause);
    }

    
    public ShelfRestockLogException(Throwable cause) {
        super(cause);
    }
}
