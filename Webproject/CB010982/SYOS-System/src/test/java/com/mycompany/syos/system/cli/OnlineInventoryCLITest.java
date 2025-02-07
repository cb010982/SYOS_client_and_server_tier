/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package com.mycompany.syos.system.cli;
/**
 *
 * @author User
 */
import com.mycompany.syos.system.designpatterns.cliview.OnlineInventoryCLI;
//import com.mycompany.syos.database.dao.OnlineInventoryDAO;
import com.mycompany.syos.system.service.OnlineInventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Scanner;
import static org.mockito.Mockito.*;


import com.mycompany.database_syos.dao.OnlineInventoryDAO;

public class OnlineInventoryCLITest {

    private OnlineInventoryService onlineInventoryService;
    private OnlineInventoryDAO onlineInventoryDAO;
    private OnlineInventoryCLI onlineInventoryCLI;
    private Scanner scanner;

    @BeforeEach
    public void setUp() {
        onlineInventoryService = mock(OnlineInventoryService.class);
        onlineInventoryDAO = mock(OnlineInventoryDAO.class);
        scanner = mock(Scanner.class);
        onlineInventoryCLI = new OnlineInventoryCLI(onlineInventoryService, onlineInventoryDAO);
    }

    @Test
    public void should_Exit_when_UserChoosesExitOption() throws Exception {
        when(scanner.hasNextInt()).thenReturn(true); 
        when(scanner.nextInt()).thenReturn(5);  
        when(scanner.nextLine()).thenReturn("");  
        onlineInventoryCLI.showOnlineInventoryMenu(scanner);
        verify(onlineInventoryService, times(0)).getAllOnlineInventories();
    }
}
