/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package com.mycompany.syos.system.factory.users;
/**
 *
 * @author User
 */
import com.mycompany.syos.system.designpatterns.factory.users.AssistantStoreKeeperFactory;
//import com.mycompany.syos.system.model.UserModel;
import com.mycompany.syos.system.service.*;
import com.mycompany.syos.system.users.AssistantStoreKeeper;
import com.mycompany.syos.system.users.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;



import com.mycompany.database_syos.models.UserModel;

class AssistantStoreKeeperFactoryTest {
    
    private AssistantStoreKeeperFactory assistantStoreKeeperFactory;
    private StoreInventoryService storeInventoryService;
    private ShelfRestockService shelfRestockService;
    private ShelfService shelfService;
    private OnlineInventoryRestockService onlineInventoryRestockService;
    private OnlineInventoryService onlineInventoryService;
    private UserModel validUserModel;

    @BeforeEach
    public void setUp() {
        storeInventoryService = mock(StoreInventoryService.class);
        shelfRestockService = mock(ShelfRestockService.class);
        shelfService = mock(ShelfService.class);
        onlineInventoryRestockService = mock(OnlineInventoryRestockService.class);
        onlineInventoryService = mock(OnlineInventoryService.class);
        assistantStoreKeeperFactory = new AssistantStoreKeeperFactory(
            storeInventoryService, shelfRestockService, shelfService, onlineInventoryRestockService, onlineInventoryService
        );
        validUserModel = new UserModel(1, "assistantStoreKeeper", "passwordHash", "assistant_store_keeper", "assistant@syos.com", null);
    }

    @Test
    public void should_CreateAssistantStoreKeeper_when_ValidUserModelIsProvided() {
        User user = assistantStoreKeeperFactory.createUser(validUserModel);
        assertNotNull(user);
        assertTrue(user instanceof AssistantStoreKeeper);
        assertEquals("assistant_store_keeper", user.getRole());  
    }
}
