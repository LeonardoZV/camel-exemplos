package br.com.itau.dw6.camel.controllers;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;

import org.apache.avro.generic.GenericContainer;
import org.apache.camel.Exchange;
import org.apache.camel.component.kafka.KafkaConstants;
import org.apache.camel.component.kafka.KafkaManualCommit;
import org.apache.kafka.common.errors.SerializationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.itau.dw6.camel.services.CacheParticoesComFalhaService;
import br.com.itau.dw6.camel.services.TopicoConsumoEventoNegocioService;
import io.confluent.kafka.serializers.GenericContainerWithVersion;

@Controller
public class ConsumoEventoNegocioKafkaController {

	@Autowired
	private TopicoConsumoEventoNegocioService topicoConsumoEventoNegocioService;
	
	@Autowired
	private CacheParticoesComFalhaService cacheParticoesComFalhaService;
	
	public List<String> obterTopicosFormatoLista() {
				
		return topicoConsumoEventoNegocioService.getListaTopicos();
		
	}
	
	public void limparCaches() {
		
		cacheParticoesComFalhaService.clear();
		
	}
	
	public String obterTopicosFormatoString() {

		StringBuilder from = new StringBuilder();
		
		Iterator<String> iterator = topicoConsumoEventoNegocioService.getListaTopicos().iterator();		
		
		while(iterator.hasNext()) {
			
			from.append(iterator.next());
			
			if (iterator.hasNext()) from.append(",");
			
		}
		
		return from.toString();
		
	}
	
	public boolean ehTopicoParticaoComFalha(Exchange exchange) {
		
		String topicoParticao = (String)exchange.getIn().getHeader("TOPICO-PARTICAO");
		
		if (cacheParticoesComFalhaService.contains(topicoParticao)) {
			return true;
		}
		
		return false;
		
	}
	
	public void dispararExceptionCasoBodySejaException(Exchange exchange) throws Exception {

		if (exchange.getIn().getBody() instanceof Exception)
			throw (Exception)exchange.getIn().getBody();
		
	}			
	
	public boolean ehParaPersistirEvento(Exchange exchange) {
		
		return true;
		
	}
	
	public void persistirEvento(Exchange exchange) throws Exception {
		
		GenericContainerWithVersion genericContainerWithVersion = (GenericContainerWithVersion)exchange.getIn().getBody();
		GenericContainer genericContainer = genericContainerWithVersion.container();
		
		if (genericContainer.toString().contains("value1")) {
			throw new Exception("Erro x");
		}
		
	}
	
	public void realizarCommitOffset(Exchange exchange) {

		((KafkaManualCommit)exchange.getIn().getHeader(KafkaConstants.MANUAL_COMMIT)).commitSync();
		
	}
	
	public boolean causaExceptionEhSerializationException(Exchange exchange) {
		
		Exception exception = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);		
		
		Throwable cause = exception.getCause();
		
		if (cause != null) {
			if (cause.getClass() == SerializationException.class) {
				return true;
			}
		}
		
		return false;
	}
	
	public void incluirTopicoParticaoComFalha(Exchange exchange) {
		
		String topicoParticao = (String)exchange.getIn().getHeader("TOPICO-PARTICAO");

		cacheParticoesComFalhaService.add(topicoParticao);		
		
	}
	
	public String formatarLogErro(Exchange exchange) {
		
		String topico = (String)exchange.getIn().getHeader("kafka.TOPIC");
		
		String particao = (String)exchange.getIn().getHeader("kafka.PARTITION").toString();
		
		Exception cause = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);		
		
        StringWriter sw = new StringWriter();
        
        cause.printStackTrace(new PrintWriter(sw));
        		
		return "Topico: " + topico + " - Partição: " + particao + " - Exception: " + sw.toString();
		
	}

}
