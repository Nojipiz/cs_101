use std::env::{self};

use reader::reader::get_plain_pseudocode;

mod reader;

fn main() {
    let args: Vec<String> = env::args().collect();
    let pseudocode: Vec<String> = get_plain_pseudocode(args[1].clone());
    println!("{:?}", pseudocode);
}
