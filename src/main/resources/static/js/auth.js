function isLoggedIn() {
    return sessionStorage.getItem("jwt") !== null;
}

function getUsername() {
    return sessionStorage.getItem("username");
}

function logout() {
    sessionStorage.clear();
    window.location.href = "/"; // o donde quieras redirigir
}
