
int command;


int
led1 = 2, //Connect LED 1 To Pin #2 Head
led2 = 3, //Connect LED 2 To Pin #3 Right Arm 
led3 = 4, //Connect LED 3 To Pin #4 Right Leg
led4 = 5, //Connect LED 4 To Pin #5 Left Leg
led5 = 6; //Connect LED 5 To Pin #6 Left Arm


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
  digitalWrite(led1, LOW);
  digitalWrite(led2, LOW);
  digitalWrite(led3, LOW);
  digitalWrite(led4, LOW);
  digitalWrite(led5, LOW);
}
//-----------------------------------------------------------------------//
void loop() {
  Serial.flush();
  if(Serial.available()) { //Check if there is an available byte to read
    while(1){ 
      delay(10); //Delay added to make thing stable
      command = Serial.read(); //Conduct a serial read
      break;
      }
    
  }  
  if(command == '1'){
    allon();
  }
  else if(command == '2'){
    alloff();  
  }
  else if(command == '3'){
    if (digitalRead(led1) == LOW) {
        digitalWrite(led1, HIGH);
      }
      else{
        digitalWrite(led1, LOW);
      }  
  }
  else if(command == '4'){
    if (digitalRead(led2) == LOW) {
        digitalWrite(led2, HIGH);
      }
      else{
        digitalWrite(led2, LOW);
      }  
  }
  else if(command == '5'){
    if (digitalRead(led3) == LOW) {
        digitalWrite(led3, HIGH);
      }
      else{
        digitalWrite(led3, LOW);
      }  
  }
  else if(command == '6'){
    if (digitalRead(led4) == LOW) {
        digitalWrite(led4, HIGH);
      }
      else{
        digitalWrite(led4, LOW);
      }  
  }
  else if(command == '7'){
    if (digitalRead(led5) == LOW) {
        digitalWrite(led5, HIGH);
      }
      else{
        digitalWrite(led5, LOW);
      }  
  }
    command = 0; //Reset the variable after initiating
} 

