package ec.sofka.springboottechtesttransaction.service;

import ec.sofka.springboottechtesttransaction.domain.Cuenta;
import ec.sofka.springboottechtesttransaction.domain.Movimiento;
import ec.sofka.springboottechtesttransaction.dto.MovimientoDTO;
import ec.sofka.springboottechtesttransaction.exception.InsufficientBalance;
import ec.sofka.springboottechtesttransaction.repository.CuentaRepository;
import ec.sofka.springboottechtesttransaction.repository.MovimientoRepository;
import ec.sofka.springboottechtesttransaction.util.AbstractService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class MovimientoService extends AbstractService<Movimiento, MovimientoRepository, Long> {

    private Logger log = LoggerFactory.getLogger(MovimientoService.class);

    private final CuentaRepository cuentaRepository;

    public MovimientoService(MovimientoRepository repository, CuentaRepository cuentaRepository) {
        super(repository, "Movimiento");
        this.cuentaRepository = cuentaRepository;
    }

    @Override
    public Movimiento preSave(Movimiento movimiento) {
        log.info("Preparando para guardar movimiento: {}", movimiento);
        return super.preSave(movimiento);
    }

    @Override
    public Movimiento preUpdate(Movimiento movimiento) {
        log.info("Preparando para actualizar movimiento: {}", movimiento);
        return super.preSave(movimiento);
    }

    @Transactional
    public Movimiento createMovimiento(MovimientoDTO movimientoDTO) {

        // Se obtiene la cuenta asociada al movimiento
        Cuenta cuenta = cuentaRepository
                .findByNumeroCuenta(movimientoDTO.getNumeroCuenta())
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));

        // Validacion de saldo
        double saldoActual = cuenta.getSaldoInicial() + repository.sumSaldoByCuentaId(cuenta.getId());
        double nuevoSaldo = saldoActual + movimientoDTO.getValor();

        if (nuevoSaldo < 0) {
            throw new InsufficientBalance("Saldo insuficiente");
        }

        // Se guarda el movimiento
        return save(
          Movimiento.builder()
                  .fecha(movimientoDTO.getFecha())
                  .tipoMovimiento(movimientoDTO.getTipoMovimiento())
                  .valor(movimientoDTO.getValor())
                  .saldo(nuevoSaldo)
                  .cuenta(cuenta)
                  .build()
        );

    }

}
