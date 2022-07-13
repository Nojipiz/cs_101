use std::fmt::Display;

use pomsky_macro::pomsky;
use regex::{Regex, RegexSet};


// In this case, i will use a enum in order to tokenize the elements of the pseudocode.
// This function takes a Vec of strings, that are the lines of the psudocode file.
pub fn plain_text_to_tokenized_code(pseudocode_lines: Box<Vec<String>>) -> Vec<Vec<Word>> {
    let document: Vec<Vec<Word>> = pseudocode_lines
        .iter()
        .map(|line| tokenized_line(line.to_string()))
        .collect();
    document
}

fn tokenized_line(line: String) -> Vec<Word> {
    let filter_white_spaces = Regex::new(pomsky!([space]+)).unwrap();
    let lines_filtered = filter_white_spaces.replace_all(&line, " ");
    let plain_words: Vec<&str> = lines_filtered.split(" ").collect();
    let words = wrap_literal_strings(Box::new(plain_words));
    words.into_iter().map(|word| tokenized_word(&word)).collect()
}

fn wrap_literal_strings(words: Box<Vec<&str>>) -> Vec<String>{
    let mut wrapped_words:Vec<String> = vec![];
    let mut last_string_element:String = String::new();
    for word in words.iter() {
        if last_string_element.is_empty() {
            if word.contains("\"") && !words.last().unwrap().eq(word){
                last_string_element.push_str(*word);
            }else{
               wrapped_words.push(word.to_string());
            }
        }
        else{
            last_string_element.push_str(*word);
            if word.contains("\"") {
                wrapped_words.push(last_string_element.clone());
                last_string_element.clear();
                continue;
            }
        }
    }
    wrapped_words
}

fn tokenized_word(word: &str) -> Word {
    let regex_exp = RegexSet::new(&[
        pomsky!(Start "<-" End),
        pomsky!(Start( ("true"|"false") | ([digit]+) | ([digit]+ ('.'|',') [digit]+) | ('"' [word]* '"') )End),
        pomsky!(Start "if" End),
        pomsky!(Start "while" End),
        pomsky!(Start "for" End),
        pomsky!(Start "until" End),
        pomsky!(Start "step" End),
        pomsky!(Start "fun" End),
        pomsky!(Start ("equals"|"different"|"gratter"|"less") End),
        pomsky!(Start ("+"|"-"|"*"|"/") End),
        pomsky!(Start "end" End),
        pomsky!(Start "print" End),
        pomsky!(["A"-"z"] [word]+), //Variable
    ])
    .unwrap();
    let matches: Vec<_> = regex_exp.matches(word).into_iter().collect();
    match matches.first().unwrap_or(&100) {
        0 => Word {
            token: Token::ASIGNATION,
            word: word.to_string(),
        },
        1 => Word {
            token: Token::LITERALVALUE,
            word: word.to_string(),
        },
        2 => Word {
            token: Token::CONDITIONAL,
            word: word.to_string(),
        },
        3 => Word {
            token: Token::WHILELOOP,
            word: word.to_string(),
        },
        4 => Word {
            token: Token::FORLOOP,
            word: word.to_string(),
        },
        5 => Word {
            token: Token::UNTIL,
            word: word.to_string(),
        },
        6 => Word {
            token: Token::STEP,
            word: word.to_string(),
        },
        7 => Word {
            token: Token::FUNCTION,
            word: word.to_string(),
        },
        8 => Word {
            token: Token::COMPARATOR,
            word: word.to_string(),
        },
        9 => Word{
            token:Token::OPERATOR,
            word: word.to_string()
        },
        10 => Word {
            token: Token::END,
            word: word.to_string(),
        },
        11 => Word{
            token: Token::PRINTER,
            word: word.to_string(),
        },
        12 => Word {
            token: Token::VARIABLE,
            word: word.to_string(),
        },
        _ =>  Word {
            token: Token::ERROR,
            word: word.to_string(),
        },
    }
}

#[derive(Debug)]
pub struct Word {
    pub token: Token,
    pub word: String,
}

impl Display for Token{
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        match *self {
            Token::ASIGNATION => write!(f, "ASIGNATION"),
            Token::LITERALVALUE => write!(f, "LITERALVALUE"),
            Token::VARIABLE => write!(f, "VARIABLE"),
            Token::CONDITIONAL => write!(f,"CONDITIONAL"),
            Token::WHILELOOP => write!(f, "WHILELOOP"),
            Token::FORLOOP => write!(f, "FORLOOP"),
            Token::UNTIL => write!(f, "UNTIL"),
            Token::STEP => write!(f, "STEP"),
            Token::FUNCTION => write!(f,"FUNCTION"),
            Token::COMPARATOR => write!(f, "COMPARATOR"),
            Token::OPERATOR => write!(f, "OPERATOR"),
            Token::END => write!(f, "END"),
            Token::PRINTER => write!(f, "PRINTER"),
            Token::ERROR => write!(f, "ERROR"),
        }
    }
}

#[derive(Debug)]
pub enum Token {
    ASIGNATION,
    LITERALVALUE,
    VARIABLE,
    CONDITIONAL,
    WHILELOOP,
    FORLOOP,
    UNTIL,
    STEP,
    FUNCTION,
    COMPARATOR,
    OPERATOR,
    END,
    PRINTER,
    ERROR
}
