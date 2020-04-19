package br.com.itau.dw6.camel.routes;

import java.io.File;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.support.processor.idempotent.FileIdempotentRepository;

//@Component
public class ConsumoEventoNegocioBucketS3Route extends RouteBuilder {
	
	@Override
	public void configure() throws Exception {
		
	    from("aws-s3://ryft-public-sample-data?amazonS3Client=#amazonS3&deleteAfterRead=false&delay=5s")
	    .routeId("rota-consumo-evento-negocio-bucket-s3")
	    .idempotentConsumer(header("CamelAwsS3ETag"), FileIdempotentRepository.fileIdempotentRepository(new File("D:\\OneDrive - leonardozv\\Desktop\\file.data")))
    	.log("${header.CamelAwsS3Key}")
	    .log("${body}")
	    ;
    
	}
	
}
