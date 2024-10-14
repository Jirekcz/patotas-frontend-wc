package pe.edu.cibertec.Patotas_frontend_wc.dto;

import java.util.Date;

public record LogoutResponseDTO(Boolean resultado, Date fecha, String mensajeError) {
}
