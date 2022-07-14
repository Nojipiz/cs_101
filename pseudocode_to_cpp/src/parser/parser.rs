use std::fmt::format;

use crate::lexical::lexical::Word;
use crate::lexical::lexical::Token;


pub fn parser(document: &Vec<Vec<Word>>, lines_syntax: &Vec<usize>) -> Vec<String>{
    document.into_iter()
            .enumerate()
            .map(|(index, line)| convert_line_to_cpp(line, lines_syntax[index]))
            .collect::<Vec<String>>()
}

fn convert_line_to_cpp(line: &Vec<Word>, line_type:usize) -> String{
    match line_type {
        0|1 => variable_asignation_parse(line),
        2|3 => conditional_parse(line),
        4 => for_loop_parse(line),
        5 => format!("void {}()", line[1].word),
        6 => String::from("}"),
        7 => print_parse(line),
        _ => String::from("ERROR")
    }
}

fn print_parse(line: &Vec<Word>) -> String{
   format!("std::cout << {}", line[1].word)
}

fn for_loop_parse(line: &Vec<Word>) -> String{
    format!("for (int i = {}; i < {}; i+={})", line[1].word, line[3].word, line[5].word)
}

fn conditional_parse(line: &Vec<Word>) -> String{
    if line.len() == 2 {
        match line.first().unwrap().token {
            Token::CONDITIONAL => format!("if ({})", line.last().unwrap().word),
            _ => format!("while ({})", line.last().unwrap().word),
        }
    } else {
        let comparator = convert_comparators(&line[2]);
        match line.first().unwrap().token {
            Token::CONDITIONAL => format!("if ({} {} {})", line[1].word, comparator, line[3].word),
            _ => format!("while ({} {} {})", line[1].word, comparator, line[3].word),
        }
    }
}

fn variable_asignation_parse(line: &Vec<Word>) -> String{
    if line.len() == 3{
        match line.last().unwrap().token {
            Token::VARIABLE => format!("todo {} = {};", line[0].word, line[2].word),
            _ => strong_type_conversion(line),
        }
    }else{
        format!("{} = {} {} {}", line[0].word, line[2].word, line[3].word, line[4].word)
    }
}

fn strong_type_conversion(line: &Vec<Word>) -> String{
    let literal = &line.last().unwrap().word;
    if literal.contains("\""){
        return format!("string {} = {};", line[0].word, line[2].word);
    } else if literal.contains(".") || literal.contains(","){
        let formatted = literal.clone().replace(",", ".");
        return format!("double {} = {};", line[0].word, formatted);
    } else if literal.eq_ignore_ascii_case("true") || literal.eq_ignore_ascii_case("false"){
        return format!("bool {} = {};", line[0].word, line[2].word)
    }else {
        return format!("int {} = {};", line[0].word, line[2].word);
    }
}


fn convert_comparators(exp: &Word)-> String{
    match exp.word.as_str(){
        "less" => String::from("<"),
        "greater" => String::from(">"),
        "equals" => String::from("=="),
        _ => String::from("!=")
    }
}

