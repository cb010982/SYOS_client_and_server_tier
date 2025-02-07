/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package com.mycompany.syos.system.cli;

/**
 *
 * @author User
 */
import com.mycompany.syos.system.designpatterns.cliview.ShelfCLI;
//import com.mycompany.syos.database.dao.ShelfDAO;
//import com.mycompany.syos.system.exceptions.ShelfException;
//import com.mycompany.syos.system.model.Shelf;
import com.mycompany.syos.system.service.ShelfService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Scanner;
import static org.mockito.Mockito.*;


import com.mycompany.database_syos.models.Shelf;
import com.mycompany.database_syos.dao.ShelfDAO;
import com.mycompany.database_syos.exceptions.ShelfException;

public class ShelfCLITest {

    private ShelfService shelfService;
    private ShelfDAO shelfDAO;
    private ShelfCLI shelfCLI;
    private Scanner scanner;

    @BeforeEach
    public void setUp() {
        shelfService = mock(ShelfService.class);
        shelfDAO = mock(ShelfDAO.class);
        scanner = mock(Scanner.class);
        shelfCLI = new ShelfCLI(shelfService, shelfDAO);
    }

    @Test
    public void should_Exit_when_UserChoosesExitOption() throws ShelfException {
        when(scanner.hasNextInt()).thenReturn(true);  
        when(scanner.nextInt()).thenReturn(5);  
        when(scanner.nextLine()).thenReturn(""); 

        shelfCLI.showShelfMenu(scanner);

        verify(shelfService, times(0)).getAllShelves();
        verify(shelfService, times(0)).addShelf(any(Shelf.class));
        verify(shelfService, times(0)).updateShelf(any(Shelf.class));
        verify(shelfService, times(0)).deleteShelf(anyInt());
        }
}
