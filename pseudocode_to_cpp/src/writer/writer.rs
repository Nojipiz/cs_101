use std::fs::{File};
use std::io::prelude::*;

pub fn write_file(document: Vec<String>){
    let mut output_cpp_file = File::create("resources/output.cpp").unwrap();
    write!(output_cpp_file,"{}", get_standart_elements());
    for line in document{
        writeln!(output_cpp_file, "{}", line);
    }
}

fn exclude_functions(){

}

fn get_standart_elements()-> String{
    String::from(
        "#include <iostream>\nusing namespace std;\n")
}
