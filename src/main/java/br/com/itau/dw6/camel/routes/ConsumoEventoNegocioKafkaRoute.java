package br.com.itau.dw6.camel.routes;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.kafka.common.serialization.StringDeserializer;

import br.com.itau.dw6.camel.controllers.ConsumoEventoNegocioKafkaController;
import br.com.itau.dw6.camel.kafka.schemaregistry.CustomKafkaAvroDeserializer;
import br.com.itau.dw6.camel.routes.policy.ConsumoEventoNegocioKafkaRoutePolicy;

public class ConsumoEventoNegocioKafkaRoute extends RouteBuilder {
	private String routeId;
	private String topicos;
	
	private ConsumoEventoNegocioKafkaController consumoEventoNegocioKafkaController;
	
	public ConsumoEventoNegocioKafkaRoute(String routeId, String topicos, ConsumoEventoNegocioKafkaController consumoEventoNegocioKafkaController) {
		
		this.routeId = routeId;
		this.topicos = topicos;
		this.consumoEventoNegocioKafkaController = consumoEventoNegocioKafkaController;
		
	}
	
	@Override
	public void configure() throws Exception {
		
		StringBuilder from = new StringBuilder();
		
		from.append("kafka:" + topicos);
		from.append("?brokers=");
		from.append("leozvasconcellos-kafka.eastus.cloudapp.azure.com:9092");
        from.append("&groupId=");
        from.append("consumidores-evento-negocio-multivisao");
        from.append("&consumersCount=");
        from.append("1");
        from.append("&autoOffsetReset=");
        from.append("earliest");
        from.append("&autoCommitEnable=");
        from.append(false);
        from.append("&allowManualCommit=");
        from.append(true);
        from.append("&keyDeserializer=");
        from.append(StringDeserializer.class.getName());
        from.append("&valueDeserializer=");
        from.append(CustomKafkaAvroDeserializer.class.getName());

        onException(Exception.class)
        .handled(true)
        .choice()
        .when().method(ConsumoEventoNegocioKafkaController.class, "causaExceptionEhSerializationException")
        	.bean(ConsumoEventoNegocioKafkaController.class, "realizarCommitOffset")
            .bean(ConsumoEventoNegocioKafkaController.class, "formatarLogErro")
            .log(LoggingLevel.ERROR, "${body}")
        .otherwise()
	        .bean(ConsumoEventoNegocioKafkaController.class, "incluirTopicoParticaoComFalha")
	        .bean(ConsumoEventoNegocioKafkaController.class, "formatarLogErro")
	        .log(LoggingLevel.ERROR, "${body}")
	        .to("mock:teste")
        .end();		
        
		from(from.toString())
		.routeId(routeId)
		.routePolicy(new ConsumoEventoNegocioKafkaRoutePolicy(consumoEventoNegocioKafkaController))
		.setHeader("TOPICO-PARTICAO").simple("${headers[kafka.TOPIC]}-${headers[kafka.PARTITION]}")
		.setHeader("TOPICO-PARTICAO-COM-FALHA").method(ConsumoEventoNegocioKafkaController.class, "ehTopicoParticaoComFalha")
		.filter().simple("${header.TOPICO-PARTICAO-COM-FALHA} == false")
			.bean(ConsumoEventoNegocioKafkaController.class, "dispararExceptionCasoBodySejaException")
			.setHeader("PERSISTIR-EVENTO", method(ConsumoEventoNegocioKafkaController.class, "ehParaPersistirEvento"))
			.filter().simple("${header.PERSISTIR-EVENTO} == true")
				.bean(ConsumoEventoNegocioKafkaController.class, "persistirEvento")
			.end()
			.bean(ConsumoEventoNegocioKafkaController.class, "realizarCommitOffset")
			.log(LoggingLevel.INFO, "Tópico: ${headers[kafka.TOPIC]} - Particao: ${headers[kafka.PARTITION]} - Body: ${body.container.toString}")
		.end();

	}

}
