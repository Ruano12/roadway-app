package br.com.roadway.routes;

import org.apache.camel.Exchange;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import br.com.roadway.RoadwayRouteBuilder;

@Component
public class ConnectCreateUsersRoute extends RoadwayRouteBuilder {

	@Value("${connect.url}")
    private String connectUrl;
	
	public static String DIRECT_CONNECT_CREATE_USER = "direct:"+ ConnectCreateUsersRoute.class.getName();
	
	@Override
	public void configure() throws Exception {
		from(DIRECT_CONNECT_CREATE_USER)
			.doTry()
			.routeId(ConnectCreateUsersRoute.class.getName()+"_ID")
			.removeHeader("*")
			.setHeader("Accept", simple("application/json"))
			.setHeader("Content-Type", constant("application/json"))
			.log("[CONNECT-CREATE-USER-ROUTE] ${exchangeProperty.createRequest}")
			.transform()
			.groovy("resource:classpath:br/com/roadway/groovy/connect/ConnectCreateUser.groovy")
			.marshal().json(JsonLibrary.Jackson)
			.log("token ${exchangeProperty.token}")
			.setHeader("Authorization", simple("${exchangeProperty.token}"))
			.setHeader(Exchange.HTTP_METHOD, constant(HttpMethod.POST))
			.log("[CONNECT-CREATE-USER-ROUTE] Criando user - ${body}")
			.to("http4:"+connectUrl+"/v1/users?bridgeEndpoint=true")
			.unmarshal().json(JsonLibrary.Jackson)
			.log("[SAAR-CREATE-PERSON] Response - ${body}")
			.setProperty("userCreateResponse", simple("${body}"))
			.end();
	}
	
}
