package com.example.royalauto.ms_cotizaciones.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import com.example.royalauto.ms_cotizaciones.client.VehiculoClient;
import com.example.royalauto.ms_cotizaciones.dto.CotizacionVehiculoDTO;
import com.example.royalauto.ms_cotizaciones.dto.VehiculoInfoDTO;
import com.example.royalauto.ms_cotizaciones.entity.CotizacionVehiculo;
import com.example.royalauto.ms_cotizaciones.repository.CotizacionVehiculoRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CotizacionVehiculoService {

    private final CotizacionVehiculoRepository cotizacionRepository;
    private final VehiculoClient vehiculoClient;

    // Guardar la solicitud de cotización del formulario público
    public void solicitarCotizacion(CotizacionVehiculoDTO dto) {
        // Valida contra ms-comercial que el vehículo exista antes de guardar la solicitud
        VehiculoInfoDTO vehiculo;
        try {
            vehiculo = vehiculoClient.obtenerPorId(dto.getVehiculoId());
        } catch (FeignException.NotFound e) {
            throw new RuntimeException("El vehículo seleccionado no es válido o no existe.");
        }

        String formaPago = "FINANCIADO".equals(dto.getFormaPago()) ? "FINANCIADO" : "CONTADO";

        CotizacionVehiculo cotizacion = new CotizacionVehiculo();
        cotizacion.setNombreCliente(dto.getNombreCliente());
        cotizacion.setCorreo(dto.getCorreo());
        cotizacion.setTelefono(dto.getTelefono());
        cotizacion.setMensaje(dto.getMensaje());
        cotizacion.setFormaPago(formaPago);
        cotizacion.setEnganche(formaPago.equals("FINANCIADO") ? dto.getEnganche() : null);
        cotizacion.setPlazoMeses(formaPago.equals("FINANCIADO") ? dto.getPlazoMeses() : null);
        cotizacion.setVehiculoId(vehiculo.getId());

        cotizacionRepository.save(cotizacion);
    }

    // Obtener todas las cotizaciones registradas (panel del administrador)
    public List<CotizacionVehiculoDTO> obtenerTodas() {
        List<CotizacionVehiculo> cotizaciones = cotizacionRepository.findAll();

        // Una sola llamada a ms-comercial por todos los vehículos involucrados,
        // en vez de una llamada por cada cotización (evita el problema N+1)
        List<Long> idsVehiculos = cotizaciones.stream()
                .map(CotizacionVehiculo::getVehiculoId)
                .distinct()
                .collect(Collectors.toList());

        Map<Long, VehiculoInfoDTO> vehiculosPorId = new HashMap<>();
        if (!idsVehiculos.isEmpty()) {
            try {
                List<VehiculoInfoDTO> vehiculos = vehiculoClient.obtenerPorIds(idsVehiculos);
                for (VehiculoInfoDTO vehiculo : vehiculos) {
                    vehiculosPorId.put(vehiculo.getId(), vehiculo);
                }
            } catch (FeignException e) {
                // ms-comercial no respondió: las cotizaciones se listan igual, sin el detalle del vehículo
            }
        }

        return cotizaciones.stream()
                .map(cotizacion -> toDTO(cotizacion, vehiculosPorId.get(cotizacion.getVehiculoId())))
                .collect(Collectors.toList());
    }

    // Actualizar el estado de una cotización
    public void actualizarEstado(Long id, String nuevoEstado) {
        if (!nuevoEstado.equals("PENDIENTE") && !nuevoEstado.equals("CONTACTADO")) {
            throw new IllegalArgumentException("El estado especificado no es válido. Solo se permite PENDIENTE o CONTACTADO.");
        }

        CotizacionVehiculo cotizacion = cotizacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("La cotización especificada no existe."));

        cotizacion.setEstado(nuevoEstado);
        cotizacionRepository.save(cotizacion);
    }

    // El vehículo vive en ms-comercial: si ya no existe o el servicio está caído,
    // la cotización se sigue mostrando, solo sin el detalle del vehículo.
    private CotizacionVehiculoDTO toDTO(CotizacionVehiculo cotizacion, VehiculoInfoDTO vehiculo) {
        CotizacionVehiculoDTO dto = new CotizacionVehiculoDTO();
        dto.setId(cotizacion.getId());
        dto.setNombreCliente(cotizacion.getNombreCliente());
        dto.setCorreo(cotizacion.getCorreo());
        dto.setTelefono(cotizacion.getTelefono());
        dto.setMensaje(cotizacion.getMensaje());
        dto.setFormaPago(cotizacion.getFormaPago());
        dto.setEnganche(cotizacion.getEnganche());
        dto.setPlazoMeses(cotizacion.getPlazoMeses());
        dto.setVehiculoId(cotizacion.getVehiculoId());
        dto.setEstado(cotizacion.getEstado());
        dto.setFechaRegistro(cotizacion.getFechaRegistro());

        if (vehiculo != null) {
            dto.setVehiculoMarca(vehiculo.getMarcaNombre());
            dto.setVehiculoModelo(vehiculo.getModelo());
            dto.setVehiculoAnio(vehiculo.getAnio());
            dto.setVehiculoPrecio(vehiculo.getPrecio());
        } else {
            dto.setVehiculoModelo("(vehículo no disponible)");
        }

        return dto;
    }
}
