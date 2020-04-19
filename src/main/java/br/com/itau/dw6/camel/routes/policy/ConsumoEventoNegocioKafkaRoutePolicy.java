package br.com.itau.dw6.camel.routes.policy;

import org.apache.camel.Route;
import org.apache.camel.support.RoutePolicySupport;

import br.com.itau.dw6.camel.controllers.ConsumoEventoNegocioKafkaController;

public class ConsumoEventoNegocioKafkaRoutePolicy extends RoutePolicySupport   {

	ConsumoEventoNegocioKafkaController consumoEventoNegocioKafkaController;

	public ConsumoEventoNegocioKafkaRoutePolicy(ConsumoEventoNegocioKafkaController consumoEventoNegocioKafkaController) {
		
		this.consumoEventoNegocioKafkaController = consumoEventoNegocioKafkaController;
		
	}
		
    public void onStop(Route route) {
    	
		consumoEventoNegocioKafkaController.limparCaches();
		
    }
	
}
