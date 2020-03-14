import java.util.*;
import java.io.*;

public class CalculatorCompiler
{
	public static void main(String[] args)
	{
		if (args.length < 1 || args.length > 2)
		{
			System.out.println("Incorrect program usage");
			System.exit(1);
		}
		
		String file;
		
		if (args.length == 2)
			file = args[1];
		else
			file = args[0];
		
		Token[] tokens = null;
		try 
		{
			tokens = Lexer.lex(file);
		} 
		catch(IOException ioe) {
			System.out.println("Could not read file");
			System.exit(2);
		}
		
		if (args[0].equals("--lex"))
		{
			printTokens(tokens);
			return;
		}
		
		Expression head = Parser.parse(tokens);
		
		if (args[0].equals("--parse"))
		{
			printAST(head);
			return;
		}
		
		System.out.println("Value: " + Evaluator.eval(head));
		
		
	}
	
	private static void printTokens(Token[] tokens)
	{
		for (int i=0; i < tokens.length; i++)
		{
			System.out.println(tokens[i]);
		}
	}
	
	private static void printAST(Expression head)
	{
		printExpression(head, 0);
	}
	
	private static void printExpression(Expression expr, 
	int depth)
	{
		printIndent(depth);
		System.out.println("Expression");
		
		if (expr.left_expr.getClass().getName()
		.equals("Expression"))
			printExpression(expr.left_expr, depth+1);
		else
			printTerm((Term)expr.left_expr, depth+1);
		
		if (expr.right_expr == null) return;
		printIndent(depth+1);
		System.out.println("OP: " + expr.op);
		if (expr.right_expr.getClass().getName()
		.equals("Expression"))
			printExpression(expr.right_expr, depth+1);
		else
			printTerm((Term)expr.right_expr, depth+1);
	}
	
	private static void printTerm(Term term, int depth)
	{
		printIndent(depth);
		System.out.println("Term");
		if (term.left_expr != null && term.left_expr
		.getClass().getName().equals("Expression"))
			printExpression(term.left_expr, depth+1);
		else if (term.left_expr != null)
			printTerm((Term)term.left_expr, depth+1);
		printIndent(depth+1);
		System.out.println("left_factor: " + 		 	
		                   term.left_factor);
		
		
		if (term.right_expr == null && 
		term.right_factor == 0)
			return;
		printIndent(depth+1);
		System.out.println("OP: " + term.op); 
		if (term.right_expr != null && term.right_expr
		.getClass().getName().equals("Expression"))
			printExpression(term.right_expr, depth+1);
		else if (term.right_expr != null)
			printTerm((Term)term.right_expr, depth+1);
		printIndent(depth+1);
		System.out.println("right_factor: " +
		                   term.right_factor);
	}
	
	private static void printIndent(int depth)
	{
		for (int i=0; i < depth; i++)
			System.out.print(" |");
	}
}



