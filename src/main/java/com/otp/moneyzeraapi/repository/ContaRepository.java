package com.otp.moneyzeraapi.repository;

import com.otp.moneyzeraapi.model.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface ContaRepository extends JpaRepository<Conta, Long> {

    //* ? Usando Query
    //* ? @Query("SELECT c FROM Conta c WHERE c.usuario.id = :id ")
    //* ? Usando Named Queries
    List<Conta> findByUsuario_Id(Long idUsuario);

    @Query(value = "SELECT SUM(saldo) FROM conta WHERE usuario_id = :id", nativeQuery = true)
    BigDecimal obterSaldoUsuario(Long id);

}

