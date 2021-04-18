package br.com.leonardozv.captura.eventos.routes;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;
import org.springframework.stereotype.Component;

@Component
public class ApiRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		
        restConfiguration()        
        .component("servlet")
        .bindingMode(RestBindingMode.auto)
        .dataFormatProperty("prettyPrint", "true")
        .contextPath("/api")
        .apiContextPath("/api-doc")
        .apiContextRouteId("rota-api")        
        .apiProperty("api.title", "Camel API")
        .apiProperty("api.version", "1.2.3")
        .apiProperty("cors", "true");
		
	    rest("/sql-server")	    
        .post()        
        	.consumes("text/plain")
        	.type(String.class)
        	.param()        		
        		.name("body")
        		.type(RestParamType.body)        		
        	.endParam()
        	.route().routeId("rota-execucao-query-sql-server")
        	.to("bean:querySqlService?method=executarQuerySqlServer");
	    
	    rest("/sap-hana")	    
        .post()        
    	.consumes("text/plain")
    	.type(String.class)
    	.param()        		
    		.name("body")
    		.type(RestParamType.body)        		
    	.endParam()
    	.route().routeId("rota-execucao-query-sap-hana")
    	.to("bean:querySqlService?method=executarQuerySapHana");
    
	}
	
}
