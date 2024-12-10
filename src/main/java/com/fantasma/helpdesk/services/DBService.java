package com.fantasma.helpdesk.services;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fantasma.helpdesk.domain.Chamado;
import com.fantasma.helpdesk.domain.Cliente;
import com.fantasma.helpdesk.domain.Tecnico;
import com.fantasma.helpdesk.domain.enums.Perfil;
import com.fantasma.helpdesk.domain.enums.Prioridade;
import com.fantasma.helpdesk.domain.enums.Status;
import com.fantasma.helpdesk.repositories.ChamadoRepository;
import com.fantasma.helpdesk.repositories.ClienteRepository;
import com.fantasma.helpdesk.repositories.TecnicoRepository;

@Service
public class DBService {
	
	@Autowired
	private TecnicoRepository tecnicoRepository;
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private ChamadoRepository chamadoRepository;
	
	public void instanciaDB() {
		Tecnico tec1 = new Tecnico(null, "Paulo Grande", "44088343000", "pauloav2013@gmail.com", "123");
		tec1.addPerfis(Perfil.ADMIN);
		
		Cliente cli1 = new Cliente(null, "Rafael", "52604731088", "rafaelggrande@gmail.com", "123");
		
		Chamado c1 = new Chamado(null, Prioridade.MEDIA, Status.ANDAMENTO, "Chamado 01", "Primeiro Chamado", tec1, cli1);
		
		tecnicoRepository.saveAll(Arrays.asList(tec1));
		clienteRepository.saveAll(Arrays.asList(cli1));
		chamadoRepository.saveAll(Arrays.asList(c1));
	}
}
