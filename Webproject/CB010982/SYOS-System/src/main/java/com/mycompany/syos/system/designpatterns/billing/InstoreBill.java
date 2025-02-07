/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.syos.system.designpatterns.billing;

/**
 *
 * @author User
 */

//import com.mycompany.syos.database.dao.ProductDAO;
//import com.mycompany.syos.system.exceptions.ProductDatabaseException;
//import com.mycompany.syos.system.model.Bill;
//import com.mycompany.syos.system.model.BillItem;
//import com.mycompany.syos.system.model.Product;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;



//new imports are added after this
import com.mycompany.database_syos.models.Bill;
import com.mycompany.database_syos.models.BillItem;
import com.mycompany.database_syos.models.Product;
import com.mycompany.database_syos.exceptions.ProductDatabaseException;
import com.mycompany.database_syos.dao.ProductDAO;

public class InstoreBill extends AbstractBill {
    private Bill bill;
    private List<BillItem> billItems;
    private ProductDAO productDAO;

    public InstoreBill(Bill bill, List<BillItem> billItems, ProductDAO productDAO) {
        this.bill = bill;
        this.billItems = billItems;
        this.productDAO = productDAO; 
    }

    @Override
    protected void printHeader() {
         System.out.println("--------------------------------------------------------------------");

        String storeInfo = "Synex Outlet Store (SYOS)\n" +
                           "Torrington Avenue, Colombo 07\n" +
                           "Store code : S001";
        String[] storeLines = storeInfo.split("\n");
        for (String line : storeLines) {
            System.out.println(centerAlign(line, 40)); 
        }
        System.out.println("--------------------------------------------------------------------");
        System.out.printf("%-25s %-20s %-15s%n", "Timestamp", "Cashier ID", "Bill no");
        System.out.println("--------------------------------------------------------------------");
        System.out.printf("%-25s %-20s %-15s%n", bill.getDate(), bill.getUserId(), bill.getBillNo());
        System.out.println("--------------------------------------------------------------------");
    }

    @Override
    protected void printItems() {
        System.out.println("--------------------------------------------------------------------");
        
        System.out.printf("%-20s %-8s %-10s %-10s%n", "Product", "Qty", "Price", "Total");
        System.out.println("--------------------------------------------------------------------");

        
        Map<Integer, Integer> productQuantities = new HashMap<>();
        Map<Integer, Double> productPrices = new HashMap<>();

        
        for (BillItem item : billItems) {
            int productId = item.getProductId();
            if (productQuantities.containsKey(productId)) {
               
                productQuantities.put(productId, productQuantities.get(productId) + item.getQuantity());
            } else {
                
                productQuantities.put(productId, item.getQuantity());
                productPrices.put(productId, item.getPrice());
            }
        }

        
        double totalBillAmount = 0.0;
        for (Map.Entry<Integer, Integer> entry : productQuantities.entrySet()) {
            int productId = entry.getKey();
            int quantity = entry.getValue();
            double price = productPrices.get(productId);
            double total = quantity * price; 

            
            String productName = null;
            try {
                productName = getProductNameById(productId);
            } catch (ProductDatabaseException ex) {
                Logger.getLogger(InstoreBill.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.printf("%-20s %-8d %-10.2f %-10.2f%n", 
                productName, quantity, price, total);

            totalBillAmount += total;
        }

  
        bill.setGrossTotal(totalBillAmount);

       
        double netTotal = totalBillAmount - bill.getDiscount();
        bill.setNetTotal(netTotal); 
    }

  
    private String centerAlign(String text, int width) {
        if (text.length() >= width) {
            return text; 
        }
        int leftPadding = (width - text.length()) / 2;
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < leftPadding; i++) {
            result.append(" ");
        }
        result.append(text);
        return result.toString();
    }

    
    private String getProductNameById(int productId) throws ProductDatabaseException {
        Product product = productDAO.getProductById(productId);
        return product != null ? product.getName() : "Unknown";
    }

    @Override
    protected void printFooter() {
        System.out.println("--------------------------------------------------------------------");
        System.out.printf("%-20s : %.2f%n", "Gross Amount", bill.getGrossTotal());
        System.out.printf("%-20s : %.2f%n", "Discount", bill.getDiscount());
        System.out.printf("%-20s : %.2f%n", "Net Amount", bill.getNetTotal()); 
        System.out.println("--------------------------------------------------------------------");
        if ("Cash".equalsIgnoreCase(bill.getPaymentType())) {
            System.out.printf("%-20s : %.2f%n", "Cash Received", bill.getCashReceived());
            System.out.printf("%-20s : %.2f%n", "Change", bill.getChangeGiven());
        } else if ("Card".equalsIgnoreCase(bill.getPaymentType())) {
            System.out.println("Payment made via card.");
        }
        System.out.println("--------------------------------------------------------------------");

        
        String footerMessage = "Thank you for shopping at SYOS!\n" +
                               "Our hotline 0778989476";
        String[] footerLines = footerMessage.split("\n");
        for (String line : footerLines) {
            System.out.println(centerAlign(line, 40)); 
        }
    }

}
