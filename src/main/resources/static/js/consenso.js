// Configuración de consenso para colecciones
(function() {
    'use strict';
    
    // Esperar a que tanto el DOM como apiFetch estén disponibles
    function init() {
        if (typeof apiFetch === 'undefined') {
            console.warn('apiFetch no está disponible aún, reintentando...');
            setTimeout(init, 100);
            return;
        }

        const form = document.getElementById('formConfigurarConsenso');
        if (!form) {
            console.error('Formulario formConfigurarConsenso no encontrado');
            return;
        }


        form.addEventListener('submit', async function(e) {
        e.preventDefault();

        const coleccionId = document.getElementById('coleccionId').value;
        const tipoConsenso = document.getElementById('tipoConsenso').value;
        const btnGuardar = document.getElementById('btnGuardar');
        const alertContainer = document.getElementById('alertContainer');


        if (!tipoConsenso) {
            showAlert('Por favor seleccione un tipo de consenso', 'danger', alertContainer);
            return;
        }

        if (!window.BACKEND_URL) {
            console.error('BACKEND_URL no está definido');
            showAlert('Error de configuración: URL del backend no definida', 'danger', alertContainer);
            return;
        }

        // Deshabilitar botón durante la petición
        btnGuardar.disabled = true;
        btnGuardar.innerHTML = '<span class="spinner-border spinner-border-sm me-2"></span>Guardando...';

        const url = `${window.BACKEND_URL}/admin/colecciones/${coleccionId}/consenso`;
        const body = JSON.stringify({ tipo: tipoConsenso });
        

        try {
            const response = await apiFetch(`/admin/colecciones/${coleccionId}/consenso`, {
                method: 'PUT',
                body: body
            });

            if (!response) {
                console.error('No se recibió respuesta (posible error 401/403)');
                showAlert('Error de autenticación. Por favor inicie sesión nuevamente', 'danger', alertContainer);
                btnGuardar.disabled = false;
                btnGuardar.innerHTML = '<i class="bi bi-save"></i> Guardar Configuración';
                return;
            }

            if (response.ok) {
                showAlert('Configuración de consenso guardada exitosamente', 'success', alertContainer);
                // Opcional: redirigir después de un momento
                setTimeout(() => {
                    window.location.href = '/colecciones';
                }, 1500);
            } else {
                let errorMessage = 'Error al guardar la configuración';
                try {
                    const errorData = await response.json();
                    if (errorData.message) {
                        errorMessage = errorData.message;
                    }
                } catch (e) {
                    // Si no se puede parsear el error, usar mensaje por defecto
                }

                if (response.status === 404) {
                    errorMessage = 'Colección no encontrada';
                } else if (response.status === 400) {
                    errorMessage = 'Datos inválidos. Por favor verifique la información';
                } else if (response.status === 500) {
                    errorMessage = 'Error del servidor. Por favor intente más tarde';
                }

                showAlert(errorMessage, 'danger', alertContainer);
            }
        } catch (error) {
            console.error('Error en la petición:', error);
            console.error('Stack:', error.stack);
            showAlert('Error de conexión: ' + error.message, 'danger', alertContainer);
        } finally {
            btnGuardar.disabled = false;
            btnGuardar.innerHTML = '<i class="bi bi-save"></i> Guardar Configuración';
            }
        });
    }

    function showAlert(message, type, container) {
        container.innerHTML = `
            <div class="alert alert-${type} alert-dismissible fade show" role="alert">
                ${message}
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        `;
    }

    // Inicializar cuando el DOM esté listo
    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', init);
    } else {
        init();
    }
})();

