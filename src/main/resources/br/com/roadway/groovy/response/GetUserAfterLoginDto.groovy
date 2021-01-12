import groovy.json.JsonBuilder

def person = exchange.properties['personByIdResponse']
def account = exchange.properties['accountByPersonResponse']

def jsonBuilder = new JsonBuilder()

out.println("body " + person)

jsonBuilder { 
	"id" person["id"]
	"name" person["name"]
	"document" person["document"]
	"person-type" person["person-type"]
	"accounts" account
}

return jsonBuilder.content