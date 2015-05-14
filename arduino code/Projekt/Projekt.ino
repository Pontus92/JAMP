

String voice;

int
led1 = 2, //Connect LED 1 To Pin #2
led2 = 3, //Connect LED 2 To Pin #3
led3 = 4, //Connect LED 3 To Pin #4
led4 = 5, //Connect LED 4 To Pin #5
led5 = 6; //Connect LED 5 To Pin #6

main() {
  setup();
  while (1) {
    loop();
  }
}
//--------------------------Call A Function-------------------------------//
void allon() {
  digitalWrite(led1, HIGH);
  digitalWrite(led2, HIGH);
  digitalWrite(led3, HIGH);
  digitalWrite(led4, HIGH);
  digitalWrite(led5, HIGH);
}
void alloff() {
  digitalWrite(led1, LOW);
  digitalWrite(led2, LOW);
  digitalWrite(led3, LOW);
  digitalWrite(led4, LOW);
  digitalWrite(led5, LOW);
}
//-----------------------------------------------------------------------//
void setup() {
  Serial.begin(9600);
  pinMode(led1, OUTPUT);
  pinMode(led2, OUTPUT);
  pinMode(led3, OUTPUT);
  pinMode(led4, OUTPUT);
  pinMode(led5, OUTPUT);
}
//-----------------------------------------------------------------------//
void loop() {
  while (Serial.available()) { //Check if there is an available byte to read
    delay(10); //Delay added to make thing stable
    char c = Serial.read(); //Conduct a serial read
    if (c == '#') {
      break; //Exit the loop when the # is detected after the word
    }
    voice += c; //Shorthand for voice = voice + c
  }
  if (voice.length() > 0) {
    Serial.println(voice);
    //-----------------------------------------------------------------------//
    //----------Control Multiple Pins/ LEDs----------//
    if (voice == "*lights on") {
      allon();
    }  //Turn Off All Pins (Call Function)
    else if (voice == "*lights off") {
      alloff(); //Turn On  All Pins (Call Function)
    }

    //----------Turn On One-By-One----------//
    else if (voice == "*head") {
      if (digitalRead(led1) == LOW) {
        digitalWrite(led1, HIGH);
      }
      else if (digitalRead(led1) == HIGH) {
        digitalWrite(led1, LOW);
      }
    }
    else if (voice == "*right arm") {
      if (digitalRead(led2) == LOW) {
        digitalWrite(led2, HIGH);
      }
      else if (digitalRead(led2) == HIGH) {
        digitalWrite(led2, LOW);
      }
    }
    else if (voice == "*right leg") {
      if (digitalRead(led3) == LOW) {
        digitalWrite(led3, HIGH);
      }
      else if (digitalRead(led3) == HIGH) {
        digitalWrite(led3, LOW);
      }
    }
    else if (voice == "*left leg") {
      if (digitalRead(led4) == LOW) {
        digitalWrite(led4, HIGH);
      }
      else if (digitalRead(led4) == HIGH) {
        digitalWrite(led4, LOW);
      }
    }
    else if (voice == "*left arm") {
      if (digitalRead(led5) == LOW) {
        digitalWrite(led5, HIGH);
      }
      else if (digitalRead(led5) == HIGH) {
        digitalWrite(led5, LOW);
      }
    }
    //-----------------------------------------------------------------------//
    voice = "";
  }
} //Reset the variable after initiating

