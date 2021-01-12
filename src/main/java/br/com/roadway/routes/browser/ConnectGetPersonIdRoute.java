package br.com.roadway.routes.browser;

import org.apache.camel.Exchange;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import br.com.roadway.RoadwayRouteBuilder;

@Component
public class ConnectGetPersonIdRoute  extends RoadwayRouteBuilder {
	
	@Value("${connect.url}")
    private String connectUrl;
	
	public static String DIRECT_CONNECT_GET_PERSON_ID = "direct:"+ ConnectGetPersonIdRoute.class.getName();
	
	@Override
	public void configure() throws Exception {
		from(DIRECT_CONNECT_GET_PERSON_ID)
			.doTry()
			.routeId(ConnectGetPersonIdRoute.class.getName()+"_ID")
			.removeHeader("*")
			.setHeader("Accept", simple("application/json"))
			.setHeader("Content-Type", constant("application/json"))
			.log("token ${exchangeProperty.token}")
			.setHeader("Authorization", simple("${exchangeProperty.token}"))
			.setHeader(Exchange.HTTP_METHOD, constant(HttpMethod.GET))
			.log("[CONNECT-GET-PERSON-ID] Buscando person-id do usuario logado")
			.to("http4:"+connectUrl+"/v1/users/find-logged-person-id?bridgeEndpoint=true")
			.unmarshal().json(JsonLibrary.Jackson)
			.log("[CONNECT-GET-PERSON-ID] Response - ${body}")
			.setProperty("userLoggedPersonId", simple("${body}"))
			.end();
	}
}
