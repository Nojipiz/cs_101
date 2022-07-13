use std::fs::{OpenOptions, File};
use std::io::prelude::*;

pub fn write_file(document: Vec<String>){
    let mut output_cpp_file = File::create("resources/output.cpp").unwrap();
    for line in document{
        writeln!(output_cpp_file, "{}", line);
    }
}
