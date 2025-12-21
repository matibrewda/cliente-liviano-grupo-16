function isLoggedIn() {
    return sessionStorage.getItem("jwt") !== null;
}

function getNombreReal() {
    return sessionStorage.getItem("nombreReal");
}

function logout() {
    sessionStorage.clear();
    window.location.href = "/"; // o donde quieras redirigir
}
