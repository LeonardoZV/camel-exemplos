package br.com.leonardozv.captura.eventos.serializers;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.kafka.common.config.ConfigException;
import org.apache.kafka.common.serialization.Deserializer;

import io.confluent.kafka.schemaregistry.client.CachedSchemaRegistryClient;
import io.confluent.kafka.serializers.AbstractKafkaAvroDeserializer;

public class CustomKafkaAvroDeserializer extends AbstractKafkaAvroDeserializer implements Deserializer<Object> {

private static final String SCHEMA_REGISTRY_URL = "http://leozvasconcellos-kafka.eastus.cloudapp.azure.com:8081";

	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
		
		try {
	    	 
			final List<String> schemas = Collections.singletonList(SCHEMA_REGISTRY_URL);
		      
			this.schemaRegistry = new CachedSchemaRegistryClient(schemas, Integer.MAX_VALUE);
		
		} catch (ConfigException e) {
			throw new org.apache.kafka.common.config.ConfigException(e.getMessage());
		}
		
	}

	@Override
	public Object deserialize(String s, byte[] bytes) {
	  
		try {
			return deserializeWithSchemaAndVersion(s, false, bytes);
		}
		catch (Exception ex) {
			return ex;
		}
	  
	}

}