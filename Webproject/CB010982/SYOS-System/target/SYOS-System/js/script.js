// Toggle password visibility
document.querySelector('.toggle-password').addEventListener('click', function () {
    const passwordField = document.getElementById('password');
    if (passwordField.type === 'password') {
        passwordField.type = 'text';
        this.classList.add('fa-eye-slash');
        this.classList.remove('fa-eye');
    } else {
        passwordField.type = 'password';
        this.classList.add('fa-eye');
        this.classList.remove('fa-eye-slash');
    }
});
