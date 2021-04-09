// Generated from ChainRuleParser.g4 by ANTLR 4.8
package antlr;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link ChainRuleParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface ChainRuleParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link ChainRuleParser#program}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram(ChainRuleParser.ProgramContext ctx);
	/**
	 * Visit a parse tree produced by {@link ChainRuleParser#callChain}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCallChain(ChainRuleParser.CallChainContext ctx);
	/**
	 * Visit a parse tree produced by {@link ChainRuleParser#call}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCall(ChainRuleParser.CallContext ctx);
	/**
	 * Visit a parse tree produced by {@link ChainRuleParser#mapCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMapCall(ChainRuleParser.MapCallContext ctx);
	/**
	 * Visit a parse tree produced by {@link ChainRuleParser#filterCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFilterCall(ChainRuleParser.FilterCallContext ctx);
	/**
	 * Visit a parse tree produced by {@link ChainRuleParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression(ChainRuleParser.ExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ChainRuleParser#binaryBitwiseAndExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBinaryBitwiseAndExpression(ChainRuleParser.BinaryBitwiseAndExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ChainRuleParser#binaryEqualityExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBinaryEqualityExpression(ChainRuleParser.BinaryEqualityExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ChainRuleParser#binaryRelationalExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBinaryRelationalExpression(ChainRuleParser.BinaryRelationalExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ChainRuleParser#binaryPlusMinusExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBinaryPlusMinusExpression(ChainRuleParser.BinaryPlusMinusExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ChainRuleParser#binaryTimesExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBinaryTimesExpression(ChainRuleParser.BinaryTimesExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ChainRuleParser#signedAtom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSignedAtom(ChainRuleParser.SignedAtomContext ctx);
	/**
	 * Visit a parse tree produced by {@link ChainRuleParser#atom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAtom(ChainRuleParser.AtomContext ctx);
	/**
	 * Visit a parse tree produced by {@link ChainRuleParser#parenthesizedExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenthesizedExpression(ChainRuleParser.ParenthesizedExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ChainRuleParser#constant}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstant(ChainRuleParser.ConstantContext ctx);
	/**
	 * Visit a parse tree produced by {@link ChainRuleParser#element}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElement(ChainRuleParser.ElementContext ctx);
}