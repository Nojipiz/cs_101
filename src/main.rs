use debug_tree::*;

mod read_elements;

use read_elements::{LanguageElements, Production};

fn main() {
    let language_element: LanguageElements = read_elements::get_language_elements();
    let tree = TreeBuilder::new();
    generate_full_parse_tree(&language_element, &tree, &language_element.start_symbol, 0);
    tree.peek_write("output.txt").unwrap();
}

fn generate_full_parse_tree(
    language: &LanguageElements,
    tree: &TreeBuilder,
    current_word: &String,
    mut level: u32,
) {
    if level > 5 {
        return;
    }
    tree.add_branch(current_word);
    let productions =
        get_avaliable_productions_per_word(&language.productions, current_word.clone());
    productions.into_iter().for_each(|production| {
        tree.enter();
        level += 1;
        generate_full_parse_tree(language, tree, &production, level);
        level -= 1;
        tree.exit();
    });
}

fn get_avaliable_productions_per_word(
    productions: &Vec<Production>,
    current_word: String,
) -> Vec<String> {
    current_word
        .char_indices()
        .into_iter()
        .flat_map(|(index, symbol)| {
            let avaliable_productions_symbols =
                get_avaliable_productions_per_symbol(productions, symbol.to_owned());
            generate_multiple_words_with_replacements(
                current_word.clone(),
                avaliable_productions_symbols.clone(),
                index,
            )
        })
        .collect::<Vec<String>>()
}

fn generate_multiple_words_with_replacements(
    current_word: String,
    replacements: Vec<&String>,
    index: usize,
) -> Vec<String> {
    replacements
        .into_iter()
        .map(|replacement| replace_symbol_in_word(&current_word, index, replacement))
        .collect::<Vec<String>>()
}

fn replace_symbol_in_word(current_word: &String, index: usize, inner_word: &String) -> String {
    let mut new_word: String = current_word.to_owned();
    new_word.remove(index);
    new_word.insert_str(index, &inner_word);
    new_word
}

fn get_avaliable_productions_per_symbol(
    productions: &Vec<Production>,
    current_symbol: char,
) -> Vec<&String> {
    productions
        .into_iter()
        .filter(|production| production.init == current_symbol.to_string())
        .map(|production| &production.result)
        .collect()
}
