package br.com.itau.dw6.camel.services;

import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class CacheParticoesComFalhaService {	

	private List<String> listaParticoesComFalha = new LinkedList<String>();
	
	public boolean contains(String topicoParticao) {
		
		if (listaParticoesComFalha.contains(topicoParticao)) 
			return true;
		else
			return false;
		
	}
	
	public void add(String topicoParticao) {
		
		listaParticoesComFalha.add(topicoParticao);
		
	}
	
	public void clear() {
		
		listaParticoesComFalha.clear();
		
	}
	
}
