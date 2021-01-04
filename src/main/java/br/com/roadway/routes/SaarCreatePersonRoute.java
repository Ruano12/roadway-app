package br.com.roadway.routes;

import org.apache.camel.Exchange;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

@Component
public class SaarCreatePersonRoute extends SpringRouteBuilder {
	
	@Value("${saar.url}")
    private String saarUrl;

	public static String DIRECT_SAAR_CREATE_PERSON = "direct:"+ SaarCreatePersonRoute.class.getName();
	
	@Override
	public void configure() throws Exception {
		from(DIRECT_SAAR_CREATE_PERSON)
			.doTry()
			.routeId(SaarCreatePersonRoute.class.getName()+"_ID")
			.removeHeader("*")
			.removeHeader(Exchange.HTTP_PATH)
			.setHeader("Accept", simple("application/json"))
			.setHeader("Content-Type", constant("application/json"))
			.transform()
			.groovy("resource:classpath:br/com/roadway/groovy/SaarCreatePerson.groovy")
			.marshal().json(JsonLibrary.Jackson)
			.log("token ${exchangeProperty.token}")
			.setHeader("Authorization", simple("${exchangeProperty.token}"))
			.setHeader(Exchange.HTTP_METHOD, constant(HttpMethod.POST))
			//.setHeader(Exchange.HTTP_PATH, simple("v1/persons?bridgeEndpoint=true"))
			.log("[SAAR-CREATE-PERSON] Criando person - ${body}")
			.to("http4:"+saarUrl+"/v1/persons?bridgeEndpoint=true")
			.unmarshal().json(JsonLibrary.Jackson)
			.log("[SAAR-CREATE-PERSON] Response - ${body}")
			.setProperty("personCreateResponse", simple("${body}"))
			.end();
	}
}
