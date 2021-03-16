#include <SoftwareSerial.h>
#include <Servo.h>


SoftwareSerial MyBlue(2, 3); // RX | TX


int fead = 0;
int pos = 0;
String v ="";

Servo servo_9;




void setup() {
  
  MyBlue.begin(9600);
  servo_9.attach(9, 500, 2500);
  MyBlue.write("Dogs are hungry");
 

}

void loop() {
  
  switch(fead){
    
    case 0: 
    if(MyBlue.available()>0){
      v = MyBlue.readString();
     
      if(v.equals("60")){
        fead = 1;
      }
      if(v.equals("90")){
        fead = 2;
      }
      if(v.equals("180")){
        fead = 3;
      }
    }
    break;

     case 1:
    MyBlue.write("Start feeding");
   
    for (pos = 0; pos <= 60; pos += 1) {
      servo_9.write(pos);
      delay(15); 
    }
    for (pos = 60; pos >= 0; pos -= 1) {
      servo_9.write(pos);
      delay(15);  
    }
    fead = 4;
    break;

     case 2:
    MyBlue.write("Start feeding");
   
    for (pos = 0; pos <= 90; pos += 1) {
      servo_9.write(pos);
      delay(15); 
    }
    for (pos = 90; pos >= 0; pos -= 1) {
      servo_9.write(pos);
      delay(15);  
    }
    fead = 4;
    break;
    
    case 3:
    MyBlue.write("Start feeding");
  
    for (pos = 0; pos <= 180; pos += 1) {
      servo_9.write(pos);
      delay(15); 
    }
    for (pos = 180; pos >= 0; pos -= 1) {
      servo_9.write(pos);
      delay(15);  
    }
    fead = 4;
    break;
    
    case 4:
    MyBlue.write("Stop feeding");
    delay(10000);
    MyBlue.write("Dogs are not hungry");
   delay(15000);
   MyBlue.write("Dogs are hungry");
   fead = 0;
    break;
    
   }
  

}
