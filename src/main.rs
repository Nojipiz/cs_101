//use debug_tree::*;
mod read_elements;
use read_elements::LanguageElements;

fn main() {
    let language_element: LanguageElements = read_elements::get_language_elements();
    print!("{:?}", language_element);
}

// fn print_tree() {
//     defer_write!("output.txt");
//     add_branch!("A Fibonacci Tree");
//     add_leaf!("{}", 3); // <~ THE MAGIC LINE
// }
