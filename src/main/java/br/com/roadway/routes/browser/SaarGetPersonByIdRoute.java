package br.com.roadway.routes.browser;

import org.apache.camel.Exchange;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import br.com.roadway.RoadwayRouteBuilder;
import br.com.roadway.routes.creator.SaarCreatePersonRoute;

@Component
public class SaarGetPersonByIdRoute extends RoadwayRouteBuilder {
	
	@Value("${saar.url}")
    private String saarUrl;

	public static String DIRECT_SAAR_GET_PERSON_BY_ID = "direct:"+ SaarGetPersonByIdRoute.class.getName();
	
	@Override
	public void configure() throws Exception {
		from(DIRECT_SAAR_GET_PERSON_BY_ID)
			.doTry()
			.routeId(SaarGetPersonByIdRoute.class.getName()+"_ID")
			.removeHeader("*")
			.setHeader("Accept", simple("application/json"))
			.setHeader("Content-Type", constant("application/json"))
			.log("token ${exchangeProperty.token}")
			.setHeader("Authorization", simple("${exchangeProperty.token}"))
			.setHeader(Exchange.HTTP_METHOD, constant(HttpMethod.GET))
			.log("[SAAR-GET-PERSON-BY-ID] Buscando person com o id ${exchangeProperty.userLoggedPersonId['person-id']}")
			.setHeader(Exchange.HTTP_QUERY, simple("id=${exchangeProperty.userLoggedPersonId['person-id']}"))
			.to("http4:"+saarUrl+"/v1/persons?bridgeEndpoint=true")
			.unmarshal().json(JsonLibrary.Jackson)
			.log("[SAAR-CREATE-PERSON] Response - ${body}")
			.setProperty("personByIdResponse", simple("${body}"))
			.end();
	}
}
