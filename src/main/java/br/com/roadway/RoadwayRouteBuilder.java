package br.com.roadway;

import org.apache.camel.Exchange;
import org.apache.camel.spring.SpringRouteBuilder;

public abstract class RoadwayRouteBuilder extends SpringRouteBuilder {
	public RoadwayRouteBuilder() {
		this(true);
	}

	public RoadwayRouteBuilder(boolean handleError) {
	    if (handleError) {
	      onException(Exception.class)
	          .routeId(this.getClass().getName())
	          .handled(true)
	          .setBody(simple("${exchangeProperty.CamelExceptionCaught.responseBody}"))
	          .setHeader(Exchange.HTTP_RESPONSE_CODE, simple("${exchangeProperty.CamelExceptionCaught.statusCode}"))
	          .log("${body}- ${header.CamelHttpResponseCode}")
	          .end();
	    }
	}
}
