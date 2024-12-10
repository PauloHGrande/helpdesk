package com.fantasma.helpdesk.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fantasma.helpdesk.domain.Chamado;

public interface ChamadoRepository extends JpaRepository<Chamado, Integer>{

}
