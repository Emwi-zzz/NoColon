# NoColon

A clean, semicolon-free imperative language where every variable is an **Integer**.
At the end of execution, the interpreter automatically lists all global variables and their final values.

---

## ⚡ Quick Start

Run your code by piping it into the Gradle wrapper:

```bash
./gradlew run < samples/nested.nc

```

---

## 🛠 Syntax Guide

### Variables & Blocks

Assignments use `=` and you can group statments with `{}`.

```rust
i1 = 0
c1 = 0
j1 = 0

while (i1 < 10) do j1 = 0, while (j1 < 10) do c1 = c1 + 1, j1 = j1 + 1, i1 = i1 + 1

i2 = 0
c2 = 0
j2 = 0

while (i2 < 10) do j2 = 0, {while (j2 < 10) do c2 = c2 + 1, j2 = j2 + 1}, i2 = i2 + 1

/*
Output:
j1 = 10
c1 = 10
j2 = 10
c2 = 100
*/

```

### Functions

Declared with `fun`, using `return` for outputs. Functions return 0 if no return statement is present.

```rust
fun multiply(a, b) {
    return a * b
}

```

### Control Flow

Supports standard `if-then-else` and `while-do` loops.

```rust
if x > 0 then {
    count = 0
    while count < x do count = count + 1
}

```

---

## 🧩 Operators

* **Math**: `+`, `-`, `*`, `/`, `%`

* **Logic**: `&&`, `||`, `!`

* **Comparison**: `==`, `!=`, `<`, `>`, `<=`, `>=`


---

## 📁 Project Contents

* `src/main/antlr/NoColon.g4`: ANTLR4 grammar definitions.


* `src/main/kotlin`: Kotlin interpreter source code.


* `src/test/kotlin`: Kotlin test source code.


* `samples/`: Example code files.