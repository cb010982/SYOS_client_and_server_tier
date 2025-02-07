document.addEventListener("DOMContentLoaded", () => {
    const cartForm = document.getElementById("cartForm");
    const sidePanel = document.getElementById("sidePanel");
    const toggleCartBtn = document.getElementById("toggleCartBtn");
    const cartIcon = document.getElementById("cartIcon");

    // Toggle Cart Panel
    toggleCartBtn.addEventListener("click", () => {
        if (sidePanel.style.right === "0px") {
            sidePanel.style.right = "-600px";
            cartIcon.classList.remove("fa-xmark");
            cartIcon.classList.add("fa-cart-shopping");
        } else {
            sidePanel.style.right = "0px";
            cartIcon.classList.remove("fa-cart-shopping");
            cartIcon.classList.add("fa-xmark");
        }
        
    });
    attachOriginalGridListeners();
        //  "Add to Cart" functionality
    document.querySelectorAll(".test-btn").forEach(button => {
        button.addEventListener("click", (event) => {
            let originalCard = event.target.closest(".col");
            let productId = originalCard.querySelector("input[name^='quantity-']").name.split('-')[1];

            // Get quantity controls in the original product grid
            let quantityControls = originalCard.querySelector(".quantity-container");
            let totalPriceContainer = originalCard.querySelector(".total-price-container");
            let trashButton = originalCard.querySelector(".trash-btn");
            let quantityInput = originalCard.querySelector(".quantity-input");

            // Set default quantity to 1
            quantityInput.value = "1";

            // Hiding quantity controls in the original product grid 
            quantityControls.style.display = "none";
            totalPriceContainer.style.display = "none";
            trashButton.style.display = "none";
            quantityControls.setAttribute("data-hidden", "true");

            // Hide "Add to Cart" button
            button.style.display = "none";

            // Extract elements for cart row
            let productImage = originalCard.querySelector(".product-img").cloneNode(true);
            let productTitle = originalCard.querySelector(".card-title").innerText;
            let productPrice = parseFloat(originalCard.querySelector(".product-price").innerText.replace(/[^0-9.]/g, "")) || 0;

            let quantityContainer = document.createElement("div");
            quantityContainer.classList.add("cart-quantity-controls");

            let decrementButton = document.createElement("button");
            decrementButton.type = "button";
            decrementButton.classList.add("quantity-btn", "decrement");
            decrementButton.innerText = "-";

            let cartQuantityInput = document.createElement("input");
            cartQuantityInput.type = "number";
            cartQuantityInput.name = `cloned-quantity-${productId}`;
            cartQuantityInput.value = "1";
            cartQuantityInput.classList.add("quantity-input");

            let incrementButton = document.createElement("button");
            incrementButton.type = "button";
            incrementButton.classList.add("quantity-btn", "increment");
            incrementButton.innerText = "+";

            quantityContainer.appendChild(decrementButton);
            quantityContainer.appendChild(cartQuantityInput);
            quantityContainer.appendChild(incrementButton);

            // Attach event listeners to the cart quantity buttons
            attachQuantityListeners(quantityContainer, cartQuantityInput, productPrice, productId);

            // Total Price Container
            let totalCell = document.createElement("div");
            totalCell.classList.add("cart-cell", "cart-total");

            let totalValue = (productPrice * 1).toFixed(2);
            totalCell.innerHTML = `<strong>$<span class="cart-total-price">${totalValue}</span></strong>`;

            // Creating a new cart row
            let rowContainer = document.createElement("div");
            rowContainer.classList.add("cart-row");
            rowContainer.setAttribute("data-product-id", productId);

            let imgCell = document.createElement("div");
            imgCell.classList.add("cart-cell", "cart-image");
            imgCell.appendChild(productImage);

            let titleCell = document.createElement("div");
            titleCell.classList.add("cart-cell", "cart-title");
            titleCell.innerHTML = `<span>${productTitle}</span>`;

            let priceCell = document.createElement("div");
            priceCell.classList.add("cart-cell", "cart-price");
            priceCell.innerHTML = `<strong> ${productPrice.toFixed(2)}</strong>`;

            let quantityCell = document.createElement("div");
            quantityCell.classList.add("cart-cell", "cart-quantity");
            quantityCell.appendChild(quantityContainer);

            let removeButton = document.createElement("button");
            removeButton.innerHTML = '<i class="fa-solid fa-trash"></i>'; 
            removeButton.classList.add("btn", "btn-danger", "remove-btn");
            removeButton.addEventListener("click", function () {
                rowContainer.remove();
                resetOriginalProduct(originalCard);
                updateGrandTotal();
            });

            let removeCell = document.createElement("div");
            removeCell.classList.add("cart-cell", "cart-remove");
            removeCell.appendChild(removeButton);

            rowContainer.appendChild(imgCell);
            rowContainer.appendChild(titleCell);
            rowContainer.appendChild(priceCell);
            rowContainer.appendChild(quantityCell);
            rowContainer.appendChild(totalCell);
            rowContainer.appendChild(removeCell);
            
            // Appending row to cart before updating grand total
            document.getElementById("sidePanelContent").appendChild(rowContainer);

            // Opening cart panel
            sidePanel.style.right = "0px";
            cartIcon.classList.remove("fa-cart-shopping");
            cartIcon.classList.add("fa-xmark");

            // Ensuring the total price updates before calculating grand total
            updateGrandTotal();
        });
    });

    // Attaching quantity button listeners for the original product grid
    function attachOriginalGridListeners() {
        document.querySelectorAll(".quantity-btn").forEach(button => {
            button.addEventListener("click", function () {
                let inputField = this.closest(".quantity-container").querySelector(".quantity-input");
                let change = this.classList.contains("increment") ? 1 : -1;
                inputField.value = Math.max(parseInt(inputField.value) + change, 1);

                // Updating total price in the original grid
                let productCard = this.closest(".product-card");
                let priceElement = productCard.querySelector(".product-price");
                let totalElement = productCard.querySelector(".total-price");

                let price = parseFloat(priceElement.innerText.replace(/[^0-9.]/g, "")) || 0;
                let quantity = parseInt(inputField.value) || 1; 
                let total = (price * quantity).toFixed(2);
                totalElement.innerText = `Total: $${total}`;

                // Syncing quantity in the cart
                let productId = inputField.name.split('-')[1];
                let cartRow = document.querySelector(`.cart-row[data-product-id="${productId}"]`);
                if (cartRow) {
                    let cartQuantity = cartRow.querySelector(".cart-quantity input");
                    if (cartQuantity) {
                        cartQuantity.value = quantity; // Mirroring the original grid quantity
                    }

                    let cartTotal = cartRow.querySelector(".cart-total .cart-total-price");
                    if (cartTotal) {
                        cartTotal.innerText = total; // Updating total in cart
                    }

                    updateGrandTotal();
                }
            });
        });
    }

    function attachQuantityListeners(container, inputField, pricePerUnit, productId) {
        container.querySelectorAll(".quantity-btn").forEach(button => {
            button.addEventListener("click", function () {
                let change = this.classList.contains("increment") ? 1 : -1;
                let newQuantity = Math.max(parseInt(inputField.value) + change, 1);
                inputField.value = newQuantity;

                let totalCell = this.closest(".cart-row").querySelector(".cart-total");
                updateTotal(inputField, totalCell, pricePerUnit);

                // Syncing back to original product grid
                let originalProduct = document.querySelector(`input[name="quantity-${productId}"]`);
                if (originalProduct) {
                    originalProduct.value = newQuantity;
                    let productCard = originalProduct.closest(".product-card");
                    let totalElement = productCard.querySelector(".total-price");
                    if (totalElement) {
                        let total = (pricePerUnit * newQuantity).toFixed(2);
                        totalElement.innerText = `Total: $${total}`;
                    }
                }

                updateGrandTotal();
            });
        });
    }

    function updateTotal(inputField, totalCell, pricePerUnit) {
        let quantity = parseInt(inputField.value) || 1;
        let total = (pricePerUnit * quantity).toFixed(2);

        console.log("Updating Total:", { pricePerUnit, quantity, total }); 

        // Updating the total price inside the cart row
        totalCell.querySelector(".cart-total-price").innerText = total;

        // Updating the total price in the original product grid
        let productCard = inputField.closest(".col");
        if (productCard) {
            let totalElement = productCard.querySelector(".total-price");
            if (totalElement) {
                totalElement.innerText = `${total}`;
            }
        }

        updateGrandTotal(); // Ensuring grand total updates after any individual update
    }

    // Function to update the grand total
    function updateGrandTotal() {
        let grandTotal = 0;
        document.querySelectorAll(".cart-total-price").forEach(priceElement => {
            let price = parseFloat(priceElement.innerText) || 0;
            grandTotal += price;
        });
        
        console.log("Updated Grand Total:", grandTotal);
        document.getElementById("grandTotal").innerText = `$${grandTotal.toFixed(2)}`;
    }

    function resetOriginalProduct(originalCard) {
        let addButton = originalCard.querySelector(".test-btn");
        let quantityControls = originalCard.querySelector(".quantity-container");
        let totalPriceContainer = originalCard.querySelector(".total-price-container");
        let trashButton = originalCard.querySelector(".trash-btn");
        let quantityInput = originalCard.querySelector(".quantity-input");
        let totalElement = originalCard.querySelector(".total-price");

        quantityControls.style.display = "none";
        totalPriceContainer.style.display = "none";
        addButton.style.display = "inline-block";
        trashButton.style.display = "none";

        // Reseting the quantity to 0 in the original grid
        quantityInput.value = "0";
        totalElement.innerText = "$0.00"; 

        let productId = quantityInput.name.split('-')[1];
        let cartRow = document.querySelector(`.cart-row[data-product-id="${productId}"]`);
        if (cartRow) {
            cartRow.remove();
        }

        updateGrandTotal();
    }

    // Handling Trash Button in the Original Product Grid
    document.querySelectorAll(".trash-btn").forEach(trashButton => {
    trashButton.addEventListener("click", function () {
        let originalCard = this.closest(".col");
        let productId = originalCard.querySelector("input[name^='quantity-']").name.split('-')[1];

        // Removing the corresponding cloned product in the cart
        let clonedRow = document.querySelector(`.cart-row[data-product-id="${productId}"]`);
        if (clonedRow) {
            clonedRow.remove();
        }

        // Reseting the quantity input in the form
        let clonedInput = document.querySelector(`input[name='cloned-quantity-${productId}']`);
        if (clonedInput) {
            clonedInput.remove(); // Removing from form submission
        }

        // Reseting the original product grid
        resetOriginalProduct(originalCard);
        removeClonedInputs(productId);
        updateGrandTotal();
    });
});

});
