#include  <SPI.h>
#include  <RF24.h>
#include <LowPower.h>
#include "packet.h"
#include <TimeLib.h>
#include <RTClib.h>

#define TIME_HEADER 'T'
#define TIME_MSG_LEN 11

RF24 radio(9,10); 
uint8_t address[6] = {0x3C,0xCE,0xCC,0xCE,0xCC};   // Adresse du pipe
payload_t rcv_val;
RTC_Millis rtc;
void setup() {
  Serial.begin(115200);    // Initialiser la communication série 
  Serial.println (F("Starting my first test")) ;
  rtc.begin(DateTime(F(__DATE__), F(__TIME__)));
  radio.begin();
  radio.setChannel(0x3c);
  radio.setDataRate(RF24_2MBPS);
  radio.enableDynamicPayloads();

  radio.openReadingPipe(0,address); // Ouvrir le Pipe en lecture
  radio.startListening();
}

void processSyncMessage()
{
  while (Serial.available())
  {
    char c = Serial.read();
    if(c == TIME_HEADER)
    {
      time_t pctime = 0;
      for (int i = 0; i < TIME_MSG_LEN; i++)
      {
        c = Serial.read();
        if (isDigit(c))
        {
            pctime = (pctime * 10) + (c - '0'); //convertit chiffres en nombres
        }
      }
      setTime(pctime);
    }
  }
}

void loop(void) {

    if (Serial.available())
    {
      processSyncMessage();
    }
    DateTime dt = rtc.now();
    time_t ti = dt.unixtime();

    /*if (hour(ti) < 10 || hour(ti) > 10)
    {
      LowPower.powerDown(SLEEP_8S, ADC_OFF, BOD_OFF);
      return;
    }*/

    
    //time_t ti = now().unixtime();
    while (radio.available()) 
    {
      radio.read(&rcv_val, sizeof(payload_t));
      Serial.println(String(rcv_val.id_capteur)+","+String(rcv_val.valeur)+","+String(ti));
    }
}



