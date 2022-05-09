use std::fs::OpenOptions;
use std::io::prelude::*;

use debug_tree::TreeBuilder;

use crate::read_elements::LanguageElements;

/// Write the full tree and particular tree to output.txt file
pub fn write_results(
    full_tree: &TreeBuilder,
    path_to_word: Vec<String>,
    language: LanguageElements,
) {
    full_tree.peek_write("output.txt").unwrap();
    let mut file = OpenOptions::new()
        .write(true)
        .append(true)
        .open("output.txt")
        .unwrap();

    if let Err(e) = {
        writeln!(
            file,
            "

        --------------PARTICULAR TREE RESULTS---------------
            "
        )
    } {
        eprintln!("Couldn't write to file: {}", e);
    }
    if path_to_word.is_empty() {
        let _sucessfull = writeln!(
            file,
            "The word {:?} can't be generated from the current language
            {:?} ∉ L(G)",
            &language.word_to_verify, language.word_to_verify
        );
    } else {
        let _sucessfull = write!(
            file,
            "{}->{}
            {:?} ∈ L(G)",
            language.start_symbol,
            path_to_word.join("->"),
            language.word_to_verify
        );
    }
}
