#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>
#include <WiFiClient.h>
#include <Arduino_JSON.h>
#include <SoftwareSerial.h>

SoftwareSerial NodeMCU(D6, D5);

const char* ssid = "Your wifi name";
const char* password = "Your wifi password";

//Your server IP address
const char* serverName = "http://192.168.1.2:8000/users/open-door";

unsigned long lastTime = 0;
unsigned long timerDelay = 2000;

String response;
int data = 0;

void setup() {
  Serial.begin(115200);
  NodeMCU.begin(115200);

  WiFi.begin(ssid, password);
  Serial.println("Connecting");
  while(WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("");
  Serial.print("Connected to WiFi network with IP Address: ");
  Serial.println(WiFi.localIP());
 
  Serial.println("Timer set to 5 seconds (timerDelay variable), it will take 5 seconds before publishing the first reading.");
}

void loop() {
  //Send an HTTP POST request every 10 minutes
  if ((millis() - lastTime) > timerDelay) {
    //Check WiFi connection status
    if(WiFi.status()== WL_CONNECTED){
//      String serverPath = serverName + "open-door/";
      
      response = httpGETRequest(serverName);
//      Serial.println(response);
      JSONVar myObject = JSON.parse(response);
  
      // JSON.typeof(jsonVar) can be used to get the type of the var
      if (JSON.typeof(myObject) == "undefined") {
        Serial.println("Parsing input failed!");
        return;
      }
      JSONVar key = myObject.keys();
      data = myObject[key[0]];

      if (data != 0) {
        Serial.println(data);
      }

      if (NodeMCU.available() > 0) {
        char req = NodeMCU.read();
        if (req == 'g' && data != 0) {
          Serial.println(data);
          NodeMCU.print("op");
        }
      }

      data = 0;
    }
    else {
      Serial.println("WiFi Disconnected");
    }
    lastTime = millis();
  }
}

String httpGETRequest(const char* serverName) {
  HTTPClient http;
     
  http.begin(serverName);
  int httpResponseCode = http.GET();
  
  String payload = "{}"; 
  
  if (httpResponseCode > 0) {
    Serial.print("HTTP Response code: ");
    Serial.println(httpResponseCode);
    payload = http.getString();
  }
  else {
    Serial.print("Error code: ");
    Serial.println(httpResponseCode);
  }
  http.end();

  return payload;
}