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
        for word in line.iter() {
            print!("{:?} ", word.token);
        }
        println!("{}", "");
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
        pomsky!(Start "<-" End),
        pomsky!(Start '"' [word]* '"' End),
        pomsky!(Start [digit]+ '.'|',' [digit]+ End),
        pomsky!(Start [digit]+ End),
        pomsky!(Start "true" End|Start "false" End),
        pomsky!(Start "if" End),
        pomsky!(Start "while" End),
        pomsky!(Start "for" End),
        pomsky!(Start "until" End),
        pomsky!(Start "step" End),
        pomsky!(Start "fun" End),
        pomsky!(Start "equals" End),
        pomsky!(Start "different" End),
        pomsky!(Start "greater" End),
        pomsky!(Start "less" End),
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
            token: Token::STRING,
            word: word.to_string(),
        },
        2 => Word {
            token: Token::FLOAT,
            word: word.to_string(),
        },
        3 => Word {
            token: Token::INTEGER,
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
            token: Token::UNTIL,
            word: word.to_string(),
        },
        9 => Word {
            token: Token::STEP,
            word: word.to_string(),
        },
        10 => Word {
            token: Token::FUNCTION,
            word: word.to_string(),
        },
        11 => Word {
            token: Token::EQUALS,
            word: word.to_string(),
        },
        12 => Word {
            token: Token::DIFFERENT,
            word: word.to_string(),
        },
        13 => Word {
            token: Token::GREATER,
            word: word.to_string(),
        },
        14 => Word {
            token: Token::LESS,
            word: word.to_string(),
        },
        15 => Word {
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
    UNTIL,
    STEP,
    FUNCTION,
    EQUALS,
    DIFFERENT,
    GREATER,
    LESS,
    ERROR
}
