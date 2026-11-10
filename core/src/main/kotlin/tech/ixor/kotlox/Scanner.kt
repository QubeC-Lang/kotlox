package tech.ixor.kotlox

import tech.ixor.kotlox.token.Token
import tech.ixor.kotlox.token.TokenType
import tech.ixor.kotlox.token.TokenType.AND
import tech.ixor.kotlox.token.TokenType.BANG
import tech.ixor.kotlox.token.TokenType.BANG_EQUAL
import tech.ixor.kotlox.token.TokenType.CLASS
import tech.ixor.kotlox.token.TokenType.COMMA
import tech.ixor.kotlox.token.TokenType.DOT
import tech.ixor.kotlox.token.TokenType.ELSE
import tech.ixor.kotlox.token.TokenType.EOF
import tech.ixor.kotlox.token.TokenType.EQUAL
import tech.ixor.kotlox.token.TokenType.EQUAL_EQUAL
import tech.ixor.kotlox.token.TokenType.FALSE
import tech.ixor.kotlox.token.TokenType.FOR
import tech.ixor.kotlox.token.TokenType.FUN
import tech.ixor.kotlox.token.TokenType.GREATER
import tech.ixor.kotlox.token.TokenType.GREATER_EQUAL
import tech.ixor.kotlox.token.TokenType.IDENTIFIER
import tech.ixor.kotlox.token.TokenType.IF
import tech.ixor.kotlox.token.TokenType.LEFT_BRACE
import tech.ixor.kotlox.token.TokenType.LEFT_PAREN
import tech.ixor.kotlox.token.TokenType.LESS
import tech.ixor.kotlox.token.TokenType.LESS_EQUAL
import tech.ixor.kotlox.token.TokenType.MINUS
import tech.ixor.kotlox.token.TokenType.NIL
import tech.ixor.kotlox.token.TokenType.NUMBER
import tech.ixor.kotlox.token.TokenType.OR
import tech.ixor.kotlox.token.TokenType.PLUS
import tech.ixor.kotlox.token.TokenType.PRINT
import tech.ixor.kotlox.token.TokenType.RETURN
import tech.ixor.kotlox.token.TokenType.RIGHT_BRACE
import tech.ixor.kotlox.token.TokenType.RIGHT_PAREN
import tech.ixor.kotlox.token.TokenType.SEMICOLON
import tech.ixor.kotlox.token.TokenType.SLASH
import tech.ixor.kotlox.token.TokenType.STAR
import tech.ixor.kotlox.token.TokenType.STRING
import tech.ixor.kotlox.token.TokenType.SUPER
import tech.ixor.kotlox.token.TokenType.THIS
import tech.ixor.kotlox.token.TokenType.TRUE
import tech.ixor.kotlox.token.TokenType.VAR
import tech.ixor.kotlox.token.TokenType.WHILE

/**
 * Converts Lox source text into a token stream.
 *
 * The scanner recognizes identifiers, reserved words, decimal numbers, strings,
 * punctuation, and operators. Spaces, tabs, carriage returns, and `//` line
 * comments are ignored. Line breaks are retained as token line metadata so later
 * errors can identify their source location.
 *
 * Errors are reported to [lox] and scanning continues, so a single pass reports
 * every lexical error in the source.
 *
 * A scanner instance is single-use. Call [scanTokens] once for a given source.
 *
 * @param source complete Lox source text
 * @param lox the interpreter instance errors are reported to
 */
internal class Scanner(private val source: String, private val lox: Lox) {
    private companion object {
        //> Keyword Map
        /**
         * A map of keywords to their respective token types.
         */
        val KEYWORDS: Map<String, TokenType> = mapOf(
            "and" to AND,
            "class" to CLASS,
            "else" to ELSE,
            "false" to FALSE,
            "for" to FOR,
            "fun" to FUN,
            "if" to IF,
            "nil" to NIL,
            "or" to OR,
            "print" to PRINT,
            "return" to RETURN,
            "super" to SUPER,
            "this" to THIS,
            "true" to TRUE,
            "var" to VAR,
            "while" to WHILE,
        )
        //< Keyword Map
    }

    private val tokens = mutableListOf<Token>()

    //> Scan Info
    private var start = 0
    private var current = 0
    private var line = 1
    //< Scan Info

    //> Is Methods
    private fun isAlpha(c: Char): Boolean = c in 'a'..'z' || c in 'A'..'Z' || c == '_'

    private fun isDigit(c: Char): Boolean = c in '0'..'9'

    private fun isAlphaNumeric(c: Char): Boolean = isAlpha(c) || isDigit(c)

    private fun isAtEnd(): Boolean = current >= source.length
    //< Is Methods

    //> Scanning Helpers
    private fun advance(): Char = source[current++]

    private fun addToken(type: TokenType, literal: Any? = null) {
        val text = source.substring(start, current)
        tokens.add(Token(type, text, literal, line))
    }

    private fun peek(): Char = if (isAtEnd()) '\u0000' else source[current]

    private fun peekNext(): Char = if (current + 1 >= source.length) '\u0000' else source[current + 1]
    //< Scanning Helpers

    //> Two-Character Tokens Finding
    private fun match(expected: Char): Boolean {
        if (isAtEnd()) return false
        if (source[current] != expected) return false
        current++
        return true
    }
    //< Two-Character Tokens Finding

    //> Number and Identifier
    private fun number() {
        while (isDigit(peek())) advance()

        // Looking for a fractional part.
        if (peek() == '.' && isDigit(peekNext())) {
            // Consume the "."
            do {
                advance()
            } while (isDigit(peek()))
        }

        addToken(NUMBER, source.substring(start, current).toDouble())
    }

    private fun identifier() {
        while (isAlphaNumeric(peek())) advance()

        val text = source.substring(start, current)

        // If the token is not a keyword, it is an identifier.
        addToken(KEYWORDS[text] ?: IDENTIFIER)
    }
    //< Number and Identifier

    //> String Finding
    private fun string() {
        while (peek() != '"' && !isAtEnd()) {
            if (peek() == '\n') line++
            advance()
        }

        if (isAtEnd()) {
            lox.error(line, "Unterminated string.")
            return
        }

        // The closing ".
        advance()

        // Trim the surrounding quotes.
        val value = source.substring(start + 1, current - 1)
        addToken(STRING, value)
    }
    //< String Finding

    //> Scanning
    @Suppress("CyclomaticComplexMethod")
    private fun scanToken() {
        when (val c = advance()) {
            //>> Single-Character Tokens
            '(' -> addToken(LEFT_PAREN)
            ')' -> addToken(RIGHT_PAREN)
            '{' -> addToken(LEFT_BRACE)
            '}' -> addToken(RIGHT_BRACE)
            ',' -> addToken(COMMA)
            '.' -> addToken(DOT)
            '-' -> addToken(MINUS)
            '+' -> addToken(PLUS)
            ';' -> addToken(SEMICOLON)
            '*' -> addToken(STAR)
            //<< Single-Character Tokens
            //>> Slash (Single-Character or Comment)
            '/' -> {
                if (match('/')) {
                    // A comment goes until the end of the line.
                    while (peek() != '\n' && !isAtEnd()) advance()
                } else {
                    addToken(SLASH)
                }
            }
            //<< Slash (Single-Character or Comment)
            //>> One or Two-Character Tokens
            '!' -> addToken(if (match('=')) BANG_EQUAL else BANG)
            '=' -> addToken(if (match('=')) EQUAL_EQUAL else EQUAL)
            '<' -> addToken(if (match('=')) LESS_EQUAL else LESS)
            '>' -> addToken(if (match('=')) GREATER_EQUAL else GREATER)
            //<< One or Two-Character Tokens
            //>> Whitespace and Line Breaks
            ' ', '\r', '\t' -> {
                // Ignore whitespace.
            }
            '\n' -> line++
            //<< Whitespace and Line Breaks
            //>> Start of String
            '"' -> string()
            //<< Start of String
            //>> Literals and Unknown
            else -> when {
                isDigit(c) -> number()
                isAlpha(c) -> identifier()
                else -> lox.error(line, "Unexpected character.")
            }
            //<< Literals and Unknown
        }
    }

    /**
     * Scans the complete source and appends a final [EOF] token.
     * Number literals are represented as [Double]; string literal values omit
     * their surrounding quotation marks.
     *
     * @return tokens in source order, including the terminating EOF token
     */
    fun scanTokens(): List<Token> {
        while (!isAtEnd()) {
            start = current
            scanToken()
        }
        tokens.add(Token(EOF, "", null, line))
        return tokens
    }
    //< Scanning
}
