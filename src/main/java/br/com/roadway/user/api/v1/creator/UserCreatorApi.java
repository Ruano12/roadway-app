package br.com.roadway.user.api.v1.creator;

import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

import br.com.roadway.RoadwayRouteBuilder;
import br.com.roadway.user.route.UserCreatorRoute;

@Component
public class UserCreatorApi extends RoadwayRouteBuilder {

	@Override
	public void configure() throws Exception {
		rest()
			.id(this.getClass().getName()+"_ID")
			.post("v1/user")
			.route()
			.unmarshal().json(JsonLibrary.Jackson)
			.log("teste")
			.toD(UserCreatorRoute.DIRECT_CREATE_USER)
			.setBody(simple("{\"Status\": \"Sucess\"}"))
			.end();
			
		
	}

}
