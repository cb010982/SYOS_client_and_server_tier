/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.syos.system.designpatterns.observer;

/**
 *
 * @author User
 */


public class AssistantStorekeeperObserver implements Observer {

    @Override
    public void update(String message) {
        System.out.println("Notification for Assistant Storekeeper: " + message);
    }
}
