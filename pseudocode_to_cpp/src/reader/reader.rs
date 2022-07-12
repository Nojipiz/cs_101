use std::fs;

pub fn get_plain_pseudocode(path: String) -> Vec<String> {
    let plain_text: String = fs::read_to_string(path).unwrap();
    plain_text
        .split("\n")
        .map(|line| line.to_string().trim().to_owned())
        .filter(|line| !line.is_empty())
        .collect()
}
