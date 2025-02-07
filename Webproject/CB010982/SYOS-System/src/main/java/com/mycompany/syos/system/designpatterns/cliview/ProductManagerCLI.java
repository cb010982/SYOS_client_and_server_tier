package com.mycompany.syos.system.designpatterns.cliview;

//import com.mycompany.syos.database.dao.ProductDAO;
//import com.mycompany.syos.database.dao.SubcategoryDAO;
//import com.mycompany.syos.database.dao.BrandDAO;
//import com.mycompany.syos.database.dao.ProductCategoryDAO;
//import com.mycompany.syos.database.dao.UnitDAO;
//import com.mycompany.syos.database.dao.MainCategoryDAO;
//import com.mycompany.syos.system.exceptions.ProductDatabaseException;
//import com.mycompany.syos.system.model.*;
import com.mycompany.syos.system.users.User;
import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

//new imports are added after this
import com.mycompany.database_syos.dao.ProductDAO;
import com.mycompany.database_syos.dao.SubcategoryDAO;
import com.mycompany.database_syos.dao.BrandDAO;
import com.mycompany.database_syos.dao.ProductCategoryDAO;
import com.mycompany.database_syos.dao.UnitDAO;
import com.mycompany.database_syos.dao.MainCategoryDAO;
import com.mycompany.database_syos.exceptions.ProductDatabaseException;
import com.mycompany.database_syos.models.*;

public class ProductManagerCLI {
    private final MainCategoryDAO mainCategoryDAO;
    private final SubcategoryDAO subcategoryDAO;
    private final ProductCategoryDAO productCategoryDAO;
    private final BrandDAO brandDAO;
    private final UnitDAO unitDAO;
    private final ProductDAO productDAO;

    public ProductManagerCLI() {
        this.mainCategoryDAO = new MainCategoryDAO();
        this.subcategoryDAO = new SubcategoryDAO();
        this.productCategoryDAO = new ProductCategoryDAO();
        this.brandDAO = new BrandDAO();
        this.unitDAO = new UnitDAO();
        this.productDAO = new ProductDAO();
    }

    public void addProduct(User currentUser) {
        Scanner scanner = new Scanner(System.in);

        try {

            MainCategory selectedMainCategory = selectOrCreateMainCategory(scanner);
            Subcategory selectedSubCategory = selectOrCreateSubcategory(scanner, selectedMainCategory.getId());
            ProductCategory selectedProductCategory = selectOrCreateProductCategory(scanner, selectedSubCategory.getId());
            Brand selectedBrand = selectOrCreateBrand(scanner);
            Unit selectedUnit = selectOrCreateUnit(scanner);

            Product product = new Product();
            product.setProductCategoryId(selectedProductCategory.getId());
            product.setSubcategoryId(selectedSubCategory.getId());  
            product.setBrandId(selectedBrand.getId());
            product.setUnitId(selectedUnit.getId());

            System.out.print("Enter the product name: ");
            String name = scanner.nextLine();
            product.setName(name);

            double costPrice = getValidDoubleInput(scanner, "Enter the cost price: ");
            product.setCostPrice(costPrice);

            double profitMargin = getValidDoubleInput(scanner, "Enter the profit margin (e.g., 0.2 for 20%): ");
            product.setProfitMargin(profitMargin);

            double taxRate = getValidDoubleInput(scanner, "Enter the tax rate (e.g., 0.1 for 10%): ");
            product.setTaxRate(taxRate);

            System.out.print("Is the product expirable? (yes/no): ");
            String isExpirableInput = scanner.nextLine().trim().toLowerCase();
            boolean isExpirable = isExpirableInput.equals("yes");
            product.setExpirable(isExpirable); 
            product.calculateFinalPrice();
            String productCode = generateProductCode(
                    selectedMainCategory.getCode(),
                    selectedSubCategory.getCode(),
                    selectedProductCategory.getCode(),
                    selectedBrand.getCode(),
                    selectedUnit.getName()
            );
            product.setProductCode(productCode);
            product.setCreatedBy(currentUser.getUsername());
            productDAO.saveProduct(product, selectedMainCategory.getId(), selectedProductCategory.getId(), selectedBrand.getId(), selectedUnit.getId());
            System.out.println("Product added successfully by " + currentUser.getUsername() + "!");
        } catch (ProductDatabaseException e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private MainCategory selectOrCreateMainCategory(Scanner scanner) throws ProductDatabaseException {
        List<MainCategory> mainCategories = mainCategoryDAO.getMainCategories();
        while (true) {
            System.out.println("Select a main category: ");
            for (int i = 0; i < mainCategories.size(); i++) {
                System.out.println((i + 1) + ". " + mainCategories.get(i).getName());
            }
            System.out.print("Or enter 0 to create a new main category: ");
            int choice = getValidIntInput(scanner, "");

            if (choice == 0) {
  
                System.out.print("Enter the name for the new main category: ");
                String name = scanner.nextLine();
                System.out.print("Enter the code for the new main category: ");
                String code = scanner.nextLine();
                mainCategoryDAO.createMainCategory(name, code);

                mainCategories = mainCategoryDAO.getMainCategories();
            } else if (choice > 0 && choice <= mainCategories.size()) {
                return mainCategories.get(choice - 1);
            } else {
                System.out.println("Invalid main category choice. Please try again.");
            }
        }
    }

    private Subcategory selectOrCreateSubcategory(Scanner scanner, int mainCategoryId) throws ProductDatabaseException {
       List<Subcategory> subCategories = subcategoryDAO.getCategoriesByMainCategory(mainCategoryId);

       if (subCategories.isEmpty()) {
           System.out.println("No subcategories found for the selected main category. Please create a new subcategory.");
           System.out.print("Enter the name for the new subcategory: ");
           String name = scanner.nextLine();
           System.out.print("Enter the code for the new subcategory: ");
           String code = scanner.nextLine();

           int newSubCategoryId = subcategoryDAO.createSubcategory(name, code, mainCategoryId);

           return new Subcategory(newSubCategoryId, name, code);
       }

    while (true) {
        System.out.println("Select a subcategory: ");
        for (int i = 0; i < subCategories.size(); i++) {
            System.out.println((i + 1) + ". " + subCategories.get(i).getName());
        }
        System.out.print("Or enter 0 to create a new subcategory: ");
        int choice = getValidIntInput(scanner, "");

        if (choice == 0) {
            System.out.print("Enter the name for the new subcategory: ");
            String name = scanner.nextLine();
            System.out.print("Enter the code for the new subcategory: ");
            String code = scanner.nextLine();

            int newSubCategoryId = subcategoryDAO.createSubcategory(name, code, mainCategoryId);

            return new Subcategory(newSubCategoryId, name, code);
        } else if (choice > 0 && choice <= subCategories.size()) {
            return subCategories.get(choice - 1);  
        } else {
            System.out.println("Invalid subcategory choice. Please try again.");
        }
        }
    }

    private ProductCategory selectOrCreateProductCategory(Scanner scanner, int subCategoryId) throws ProductDatabaseException {
        List<ProductCategory> productCategories = productCategoryDAO.getProductCategoriesBySubCategory(subCategoryId);

        if (productCategories.isEmpty()) {
            System.out.println("No product categories found for the selected subcategory. Please create a new product category.");
            System.out.print("Enter the name for the new product category: ");
            String name = scanner.nextLine();
            System.out.print("Enter the code for the new product category: ");
            String code = scanner.nextLine();

            int newProductCategoryId = productCategoryDAO.createProductCategory(name, code, subCategoryId);

            return new ProductCategory(newProductCategoryId, name, code);
        }

        while (true) {
            System.out.println("Select a product category: ");
            for (int i = 0; i < productCategories.size(); i++) {
                System.out.println((i + 1) + ". " + productCategories.get(i).getName());
            }
            System.out.print("Or enter 0 to create a new product category: ");
            int choice = getValidIntInput(scanner, "");

            if (choice == 0) {
                System.out.print("Enter the name for the new product category: ");
                String name = scanner.nextLine();
                System.out.print("Enter the code for the new product category: ");
                String code = scanner.nextLine();

                int newProductCategoryId = productCategoryDAO.createProductCategory(name, code, subCategoryId);

                return new ProductCategory(newProductCategoryId, name, code);
            } else if (choice > 0 && choice <= productCategories.size()) {
                return productCategories.get(choice - 1);  
            } else {
                System.out.println("Invalid product category choice. Please try again.");
            }
        }
    }

    private Brand selectOrCreateBrand(Scanner scanner) throws ProductDatabaseException {
        List<Brand> brands = brandDAO.getBrands();
        System.out.println("Select a brand: ");
        for (int i = 0; i < brands.size(); i++) {
            System.out.println((i + 1) + ". " + brands.get(i).getName());
        }
        System.out.print("Or enter 0 to create a new brand: ");
        int choice = getValidIntInput(scanner, "");
        if (choice == 0) {
            System.out.print("Enter the name for the new brand: ");
            String name = scanner.nextLine();
            System.out.print("Enter the code for the new brand: ");
            String code = scanner.nextLine();
            int newBrandId = brandDAO.createBrand(name, code);
            return new Brand(newBrandId, name, code);
        } else {
            return brands.get(choice - 1);
        }
    }

    private Unit selectOrCreateUnit(Scanner scanner) throws ProductDatabaseException {
        List<Unit> units = unitDAO.getUnits();
        System.out.println("Select a unit: ");
        for (int i = 0; i < units.size(); i++) {
            System.out.println((i + 1) + ". " + units.get(i).getName());
        }
        System.out.print("Or enter 0 to create a new unit: ");
        int choice = getValidIntInput(scanner, "");
        if (choice == 0) {
            System.out.print("Enter the name for the new unit: ");
            String name = scanner.nextLine();
            int newUnitId = unitDAO.createUnit(name);
            return new Unit(newUnitId, name);
        } else {
            return units.get(choice - 1);
        }
    }

    private int getValidIntInput(Scanner scanner, String prompt) {
        int input = -1;
        boolean validInput = false;
        while (!validInput) {
            System.out.print(prompt);
            try {
                input = Integer.parseInt(scanner.nextLine().trim());
                validInput = true;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
        return input;
    }

    private double getValidDoubleInput(Scanner scanner, String prompt) {
        double input = -1;
        boolean validInput = false;
        while (!validInput) {
            System.out.print(prompt);
            try {
                input = Double.parseDouble(scanner.nextLine().trim());
                validInput = true;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
        return input;
    }

    private String generateProductCode(String mainCategoryCode, String subCategoryCode, String productCategoryCode, String brandCode, String unitName) {
        return String.format("%s-%s-%s-%s-%s", mainCategoryCode, subCategoryCode, productCategoryCode, brandCode, unitName);
    }
    
    public void editProduct(Scanner scanner) {
        try {
 
            List<Product> products = productDAO.getAllProducts();

            if (products.isEmpty()) {
                System.out.println("No products available to edit.");
                return;
            }

     
            System.out.println("Select a product to edit: ");
            for (int i = 0; i < products.size(); i++) {
                System.out.println((i + 1) + ". " + products.get(i).getName());
            }

             int choice = -1;
            boolean validInput = false;

            while (!validInput) {
                System.out.print("Enter the product number: ");
                if (scanner.hasNextInt()) {
                    choice = scanner.nextInt();
                
                    scanner.nextLine(); 

                    if (choice >= 1 && choice <= products.size()) {
                        validInput = true;
                    } else {
                        System.out.println("Invalid choice. Please select a number between 1 and " + products.size() + ".");
                    }
                } else {
                    System.out.println("Invalid input. Please enter a valid number.");
                    scanner.next(); 
                }
            }

            Product selectedProduct = products.get(choice - 1);

            String mainCategoryName = mainCategoryDAO.getMainCategoryById(selectedProduct.getMainCategoryId()).getName();
            String subCategoryName = subcategoryDAO.getSubcategoryById(selectedProduct.getSubcategoryId()).getName();
            String productCategoryName = productCategoryDAO.getProductCategoryById(selectedProduct.getProductCategoryId()).getName();
            String brandName = brandDAO.getBrandById(selectedProduct.getBrandId()).getName();
            String unitName = unitDAO.getUnitById(selectedProduct.getUnitId()).getName();

           
            System.out.println("Current product details:");
            System.out.println("Name: " + selectedProduct.getName());
            System.out.println("Cost Price: " + selectedProduct.getCostPrice());
            System.out.println("Profit Margin: " + selectedProduct.getProfitMargin());
            System.out.println("Tax Rate: " + selectedProduct.getTaxRate());
            System.out.println("Final Price: " + selectedProduct.getFinalPrice()); 
            System.out.println("Is Expirable: " + (selectedProduct.isExpirable() ? "Yes" : "No"));
            System.out.println("Main Category: " + mainCategoryName); 
            System.out.println("Subcategory: " + subCategoryName);    
            System.out.println("Product Category: " + productCategoryName); 
            System.out.println("Brand: " + brandName);                 
            System.out.println("Unit: " + unitName);                   

            
            System.out.print("Enter the new product name (" + selectedProduct.getName() + "): ");
            String newName = scanner.nextLine();
            if (!newName.isEmpty()) {
                selectedProduct.setName(newName);
            }

            double newCostPrice = getValidDoubleInput(scanner, "Enter the new cost price (" + selectedProduct.getCostPrice() + "): ");
            selectedProduct.setCostPrice(newCostPrice);

            double newProfitMargin = getValidDoubleInput(scanner, "Enter the new profit margin (" + selectedProduct.getProfitMargin() + "): ");
            selectedProduct.setProfitMargin(newProfitMargin);

            double newTaxRate = getValidDoubleInput(scanner, "Enter the new tax rate (" + selectedProduct.getTaxRate() + "): ");
            selectedProduct.setTaxRate(newTaxRate);

           
            selectedProduct.calculateFinalPrice();

           
            System.out.print("Is the product expirable? (yes/no) (" + (selectedProduct.isExpirable() ? "yes" : "no") + "): ");
            String isExpirableInput = scanner.nextLine().trim().toLowerCase();
            boolean isExpirable = isExpirableInput.equals("yes");
            selectedProduct.setExpirable(isExpirable);

            
            System.out.println("Do you want to change the main category? (yes/no): ");
            String changeMainCategory = scanner.nextLine().trim().toLowerCase();
            if (changeMainCategory.equals("yes")) {
                MainCategory selectedMainCategory = selectOrCreateMainCategory(scanner);
                selectedProduct.setMainCategoryId(selectedMainCategory.getId());

                
                System.out.println("Do you want to change the subcategory? (yes/no): ");
                String changeSubcategory = scanner.nextLine().trim().toLowerCase();
                if (changeSubcategory.equals("yes")) {
                    Subcategory selectedSubcategory = selectOrCreateSubcategory(scanner, selectedMainCategory.getId());
                    selectedProduct.setSubcategoryId(selectedSubcategory.getId());
                }
            }

            
            if (!changeMainCategory.equals("yes")) {
                System.out.println("Do you want to change the subcategory? (yes/no): ");
                String changeSubcategory = scanner.nextLine().trim().toLowerCase();
                if (changeSubcategory.equals("yes")) {
                    Subcategory selectedSubcategory = selectOrCreateSubcategory(scanner, selectedProduct.getMainCategoryId());
                    selectedProduct.setSubcategoryId(selectedSubcategory.getId());  
                }
            }

            
            System.out.println("Do you want to change the product category? (yes/no): ");
            String changeProductCategory = scanner.nextLine().trim().toLowerCase();
            if (changeProductCategory.equals("yes")) {
                ProductCategory selectedProductCategory = selectOrCreateProductCategory(scanner, selectedProduct.getSubcategoryId());
                selectedProduct.setProductCategoryId(selectedProductCategory.getId());
            }

            
            System.out.println("Do you want to change the brand? (yes/no): ");
            String changeBrand = scanner.nextLine().trim().toLowerCase();
            if (changeBrand.equals("yes")) {
                Brand selectedBrand = selectOrCreateBrand(scanner);
                selectedProduct.setBrandId(selectedBrand.getId());
            }

            
            System.out.println("Do you want to change the unit? (yes/no): ");
            String changeUnit = scanner.nextLine().trim().toLowerCase();
            if (changeUnit.equals("yes")) {
                Unit selectedUnit = selectOrCreateUnit(scanner);
                selectedProduct.setUnitId(selectedUnit.getId());
            }

            
            String newProductCode = generateProductCode(
                    mainCategoryDAO.getMainCategoryById(selectedProduct.getMainCategoryId()).getCode(),
                    subcategoryDAO.getSubcategoryById(selectedProduct.getSubcategoryId()).getCode(),
                    productCategoryDAO.getProductCategoryById(selectedProduct.getProductCategoryId()).getCode(),
                    brandDAO.getBrandById(selectedProduct.getBrandId()).getCode(),
                    unitDAO.getUnitById(selectedProduct.getUnitId()).getName()
            );
            selectedProduct.setProductCode(newProductCode);  

            
            productDAO.updateProduct(selectedProduct);
            System.out.println("Product updated successfully with new product code: " + newProductCode);

        } catch (ProductDatabaseException e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public void viewAllProducts() {
        try {
            List<Product> products = productDAO.getAllProducts();

            if (products.isEmpty()) {
                System.out.println("No products available.");
            } else {
            
                System.out.println("--- List of All Products ---");
                System.out.printf("%-10s %-20s %-15s %-15s %-20s %-15s %-10s %-12s %-12s %-12s %-12s %-15s\n",
                        "Product ID", "Name", "Main Category", "Subcategory", "Product Category", "Brand", "Unit",
                        "Cost Price", "Profit Margin", "Tax Rate", "Final Price", "Product Code");

                System.out.println("-----------------------------------------------------------------------------------------------------------------------");

                
                for (Product product : products) {
                    
                    String mainCategoryName = mainCategoryDAO.getMainCategoryById(product.getMainCategoryId()).getName();
                    String subCategoryName = subcategoryDAO.getSubcategoryById(product.getSubcategoryId()).getName();
                    String productCategoryName = productCategoryDAO.getProductCategoryById(product.getProductCategoryId()).getName();
                    String brandName = brandDAO.getBrandById(product.getBrandId()).getName();
                    String unitName = unitDAO.getUnitById(product.getUnitId()).getName();

                   
                    System.out.printf("%-10d %-20s %-15s %-15s %-20s %-15s %-10s %-12.2f %-12.2f %-12.2f %-12.2f %-15s\n",
                            product.getId(), product.getName(), mainCategoryName, subCategoryName, productCategoryName, 
                            brandName, unitName, product.getCostPrice(), product.getProfitMargin(), 
                            product.getTaxRate(), product.getFinalPrice(), product.getProductCode());
                }
            }
        } catch (ProductDatabaseException e) {
            System.out.println("Error retrieving products: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void deleteProduct(Scanner scanner) {
        try {
           
            List<Product> products = productDAO.getAllProducts();

            if (products.isEmpty()) {
                System.out.println("No products available to delete.");
                return;
            }

           
            System.out.println("Select a product to delete: ");
            for (int i = 0; i < products.size(); i++) {
                System.out.println((i + 1) + ". " + products.get(i).getName());
            }

            
            int choice = getValidIntInput(scanner, "Enter the product number: ", products.size());

            Product selectedProduct = products.get(choice - 1);
           
            scanner.nextLine();  

           
            System.out.print("Are you sure you want to delete the product " + selectedProduct.getName() + "? (yes/no): ");
            String confirmation = scanner.nextLine().trim().toLowerCase();

            if (confirmation.equals("yes")) {
               
                productDAO.deleteProduct(selectedProduct.getId());
                System.out.println("Product deleted successfully.");
            } else {
                System.out.println("Product deletion canceled.");
            }

        } catch (ProductDatabaseException e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private int getValidIntInput(Scanner scanner, String prompt, int maxOption) {
        int choice = -1;
        boolean validInput = false;
        while (!validInput) {
            System.out.print(prompt);
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                if (choice > 0 && choice <= maxOption) {
                    validInput = true; 
                } else {
                    System.out.println("Invalid choice. Please enter a number between 1 and " + maxOption + ".");
                }
            } else {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.next(); 
            }
        }
        return choice;
    }
}