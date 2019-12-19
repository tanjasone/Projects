

public class Evaluator
{
	public static int eval(Expression head)
	{
		
		
		return 0;
	}
	
	private static int visitExpression(Expression expr)
	{
		
		
		return 0;
	}
	
	private static int visitTerm(Term term)
	{
		
		
		return 0;
	}
	
	private static int evalOp(int op1, Token op, int op2)
	{
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
			default
				System.out.println("Not a valid operation [" + 
				                   op + "]");
				System.exit(4);
		}
	}
}
