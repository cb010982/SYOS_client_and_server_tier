package com.mycompany.syos.system;

import com.mycompany.syos.system.designpatterns.facade.SystemFacade;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);

            // Initialize the SystemFacade and start the system
            SystemFacade systemFacade = new SystemFacade();
            systemFacade.startSystem(scanner);

        } catch (Exception e) {
            System.out.println("An unexpected error occurred.");
            e.printStackTrace();
        }
    }
}
