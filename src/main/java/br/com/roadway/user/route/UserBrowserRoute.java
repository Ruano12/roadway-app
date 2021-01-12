package br.com.roadway.user.route;

import org.springframework.stereotype.Component;

import br.com.roadway.RoadwayRouteBuilder;
import br.com.roadway.routes.browser.ConnectGetPersonIdRoute;
import br.com.roadway.routes.browser.SaarGetPersonByIdRoute;
import br.com.roadway.routes.browser.SandalGetAccountsByPerson;

@Component
public class UserBrowserRoute extends RoadwayRouteBuilder {

	public final static String DIRECT_GET_PERSON = "direct:"+ UserBrowserRoute.class.getName();
	
	@Override
	public void configure() throws Exception {
		
		from(DIRECT_GET_PERSON)
		.doTry()
			.routeId(UserBrowserRoute.class.getName()+"_ID")
			.setProperty("token", header("Authorization"))
			.log("[USER-BROWSER-ROUTE] Buscando person-id do usuario logado no connect")
			.toD(ConnectGetPersonIdRoute.DIRECT_CONNECT_GET_PERSON_ID)
			.log("[USER-BROWSER-ROUTE] Buscando person logado no saar")
			.toD(SaarGetPersonByIdRoute.DIRECT_SAAR_GET_PERSON_BY_ID)
			.log("[USER-BROWSER-ROUTE] Buscando contas do person logado.")
			.toD(SandalGetAccountsByPerson.DIRECT_SANDAL_GET_ACCOUNT_PERSON)
			.log("[USER-BROWSER-ROUTE] Montando response.")
			.transform()
			.groovy("resource:classpath:br/com/roadway/groovy/response/GetUserAfterLoginDto.groovy")
			.end();
		
	}

}
