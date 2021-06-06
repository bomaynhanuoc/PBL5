#include <SoftwareSerial.h>
SoftwareSerial Arduino(5, 6);

const int relayPin = 7;
String data = "";
String temp = "";
char response;

void setup() {
  Serial.begin(9600);
  Arduino.begin(115200);
  pinMode(relayPin, OUTPUT);
}

void loop() {
  Arduino.write("g");
  while (Arduino.available()){
    response = Arduino.read();
    temp = String(response);
    data.concat(temp);
  }
  Serial.println(data);
  if (data == "op") {
    digitalWrite(relayPin, HIGH);
    delay(5000);
    digitalWrite(relayPin, LOW);
  }

  data = "";
  delay(2000);
}