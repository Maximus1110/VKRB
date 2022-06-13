#include <Wire.h>
#include <EEPROM.h>  

rgb_lcd lcd;

//Пины
const int hallPin = 2;     // датчик Холла
const int ledPin =  13;      // светодиод

//Доплнительные переменные 
unsigned long debounce=0;
unsigned long timeTurn=0;
volatile bool flag = true;
float sp;
float dist;
int diametr=600;
float len=0;

void setup() {
  
    // Настройка пинов.
    pinMode(ledPin, OUTPUT);
    pinMode(hallPin, INPUT);
    
    //UART порт 
    Serial.begin(9600); // инициализация порта
    Serial.println("Conected..."); 

    //Прерывание
    attachInterrupt(0, isr, RISING);
    dist=(float)EEPROM.read(0)/10.0;

    len=(float)diametr*3.14/1000.0
}

void isr() { 
  

  
  if ( (millis() - debounce) >= 100 ) { // защита от дребизга
    timeTurn=millis() - debounce;
    flag=false;
    sp=len/((float)(millis() - debounce)/1000)*3.6; 
    debounce = millis();
    dist=dist+len/1000;
  }
  
}


void loop() {
  int int_sp=floor(sp*10);
  int int_dist=floor(dist*10);

  if ((millis()-debounce)>4000 && !flag){ 
    flag=true;
    sp=0;  
    EEPROM.write(0,(float)dist*10.0); 
  } 
  Serial.print("start,V:"+String(int_sp/10)+"."+String(int_sp%10)+",D:"+String(int_dist/10)+"."+String(int_dist%10)+",end");
}
