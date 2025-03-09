package ec.sofka.springboottechtesttransaction.service;

import ec.sofka.springboottechtesttransaction.domain.Cuenta;
import ec.sofka.springboottechtesttransaction.dto.ClienteCuentaDTO;
import ec.sofka.springboottechtesttransaction.repository.CuentaRepository;
import ec.sofka.springboottechtesttransaction.util.AbstractService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CuentaService extends AbstractService<Cuenta, CuentaRepository, Long> {

    private Logger log = LoggerFactory.getLogger(CuentaService.class);

    public CuentaService(CuentaRepository repository) {
        super(repository, "Cuenta");
    }

    @Override
    public Cuenta preSave(Cuenta cuenta) {
        log.info("Preparando para guardar cuenta: {}", cuenta);
        return super.preSave(cuenta);
    }

    @Override
    public Cuenta preUpdate(Cuenta cuenta) {
        log.info("Preparando para actualizar cuenta: {}", cuenta);
        return super.preSave(cuenta);
    }

    public Cuenta createNewDefaultAccount(ClienteCuentaDTO clienteCuentaDTO) {
        return save(new Cuenta()
                .setNumeroCuenta(clienteCuentaDTO.getNumeroCuenta())
                .setTipoCuenta(clienteCuentaDTO.getTipoCuenta())
                .setSaldoInicial(clienteCuentaDTO.getSaldoInicial())
                .setEstado(clienteCuentaDTO.getEstado())
                .setClienteId(clienteCuentaDTO.getCliente().getClienteId())
        );
    }

    public Cuenta updateCuenta(Long id, Cuenta cuenta) throws Throwable {
        Cuenta cuentaToUpdate = read(id);
        return update(
                cuentaToUpdate
                        .setNumeroCuenta(cuenta.getNumeroCuenta())
                        .setTipoCuenta(cuenta.getTipoCuenta())
                        .setSaldoInicial(cuenta.getSaldoInicial())
                        .setEstado(cuenta.getEstado())
                        .setClienteId(cuenta.getClienteId())
        );
    }

}
