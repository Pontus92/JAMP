
void setup();
void loop();
void allon();
void alloff();

String voice;

char inChar;
char inData[20];
byte index = 0;
int
txPin = 0,
rxPin = 1,
led1 = 2, //Connect LED 1 To Pin #2
led2 = 3, //Connect LED 2 To Pin #3
led3 = 4, //Connect LED 3 To Pin #4
led4 = 5, //Connect LED 4 To Pin #5
led5 = 6; //Connect LED 5 To Pin #6

/*
main() {
  setup();
  loop(); 
}
*/

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
  pinMode(txPin, OUTPUT);
  pinMode(rxPin, INPUT);
  pinMode(led1, OUTPUT);
  pinMode(led2, OUTPUT);
  pinMode(led3, OUTPUT);
  pinMode(led4, OUTPUT);
  pinMode(led5, OUTPUT);
}
//-----------------------------------------------------------------------//
void loop() {
  //char charBuffer[1024];
  //int maxLenght = 1024; 
  /*
   if(Serial.available()) { //Check if there is an available byte to read
    while(1){ 
      delay(10); //Delay added to make thing stable
      //char c = Serial.read(); //Conduct a serial read
      
      if (c == '#') {
        break; //Exit the loop when the # is detected after the word
    }
    voice += c; //Shorthand for voice = voice + c
    }
    
  }  
*/  
  while(Serial.available() > 0) // Don't read unless
                                                 // there you know there is data
  {
      if(index < 19) // One less than the size of the array
      {
          inChar = Serial.read(); // Read a character
          inData[index] = inChar; // Store it
          index++; // Increment where to write next
          inData[index] = '\0'; // Null terminate the string
      }
  }
  String command = String(inData);
  if(command.equals("lightson#")){
     allon();
  }
  // Now do something with the string (but not using ==)

  //-----------------------------------------------------------------------//
    //----------Control Multiple Pins/ LEDs----------//
    /*
    if (voice == "lightson") {
      allon();
    }  //Turn On All Pins (Call Function)
    */
    else if (voice == "lightsoff") {
      alloff(); //Turn Off  All Pins (Call Function)
    }

    //----------Turn On One-By-One----------//
    else if (voice == "head") {
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
    voice = ""; //Reset the variable after initiating
} 
