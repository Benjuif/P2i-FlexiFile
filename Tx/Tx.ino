 #include  <SPI.h>
#include  <RF24.h>
#include "packet.h"
#include <Ultrasonic.h>
#include <LowPower.h>
#include <TimeLib.h>
#include <RTClib.h>

#define TIME_HEADER 'T'
#define TIME_MSG_LEN 11

Ultrasonic ultrasonic(A0);

RF24 radio(9,10);
uint8_t address[6] = {0x3C,0xCE,0xCC,0xCE,0xCC};   // Adresse du pipe
payload_t val;  // Valeur à envoyer 

const int idCapteur= 2;
RTC_Millis rtc;
double temp_max = 0;
time_t temp = 0;

//https://playground.arduino.cc/Code/DateTime

void setup() {
  Serial.begin(115200);    // Initialiser la communication série 
  Serial.println (F("Starting my first test")) ;
  rtc.begin(DateTime(F(__DATE__), F(__TIME__)));
  temp = rtc.now().unixtime();
  radio.begin();
  radio.setChannel(0x3c);
  radio.setDataRate(RF24_2MBPS);
  radio.enableDynamicPayloads();
  radio.openWritingPipe(address);    // Ouvrir le Pipe en écriture
  radio.stopListening();   
  radio.setRetries(15, 15);          
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
    time_t t = dt.unixtime();

    /*if (hour(t) < 10 || hour(t) > 11)
    {
      LowPower.powerDown(SLEEP_8S, ADC_OFF, BOD_OFF);
      return;
    }*/
    int delta = abs(t - temp);
    //Serial.println(delta);
    if (delta < 10)
    {
      //Serial.println("mesure");
      double mesure = (double)ultrasonic.MeasureInCentimeters();
      if (temp_max < mesure)
        temp_max = mesure; 
      delay(1000);
    }
    else if (delta < 30)
    {
      if (temp_max > 0)
      {
        val.id_capteur = idCapteur;
        val.valeur = temp_max;
        Serial.println("Now sending Packet ");
        
        if (radio.write(&val,sizeof(val))) 
        { 
          Serial.println(" ... Ok ... ");     
        }
        else
        { 
          Serial.println(" ... failed ... "); 
        }
        temp_max = 0;
        
      }
      else
        LowPower.powerDown(SLEEP_1S, ADC_OFF, BOD_OFF);
      delay(1000);
    }
    else
    {
      //Serial.println("Resetting temp");
      temp = rtc.now().unixtime();
    }
  }

