#include <EEPROM.h>   //библиотека для работы со внутренней памятью ардуино

unsigned long lastturn, time_press; //переменные хранения времени
float SPEED; //переменная хранения скорости в виде десятичной дроби
float DIST; //переменная хранения расстояния в виде десятичной дроби

float w_length=2.0; //длина окружности колеса в метрах


void setup() {
 Serial.begin(9600); //открыть порт
  attachInterrupt(0,sens,RISING); //подключить прерывание на 2 пин при повышении сигнала
  DIST=(float)EEPROM.read(0)/10.0; //вспоминаем пройденное расстояние при запуске системы (деление на 10 нужно для сохранения десятых долей расстояния)
}

void sens() {
  if (millis()-lastturn > 80) {  //защита от случайных измерений (основано на том, что велосипед не будет ехать быстрее 120 кмч)
    SPEED=w_length/((float)(millis()-lastturn)/1000)*3.6;  //расчет скорости, км/ч
    lastturn=millis();  //запомнить время последнего оборота
    DIST=DIST+w_length/1000;  //прибавляем длину колеса к дистанции при каждом обороте
  }
}

void loop() {
  int cel_sp=floor(SPEED);
  int sot_sp=(((float)cel_sp/1000)-floor((float)cel_sp/1000))*10;
  int des_sp=(((float)cel_sp/100)-floor((float)cel_sp/100))*10;
  int ed_sp=(((float)cel_sp/10)-floor((float)cel_sp/10))*10;
  int dr_sp=(float)(SPEED-floor(SPEED))*10;
  

  int cel_di=floor(DIST);  //целые
  int sot_di=(((float)cel_di/1000)-floor((float)cel_di/1000))*10;  //сотни
  int des_di=(((float)cel_di/100)-floor((float)cel_di/100))*10;  //десятки
  int ed_di=floor(((float)((float)cel_di/10)-floor((float)cel_di/10))*10);  //единицы 
  int dr_di=(float)(DIST-floor(DIST))*10;  //десятые части
  
  Serial.print("start,V:"+String()+",D:"+String()+",end" )

  if ((millis()-lastturn)>4000){ //если сигнала нет больше 4 секунды
    SPEED=0;  //считаем что SPEED 0
    EEPROM.write(0,(float)DIST*10.0); //записываем DIST во внутреннюю память c сохранением десятых
  }  
}
