package br.com.itau.dw6.camel.kafka.schemaregistry;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.kafka.common.serialization.Serializer;

import io.confluent.common.config.ConfigException;
import io.confluent.kafka.schemaregistry.client.CachedSchemaRegistryClient;
import io.confluent.kafka.serializers.AbstractKafkaAvroSerializer;
import io.confluent.kafka.serializers.AvroSchemaUtils;

public class CustomKafkaAvroSerializer extends AbstractKafkaAvroSerializer implements Serializer<Object> {

	private static final String SCHEMA_REGISTRY_URL = "http://leozvasconcellos-kafka.eastus.cloudapp.azure.com:8081";

	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
		
	    try {
	    	
	        final List<String> schemas = Collections.singletonList(SCHEMA_REGISTRY_URL);
	        
	        this.schemaRegistry = new CachedSchemaRegistryClient(schemas, Integer.MAX_VALUE);
	        
	        this.autoRegisterSchema = false;
	
	     } catch (ConfigException e) {
	  	   throw new org.apache.kafka.common.config.ConfigException(e.getMessage());
	   }
	    
	}

	@Override
	public byte[] serialize(String topic, Object record) {
		
		return serializeImpl(getSubjectName(topic, false, record, AvroSchemaUtils.getSchema(record)), record);
		
	}
}