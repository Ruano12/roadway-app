package br.com.roadway.user.route;

import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

import br.com.roadway.RoadwayRouteBuilder;
import br.com.roadway.routes.creator.ConnectCreateUsersRoute;
import br.com.roadway.routes.creator.SaarCreatePersonRoute;
import br.com.roadway.routes.creator.SandalCreateAccountRoute;

@Component
public class UserCreatorRoute extends RoadwayRouteBuilder {

	public static String DIRECT_CREATE_USER = "direct:"+ UserCreatorRoute.class.getName();
	
	@Override
	public void configure() throws Exception {
		from(DIRECT_CREATE_USER)
		.doTry()
			.routeId(UserCreatorRoute.class.getName()+"_ID")
			.setProperty("createRequest").simple("${body}")
			.setProperty("token", header("Authorization"))
			.log("[USER-CREATOR-ROUTER] Criando Person no Saar.")
			.toD(SaarCreatePersonRoute.DIRECT_SAAR_CREATE_PERSON)
			.log("[USER-CREATOR-ROUTER] Criando User no connect.")
			.toD(ConnectCreateUsersRoute.DIRECT_CONNECT_CREATE_USER)
			.log("[USER-CREATOR-ROUTER] Criando conta no sandal.")
			.toD(SandalCreateAccountRoute.DIRECT_SANDAL_CREATE_ACCOUNT)
			.transform()
			.groovy("resource:classpath:br/com/roadway/groovy/connect/ConnectCreateUser.groovy")
			.marshal().json(JsonLibrary.Jackson)
			.end();
	}

}
