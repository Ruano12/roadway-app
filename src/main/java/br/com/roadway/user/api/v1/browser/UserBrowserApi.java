package br.com.roadway.user.api.v1.browser;

import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

import br.com.roadway.RoadwayRouteBuilder;
import br.com.roadway.user.route.UserBrowserRoute;

@Component
public class UserBrowserApi extends RoadwayRouteBuilder {
	
	@Override
	public void configure() throws Exception {
		rest()
			.id(this.getClass().getName()+"_ID")
			.get("v1/user")
			.route()
			.toD(UserBrowserRoute.DIRECT_GET_PERSON)
			.marshal().json(JsonLibrary.Jackson)
			.end();
			
		
	}
	
}
