use serde::{Deserialize, Serialize};
use std::fs;

pub fn get_language_elements() -> LanguageElements {
    let input_content = read_input();
    let plain_elements: PlainElements = serde_json::from_str(&input_content).unwrap();
    let language_elements = create_language_elements_from(plain_elements);
    language_elements
}

fn read_input() -> String {
    let input_content: String = fs::read_to_string("input.json").unwrap_or(String::from("Error"));
    if input_content == "Error" {
        panic!("input.json file not found, please create one or change the read permissions");
    }
    input_content
}

fn create_language_elements_from(plain_elements: PlainElements) -> LanguageElements {
    let plain_productions: &Vec<String> = &plain_elements.productions;
    let productions: Vec<Production> = plain_productions
        .into_iter()
        .map(|production| generate_production_from_string(production.to_owned(), &plain_elements))
        .collect();
    LanguageElements {
        non_terminals: plain_elements.non_terminals,
        terminals: plain_elements.terminals,
        productions,
        start_symbol: plain_elements.start_symbol,
        word_to_verify: plain_elements.word_to_verify,
    }
}

fn generate_production_from_string(
    plain_production: String,
    plain_elements: &PlainElements,
) -> Production {
    let elements: Vec<&str> = plain_production.split("->").collect();
    if elements.len() != 2 {
        panic!("Bad production syntax, check the productions at input.json file")
    }
    check_and_generate_production(
        elements[0].to_owned(),
        elements[1].to_owned(),
        plain_elements,
    )
}

fn check_and_generate_production(
    init: String,
    result: String,
    plain_elements: &PlainElements,
) -> Production {
    if !plain_elements.non_terminals.contains(&init) {
        panic!("The productions can't contain non terminal symbols before the -> ")
    }
    Production { init, result }
}

#[derive(Debug, Deserialize, Serialize)]
pub struct LanguageElements {
    pub non_terminals: Vec<String>,
    pub terminals: Vec<String>,
    pub productions: Vec<Production>,
    pub start_symbol: String,
    pub word_to_verify: String,
}

#[derive(Debug, Deserialize, Serialize)]
pub struct Production {
    pub init: String,
    pub result: String,
}

#[derive(Debug, Deserialize, Serialize)]
struct PlainElements {
    non_terminals: Vec<String>,
    terminals: Vec<String>,
    productions: Vec<String>,
    start_symbol: String,
    word_to_verify: String,
}
