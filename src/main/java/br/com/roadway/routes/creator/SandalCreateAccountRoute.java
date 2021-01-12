package br.com.roadway.routes.creator;

import org.apache.camel.Exchange;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import br.com.roadway.RoadwayRouteBuilder;

@Component
public class SandalCreateAccountRoute extends RoadwayRouteBuilder {

	@Value("${sandal.url}")
    private String sandalUrl;

	public static String DIRECT_SANDAL_CREATE_ACCOUNT = "direct:"+ SandalCreateAccountRoute.class.getName();

	@Override
	public void configure() throws Exception {
		from(DIRECT_SANDAL_CREATE_ACCOUNT)
		.doTry()
			.routeId(SandalCreateAccountRoute.class.getName()+"_ID")
			.removeHeader("*")
			.setHeader("Accept", simple("application/json"))
			.setHeader("Content-Type", constant("application/json"))
			.transform()
			.groovy("resource:classpath:br/com/roadway/groovy/sandal/SandalCreateAccount.groovy")
			.marshal().json(JsonLibrary.Jackson)
			.log("token ${exchangeProperty.token}")
			.setHeader("Authorization", simple("${exchangeProperty.token}"))
			.setHeader(Exchange.HTTP_METHOD, constant(HttpMethod.POST))
			.log("[SANDAL-CREATE-ACCOUNT] Criando conta - ${body}")
			.to("http4:"+sandalUrl+"/v1/digital-accounts?bridgeEndpoint=true")
			.unmarshal().json(JsonLibrary.Jackson)
			.log("[SANDAL-CREATE-ACCOUNT] Response - ${body}")
			.setProperty("accountResponse", simple("${body}"))
			.end();
		
	}

}
