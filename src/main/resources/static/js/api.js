async function apiFetch(endpoint, options = {}) {

    const API_BASE_URL = window.BACKEND_URL;
    const token = sessionStorage.getItem("jwt");

    const finalOptions = {
        ...options,
        headers: {
            "Content-Type": "application/json",
            ...(options.headers || {}),
            ...(token ? { "Authorization": `Bearer ${token}` } : {})
        }
    };

    const response = await fetch(`${API_BASE_URL}${endpoint}`, finalOptions);

    if (response.status === 401 || response.status === 403) {
        console.warn("Token inválido o expirado. Redirigiendo a login...");
        sessionStorage.clear();
        return;
    }

    return response;
}
