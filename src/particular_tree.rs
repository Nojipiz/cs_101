use crate::full_parse_tree::get_avaliable_productions_per_word;
use crate::read_elements::LanguageElements;

pub fn generate_particular_tree(language: &LanguageElements) {
    let res = find_path(&language, language.start_symbol.clone(), &mut Vec::new());
    if !res {
        println!("We can't generate the word ");
    }
}

fn find_path(language: &LanguageElements, current_word: String, path: &mut Vec<String>) -> bool {
    let posible_words = get_avaliable_productions_per_word(&language.productions, current_word);
    for word in posible_words {
        if !compare_words(language, &word, &language.word_to_verify) {
            continue;
        }
        path.push(word.clone());
        if word == language.word_to_verify {
            print!(
                "The current path to generate the word {:?} is :
                {}->{}",
                &language.word_to_verify,
                language.start_symbol,
                path.join("->")
            );
            return true;
        }
        let finded = find_path(&language, word.clone(), path);
        if finded {
            return finded; // || true
        }
        path.remove(path.len() - 1);
    }
    return false;
}

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
