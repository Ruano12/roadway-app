package br.com.roadway.user.route;

import org.springframework.stereotype.Component;

import br.com.roadway.RoadwayRouteBuilder;
import br.com.roadway.routes.ConnectCreateUsersRoute;
import br.com.roadway.routes.SaarCreatePersonRoute;
import br.com.roadway.routes.SandalCreateAccountRoute;

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
			.end();
	}

}
