use std::fs::File;
use std::io::prelude::*;

pub fn write_file(document: Vec<String>) {
    let mut output_cpp_file = File::create("resources/output.cpp").unwrap();
    let _res = write!(output_cpp_file, "{}", get_standart_elements());
    let first_function_index = get_first_function_index(&document);
    for (index, element) in (&document).into_iter().enumerate() {
        if index >= first_function_index{
            let _res = writeln!(output_cpp_file, "{}", element);
        }
    }
    let _res = writeln!(output_cpp_file, "{}", get_main_function_init());
    for (index, element) in (&document).into_iter().enumerate(){
        if index < first_function_index{
            let _res = writeln!(output_cpp_file, "{}", element);
        }
    }
    let _res = writeln!(output_cpp_file, "{}", get_last_elements());

}

fn get_first_function_index(document: &Vec<String>) -> usize{
    document
        .into_iter()
        .position(|line| line.contains("void ")).unwrap_or(document.len()-1)
}

fn get_standart_elements() -> String {
    String::from("#include <iostream>\nusing namespace std;\n")
}

fn get_main_function_init() -> String{
    String::from("int main(){")
}

fn get_last_elements() -> String {
    String::from("return 0;\n}\n")
}
