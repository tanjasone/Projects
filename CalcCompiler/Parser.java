


class Expression
{
	Expression left_expr;
	Expression right_expr;
	Token op;
	int node_value;
	
	Expression()
	{
		left_expr = null;
		right_expr = null;
		op = null;
		node_value = 0;
	}
}

class Term extends Expression
{
	int left_factor;
	int right_factor;
	
	Term()
	{
		super();
		left_factor = 0;
		right_factor = 0; 
	}
}

public class Parser
{
	private static final int FACTOR_OP = 0;
	private static final int TERM_OP = 1;
	private static Token[] tokens;
	private static int i;
	
	public static Expression parse(Token[] tokens)
	{
		Parser.tokens = tokens;
		i = -1;
		Expression expr = expression();
		
		if (tokens[++i].type != Token.END)
			parse_error(tokens[i]);
		return expr; 
	}
	
	private static Expression expression()
	{
		Expression node = new Expression();
		
		node.left_expr = term();
		
		i++;
		if (isOp(tokens[i]) < 2)
		{
			node.op = tokens[i];
			node.right_expr = term();
		}
		else 
			i--;
		return node;
	}
	
	private static Expression term()
	{
		i++;
		if (tokens[i].type == Token.LPAREN)
		{
			Expression node = expression();
			i++;
			if (tokens[i].type != Token.RPAREN)
				parse_error(tokens[i]);
			return node;			
		}
		else if (tokens[i].type == Token.NUM_FACTOR)
		{
			Term node = new Term();
			node.left_factor = tokens[i].value;
			
			i++;
			if (isOp(tokens[i]) == Parser.FACTOR_OP)
			{
				node.op = tokens[i];
				i++;
				
				// factor
				if (tokens[i].type == Token.NUM_FACTOR)
					node.right_factor = tokens[i].value;
					
				// another term
				else if (tokens[i].type == Token.LPAREN)
				{
					
					node.right_expr = expression();
					i++;
					if (tokens[i].type != Token.RPAREN)
						parse_error(tokens[i]);
				}
			}
			else
				i--;
			
			return node;
		}
		else
			parse_error(tokens[i]);
		return null;
	}

	
	private static int isOp(Token t)
	{
		switch(t.type)
		{
			case Token.OP_ADD:
			case Token.OP_SUB:
				return 0;
			case Token.OP_MULT:
			case Token.OP_DIV:
				return 1;
			default:
				return 2;
		}
	}
	
	private static void parse_error(Token t)
	{
		System.out.println("Parse error on " + t);
		Exception e = new Exception();
		e.printStackTrace();
		System.exit(3);
	}
}
