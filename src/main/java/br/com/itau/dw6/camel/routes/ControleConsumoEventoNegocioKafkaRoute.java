package br.com.itau.dw6.camel.routes;

import java.util.LinkedList;
import java.util.List;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.itau.dw6.camel.controllers.ConsumoEventoNegocioKafkaController;

@Component
public class ControleConsumoEventoNegocioKafkaRoute extends RouteBuilder {

	@Autowired
	private ConsumoEventoNegocioKafkaController consumoEventoNegocioKafkaController;
	
	@SuppressWarnings("unused")
	private boolean inicializando = true;
	
	@SuppressWarnings("unused")
	private List<String> listaTopicos = new LinkedList<String>();
		
	@Override
	public void configure() throws Exception {
		
		from("timer://rota-controle-consumo-evento-negocio-kafka?fixedRate=true&period=60000")
		.routeId("rota-controle-consumo-evento-negocio-kafka")
		.process(exchange -> {
			
//			List<String> listaTopicosNova = consumoEventoNegocioKafkaController.obterTopicosFormatoLista();
//			
//			listaTopicos.forEach(t -> {
//				
//				if (!listaTopicosNova.contains(t))
//				{
//					try {
//						this.getContext().getRouteController().stopRoute("rota-consumo-evento-topico-" + t);
//						this.getContext().removeRoute("rota-consumo-evento-topico-" + t);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}	
//				}
//				
//			});
//			
//			listaTopicosNova.forEach(t -> {
//				
//				if (!listaTopicos.contains(t))
//				{		
//					try {
//						this.getContext().addRoutes(new ConsumoEventoNegocioKafkaRoute("rota-consumo-evento-topico-" + t, t, consumoEventoNegocioKafkaController));
//					} catch (Exception e) {
//						e.printStackTrace();
//					}	
//				}
//				
//			});
//			
//			listaTopicos = listaTopicosNova;
	
			try {
				
				String topicosNovos = consumoEventoNegocioKafkaController.obterTopicosFormatoString();
				
				if (inicializando && topicosNovos != "") {
					
					this.getContext().addRoutes(new ConsumoEventoNegocioKafkaRoute("rota-consumo-evento-negocio-kafka", topicosNovos, consumoEventoNegocioKafkaController));
					
					inicializando = false;
					
				}
				else {
					
					this.getContext().getRouteController().stopRoute("rota-consumo-evento-negocio-kafka");
					
					this.getContext().removeRoute("rota-consumo-evento-negocio-kafka");
					
					if (topicosNovos != "")
						this.getContext().addRoutes(new ConsumoEventoNegocioKafkaRoute("rota-consumo-evento-negocio-kafka", topicosNovos, consumoEventoNegocioKafkaController));
					
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		});
		
	}

}
