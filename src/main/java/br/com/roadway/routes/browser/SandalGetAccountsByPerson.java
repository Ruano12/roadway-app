package br.com.roadway.routes.browser;

import org.apache.camel.Exchange;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import br.com.roadway.RoadwayRouteBuilder;

@Component
public class SandalGetAccountsByPerson extends RoadwayRouteBuilder {
	
	@Value("${sandal.url}")
    private String sandalUrl;

	public static String DIRECT_SANDAL_GET_ACCOUNT_PERSON = "direct:"+ SandalGetAccountsByPerson.class.getName();

	@Override
	public void configure() throws Exception {
		from(DIRECT_SANDAL_GET_ACCOUNT_PERSON)
		.doTry()
			.routeId(SandalGetAccountsByPerson.class.getName()+"_ID")
			.removeHeader("*")
			.setHeader("Accept", simple("application/json"))
			.setHeader("Content-Type", constant("application/json"))
			.log("token ${exchangeProperty.token}")
			.setHeader("Authorization", simple("${exchangeProperty.token}"))
			.setHeader(Exchange.HTTP_METHOD, constant(HttpMethod.GET))
			.log("[SANDAL-GET-BY-PERSON] Buscando contas com o person-id ${exchangeProperty.userLoggedPersonId['person-id']}")
			.setHeader(Exchange.HTTP_QUERY, simple("person-id=${exchangeProperty.userLoggedPersonId['person-id']}"))
			.to("http4:"+sandalUrl+"/v1/digital-accounts/find-by-person-id?bridgeEndpoint=true")
			.unmarshal().json(JsonLibrary.Jackson)
			.log("[SANDAL-GET-ACCOUNT-BY-PERSON] Response - ${body}")
			.setProperty("accountByPersonResponse", simple("${body}"))
			.end();
	}
	
}
