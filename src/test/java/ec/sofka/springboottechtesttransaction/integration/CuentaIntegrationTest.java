package ec.sofka.springboottechtesttransaction.integration;

import ec.sofka.springboottechtesttransaction.domain.Cuenta;
import ec.sofka.springboottechtesttransaction.repository.CuentaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CuentaIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CuentaRepository cuentaRepository;

    private Cuenta cuenta;

    @BeforeEach
    void setUp() {
        cuenta = Cuenta.builder()
                .numeroCuenta("CTA123")
                .tipoCuenta("Ahorros")
                .clienteId("CLI123")
                .saldoInicial(1000.0)
                .estado(true)
                .build();
        cuentaRepository.deleteByClienteId(cuenta.getClienteId());
        cuentaRepository.save(cuenta);
    }

    @AfterEach
    void clean() {
        cuentaRepository.deleteByClienteId(cuenta.getClienteId());
    }

    @Test
    void testIntegrationCuentaDeleteCreateReadRepositoryController() {
        ResponseEntity<Cuenta> response = restTemplate
                .getForEntity("/cuentas/{id}", Cuenta.class, cuenta.getId());
        assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(cuenta.getNumeroCuenta(), response.getBody().getNumeroCuenta());
    }

}