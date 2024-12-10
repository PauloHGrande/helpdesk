package com.fantasma.helpdesk.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fantasma.helpdesk.domain.Pessoa;

public interface PessoaRepository extends JpaRepository<Pessoa, Integer>{

}
