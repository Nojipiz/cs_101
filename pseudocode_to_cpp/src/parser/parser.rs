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
        0 => variable_asignation_parse(line),
        1 => variable_asignation_parse(line),
        _ => variable_asignation_parse(line),
    }
}

fn variable_asignation_parse(line: &Vec<Word>) -> String{
    if line.len() == 3{
        match line.last().unwrap().token {
            Token::VARIABLE => format!("todo {} = {};", line[0].word, line[2].word),
            _ => strong_type_conversion(line),
        }
    }else{
        String::from("xd")
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

