use pomsky_macro::pomsky;
use regex::{Regex, RegexSet};

// In this case, i will use a enum in order to tokenize the elements of the pseudocode.
// This function takes a Vec of strings, that are the lines of the psudocode file.
pub fn plain_text_to_tokenized_code(pseudocode_lines: Box<Vec<String>>) {
    let document: Vec<Vec<Word>> = pseudocode_lines
        .iter()
        .map(|line| tokenized_line(line.to_string()))
        .collect();
    for line in document.iter() {
        println!("{:?}", line);
    }
}

fn tokenized_line(line: String) -> Vec<Word> {
    let filter_white_spaces = Regex::new(pomsky!([space]+)).unwrap();
    let lines_filtered = filter_white_spaces.replace_all(&line, " ");
    let words: Vec<&str> = lines_filtered.split(" ").collect();

    words.into_iter().map(|word| tokenized_word(word)).collect()
}

fn tokenized_word(word: &str) -> Word {
    let regex_exp = RegexSet::new(&[
        pomsky!("<-"),                       // Asignation
        pomsky!( [digit]+ ),                 // Integer
        pomsky!( [digit]+ '.'|',' [digit]+), // Float
        pomsky!( '"' [word]* '"'),            // String
        pomsky!("true" | "false"),           // Boolean
        pomsky!("if"),
        pomsky!("while"),
        pomsky!("for"),
        pomsky!("fun"),
        pomsky!(["A"-"z"] [word]+), //Variable
    ])
    .unwrap();
    let matches: Vec<_> = regex_exp.matches(word).into_iter().collect();
    match matches.first().unwrap() {
        0 => Word {
            token: Token::ASIGNATION,
            word: word.to_string(),
        },
        1 => Word {
            token: Token::INTEGER,
            word: word.to_string(),
        },
        2 => Word {
            token: Token::FLOAT,
            word: word.to_string(),
        },
        3 => Word {
            token: Token::STRING,
            word: word.to_string(),
        },
        4 => Word {
            token: Token::BOOLEAN,
            word: word.to_string(),
        },
        5 => Word {
            token: Token::CONDITIONAL,
            word: word.to_string(),
        },
        6 => Word {
            token: Token::WHILELOOP,
            word: word.to_string(),
        },
        7 => Word {
            token: Token::FORLOOP,
            word: word.to_string(),
        },
        8 => Word {
            token: Token::FUNCTION,
            word: word.to_string(),
        },
        9 => Word {
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
struct Word {
    token: Token,
    word: String,
}

#[derive(Debug)]
enum Token {
    ASIGNATION,
    INTEGER,
    FLOAT,
    STRING,
    BOOLEAN,
    VARIABLE,
    CONDITIONAL,
    WHILELOOP,
    FORLOOP,
    FUNCTION,
    ERROR
}
