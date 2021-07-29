package com.otp.moneyzeraapi.repository;

import com.otp.moneyzeraapi.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

//    List<Categoria> findByUsuario_Id(Long usuarioId);

}
