// the setup function runs once when you press reset or power the board
void setup() {
  // initialize digital pin 13 as an output.
  pinMode(13, OUTPUT);

  // initialize serial communication at 9600 bits per second:
  Serial.begin(9600);
}

const int FastBlinkDelay = 100; // ms

// the loop function runs over and over again forever
void loop() {

  Serial.println("ALLUMER");

  digitalWrite(13, HIGH);   // turn the LED on (HIGH is the voltage level)
  delay(2000);              // wait for a second

  Serial.println("ETEINDRE");

  digitalWrite(13, LOW);    // turn the LED off by making the voltage LOW
  delay(1000);              // wait for a second

  if (Serial.available() > 0) {


    int number = Serial.parseInt();
    if (Serial.read() == '\n') {

      Serial.print("CLIGNOTER ");
      Serial.print(number);
      Serial.println(" fois...");

      for (int i = 0; i < number; i++) {

        digitalWrite(13, HIGH);
        delay(FastBlinkDelay);

        digitalWrite(13, LOW);
        delay(FastBlinkDelay);
      }

    }

  }

}
