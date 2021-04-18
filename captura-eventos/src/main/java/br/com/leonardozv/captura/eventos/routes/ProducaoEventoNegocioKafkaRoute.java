package br.com.leonardozv.captura.eventos.routes;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import br.com.leonardozv.captura.eventos.serializers.CustomKafkaAvroSerializer;

@Component
public class ProducaoEventoNegocioKafkaRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		
//		String json = "{\"menu\": { \"id\": \"asd\", \"texto1\": \"texto1\", \"numero1\": 1, \"texto2\": \"texto2\", \"numero2\": 2, \"popup\": { \"menuitem\": [ {\"value\": \"New\" }, {\"value\": \"Open\" }, {\"value\": \"Close\" } ] } }}";
//		
//		Schema schema = new Schema.Parser().parse("{\"name\": \"MyClass\",\"type\": \"record\",\"namespace\": \"com.acme.avro\",\"fields\": [{\"name\": \"menu\",\"type\": {\"name\": \"menu\",\"type\": \"record\",\"fields\": [{\"name\": \"id\",\"type\": \"string\"},{\"name\": \"texto1\",\"type\": \"string\"},{\"name\": \"numero1\",\"type\": \"int\"},{\"name\": \"texto2\",\"type\": \"string\"},{\"name\": \"numero2\",\"type\": \"int\"},{\"name\": \"popup\",\"type\": {\"name\": \"popup\",\"type\": \"record\",\"fields\": [{\"name\": \"menuitem\",\"type\": {\"type\": \"array\",\"items\": {\"name\": \"menuitem_record\",\"type\": \"record\",\"fields\": [{\"name\": \"value\",\"type\": \"string\"}]}}}]}}]}}]}");
		
		String json = "{\"f1\": \"value2\", \"detalhes\":[{\"f2\": \"value2\"}]}";
		
		Schema schema = new Schema.Parser().parse("{\"type\":\"record\",\"name\":\"myrecord\",\"fields\":[{\"name\":\"f1\",\"type\":\"string\"},{\"name\":\"detalhes\", \"type\":{\"name\": \"detalhes\", \"type\":\"array\",\"items\":{\"name\": \"detalhes\",\"type\":\"record\",\"fields\":[{\"name\":\"f2\",\"type\":\"string\"}]}}}]}");
				
		StringBuilder to = new StringBuilder();
		
		to.append("kafka:test3");
		to.append("?brokers=");
		to.append("leozvasconcellos-kafka.eastus.cloudapp.azure.com:9092");
		to.append("&serializerClass=");
		to.append(CustomKafkaAvroSerializer.class.getName());
        		
		from("timer://teste?fixedRate=true&period=1")
		.routeId("rota-producao-evento-negocio-kafka")
		.process(exchange -> {
			DecoderFactory decoderFactory = new DecoderFactory();
			Decoder decoder = decoderFactory.jsonDecoder(schema, json);
			DatumReader<GenericRecord> reader = new GenericDatumReader<GenericRecord>(schema);
			GenericRecord genericRecord = reader.read(null, decoder);
			exchange.getIn().setBody(genericRecord);
		})
		.to(to.toString())
		.log("Mensagem postada!");

	}
	
}
