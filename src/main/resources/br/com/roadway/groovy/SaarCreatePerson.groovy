import groovy.json.JsonBuilder

def person = exchange.properties['createRequest']

def jsonBuilder = new JsonBuilder()

out.println("body " + person)

jsonBuilder { 
	"name" person['first-name'] + " " + person['last-name']
	"document" person['document']
	"person-type" person['person-type']
}

return jsonBuilder.content
