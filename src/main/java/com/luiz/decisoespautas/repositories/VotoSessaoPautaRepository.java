package com.luiz.decisoespautas.repositories;

import com.luiz.decisoespautas.entities.VotoSessaoPauta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface VotoSessaoPautaRepository extends JpaRepository<VotoSessaoPauta, Long> {
    @Query("SELECT count(*) FROM VotoSessaoPauta V WHERE V.sessaoPauta.id = ?1 AND V.usuario.cpf = ?2")
    public int existeVotoUsuarioNaSessao(Long idSessaoPauta, String cpf);
}
