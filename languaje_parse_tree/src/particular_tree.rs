use crate::full_parse_tree::get_avaliable_productions_per_word;
use crate::read_elements::LanguageElements;

/// Generate a path for create a custom word with the language rules
/// if there isn't a path, this return a void Vec of Strings
pub fn generate_particular_tree(language: &LanguageElements) -> Vec<String> {
    let finded_path = find_path(&language, language.start_symbol.clone(), &mut Vec::new());
    match finded_path {
        Some(path) => path,
        None => Vec::new(),
    }
}

/// A backtracking version of the horizontal particular tree,
/// This method returns a Option of Vec of Strings that is essentially the path for
/// the word generation or a None, when the word can't be generated
fn find_path(
    language: &LanguageElements,
    current_word: String,
    path: &mut Vec<String>,
) -> Option<Vec<String>> {
    let posible_words = get_avaliable_productions_per_word(&language.productions, current_word);
    for word in posible_words {
        if !compare_words(language, &word, &language.word_to_verify) {
            continue;
        }
        path.push(word.clone());
        if word == language.word_to_verify {
            return Some(path.to_owned());
        }
        let finded = find_path(&language, word.clone(), path);
        if finded.is_some() {
            return finded;
        }
        path.remove(path.len() - 1);
    }
    return None;
}

/// This is a custom comparition function, this allow to continue or break the
/// generation of words on the current branch, the resturn depends of the language non_terminals symbols,
/// example: current_word: "abA", verify_word: "abb"
/// We know that "A" is a non terminal symbol, so we can verify before the non-terminal symbol.
/// In this case "abA" and "abb" will be a true, because we don't know if A can generate B, we need to check
fn compare_words(language: &LanguageElements, current_word: &String, verify_word: &String) -> bool {
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
