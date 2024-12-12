package com.fantasma.helpdesk.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fantasma.helpdesk.domain.Chamado;
import com.fantasma.helpdesk.repositories.ChamadoRepository;
import com.fantasma.helpdesk.services.exceptions.ObjectNotFoundException;

@Service
public class ChamadoService {

	@Autowired
	private ChamadoRepository chamadoRepository;
	
	public Chamado findById(Integer id) {
		Optional<Chamado> obj = chamadoRepository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Chamado não Encontrado! id: " + id));
	
	}

	public List<Chamado> findAll() {
		return chamadoRepository.findAll();
	}
}
