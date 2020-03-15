

public class Evaluator
{
	public static int eval(Expression head)
	{		
		return visitExpression(head);
	}
	
	private static int visitExpression(Expression expr)
	{	
		int term1 = (expr.left_expr.getClass().getName().equals("Expression")) ?
			visitExpression(expr.left_expr) : visitTerm((Term)expr.left_expr);
		
		int term2 = 0;
		if(expr.right_expr != null) {
			term2 = (expr.right_expr.getClass().getName().equals("Expression")) ?
				visitExpression(expr.right_expr) : visitTerm((Term)expr.right_expr);
		}
		
		return evalOp(term1, expr.op, term2);
	}
	
	private static int visitTerm(Term term)
	{
		if(term == null) return 0;
		
		int factor1 = (term.left_expr != null) ? 
			visitExpression(term.left_expr) : term.left_factor;
		int factor2 = (term.right_expr != null) ?
			visitExpression(term.right_expr) : term.right_factor;
		
		return evalOp(factor1, term.op, factor2);
	}
	
	private static int evalOp(int op1, Token op, int op2)
	{
		if(op == null) return op1;
		
		switch(op.type)
		{
			case Token.OP_ADD:
				return op1 + op2;
			case Token.OP_SUB:
				return op1 - op2;
			case Token.OP_MULT:
				return op1 * op2;
			case Token.OP_DIV:
				return op1 / op2;
			default:
				System.out.println("Not a valid operation [" + 
				                   op + "]");
				System.exit(4);
		}
		return 0;
	}
}
