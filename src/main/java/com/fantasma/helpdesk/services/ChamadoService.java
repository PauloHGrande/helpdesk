package com.fantasma.helpdesk.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fantasma.helpdesk.domain.Chamado;
import com.fantasma.helpdesk.domain.Cliente;
import com.fantasma.helpdesk.domain.Tecnico;
import com.fantasma.helpdesk.domain.dtos.ChamadoDTO;
import com.fantasma.helpdesk.domain.enums.Prioridade;
import com.fantasma.helpdesk.domain.enums.Status;
import com.fantasma.helpdesk.repositories.ChamadoRepository;
import com.fantasma.helpdesk.services.exceptions.ObjectNotFoundException;

import jakarta.validation.Valid;

@Service
public class ChamadoService {

	@Autowired
	private ChamadoRepository chamadoRepository;
	
	@Autowired
	private TecnicoService tecnicoService;
	
	@Autowired
	private ClienteService clienteService;
	
	public Chamado findById(Integer id) {
		Optional<Chamado> obj = chamadoRepository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Chamado não Encontrado! id: " + id));
	
	}

	public List<Chamado> findAll() {
		return chamadoRepository.findAll();
	}

	public Chamado create(@Valid ChamadoDTO objDTO) {
		return chamadoRepository.save(newChamado(objDTO));
	}
	
	private Chamado newChamado(ChamadoDTO obj) {
		Tecnico tecnico = tecnicoService.findById(obj.getTecnico());
		Cliente cliente = clienteService.findById(obj.getCliente());
		
		Chamado chamado = new Chamado();
		if(obj.getId() != null) {
			chamado.setId(obj.getId());
		}
		
		if(obj.getStatus().equals(2)) {
			chamado.setDataFechamento(LocalDate.now());
		}
		
		chamado.setTecnico(tecnico);
		chamado.setCliente(cliente);
		chamado.setPrioridade(Prioridade.toEnum(obj.getPrioridade()));
		chamado.setStatus(Status.toEnum(obj.getStatus()));
		chamado.setTitulo(obj.getTitulo());
		chamado.setObservacoes(obj.getObservacoes());
		
		return chamado;
	}

	public Chamado update(Integer id, @Valid ChamadoDTO objDTO) {
		objDTO.setId(id);
		Chamado oldObj = findById(id);
		oldObj = newChamado(objDTO);
		return chamadoRepository.save(oldObj);
	}
}
