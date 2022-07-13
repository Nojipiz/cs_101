use pomsky_macro::pomsky;
use regex::RegexSet;

use crate::lexical::lexical::Word;

pub fn check_syntaxis(document: &Vec<Vec<Word>>) -> Vec<bool>{
    let results_list = document.into_iter()
            .map(|line| check_line(line))
            .collect::<Vec<bool>>();
    check_for_errors(&results_list);
    results_list
}

fn check_line(line: &Vec<Word>) -> bool{
    let tokens_of_line = line
        .into_iter()
        .map(|word| format!("{}", word.token))
        .collect::<String>();
    let regex_exp = RegexSet::new(&[
        pomsky!(Start "VARIABLE" "ASIGNATION" ("LITERALVALUE"|"VARIABLE") End),
        pomsky!(Start "VARIABLE" "ASIGNATION" (("LITERALVALUE"|"VARIABLE") "OPERATOR" ("LITERALVALUE"|"VARIABLE")) End),
        pomsky!(Start ("CONDITIONAL"|"WHILELOOP") ("LITERALVALUE"|"VARIABLE") "COMPARATOR" ("LITERALVALUE"|"VARIABLE") End),
        pomsky!(Start ("CONDITIONAL"|"WHILELOOP") ("LITERALVALUE"|"VARIABLE") End),
        pomsky!(Start "FORLOOP" "VARIABLE" "UNTIL" ("LITERALVALUE"|"VARIABLE") "STEP" ("LITERALVALUE"|"VARIABLE") End),
        pomsky!(Start "FUNCTION" "VARIABLE" End),
        pomsky!(Start "END" End),
        pomsky!(Start "PRINTER" ("LITERALVALUE"|"VARIABLE") End),
    ])
    .unwrap();
    let matches:Vec<_> = regex_exp.matches(&tokens_of_line)
                                  .into_iter()
                                  .collect();
    return match matches.first().unwrap_or(&100) {
        0|1|2|3|4|5|6|7|8|9 => true,
        _ => false
    }
}


fn check_for_errors(results_list:&Vec<bool>){
    results_list.into_iter()
                           .filter(|element| element == &&false)
                           .enumerate()
                           .for_each(|(index, _element)| println!("Error at line {}", index));
}
