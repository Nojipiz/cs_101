# :memo: Simple language parse tree.
Homework for "Lenguajes formales" class, a CLI software that can generate the full language parse tree or particular parse tree, you can start installing 
Rustup and Cargo, the dependencies will be installed from the Cargo.toml file.

The input.json file, contains the language elements to create a grammar :memo:
```javascript
{
  "non_terminals": [
    "S",
    "A"
  ],
  "terminals": [
    "a",
    "b"
  ],
  "productions": [
    "S->aA",
    "A->aA",
    "A->bA",
    "A->b"
  ],
  "start_symbol": "S",
  "word_to_verify": "ababab"
}
```
And the results will be generated at output.txt file.
```
S
└╼ aA
  ├╼ aaA
  │ ├╼ aaaA
  │ │ ├╼ aaaaA
  │ │ │ ├╼ aaaaaA
  │ │ │ ├╼ aaaabA
  │ │ │ └╼ aaaab
  │ │ ├╼ aaabA
  │ │ │ ├╼ aaabaA
  │ │ │ ├╼ aaabbA
  │ │ │ └╼ aaabb
  │ │ └╼ aaab
  │ ├╼ aabA
  .
  .
  .
  
--------------PARTICULAR TREE RESULTS---------------
            
    S->aA->abA->abaA->ababA->ababaA->ababab
                "ababab" ∈ L(G)
```

