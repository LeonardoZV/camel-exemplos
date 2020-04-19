package br.com.itau.dw6.camel.services;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Service;

@Service
public class TopicoConsumoEventoNegocioService {

	boolean inicializando = true;
	
	private List<String> listaTopicos = new CopyOnWriteArrayList<String>();
	
	public TopicoConsumoEventoNegocioService() {
		
		Thread threadAtualizacao = new Thread(() -> {
			
			this.atualizarTopicos();
			
			inicializando = false;
			
			try { Thread.sleep(3600000); } catch (InterruptedException e) {}
			
		});
		
		threadAtualizacao.start();	
		
	}
	
	public List<String> getListaTopicos() {
		
		while(inicializando) {
			try	{ Thread.sleep(1000); } catch (InterruptedException e) {}
		}
		
		return listaTopicos;
		
	}
	
	public void atualizarTopicos() {
		
		this.listaTopicos.clear();
		
		int i = 1;
		
		while(i <= 2) {
			this.listaTopicos.add("test" + i);			
			i++;
		}
		
//		this.listaTopicos.add("test2");
		
	}
		
}
