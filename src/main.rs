use debug_tree::*;
mod read_elements;

use read_elements::{LanguageElements, Production};

fn main() {
    let language_element: LanguageElements = read_elements::get_language_elements();
    let tree = TreeBuilder::new();
    print_tree(&language_element, &tree, &language_element.start_symbol, 0);
    tree.print();
    //tree.peek_write("output.txt").unwrap()
}

fn print_tree(
    language: &LanguageElements,
    tree: &TreeBuilder,
    current_symbol: &String,
    mut level: u32,
) {
    if level > 5 {
        return;
    }
    tree.add_branch(current_symbol);
    let productions = get_avaliable_productions(&language.productions, current_symbol.to_owned());
    productions.into_iter().for_each(|production| {
        production.chars().into_iter().for_each(|symbol| {
            tree.enter();
            level += 1;
            print_tree(language, tree, &symbol.to_string(), level);
            level -= 1;
            tree.exit();
        });
    });
}

fn get_avaliable_productions(
    productions: &Vec<Production>,
    current_symbol: String,
) -> Vec<&String> {
    productions
        .into_iter()
        .filter(|production| production.init == current_symbol)
        .map(|production| &production.result)
        .collect()
}
