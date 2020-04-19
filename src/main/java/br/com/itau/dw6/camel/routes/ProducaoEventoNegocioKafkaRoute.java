package br.com.itau.dw6.camel.routes;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.camel.builder.RouteBuilder;

import br.com.itau.dw6.camel.kafka.schemaregistry.CustomKafkaAvroSerializer;

//@Component
public class ProducaoEventoNegocioKafkaRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		
		String json = "{\"menu\": { \"id\": \"asd\", \"texto1\": \"texto1\", \"numero1\": 1, \"texto2\": \"texto2\", \"numero2\": 2, \"popup\": { \"menuitem\": [ {\"value\": \"New\" }, {\"value\": \"Open\" }, {\"value\": \"Close\" } ] } }}";
		
		Schema schema = new Schema.Parser().parse("{\"name\": \"MyClass\",\"type\": \"record\",\"namespace\": \"com.acme.avro\",\"fields\": [{\"name\": \"menu\",\"type\": {\"name\": \"menu\",\"type\": \"record\",\"fields\": [{\"name\": \"id\",\"type\": \"string\"},{\"name\": \"texto1\",\"type\": \"string\"},{\"name\": \"numero1\",\"type\": \"int\"},{\"name\": \"texto2\",\"type\": \"string\"},{\"name\": \"numero2\",\"type\": \"int\"},{\"name\": \"popup\",\"type\": {\"name\": \"popup\",\"type\": \"record\",\"fields\": [{\"name\": \"menuitem\",\"type\": {\"type\": \"array\",\"items\": {\"name\": \"menuitem_record\",\"type\": \"record\",\"fields\": [{\"name\": \"value\",\"type\": \"string\"}]}}}]}}]}}]}");
		
		StringBuilder to = new StringBuilder();

		to.append("kafka:evento-negocio-1");
		to.append("?brokers=");
		to.append("leozvasconcellos-kafka.eastus.cloudapp.azure.com:9092");
		to.append("&serializerClass=");
		to.append(CustomKafkaAvroSerializer.class.getName());
        		
		from("timer://teste?fixedRate=true&period=1")
		.routeId("rota-producao-evento-negocio-kafka")
		.process(exchange -> {
			DecoderFactory decoderFactory = new DecoderFactory();
			Decoder decoder = decoderFactory.jsonDecoder(schema, json);
			DatumReader<GenericData.Record> reader = new GenericDatumReader<>(schema);
			GenericRecord genericRecord = reader.read(null, decoder);
			exchange.getIn().setBody(genericRecord);
		})
		.to(to.toString());

	}
	
}
