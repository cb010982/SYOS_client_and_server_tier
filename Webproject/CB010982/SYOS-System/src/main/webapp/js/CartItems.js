/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */


    document.getElementById('expDate').addEventListener('input', function (e) {
    var input = e.target.value;
    // Remove any non-digit characters from the input
    input = input.replace(/\D/g, '');
    // Slice the input to get the first 4 digits only (MMYY)
    input = input.substring(0, 4);
    // Add a slash after the first 2 digits (MM/)
    if (input.length > 2) {
        input = input.substring(0, 2) + '/' + input.substring(2, 4);
    }
    e.target.value = input;  
    });

    document.getElementById('expDate').addEventListener('change', function (e) {
        var value = e.target.value;
        var month = parseInt(value.substring(0, 2), 10);
        var year = parseInt(value.substring(3), 10);
        var currentYear = new Date().getFullYear() % 100;  // Get the last two digits of the current year
        var currentMonth = new Date().getMonth() + 1;  // Get current month (1-12)

        if (year < currentYear || (year === currentYear && month < currentMonth)) {
            alert('The expiration date is invalid because it has already passed.');
            e.target.value = ''; // Clear invalid expiration date
        } else if (month < 1 || month > 12) {
            alert('Please enter a valid month (01-12).');
            e.target.value = ''; // Clear invalid month input
        }
    });
    
    document.getElementsByName('telephone')[0].addEventListener('input', function (e) {
        var input = e.target.value;
        // Remove any non-digit characters
        input = input.replace(/\D/g, '');
        // Limit the input to 10 digits
        input = input.substring(0, 10);
        e.target.value = input;  
    });
    
    document.getElementsByName('cvv')[0].addEventListener('input', function (e) {
        var input = e.target.value;
        // Remove any non-digit characters
        input = input.replace(/\D/g, '');
        // Limit the input to 3 digits
        input = input.substring(0, 3);
        e.target.value = input;  
    });
    
    document.getElementsByName('cardName')[0].addEventListener('input', function (e) {
        // Remove any digit from the card name
        e.target.value = e.target.value.replace(/\d+/g, '');
    });

//    document.getElementsByName('cardNumber')[0].addEventListener('input', function (e) {
//        var input = e.target.value;
//        // Remove any non-digit characters
//        input = input.replace(/\D/g, '');
//        // Limit the input to 16 digits
//        input = input.substring(0, 16);
//        e.target.value = input;  
//    });
//    
document.getElementsByName('cardNumber')[0].addEventListener('input', async function (e) {
    let input = e.target.value.replace(/\D/g, '').substring(0, 16);
    e.target.value = input;

    if (input.length === 16) {
        let response = await fetch(`/validateCard?number=${input}`);
        let data = await response.json();
        if (!data.valid) {
            alert("Invalid Card Number!");
            e.target.value = '';
        }
    }
});

    function restrictInput(e) {
        // Allow only letters, apostrophes, hyphens, and spaces
        e.target.value = e.target.value.replace(/[^A-Za-z\-'\s]/g, '');
    }

    document.getElementsByName('firstName')[0].addEventListener('input', restrictInput);
    document.getElementsByName('lastName')[0].addEventListener('input', restrictInput);