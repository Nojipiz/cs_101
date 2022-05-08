use debug_tree::*;

mod full_parse_tree;
mod particular_tree;
mod read_elements;

use full_parse_tree::generate_full_parse_tree;
use particular_tree::generate_particular_tree;
use read_elements::LanguageElements;

fn main() {
    let language_element: LanguageElements = read_elements::get_language_elements();
    let tree = TreeBuilder::new();
    generate_full_parse_tree(&language_element, &tree, &language_element.start_symbol, 0);

    generate_particular_tree(&language_element);
    tree.peek_write("output.txt").unwrap();
}
