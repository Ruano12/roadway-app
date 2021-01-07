import groovy.json.JsonBuilder

def user = exchange.properties['createRequest']
def person = exchange.properties['personCreateResponse']

def jsonBuilder = new JsonBuilder()

out.println("body - " + user)

jsonBuilder {
	"account-number" user['account-number']
	"person-id" person['person-id']
}

return jsonBuilder.content
