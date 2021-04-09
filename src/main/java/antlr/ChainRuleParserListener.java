// Generated from ChainRuleParser.g4 by ANTLR 4.8
package antlr;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link ChainRuleParser}.
 */
public interface ChainRuleParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link ChainRuleParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(ChainRuleParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link ChainRuleParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(ChainRuleParser.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by {@link ChainRuleParser#callChain}.
	 * @param ctx the parse tree
	 */
	void enterCallChain(ChainRuleParser.CallChainContext ctx);
	/**
	 * Exit a parse tree produced by {@link ChainRuleParser#callChain}.
	 * @param ctx the parse tree
	 */
	void exitCallChain(ChainRuleParser.CallChainContext ctx);
	/**
	 * Enter a parse tree produced by {@link ChainRuleParser#call}.
	 * @param ctx the parse tree
	 */
	void enterCall(ChainRuleParser.CallContext ctx);
	/**
	 * Exit a parse tree produced by {@link ChainRuleParser#call}.
	 * @param ctx the parse tree
	 */
	void exitCall(ChainRuleParser.CallContext ctx);
	/**
	 * Enter a parse tree produced by {@link ChainRuleParser#mapCall}.
	 * @param ctx the parse tree
	 */
	void enterMapCall(ChainRuleParser.MapCallContext ctx);
	/**
	 * Exit a parse tree produced by {@link ChainRuleParser#mapCall}.
	 * @param ctx the parse tree
	 */
	void exitMapCall(ChainRuleParser.MapCallContext ctx);
	/**
	 * Enter a parse tree produced by {@link ChainRuleParser#filterCall}.
	 * @param ctx the parse tree
	 */
	void enterFilterCall(ChainRuleParser.FilterCallContext ctx);
	/**
	 * Exit a parse tree produced by {@link ChainRuleParser#filterCall}.
	 * @param ctx the parse tree
	 */
	void exitFilterCall(ChainRuleParser.FilterCallContext ctx);
	/**
	 * Enter a parse tree produced by {@link ChainRuleParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(ChainRuleParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ChainRuleParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(ChainRuleParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ChainRuleParser#binaryBitwiseAndExpression}.
	 * @param ctx the parse tree
	 */
	void enterBinaryBitwiseAndExpression(ChainRuleParser.BinaryBitwiseAndExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ChainRuleParser#binaryBitwiseAndExpression}.
	 * @param ctx the parse tree
	 */
	void exitBinaryBitwiseAndExpression(ChainRuleParser.BinaryBitwiseAndExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ChainRuleParser#binaryEqualityExpression}.
	 * @param ctx the parse tree
	 */
	void enterBinaryEqualityExpression(ChainRuleParser.BinaryEqualityExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ChainRuleParser#binaryEqualityExpression}.
	 * @param ctx the parse tree
	 */
	void exitBinaryEqualityExpression(ChainRuleParser.BinaryEqualityExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ChainRuleParser#binaryRelationalExpression}.
	 * @param ctx the parse tree
	 */
	void enterBinaryRelationalExpression(ChainRuleParser.BinaryRelationalExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ChainRuleParser#binaryRelationalExpression}.
	 * @param ctx the parse tree
	 */
	void exitBinaryRelationalExpression(ChainRuleParser.BinaryRelationalExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ChainRuleParser#binaryPlusMinusExpression}.
	 * @param ctx the parse tree
	 */
	void enterBinaryPlusMinusExpression(ChainRuleParser.BinaryPlusMinusExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ChainRuleParser#binaryPlusMinusExpression}.
	 * @param ctx the parse tree
	 */
	void exitBinaryPlusMinusExpression(ChainRuleParser.BinaryPlusMinusExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ChainRuleParser#binaryTimesExpression}.
	 * @param ctx the parse tree
	 */
	void enterBinaryTimesExpression(ChainRuleParser.BinaryTimesExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ChainRuleParser#binaryTimesExpression}.
	 * @param ctx the parse tree
	 */
	void exitBinaryTimesExpression(ChainRuleParser.BinaryTimesExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ChainRuleParser#signedAtom}.
	 * @param ctx the parse tree
	 */
	void enterSignedAtom(ChainRuleParser.SignedAtomContext ctx);
	/**
	 * Exit a parse tree produced by {@link ChainRuleParser#signedAtom}.
	 * @param ctx the parse tree
	 */
	void exitSignedAtom(ChainRuleParser.SignedAtomContext ctx);
	/**
	 * Enter a parse tree produced by {@link ChainRuleParser#atom}.
	 * @param ctx the parse tree
	 */
	void enterAtom(ChainRuleParser.AtomContext ctx);
	/**
	 * Exit a parse tree produced by {@link ChainRuleParser#atom}.
	 * @param ctx the parse tree
	 */
	void exitAtom(ChainRuleParser.AtomContext ctx);
	/**
	 * Enter a parse tree produced by {@link ChainRuleParser#parenthesizedExpression}.
	 * @param ctx the parse tree
	 */
	void enterParenthesizedExpression(ChainRuleParser.ParenthesizedExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ChainRuleParser#parenthesizedExpression}.
	 * @param ctx the parse tree
	 */
	void exitParenthesizedExpression(ChainRuleParser.ParenthesizedExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ChainRuleParser#constant}.
	 * @param ctx the parse tree
	 */
	void enterConstant(ChainRuleParser.ConstantContext ctx);
	/**
	 * Exit a parse tree produced by {@link ChainRuleParser#constant}.
	 * @param ctx the parse tree
	 */
	void exitConstant(ChainRuleParser.ConstantContext ctx);
	/**
	 * Enter a parse tree produced by {@link ChainRuleParser#element}.
	 * @param ctx the parse tree
	 */
	void enterElement(ChainRuleParser.ElementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ChainRuleParser#element}.
	 * @param ctx the parse tree
	 */
	void exitElement(ChainRuleParser.ElementContext ctx);
}