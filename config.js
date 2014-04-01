
{
	"port" : 1234,
	"hosts" : [{
		"name" : "mon-site.com",
		"document_root" : "C:/var/www/site1.com",
		"handlers" : [ {
			"clazz" : "org.esgi.http.handlers.StaticHandler",
			"pattern" : "^.*$"
		}
		]
	},{
		"name" : "un-site.org",
		"document_root" : "C:/var/www/site2.com",
		"handlers" : [ {
			"clazz" : "org.esgi.http.handlers.StaticHandler",
			"pattern" : "^.*$"
		}]
	}]
}