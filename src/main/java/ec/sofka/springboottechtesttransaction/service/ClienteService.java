package ec.sofka.springboottechtesttransaction.service;

import ec.sofka.springboottechtesttransaction.dto.ClienteDTO;
import ec.sofka.springboottechtesttransaction.exception.DataNotFound;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
public class ClienteService {

    private final WebClient webClient = WebClient.create("http://localhost:8080");

    /**
     * Método síncrono que obtiene la información de un cliente por su id
     * @param clienteId
     * @return
     */
    public Optional<ClienteDTO> getClientById(String clienteId) {
        return Optional.ofNullable(webClient.get()
                .uri("/clientes/{clienteId}", clienteId)
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        res -> Mono.error(new DataNotFound("Cliente no encontrado"))
                )
                .onStatus(HttpStatusCode::is5xxServerError,
                        res -> Mono.error(new RuntimeException("Error en servidor"))
                )
                .bodyToMono(ClienteDTO.class)
                .block());
    }

}
