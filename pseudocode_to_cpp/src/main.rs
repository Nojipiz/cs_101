use std::env;

use lexical::lexical::plain_text_to_tokenized_code;
use reader::reader::get_plain_pseudocode;
use syntactic::syntactic::check_syntaxis;

mod reader;
mod lexical;
mod syntactic;

fn main() {
    let args: Vec<String> = env::args().collect();
    let pseudocode: Vec<String> = get_plain_pseudocode(args[1].clone());
    let boxed_pseudocode: Box<Vec<String>> = Box::new(pseudocode);
    let tokenized_code = plain_text_to_tokenized_code(boxed_pseudocode);
    check_syntaxis(tokenized_code);
}
