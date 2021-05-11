# PagoPA
This is an API client that can be used to send a “message” to a citizen using the “IO” REST API.

I use spring boot to realize this project. The application expose one REST operation and a simple web client in HTML.
## Run
To start the application use this command: `mvn spring-boot:run`

## API
You can find the interface [here](https://app.swaggerhub.com/apis/jacopo1395/io-client_api/1.0.0) or see `/api-docs.json` in the root of project.

### Submit a message
Send a message to a user

> **POST** localhost:8080/v1/submitMessageForUser

JSON body:
``` json
{
  "message": {
    "subject": "Welcome new user !",
    "markdown": "# This is a markdown header\\n\\nto show how easily markdown can be converted to **HTML**\\n\\nRemember: this has to be a long text."
  },
  "fiscalCode": "AAAAAA00A00A000A"
}
```

- **fiscalCode** identify the user 
- **message.subject** is the subject of the message
- **message.markdown** is the text of the message in markdown

_Note: AAAAAA00A00A000A is a fiscal code for testing_


## WebClient
Open in your browser `localhost:8080` for the web client. Just insert a fiscal code and a message and push "Send".
The web client is just for testing the Rest API.

_Disable CORS if you work in local_
