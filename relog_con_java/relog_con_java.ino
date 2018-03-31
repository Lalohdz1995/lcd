#include <LiquidCrystal.h>
LiquidCrystal lcd(12,11,5,4,3,2);

byte PIN_SENSOR = A0;
int dato_serial = 0;
float C;

int temp;

void setup() {
  lcd.begin(16,2);
  Serial.begin(9600);
  Serial.setTimeout(50);
  pinMode(8, OUTPUT);
  pinMode(9, OUTPUT);
  pinMode(10, OUTPUT);
  
  
}

void loop() {
  C = (5.0 * analogRead(PIN_SENSOR) * 100.0)/ 1024;
  temp = C;
  if ( Serial.available() > 0){
   //lectura_dato();
   dato_serial = Serial.read();
    comparacion_dato(); 

    if(C < 15)
    {
    digitalWrite(8,HIGH); 
    digitalWrite(9,LOW);
    digitalWrite(10,LOW);
    }
    if(C > 20)
    {
    digitalWrite(8,LOW); 
    digitalWrite(9,LOW);
    digitalWrite(10,HIGH);
    }
    if(C < 20 )
    {
      if(C > 15)
      {
        digitalWrite(8,LOW); 
        digitalWrite(9,HIGH);
        digitalWrite(10,LOW);
      }
    }
  
  }
  
  String text = Serial.readString();
  String line1 = text.substring(0, 16);
  String line2 = text.substring(16, 32);
  
if(text.length() > 0){
  lcd.setCursor(0,0);
  lcd.print("                ");
  lcd.setCursor(0,1);
  lcd.print("                ");
  }

  lcd.setCursor(0,0);
  lcd.print(line1);
  lcd.setCursor(0,1);
  lcd.print(line2);
  
}

void lectura_dato (void ){
 
 dato_serial = Serial.read();
 
}

void comparacion_dato (void){
  if(dato_serial == 'T'){ 
    Serial.write(temp); 
    
  }
}
