#include <iostream>
using namespace std;
void xd(){
std::cout << "hola";
}
void other(){
std::cout << "hola";
}
int main(){
int counter = 0;
while (counter < 10){
std::cout << counter;
counter = counter + 1;
}
for (int i = 0; i < 3; i+=1){
xd();
}
return 0;
}

