use std::fmt::Display;
use std::fmt::format;

use crate::lexical::lexical::Token;
use crate::lexical::lexical::Word;

pub fn parser(document: &Vec<Vec<Word>>, lines_syntax: &Vec<usize>) -> Vec<String> {
    let mut assigned_vars: Vec<Variables> = vec![];
    document
        .into_iter()
        .enumerate()
        .map(|(index, line)| convert_line_to_cpp(line, lines_syntax[index], &mut assigned_vars).replace("&", "{"))
        .collect::<Vec<String>>()
}

fn convert_line_to_cpp(
    line: &Vec<Word>,
    line_type: usize,
    assigned_vars: &mut Vec<Variables>,
) -> String {
    match line_type {
        0 | 1 => variable_asignation_parse(line, assigned_vars),
        2 | 3 => conditional_parse(line),
        4 => for_loop_parse(line),
        5 => format!("void {}()&", line[1].word),
        6 => String::from("}"),
        7 => print_parse(line),
        8 => format!("{};", line[0].word),
        _ => String::from("ERROR"),
    }
}

fn print_parse(line: &Vec<Word>) -> String {
    format!("std::cout << {};", line[1].word)
}

fn for_loop_parse(line: &Vec<Word>) -> String {
    format!(
        "for (int i = {}; i < {}; i+={})&",
        line[1].word, line[3].word, line[5].word
    )
}

fn conditional_parse(line: &Vec<Word>) -> String {
    if line.len() == 2 {
        match line.first().unwrap().token {
            Token::CONDITIONAL => format!("if ({})&", line.last().unwrap().word),
            _ => format!("while ({})&", line.last().unwrap().word),
        }
    } else {
        let comparator = convert_comparators(&line[2]);
        match line.first().unwrap().token {
            Token::CONDITIONAL => format!("if ({} {} {})&", line[1].word, comparator, line[3].word),
            _ => format!("while ({} {} {})&", line[1].word, comparator, line[3].word),
        }
    }
}

fn variable_asignation_parse(line: &Vec<Word>, assigned_vars: &mut Vec<Variables>) -> String {
    let variable_has_been_assigned = assigned_vars
        .into_iter()
        .map(|var| &var.name)
        .collect::<Vec<&String>>()
        .contains(&&line[0].word);
    if line.len() == 3 {
        if variable_has_been_assigned {
            format!("{} = {};", line[0].word, line[2].word)
        } else {
            match line.last().unwrap().token {
                Token::VARIABLE => variable_declaration_from_other_variable(line, assigned_vars),
                _ => variable_declaration_strong_type(line, assigned_vars),
            }
        }
    } else {
        if variable_has_been_assigned {
            format!(
                "{} = {} {} {};",
                line[0].word, line[2].word, line[3].word, line[4].word
            )
        } else {
            variable_declaration_from_operation(line, assigned_vars)
        }
    }
}

fn variable_declaration_strong_type(
    line: &Vec<Word>,
    assigned_vars: &mut Vec<Variables>,
) -> String {
    let literal = &line.last().unwrap().word;
    if literal.contains("\"") {
        assigned_vars.push(Variables {
            name: line[0].word.clone(),
            var_type: Type::STRING,
        });
        return format!("string {} = {};", line[0].word, line[2].word);
    } else if literal.contains(".") || literal.contains(",") {
        assigned_vars.push(Variables {
            name: line[0].word.clone(),
            var_type: Type::DOUBLE,
        });
        let formatted = literal.clone().replace(",", ".");
        return format!("double {} = {};", line[0].word, formatted);
    } else if literal.eq_ignore_ascii_case("true") || literal.eq_ignore_ascii_case("false") {
        assigned_vars.push(Variables {
            name: line[0].word.clone(),
            var_type: Type::BOOLEAN,
        });
        return format!("bool {} = {};", line[0].word, line[2].word);
    } else {
        assigned_vars.push(Variables {
            name: line[0].word.clone(),
            var_type: Type::INTEGER,
        });
        return format!("int {} = {};", line[0].word, line[2].word);
    }
}

fn variable_declaration_from_other_variable(
    line: &Vec<Word>,
    assigned_vars: &mut Vec<Variables>,
) -> String {
    let variable_type: Type = assigned_vars
        .into_iter()
        .find(|var| var.name == line[2].word)
        .unwrap()
        .var_type;
    assigned_vars.push(Variables {name: line[0].word.clone(),var_type: variable_type.clone()});
    format!("{} {} = {};", variable_type, line[0].word, line[2].word)
}

fn variable_declaration_from_operation(
    line: &Vec<Word>,
    assigned_vars: &mut Vec<Variables>,
) -> String {
    let mut assigned_iter = assigned_vars.into_iter().map(|var| var.name.clone());
    let first_element = assigned_iter.find(|name| name == &line[2].word);
    let second_element = assigned_iter.find(|name| name == &line[4].word);
    if first_element.is_some() {
        let variable_type:&Type = &get_variable_type(assigned_vars, first_element.unwrap());
        assigned_vars.push(Variables {name: line[0].word.clone(),var_type: variable_type.clone()});
        format!(
            "{} {} = {} {} {};",
            variable_type, line[0].word, line[2].word, line[3].word, line[4].word
        )
    } else if second_element.is_some() {
        let variable_type:&Type = &get_variable_type(assigned_vars, second_element.unwrap());
        assigned_vars.push(Variables {name: line[0].word.clone(),var_type: variable_type.clone()});
        format!(
            "{} {} = {} {} {};",
            variable_type, line[0].word, line[2].word, line[3].word, line[4].word
        )
    } else {
        let literal = &line[2].word;
        if literal.contains("\"") {
            assigned_vars.push(Variables {
                name: line[0].word.clone(),
                var_type: Type::STRING,
            });
            return format!(
                "string {} = {} {} {};",
                line[0].word, line[2].word, line[3].word, line[4].word
            );
        } else if literal.contains(".") || literal.contains(",") {
            assigned_vars.push(Variables {
                name: line[0].word.clone(),
                var_type: Type::DOUBLE,
            });
            let formatted = literal.clone().replace(",", ".");
            return format!(
                "double {} = {} {} {};",
                line[0].word, formatted, line[3].word, line[4].word
            );
        } else if literal.eq_ignore_ascii_case("true") || literal.eq_ignore_ascii_case("false") {
            assigned_vars.push(Variables {
                name: line[0].word.clone(),
                var_type: Type::BOOLEAN,
            });
            return format!(
                "bool {} = {} {} {};",
                line[0].word, line[2].word, line[3].word, line[4].word
            );
        } else {
            assigned_vars.push(Variables {
                name: line[0].word.clone(),
                var_type: Type::INTEGER,
            });
            return format!(
                "int {} = {} {} {};",
                line[0].word, line[2].word, line[3].word, line[4].word
            );
        }
    }
}

fn get_variable_type(assigned_vars: &mut Vec<Variables>, variable_name:String) -> Type{
    let variable = assigned_vars.into_iter().find(|var| var.name == variable_name);
    if variable.is_none(){
        Type::BOOLEAN
    }else{
        variable.unwrap().var_type
    }
}

fn convert_comparators(exp: &Word) -> String {
    match exp.word.as_str() {
        "less" => String::from("<"),
        "greater" => String::from(">"),
        "equals" => String::from("=="),
        _ => String::from("!="),
    }
}

struct Variables {
    name: String,
    var_type: Type,
}

#[derive(Copy, Clone)]
enum Type {
    STRING,
    DOUBLE,
    BOOLEAN,
    INTEGER,
}

impl Display for Type {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        match self {
            Type::STRING => write!(f, "string"),
            Type::DOUBLE => write!(f, "double"),
            Type::BOOLEAN => write!(f, "bool"),
            Type::INTEGER => write!(f, "int"),
        }
    }
}
