use crate::full_parse_tree::get_avaliable_productions_per_word;
use crate::read_elements::{LanguageElements, Production};

pub fn generate_particular_tree(language: &LanguageElements) {
    print_combinations(&language, language.start_symbol.clone());
}

fn print_combinations(language: &LanguageElements, current_word: String) {
    let posible_words = get_avaliable_productions_per_word(&language.productions, current_word);
    posible_words.into_iter().for_each(|word| {
        let is_valid =
            compare_current_word_with_verify_word(language, &word, &language.word_to_verify);
        if !is_valid {
            return;
        }
        if word == language.word_to_verify {
            println!("Word founded {}", word);
            return;
        }
        print!("Possible valid word {:?}", word);
        print_combinations(&language, word);
    });
}

fn compare_current_word_with_verify_word(
    language: &LanguageElements,
    current_word: &String,
    verify_word: &String,
) -> bool {
    for (index, character) in current_word.char_indices() {
        if language.non_terminals.contains(&character.to_string()) {
            break;
        }
        let verify_character: Option<char> = verify_word.chars().nth(index);
        if verify_character.is_none() || character != verify_character.unwrap() {
            return false;
        }
    }
    return true;
}
