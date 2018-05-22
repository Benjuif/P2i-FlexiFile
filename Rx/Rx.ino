#include  <SPI.h>
#include  <RF24.h>
#include <LowPower.h>
#include "packet.h"
#include <TimeLib.h>
#include <RTClib.h>

#define TIME_HEADER 'T'
#define TIME_END '\n'

int  serIn;             // var that will hold the bytes-in read from the serialBuffer
char serInString[100];  // array that will hold the different bytes  100=100characters;
                        // -> you must state how long the array will be else it won't work.
int  serInIndx  = 0;    // index of serInString[] in which to insert the next incoming byte
int  serOutIndx = 0;    // index of the outgoing serInString[] array;
int id_groupe = 3;
RF24 radio(9,10); 
uint8_t address[6] = {0x3C,0xCE,0xCC,0xCE,0xCC};   // Adresse du pipe
payload_t rcv_val;
RTC_Millis rtc;
void setup() {
  Serial.begin(115200);    // Initialiser la communication série 
  Serial.println (F("Starting my first test")) ;
  rtc.begin(DateTime(F(__DATE__), F(__TIME__)));
  Serial.println(rtc.now().unixtime());
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
      long unsigned int pctime = 0;
      while (c != TIME_END)
      {
        //Serial.println("reading char"); 
        c = Serial.read();
        Serial.println(c);
        if (isDigit(c))
        {
            pctime = (pctime * 10) + (c - '0'); //convertit chiffres en nombres
        }
      }
      Serial.println(pctime);
      DateTime date = DateTime(pctime);
      Serial.println(date.unixtime());
      //setTime(pctime);
      rtc.adjust(date);
    }
    Serial.read();
  }
  Serial.println("exiting");
}


void loop(void) {
    
    if (Serial.available())
    {
      processSyncMessage();
    }
    time_t t = now();

   /* if (hour(t) < 11 || hour(t) > 14)
    {
      LowPower.powerDown(SLEEP_8S, ADC_OFF, BOD_OFF);
      return;
    }*/

    
    unsigned long wait = micros();
    boolean timeout = false;
    DateTime dt = rtc.now();
    time_t ti = dt.unixtime();
    
    while (radio.available()) 
    {
      //Serial.println(String(ti));
      radio.read(&rcv_val, sizeof(payload_t));
      //Serial.print(F("received value : ")); 
      Serial.println(String(rcv_val.id_capteur)+","+String(rcv_val.valeur)+","+String(ti)+","+String(id_groupe));
    }
}


