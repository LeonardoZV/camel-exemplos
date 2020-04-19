package br.com.itau.dw6.camel.actuator;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

@Component
@Endpoint(id = "healthcheck")
public class HealthCheck {
	
	@ReadOperation
    public String healthCheck() {
        return "200 OK";
    }
    
}