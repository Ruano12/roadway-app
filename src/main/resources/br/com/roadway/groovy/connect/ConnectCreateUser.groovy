import groovy.json.JsonBuilder

def user = exchange.properties['createRequest']
def person = exchange.properties['personCreateResponse']

def jsonBuilder = new JsonBuilder()

out.println("body - " + user)

jsonBuilder {
    "username" user['username']
    "password" user['password']
    "first-name" user['first-name']
    "last-name" user['last-name']
    "email" user['email']
    "person-id" person['person-id']
}

return jsonBuilder.content
