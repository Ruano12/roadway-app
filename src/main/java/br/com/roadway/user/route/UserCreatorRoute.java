package br.com.roadway.user.route;

import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.stereotype.Component;

import br.com.roadway.routes.SaarCreatePersonRoute;

@Component
public class UserCreatorRoute extends SpringRouteBuilder {

	public static String DIRECT_CREATE_USER = "direct:"+ UserCreatorRoute.class.getName();
	
	@Override
	public void configure() throws Exception {
		from(DIRECT_CREATE_USER)
		.doTry()
			.routeId(UserCreatorRoute.class.getName()+"_ID")
			.setProperty("createRequest").simple("${body}")
			.log(" ${exchangeProperty.createRequest}")
			.setProperty("token", header("Authorization"))
			.toD(SaarCreatePersonRoute.DIRECT_SAAR_CREATE_PERSON)
			.end();
	}

}
