use debug_tree::*;

use crate::read_elements::{LanguageElements, Production};

/// Main function for generation, contians a type of backtracking to create a tree
/// into the tree element, contians a level variable that will be checked at the start of
/// an interation, for this homework the max value will be 5 but it works fine with more than 20 iterations
pub fn generate_full_parse_tree(
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

/// Generates a list of words with the replacements for every single symbol
/// in the word, and return a list with the next generation of elements in the
/// tree
pub fn get_avaliable_productions_per_word(
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

/// See the function replace_symbol_in_word, this function do the same with
/// multiple replacements
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

/// Replace a &str in String in the defined index
/// example: current_word: "aBc" index: 1 inner_word:"X"
/// so this function return "aXc"
fn replace_symbol_in_word(current_word: &String, index: usize, inner_word: &String) -> String {
    let mut new_word: String = current_word.to_owned();
    new_word.remove(index);
    new_word.insert_str(index, &inner_word);
    new_word
}

/// Search for the avaliable prodcutions for a symbol, example:
/// current_symbol:"S" and productions: "S->a", "S->b"
/// so this function return "a", "b"
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
