package ec.sofka.springboottechtesttransaction.messaging;

import ec.sofka.springboottechtesttransaction.dto.ClienteCuentaDTO;
import ec.sofka.springboottechtesttransaction.service.CuentaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class ClienteConsumer {

    Logger log = LoggerFactory.getLogger(ClienteConsumer.class);

    @Autowired
    private CuentaService cuentaService;

    @RabbitListener(queues = "clienteQueue")
    public void recibirClienteCreado(ClienteCuentaDTO clienteCuentaDTO) {
        log.info("Mensaje recibido: Cliente creado con ID " + clienteCuentaDTO.getCliente().getClienteId());
        cuentaService.createNewDefaultAccount(clienteCuentaDTO);
    }

}
