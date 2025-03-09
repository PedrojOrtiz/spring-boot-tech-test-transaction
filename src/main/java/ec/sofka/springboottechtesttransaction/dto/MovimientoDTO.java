package ec.sofka.springboottechtesttransaction.dto;

import lombok.*;

import java.time.LocalDate;

@Getter @Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class MovimientoDTO {

    private LocalDate fecha;

    private String cliente;

    private String numeroCuenta;

    private String tipoCuenta;

    private Double saldoInicial;

    private String tipoMovimiento;

    private Boolean estado;

    private Double valor;

    private Double saldo;

}
