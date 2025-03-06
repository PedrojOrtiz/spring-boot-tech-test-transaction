package ec.sofka.springboottechtesttransaction.service;

import ec.sofka.springboottechtesttransaction.domain.Cuenta;
import ec.sofka.springboottechtesttransaction.domain.Movimiento;
import ec.sofka.springboottechtesttransaction.dto.EstadoCuentaDTO;
import ec.sofka.springboottechtesttransaction.dto.MovimientoDTO;
import ec.sofka.springboottechtesttransaction.exception.DataNotFound;
import ec.sofka.springboottechtesttransaction.repository.CuentaRepository;
import ec.sofka.springboottechtesttransaction.repository.MovimientoRepository;
import ec.sofka.springboottechtesttransaction.util.AbstractService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class ReporteService extends AbstractService<Movimiento, MovimientoRepository, Long> {

    Logger log = Logger.getLogger(ReporteService.class.getName());

    private final CuentaRepository cuentaRepository;

    public ReporteService(MovimientoRepository repository, CuentaRepository cuentaRepository) {
        super(repository, "Reporte");
        this.cuentaRepository = cuentaRepository;
    }

    public List<EstadoCuentaDTO> generarReporte(LocalDate fechaInicial, LocalDate fechaFinal, String... clienteId) {

        log.info("Generando reporte");

        List<Cuenta> cuentaList;

        if (clienteId == null || clienteId.length == 0) {
            cuentaList = cuentaRepository.findAll();
            if (cuentaList == null || cuentaList.isEmpty()) {
                throw new DataNotFound("No se encontraron cuentas");
            }
        } else {
            cuentaList = cuentaRepository.findByClienteId(clienteId[0]);
            if (cuentaList == null || cuentaList.isEmpty()) {
                throw new DataNotFound("No se encontraron cuentas para el cliente");
            }
        }

        return cuentaList
                .stream()
                .map(cuenta -> {
                    List<MovimientoDTO> movimientoDTOList = repository
                            .findByCuentaIdAndFechaBetween(cuenta.getId(), fechaInicial, fechaFinal)
                            .stream()
                            .map(mov ->
                                    MovimientoDTO.builder()
                                            .fecha(mov.getFecha())
                                            .tipoMovimiento(mov.getTipoMovimiento())
                                            .valor(mov.getValor())
                                            .saldo(mov.getSaldo())
                                            .build()
                                    )
                            .collect(Collectors.toList());

                    return EstadoCuentaDTO.builder()
                            .numeroCuenta(cuenta.getNumeroCuenta())
                            .tipoCuenta(cuenta.getTipoCuenta())
                            .saldoInicial(cuenta.getSaldoInicial())
                            .saldoActual(
                                    cuenta.getSaldoInicial() + repository.sumSaldoByCuentaId(cuenta.getId())
                            )
                            .movimientos(movimientoDTOList)
                            .build();
                }).collect(Collectors.toList());

    }

}
