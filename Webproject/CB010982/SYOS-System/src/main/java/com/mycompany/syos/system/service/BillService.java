/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.syos.system.service;
/**
 *
 * @author User
 */
import com.mycompany.syos.system.designpatterns.billing.InstoreBill;
import com.mycompany.syos.system.designpatterns.billing.discount.BasicDiscountHandler;
import com.mycompany.syos.system.designpatterns.billing.discount.DiscountHandler;
import com.mycompany.syos.system.designpatterns.billing.discount.FifteenPercentDiscountHandler;
import com.mycompany.syos.system.designpatterns.billing.discount.TenPercentDiscountHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.time.LocalDateTime;


import com.mycompany.database_syos.dao.BillDAO;
import com.mycompany.database_syos.dao.ProductDAO;
import com.mycompany.database_syos.dao.ShelfDAO;
import com.mycompany.database_syos.dao.ShelfItemDAO;
import com.mycompany.database_syos.dao.UserDAO;
import com.mycompany.database_syos.models.Bill;
import com.mycompany.database_syos.models.BillItem;
import com.mycompany.database_syos.models.Product;
import com.mycompany.database_syos.models.Shelf;
import com.mycompany.database_syos.models.ShelfItem;
import com.mycompany.database_syos.models.UserModel;


public class BillService {
    private BillDAO billDAO;
    private ProductDAO productDAO;
    private ShelfItemDAO shelfItemDAO;
    private ShelfDAO shelfDAO;
    private UserModel currentUser;
    private UserDAO userDAO;

    public BillService(BillDAO billDAO, ProductDAO productDAO, ShelfItemDAO shelfItemDAO, ShelfDAO shelfDAO, UserDAO userDAO, UserModel currentUser) {
        this.billDAO = billDAO;
        this.productDAO = productDAO;
        this.shelfItemDAO = shelfItemDAO;
        this.shelfDAO = shelfDAO;
        this.userDAO = userDAO; 
        this.currentUser = currentUser;
    }

     public void applyDiscountsToBill(Bill bill) {

        DiscountHandler basicDiscount = new BasicDiscountHandler();
        DiscountHandler tenPercentDiscount = new TenPercentDiscountHandler();
        DiscountHandler fifteenPercentDiscount = new FifteenPercentDiscountHandler();     
        basicDiscount.setNextHandler(tenPercentDiscount);
        tenPercentDiscount.setNextHandler(fifteenPercentDiscount);    
        basicDiscount.applyDiscount(bill);
    }
     
     public void processSale(Scanner scanner) {
        try {
            int billNumber;

            synchronized (this) {
                int lastBillNumber = billDAO.getLastBillNumber();
                billNumber = lastBillNumber + 1; 
            }


            Bill bill = new Bill(
                currentUser.getUserId(),
                "BILL_" + billNumber,
                LocalDateTime.now(),
                0.0, 
                0.0,
                0.0,
                0.0,
                0.0,
                0.0,
                0.0,
                "Cash" 
            );

            List<BillItem> billItems = new ArrayList<>();
            double grossAmount = 0.0;


            while (true) {
                System.out.println("Enter product code or type 'done' to finish:");
                String productCode = scanner.nextLine().trim();

                if ("done".equalsIgnoreCase(productCode)) {
                    break;
                }

                if (productCode.isEmpty()) {
                    System.out.println("Product code cannot be empty. Please enter a valid product code.");
                    continue;
                }

                Product product = productDAO.getProductByCode(productCode);

                if (product != null) {
                    System.out.println("Product fetched: " + product.getName());
                    System.out.println("Enter quantity:");

                    while (!scanner.hasNextInt()) {
                        System.out.println("Invalid input. Please enter a valid quantity:");
                        scanner.next();
                    }
                    int quantity = scanner.nextInt();
                    scanner.nextLine();
                    List<BillItem> itemsFromShelf = processShelfItems(product.getId(), quantity);
                    billItems.addAll(itemsFromShelf);
                    grossAmount += itemsFromShelf.stream().mapToDouble(item -> item.getQuantity() * item.getPrice()).sum();
                } else {
                    System.out.println("Product not found for code: " + productCode);
                }
            }

            // Set gross and net totals
            bill.setGrossTotal(grossAmount);
            applyDiscountsToBill(bill);
            double netTotal = bill.getGrossTotal() - bill.getDiscount();
            bill.setNetTotal(netTotal);
            bill.setTotal(netTotal);

            System.out.printf("Net Amount to be Paid: %.2f%n", bill.getNetTotal());

            // Handle payment details
            System.out.println("Enter payment type (Cash/Card):");
            String paymentType = scanner.nextLine().trim();
            bill.setPaymentType(paymentType);

            if ("Cash".equalsIgnoreCase(paymentType)) {
                System.out.println("Enter cash received:");
                while (!scanner.hasNextDouble()) {
                    System.out.println("Invalid input. Please enter a valid cash amount:");
                    scanner.next();
                }
                double cashReceived = scanner.nextDouble();
                double change = cashReceived - bill.getNetTotal();
                bill.setCashReceived(cashReceived);
                bill.setChangeGiven(change);
            } else if ("Card".equalsIgnoreCase(paymentType)) {
                System.out.println("Please swipe your card on the payment machine.");
                bill.setCashReceived(bill.getNetTotal());
                bill.setChangeGiven(0.0);
            } else {
                System.out.println("Invalid payment type entered. Defaulting to Cash.");
            }

            // Save the bill and all bill items
            billDAO.saveBill(bill);
            for (BillItem item : billItems) {
                item.setBillId(bill.getBillId());
                billDAO.saveBillItem(item);
            }

            System.out.println("Bill successfully saved.");

            // Generate and print the bill
            InstoreBill instoreBill = new InstoreBill(bill, billItems, productDAO);
            instoreBill.generateBill();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<BillItem> processShelfItems(int productId, int quantityRequired) throws Exception {
        List<BillItem> billItems = new ArrayList<>();
        List<ShelfItem> shelfItems = shelfItemDAO.getShelfItemsForProduct(productId); 

        System.out.println("Retrieved " + shelfItems.size() + " shelf items for product ID: " + productId);

        int remainingQuantity = quantityRequired; 

        for (ShelfItem item : shelfItems) {
            if (remainingQuantity <= 0) break; 

            System.out.println("Processing Shelf Item: " + item.getItemSerialNumber());
            System.out.println("Batch ID: " + item.getBatchId());
            System.out.println("Shelf ID: " + item.getShelfId());

            double price = productDAO.getPriceByProductId(productId); 

            if (item.getShelfId() > 0) { 
                Shelf shelf = shelfDAO.findShelfById(item.getShelfId());
                System.out.println("Found shelf with ID: " + shelf.getShelfId() + " and current quantity: " + shelf.getCurrentQuantity());

                int quantityToUse = 1; 

                double total = price * quantityToUse;

                BillItem billItem = new BillItem(0, productId, item.getBatchId(), item.getItemSerialNumber(), quantityToUse, price);
                billItems.add(billItem); 

                shelfItemDAO.removeShelfItem(item.getShelfItemId());
                System.out.println("Removed Shelf Item with ID: " + item.getShelfItemId());

                int newShelfQuantity = shelf.getCurrentQuantity() - quantityToUse; 
                if (newShelfQuantity < 0) {
                    newShelfQuantity = 0; 
                }
                shelfDAO.updateShelfQuantity(item.getShelfId(), newShelfQuantity); 
                System.out.println("Updated Shelf ID: " + item.getShelfId() + " with new quantity: " + newShelfQuantity);

                remainingQuantity -= quantityToUse; 
            } else {
                System.out.println("Invalid shelf ID for item: " + item.getItemSerialNumber());
            }
        }

        if (remainingQuantity > 0) {
            System.out.println("Insufficient stock for product ID: " + productId + ". Could not fulfill the required quantity of " + quantityRequired);
        }
        return billItems; 
        }

    public void viewTransactions(Scanner scanner) {
        System.out.println("Viewing previous transactions...");
        List<Bill> bills = billDAO.getAllBills();
        for (Bill bill : bills) {
            System.out.println(bill);
        }
    }
}

