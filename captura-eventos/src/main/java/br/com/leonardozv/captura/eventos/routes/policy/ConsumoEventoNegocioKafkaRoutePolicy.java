package br.com.leonardozv.captura.eventos.routes.policy;

import br.com.leonardozv.captura.eventos.controllers.ConsumoEventoNegocioKafkaController;
import org.apache.camel.Route;
import org.apache.camel.support.RoutePolicySupport;

public class ConsumoEventoNegocioKafkaRoutePolicy extends RoutePolicySupport   {

	private ConsumoEventoNegocioKafkaController consumoEventoNegocioKafkaController;

	public ConsumoEventoNegocioKafkaRoutePolicy(ConsumoEventoNegocioKafkaController consumoEventoNegocioKafkaController) {
		
		this.consumoEventoNegocioKafkaController = consumoEventoNegocioKafkaController;
		
	}
		
    public void onStop(Route route) {
    	
		consumoEventoNegocioKafkaController.limparCaches();
		
    }
	
}
