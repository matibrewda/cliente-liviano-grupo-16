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

    const url = `${API_BASE_URL}${endpoint}`;

    try {
        const response = await fetch(url, finalOptions);
        console.log('apiFetch - Response status:', response.status);

        if (response.status === 401 || response.status === 403) {
            console.warn("Token inválido o expirado. Redirigiendo a login...");
            sessionStorage.clear();
            return null;
        }

        return response;
    } catch (error) {
        console.error('apiFetch - Error en fetch:', error);
        throw error;
    }
}
