# kotlox

A Kotlin (JVM) library implementing the [Lox language](https://craftinginterpreters.com/the-lox-language.html),
following the tree-walk interpreter from [Crafting Interpreters](https://craftinginterpreters.com/).

Project setup is based on [kotlin-library-template](https://github.com/christian-draeger/kotlin-library-template),
reduced to JVM-only and without publishing.

## Modules

| Module  | Path            | Description                                                        |
|---------|-----------------|--------------------------------------------------------------------|
| `:core` | `core/`         | The library (explicit API mode).                                   |
| `:repl` | `examples/repl` | Example app: REPL, or runs a script file, printing scanned tokens. |
| `:ast-printer` | `examples/ast-printer` | Example app: prints a hand-built syntax tree with `AstPrinter`. |
| `:generate-ast` | `tools/generate-ast` | Generates the syntax tree classes (`Expr.kt`) for `:core`. |

## Usage

```sh
./gradlew build                          # compile, test, detekt, kover coverage reports
./gradlew :repl:run -q --console=plain   # interactive prompt
./gradlew :repl:run --args="script.lox"  # run a file
./gradlew :ast-printer:run -q --console=plain  # print a sample syntax tree
./gradlew :generate-ast:run --args="core/src/main/kotlin/tech/ixor/kotlox/ast"  # regenerate the AST
./gradlew versionCatalogUpdate           # update gradle/libs.versions.toml
```

The Gradle daemon and Kotlin toolchain run on JDK 21 (auto-provisioned via foojay if missing).

## License

[MIT](LICENSE)
