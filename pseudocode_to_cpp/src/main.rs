use std::env;

use lexical::lexical::{plain_text_to_tokenized_code, Word};
use reader::reader::get_plain_pseudocode;
use syntactic::syntactic::check_syntaxis;
use writer::writer::write_file;

mod reader;
mod lexical;
mod syntactic;
mod writer;

fn main() {
    let args: Vec<String> = env::args().collect();
    let pseudocode: Vec<String> = get_plain_pseudocode(args[1].clone());
    let boxed_pseudocode: Box<Vec<String>> = Box::new(pseudocode);
    let tokenized_code = plain_text_to_tokenized_code(boxed_pseudocode);
    check_syntaxis(&tokenized_code);
    write_file(tokenized_code);
}
