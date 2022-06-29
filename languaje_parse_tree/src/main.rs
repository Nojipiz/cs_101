use debug_tree::*;

mod full_parse_tree;
mod particular_tree;
mod read_elements;
mod write_elements;

use full_parse_tree::generate_full_parse_tree;
use particular_tree::generate_particular_tree;
use read_elements::LanguageElements;
use write_elements::write_results;

fn main() {
    let language_element: LanguageElements = read_elements::get_language_elements();
    check_language_properties(&language_element);

    let tree = TreeBuilder::new();
    generate_full_parse_tree(&language_element, &tree, &language_element.start_symbol, 0);

    let path: Vec<String> = generate_particular_tree(&language_element);
    write_results(&tree, path, language_element)
}

/// This function check the data consistency after generates the LanguageElements,
/// with the rules given in class
fn check_language_properties(language_element: &LanguageElements) {
    if language_element.non_terminals.is_empty() || language_element.terminals.is_empty() {
        panic!("The non terminals and terminals elements can't be void");
    }
    for symbol in &language_element.terminals {
        if language_element.non_terminals.contains(&symbol) {
            panic!("Non terminals and terminals elements can't cointain the same symbols");
        }
    }
    if !language_element
        .non_terminals
        .contains(&language_element.start_symbol)
    {
        panic!("The start symbol must be an element of non terminal's set")
    }
}
