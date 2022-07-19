# :memo: Pseudocode to CPP
This project converts the input of the main function to C++ executable code, the output file is called "output.cpp" and will be generated in the resources folder.
```
    cargo run path/to/file
```
Here is an example of the pseudocode that can be transpiled into the compiler:
```
counter <- 2
for counter until 20 step 1
    counter <- counter + 1
end

anothercounter <- 0
while counter less 10
    print "hello world!"
end
```

And the output for this code will be:
```cpp
#include <iostream>
using namespace std;
int main(){
    int counter = 2;
    for (int i = counter; i < 20; i+=1){
        counter = counter + 1;
    }
    int anotherCounter = 0;
    while (counter < 10){
        std::cout << "HelloWorld!";
    }
    return 0;
}
```

You also can create your own functions, BUT, the functions must be placed **at the end of the pseudocode**, :smiley: here is an example with the last code plus a simple function that prints "This is my function"
```
counter <- 2
for counter until 20 step 1
    counter <- counter + 1
end

anothercounter <- 0
while counter less 10
    print "hello world!"
end
fun myFunction
    print "This is my function"
end
```

And the output will be this:
```cpp
#include <iostream>
using namespace std;
void myFunction(){
    std::cout << "Thisismyfunction";
}
int main(){
    int counter = 2;
    for (int i = counter; i < 20; i+=1){
        counter = counter + 1;
    }
    int anotherCounter = 0;
    while (counter < 10){
        std::cout << "HelloWorld!";
    }
    return 0;
}
```
This was a homework for a college class :notebook_with_decorative_cover:, you can grab this and made whatever you want!
