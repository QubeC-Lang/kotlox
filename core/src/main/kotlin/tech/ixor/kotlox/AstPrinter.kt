package tech.ixor.kotlox

import tech.ixor.kotlox.ast.Expr

/**
 * Prints a syntax tree as an unambiguous, Lisp-like string, e.g. `(* (- 123) (group 45.67))`.
 *
 * Every operation is wrapped in parentheses with its operator first, so the nesting of
 * the tree is visible. Useful for debugging the parser.
 */
public class AstPrinter : Expr.Visitor<String> {
    //> Printing Utility Methods
    private fun parenthesize(name: String, vararg exprs: Expr): String =
        exprs.joinToString(separator = "", prefix = "($name", postfix = ")") { " ${it.accept(this)}" }
    //< Printing Utility Methods

    //> Visit Methods
    override fun visitBinaryExpr(expr: Expr.Binary): String =
        parenthesize(expr.operator.lexeme, expr.left, expr.right)

    override fun visitGroupingExpr(expr: Expr.Grouping): String = parenthesize("group", expr.expression)

    override fun visitLiteralExpr(expr: Expr.Literal): String = expr.value?.toString() ?: "nil"

    override fun visitUnaryExpr(expr: Expr.Unary): String = parenthesize(expr.operator.lexeme, expr.right)
    //< Visit Methods

    /**
     * Returns the printed form of [expr].
     */
    public fun print(expr: Expr): String = expr.accept(this)
}
