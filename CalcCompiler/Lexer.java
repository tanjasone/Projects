import java.io.*;
import java.util.*;

class Token
{
	public static final int OP_ADD = 1;
	public static final int OP_SUB = 2;
	public static final int OP_MULT = 3;
	public static final int OP_DIV = 4;
	public static final int NUM_FACTOR = 5;
	public static final int LPAREN = 6;
	public static final int RPAREN = 7;
	public static final int END = 8;

	public int type;
	public int value;
	public int idx;

	public Token(int idx, int type)
	{
		this.idx = idx;
		this.type = type;
	}

	public Token(int idx, int type, int value)
	{
		this.idx = idx;
		this.type = type;
		this.value = value;
	}

	public String toString()
	{
		String str = "[" + idx + "]";

		switch(type)
		{
			case 1:
				str += "ADD";
				break;
			case 2:
				str += "SUB";
				break;
			case 3:
				str += "MULT";
				break;
			case 4:
				str += "DIV";
				break;
			case 5:
				str += "NUM " + value;
				break;
			case 6:
				str += "LPAREN";
				break;
			case 7:
				str += "RPAREN";
				break;
			case 8:
				str += "END";
				break;
		}

		return str;
	}
}

public class Lexer
{
	public static Token[] lex(String filename) throws IOException
	{
		FileReader fr = new FileReader(filename);
		Token[] tokens = new Token[512];
		char c;
		String buffer;
		int i = 0;

		c = (char)fr.read();
		while (true)
		{
			buffer = "";
			if (Character.isDigit(c))
			{
				while (Character.isDigit(c))
				{
					buffer += c;
					c = (char)fr.read();
				}
				tokens[i++] = new Token(i, Token.NUM_FACTOR, Integer.parseInt(buffer));
			}
			if (c == '+') {
				tokens[i++] = new Token(i, Token.OP_ADD);
				c = (char)fr.read();
			}
			if (c == '-') {
				tokens[i++] = new Token(i, Token.OP_SUB);
				c = (char)fr.read();
			}
			if (c == '*') {
				tokens[i++] = new Token(i, Token.OP_MULT);
				c = (char)fr.read();
			}
			if (c == '/') {
				tokens[i++] = new Token(i, Token.OP_DIV);
				c = (char)fr.read();
			}
			if (c == '(') {
				tokens[i++] = new Token(i, Token.LPAREN);
				c = (char)fr.read();
			}
			if (c == ')') {
				tokens[i++] = new Token(i, Token.RPAREN);
				c = (char)fr.read();
			}
			if (c == '\n') {
				tokens[i++] = new Token(i, Token.END); 
				break;
			}
			if (Character.isWhitespace(c)) {
				c = (char)fr.read();
			}
		}
		
		return Arrays.copyOf(tokens, i);
	}
}



