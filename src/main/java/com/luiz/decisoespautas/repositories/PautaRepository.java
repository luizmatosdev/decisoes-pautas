package com.luiz.decisoespautas.repositories;

import com.luiz.decisoespautas.entities.Pauta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PautaRepository extends JpaRepository<Pauta, Long> {
    @Query(
        "SELECT new Pauta( " +
            "pauta.id, " +
            "pauta.titulo, " +
            "pauta.descricao, " +
            "pauta.tempoLimiteEmAberto, " +
            "pauta.minutosEmAberto, " +
            "SUM(case when(voto.votoPositivo = true) then 1 else 0 end)," +
            "SUM(case when(voto.votoPositivo = false) then 1 else 0 end) " +
        ") " +
        "FROM Pauta pauta " +
        "LEFT JOIN VotoSessaoPauta voto ON voto.pauta.id = pauta.id " +
        "GROUP BY pauta.id, pauta.minutosEmAberto, pauta.tempoLimiteEmAberto, pauta.descricao, pauta.titulo"
    )
    List<Pauta> listaPautasComVotos();
}
