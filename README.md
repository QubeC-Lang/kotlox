# kotlox

A Kotlin (JVM) library implementing the [Lox language](https://craftinginterpreters.com/the-lox-language.html),
following the tree-walk interpreter from [Crafting Interpreters](https://craftinginterpreters.com/).

Project setup is based on [kotlin-library-template](https://github.com/christian-draeger/kotlin-library-template),
reduced to JVM-only and without publishing.

## Modules

| Module          | Path            | Description                                                      |
|-----------------|-----------------|------------------------------------------------------------------|
| `:core`         | `core/`         | The library (explicit API mode).                                 |
| `:repl`         | `examples/repl` | Example app using the library.                                   |

## Usage

```sh
./gradlew build                          # compile, test, detekt, kover coverage reports
./gradlew :repl:run -q --console=plain   # run the example
./gradlew versionCatalogUpdate           # update gradle/libs.versions.toml
```

The Gradle daemon and Kotlin toolchain run on JDK 21 (auto-provisioned via foojay if missing).

## License

[MIT](LICENSE)
