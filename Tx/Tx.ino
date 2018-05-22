#include  <SPI.h>
#include  <RF24.h>
#include "packet.h"
#include <Ultrasonic.h>
#include <LowPower.h>
#include <TimeLib.h>

Ultrasonic ultrasonic(A0);

RF24 radio(9, 10);
uint8_t address[6] = {0x3C, 0xCE, 0xCC, 0xCE, 0xCC}; // Adresse du pipe
payload_t val;  // Valeur à envoyer

const int idCapteur = 3;
const int idGroupe = 1;

//https://playground.arduino.cc/Code/DateTime

void setup() {
  Serial.begin(115200);    // Initialiser la communication série
  Serial.println (F("Starting my first test")) ;

  radio.begin();
  radio.setChannel(0x3c);
  radio.setDataRate(RF24_2MBPS);
  radio.enableDynamicPayloads();
  radio.openWritingPipe(address);    // Ouvrir le Pipe en écriture
  radio.stopListening();
  radio.setRetries(15,15);
}

void loop(void) {

  time_t t = now();
  /*if (hour(t) < 11 || hour(t) > 14)
    {
    LowPower.powerDown(SLEEP_8S, ADC_OFF, BOD_OFF);
    return;
    }*/

  val.id_capteur = idCapteur;
  val.valeur = (double)ultrasonic.MeasureInCentimeters();
  Serial.print(F("\n Now sending Packet "));

  if (radio.write(&val, sizeof(val)))
  { Serial.print(F(" ... Ok ... "));
  }
  else
  { Serial.print(F(" ... failed ... "));
  }
  delay(3000);
}

