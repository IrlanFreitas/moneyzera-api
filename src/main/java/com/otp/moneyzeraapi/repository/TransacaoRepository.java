package com.otp.moneyzeraapi.repository;

import com.otp.moneyzeraapi.model.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {
}
