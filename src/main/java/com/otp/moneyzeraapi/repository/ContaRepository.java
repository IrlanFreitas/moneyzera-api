package com.otp.moneyzeraapi.repository;

import com.otp.moneyzeraapi.model.Conta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContaRepository extends JpaRepository<Conta, Long> {
}
