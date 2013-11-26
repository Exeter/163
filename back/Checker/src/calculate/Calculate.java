package calculate;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;

import util.Text;
import _library.LinkedList;
import _library.Queue;
import _library.Stack;
import calculate.operators.CLOSING_PARENTHESIS;
import calculate.operators.COMMA;
import calculate.operators.Divide;
import calculate.operators.Minus;
import calculate.operators.Multiply;
import calculate.operators.OPENING_PARENTHESIS;
import calculate.operators.Plus;
import calculate.structures.Expression;
import calculate.structures.Fraction;
import calculate.structures.Function;
import calculate.structures.Number;
import calculate.structures.Operator;
import calculate.structures.Token;
import calculate.structures.Variable;

final public class Calculate 
{

	//solution properties
	/**
	 * minimum number of decimal places to which answers are displayed
	 */
	final public static int MIN_FLOATING_POINT = 1;
	/**
	 * maximum number of decimal places to which answers are displayed
	 */
	final public static int MAX_FLOATING_POINT = 12;
	/**
	 * default number of decimal places to which answers are displayed
	 */
	final public static int DEFAULT_FLOATING_POINT = MAX_FLOATING_POINT;
	/**
	 * number of decimal places to which answers are displayed
	 */
	private int FLOATING_POINT = DEFAULT_FLOATING_POINT;
	
	/**
	 * defines the output to be approximate (as a decimal)
	 */
	final public static int OUTPUT_APPROXIMATE_MODE = 0;
	/**
	 * defines the output to be exact (as a fraction or whole number) if possible
	 */
	final public static int OUTPUT_EXACT_MODE = 1;
	/**
	 * the initial type of output mode
	 */
	final public static int DEFAULT_OUTPUT_MODE = OUTPUT_EXACT_MODE;
	/**
	 * whether to attempt to produce answers in exact fractional form or not
	 */
	private int OUTPUT_MODE = DEFAULT_OUTPUT_MODE;
	
	final public int getOutputMode()
	{
		return this.OUTPUT_MODE;
	}
	
	final public void setExactOutput()
	{
		this.OUTPUT_MODE = Calculate.OUTPUT_EXACT_MODE;
	}
	
	final public void setApproximateOutput()
	{
		this.OUTPUT_MODE = Calculate.OUTPUT_APPROXIMATE_MODE;
	}
	
	//constants
	/**
	 * opening parenthesis "("
	 */
	final public static OPENING_PARENTHESIS OPENING_PARENTHESIS = new OPENING_PARENTHESIS();
	/**
	 * closing parenthesis ")"
	 */
	final public static CLOSING_PARENTHESIS CLOSING_PARENTHESIS = new CLOSING_PARENTHESIS();
	
	/**
	 * list of predefined functions, such as sine and cosine
	 */
	final private Function[] m_functions = {};
	
	//predefined operators:
	/**
	 * addition
	 */
	final public static Plus PLUS = new Plus();
	/**
	 * subtraction
	 */
	final public static Minus MINUS = new Minus();
	/**
	 * multiplication
	 */
	final public static Multiply MULTIPLY = new Multiply();
	/**
	 * division
	 */
	final public static Divide DIVIDE = new Divide();
	
	/**
	 * list of predefined operators, such as +, --, *, /
	 */
	final private Operator[] m_operators = {PLUS, MINUS, MULTIPLY, DIVIDE};
	
	/**
	 * list of predefined numerical constants such as PI, e
	 */
	final private Number[] m_numericalConstants = {};
	/**
	 * the 10 digits
	 */
	final private String[] m_digits = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
	/**
	 * list of variables that can be modified at any time
	 */
	private ArrayList<Variable> m_variables = new ArrayList<Variable>();
	
	//structures used
	private LinkedList<Token> m_tokenizedExpression = new LinkedList<Token>();
	/**
	 * stack used to temporarily store operations before they are placed into the postfix output
	 */
	private Stack<Token> m_operationsStack = new Stack<Token>();
	/**
	 * queue used to output a given infix expression in postfix
	 */
	private Queue<Token> m_outputQueue = new Queue<Token>();
	
	
	/**
	 * Constructor. creates something that is able to perform calculations
	 */
	public Calculate()
	{
		
	}
	
	/**
	 * stores the output mode state before a temporary reset
	 */
	private int m_savedOutputMode = this.OUTPUT_MODE;
	/**
	 * stores variables before a temporary reset
	 */
	private ArrayList<Variable> m_savedVariables = new ArrayList<Variable>();
	/**
	 * saves the current state (such as variables and output mode) and then resets
	 * so that certain special calculations can take place. afterwards, the state can
	 * be restored by calling <code>restore()</code>
	 */
	final public void temporarilyReset()
	{
		this.m_savedOutputMode = this.OUTPUT_MODE;
		this.m_savedVariables.clear();
		for (int i = 0; i < this.m_variables.size(); i++)
		{
			this.m_savedVariables.add(this.m_variables.get(i));
		}
		resetVariables();
	}
	
	final public void restore()
	{
		this.OUTPUT_MODE = this.m_savedOutputMode;
		this.m_variables.clear();
		for (int i = 0; i < this.m_savedVariables.size(); i++)
		{
			this.m_variables.add(this.m_savedVariables.get(i));
		}
	}

	final public void resetStructures()
	{
		this.m_tokenizedExpression = new LinkedList<Token>();
		this.m_operationsStack = new Stack<Token>();
		this.m_outputQueue = new Queue<Token>();
	}
	
	final public void resetVariables()
	{
		this.m_variables.clear();
	}
	
	final public void addVariable(String variableName, BigDecimal variableValue)
	{
		Variable variableToAdd = new Variable(variableName, variableValue);
		//check if the variable already exists
		int indexOfCurrentVariable = this.m_variables.indexOf(variableToAdd);
		if (indexOfCurrentVariable == -1)
		{
			this.m_variables.add(variableToAdd);
		} else
		{
			this.m_variables.remove(indexOfCurrentVariable);
			this.m_variables.add(variableToAdd);
		}
		if (this.m_variables.contains(variableToAdd))
		{
			this.m_variables.remove(variableToAdd);
			this.m_variables.add(variableToAdd);
		} else
		{
			this.m_variables.add(variableToAdd);
		}
	}
	
	/**
	 * evaluates a given expression and returns the result. 
	 *
	 * @param expression				expression to evaluate
	 * @return							the simplified value of the expression
	 * @throws InvalidInputException	if the input was syntactically incorrect
	 * @throws RuntimeException			if the input was syntactically incorrect
	 */
	final public String evaluate(String expression) throws InvalidInputException, RuntimeException
	{
		resetStructures();
		tokenizeExpression(expression);
		insertImplicitMultiplication();
		convertToPostfix();
		int previousFunctionMode = Function.outputMode;
		int previousOperatorMode = Operator.outputMode;
		if (this.OUTPUT_MODE == Calculate.OUTPUT_APPROXIMATE_MODE && Function.outputMode == Calculate.OUTPUT_EXACT_MODE)
		{
			Function.outputMode = Calculate.OUTPUT_APPROXIMATE_MODE;
			Operator.outputMode = Calculate.OUTPUT_APPROXIMATE_MODE;
		}
		Expression result = evaluatePostfix();
		Function.outputMode = previousFunctionMode;
		Operator.outputMode = previousOperatorMode;
		if (this.OUTPUT_MODE == Calculate.OUTPUT_EXACT_MODE)
		{
			if (result instanceof Number)
			{
				if (result instanceof Fraction)
				{
					return ((Fraction) result).getExactFractionalValue();
				} else
				{
					return trimZeroes(( (Number) result ).getRepresentation());
				}
			} else
			{
				return trimZeroes(result.getRepresentation());
			}
		} else
		{
			if (result instanceof Number)
			{
				 BigDecimal rtn = (( (Number) result ).getValue().round(new MathContext(this.FLOATING_POINT)));
				 //if the result is less than 10^(-floating point), then round it to zero
				 rtn = rtn.setScale(this.FLOATING_POINT, RoundingMode.HALF_UP);
				 return	trimZeroes(rtn.stripTrailingZeros().toPlainString());
			} else
			{
				return result.getRepresentation();
			}
		}
	}
	
	final public void tokenizeExpression(String expression) throws InvalidInputException
	{
		//represent the start of a number token
		int startOfNumber = 0;
		//represent the end of a number token
		int endOfNumber = 0;
		//go through the expression
		for (int expressionIndex = 0; expressionIndex < expression.length();)
		{
			//assume we are at the start of a normal number token, such as 9 or 10
			//(not predefined, such as e or pi)
			boolean isNumber = true;
			//check if we are actually at the start of a predefined number
			//go through all the predefined numbers we know if
			for (int predefNumberIndex = 0; predefNumberIndex < this.m_numericalConstants.length; predefNumberIndex++)
			{
				Number aPredefinedNumber = this.m_numericalConstants[predefNumberIndex];
				int lengthOfPredefinedNumberRepresentation = aPredefinedNumber.getRepresentation().length();
				//make sure not to go out of bounds when checking
				if (expressionIndex + lengthOfPredefinedNumberRepresentation <= expression.length())
				{
					if (aPredefinedNumber.getRepresentation().equals(expression.substring(expressionIndex, expressionIndex + lengthOfPredefinedNumberRepresentation)))
					{
						isNumber = false;
						//see if there was a number before this predefined number
						endOfNumber = expressionIndex;
						createAndAddNumber(expression.substring(startOfNumber, endOfNumber));
						//add the predefined number to the tokenized expression
						addTokenToTokenizedExpression(aPredefinedNumber);
						//move the start of the next number to the end of this operator in the expression
						startOfNumber = expressionIndex + lengthOfPredefinedNumberRepresentation;
						//skip past this predefined number in the expression
						expressionIndex += lengthOfPredefinedNumberRepresentation;
					}
				}
			}
			//check if we are actually at the start of a function
			//go through all the functions we know of
			for (int functionIndex = 0; functionIndex < this.m_functions.length; functionIndex++)
			{
				Function aFunction = this.m_functions[functionIndex];
				int lengthOfFunctionRepresentation = aFunction.getRepresentation().length();
				//make sure not to go out of bounds
				if (expressionIndex + lengthOfFunctionRepresentation <= expression.length())
				{
					if (aFunction.getRepresentation().equals(expression.substring(expressionIndex, expressionIndex + lengthOfFunctionRepresentation)))
					{
						//we are no longer at the start of a number token
						isNumber = false;
						endOfNumber = expressionIndex;
						createAndAddNumber(expression.substring(startOfNumber, endOfNumber));
						//see if there was a number before this function
						//add the function to the tokenized expression
						addTokenToTokenizedExpression(aFunction);
						//move the start of the next number to the end of this operator in the expression
						startOfNumber = expressionIndex + lengthOfFunctionRepresentation;
						//skip past this function in the expression
						expressionIndex += lengthOfFunctionRepresentation;
					}
				}
			}
			//check if we are actually at the start of an operator
			for (int operatorIndex = 0; operatorIndex < this.m_operators.length; operatorIndex++)
			{
				Operator anOperator = this.m_operators[operatorIndex];
				int lengthOfOperatorRepresentation = anOperator.getRepresentation().length();
				//make sure not to go out of bounds
				if (expressionIndex + lengthOfOperatorRepresentation <= expression.length())
				{
					if (anOperator.getRepresentation().equals(expression.substring(expressionIndex, expressionIndex + lengthOfOperatorRepresentation)))
					{
						//we are no longer at the start of a number token
						isNumber = false;
						//the end of the previous number token must be the start of this functiontoken
						endOfNumber = expressionIndex;
						//there must have been a number before this operator
						createAndAddNumber(expression.substring(startOfNumber, endOfNumber));
						//add the operator to the tokenized expression
						addTokenToTokenizedExpression(anOperator);
						//move the location of the start of the next number to the end of this operator in
						//the inputed expression
						startOfNumber = expressionIndex + lengthOfOperatorRepresentation;
						//move the current location in the expression to the end of the operator
						expressionIndex += lengthOfOperatorRepresentation;
					}
				}
			}
			/* COMMA Not Supported in 163
			//check if we are at a comma
			String commaRepresentation = Calculate.COMMA.getRepresentation();
			if (expressionIndex + commaRepresentation.length() <= expression.length())
			{
				if (commaRepresentation.equals(expression.substring(expressionIndex, expressionIndex + commaRepresentation.length())))
				{
					//we are not at the start of a number token
					isNumber = false;
					//see if there was a number before this comma
					endOfNumber = expressionIndex;
					createAndAddNumber(expression.substring(startOfNumber, endOfNumber));
					//add the comma to the tokenized expression
					addTokenToTokenizedExpression(Calculate.COMMA);
					//move the start of the next number to the end of this comma
					startOfNumber = expressionIndex + commaRepresentation.length();
					//move the current location in the expression to the end of this comma
					expressionIndex += commaRepresentation.length();
				}
			}*/
			//check if we are at an opening parenthesis
			String openingParenthesisRepresentation = Calculate.OPENING_PARENTHESIS.getRepresentation();
			//make sure not to go out of bounds
			if (expressionIndex + openingParenthesisRepresentation.length() <= expression.length())
			{
				if (openingParenthesisRepresentation.equals(expression.substring(expressionIndex, expressionIndex + openingParenthesisRepresentation.length())))
				{
					//we are not at the start of a number token
					isNumber = false;
					//see if there was a number before this open parenthesis
					endOfNumber = expressionIndex;
					createAndAddNumber(expression.substring(startOfNumber, endOfNumber));
					//add the opening parenthesis to the tokenized expression
					addTokenToTokenizedExpression(Calculate.OPENING_PARENTHESIS);
					//move the start of the next number to the end of this parenthesis
					startOfNumber = expressionIndex + openingParenthesisRepresentation.length();
					//move the current location in the expression to the end of this parenthesis
					expressionIndex += openingParenthesisRepresentation.length();
				}
			}
			//check if we are at a closing parenthesis
			String closingParenthesisRepresentation = Calculate.CLOSING_PARENTHESIS.getRepresentation();
			//make sure not to go out of bounds
			if (expressionIndex + closingParenthesisRepresentation.length() <= expression.length())
			{
				if (closingParenthesisRepresentation.equals(expression.substring(expressionIndex, expressionIndex + closingParenthesisRepresentation.length())))
				{
					//we are not at the start of a number token
					isNumber = false;
					//see if there was a number before this close parenthesis
					endOfNumber = expressionIndex;
					createAndAddNumber(expression.substring(startOfNumber, endOfNumber));
					//add the closing parenthesis to the tokenized expression
					addTokenToTokenizedExpression(Calculate.CLOSING_PARENTHESIS);
					//move the start of the next number to the end of this parenthesis
					startOfNumber = expressionIndex + closingParenthesisRepresentation.length();
					//move the current location in the expression to the end of this parenthesis
					expressionIndex += closingParenthesisRepresentation.length();
				}
			}
			if (isNumber)
			{
				expressionIndex++;
				endOfNumber = expressionIndex;
			}
		}
		createAndAddNumber(expression.substring(startOfNumber, expression.length()));
	}
	
	final private void createAndAddNumber(String number) throws InvalidInputException
	{
		//see if we were given a valid number
		try
		{
			if (!number.trim().equals(""))
			{
				if (this.OUTPUT_MODE == Calculate.OUTPUT_EXACT_MODE)
				{
					Fraction newNumber = new Fraction(new BigDecimal(number.trim()), Fraction.ONE_DENOMINATOR);
					addTokenToTokenizedExpression(newNumber);
				} else
				{
					Number newNumber = new Number(number);
					addTokenToTokenizedExpression(newNumber);
				}
			}
		} catch (NumberFormatException invalidNumber)
		{
			//if the number was invalid, see if there's a variable somewhere in there
			int startOfNumber = 0;
			int endOfNumber = 0;
			int startOfVariable = 0;
			int endOfVariable = 0;
			boolean readingInNumber = true;
			for (int expressionIndex = 0; expressionIndex < number.length(); expressionIndex++)
			{
				if (isDigit(number.substring(expressionIndex, expressionIndex + 1)))
				{
					if (!readingInNumber)
					{
						readingInNumber = true;
						endOfVariable = expressionIndex;
						createAndAddVariable(number.substring(startOfVariable, endOfVariable).trim());
					}
					startOfVariable = expressionIndex + 1;
				} else
				{
					if (readingInNumber)
					{
						readingInNumber = false;
						endOfNumber = expressionIndex;
						createAndAddNumber(number.substring(startOfNumber, endOfNumber).trim());
					}
					startOfNumber = expressionIndex + 1;
				}
			}
			if (readingInNumber)
			{
				createAndAddNumber(number.substring(startOfNumber, number.length()).trim());
			} else
			{
				createAndAddVariable(number.substring(startOfVariable, number.length()).trim());
			}
		}
	}
	
	/**
	 * determines and adds the correct variable value to the tokenized expression
	 * 
	 * @param variableRepresentation		how the variable is represented
	 * @throws InvalidInputException		if the variable is not known, i.e. if it was never
	 * 										added to the list of known variables
	 */
	final private void createAndAddVariable(String variableRepresentation) throws InvalidInputException
	{
		//ignore empty variable names 
		if (variableRepresentation.trim().equals(""))
		{
			return;
		}
		//find the variable and then add it to the tokenized expression
		for (int variableIndex = 0; variableIndex < this.m_variables.size(); variableIndex++)
		{
			Variable aVariable = this.m_variables.get(variableIndex);
			if (variableRepresentation.equals(aVariable.getRepresentation()))
			{
				this.m_tokenizedExpression.addElement(new Variable(variableRepresentation, aVariable.getValue()));
				return;
			}
		}
		throw new InvalidInputException(Text.CALCULATIONS.getInvalidNumberMessage(variableRepresentation));
	}
	
	final private boolean isDigit(String strToCheck)
	{
		for (int digitIndex = 0; digitIndex < this.m_digits.length; digitIndex++)
		{
			if (strToCheck.equals(this.m_digits[digitIndex]))
			{
				return true;
			}
		}
		return false;
	}
	
	final private void addTokenToTokenizedExpression(Token aToken)
	{
		this.m_tokenizedExpression.addElement(aToken);
	}
	
	/**
	 * inserts implicit multiplication signs into a tokenized expression. for example,
	 * the expression 5(6+7) would be changed to 5*(6+7).
	 */
	final private void insertImplicitMultiplication()
	{
		this.m_tokenizedExpression.moveToStart();
		while (this.m_tokenizedExpression.hasNext())
		{
			Token currentToken = this.m_tokenizedExpression.getCurrentElement();
			//check to see if the current token is a number
			if (currentToken instanceof Number)
			{
				//if current token is a number and a parenthesis, number or function directly follows
				//then insert the implicit multiplication
				if (this.m_tokenizedExpression.hasNext())
				{
					Token nextToken = this.m_tokenizedExpression.peek();
					if (nextToken instanceof OPENING_PARENTHESIS || nextToken instanceof Number || nextToken instanceof Function)
					{
						this.m_tokenizedExpression.insert(Calculate.MULTIPLY);
					}
				}
			}
			if (currentToken instanceof CLOSING_PARENTHESIS)
			{
				//if current token is a close parenthesis and an open parenthesis, number or function
				//directly follows, then insert the implicit multiplication
				if (this.m_tokenizedExpression.hasNext())
				{
					Token nextToken = this.m_tokenizedExpression.peek();
					if (nextToken instanceof OPENING_PARENTHESIS || nextToken instanceof Number || nextToken instanceof Function)
					{
						this.m_tokenizedExpression.insert(Calculate.MULTIPLY);
					}
				}
			}
			this.m_tokenizedExpression.advance();
		}
	}
	
	/**
	 * converts the current tokenized expression to postfix. assumes that there
	 * are already tokens in <code>m_tokenizedExpression</code>.
	 * 
	 * @return			the infix expression converted to postfix
	 */
	final private void convertToPostfix() throws InvalidInputException
	{
		this.m_tokenizedExpression.moveToStart();
		//read through all the tokens
		for (; this.m_tokenizedExpression.hasCurrent(); this.m_tokenizedExpression.advance())
		{
			Token nextToken = this.m_tokenizedExpression.getCurrentElement();
			//check if token is a function
			if (nextToken instanceof Function)
			{
				pushOperation(nextToken);
			//check if token is an operator
			} else if (nextToken instanceof Operator)
			{
				//if token is an operator
				//check if token is a comma
				if (nextToken instanceof COMMA)
				{
					//if token is a comma,
					//remove operators from the operations stack until an opening
					//parenthesis is at the top
					while (!(this.m_operationsStack.peek() instanceof OPENING_PARENTHESIS))
					{
						pushOutput(this.m_operationsStack.pop());
					}
				//check if token is an opening parenthesis
				} else if (nextToken instanceof OPENING_PARENTHESIS)
				{
					//push the token onto the operations stack
					pushOperation(nextToken);
				//check if token is a closing parenthesis
				} else if (nextToken instanceof CLOSING_PARENTHESIS)
				{
					//until an opening parenthesis is removed from the
					//operations stack, continue to pop operations and
					//add the to the output
					Token aPreviousOperation = this.m_operationsStack.pop();
					while (!(aPreviousOperation instanceof OPENING_PARENTHESIS))
					{
						pushOutput(aPreviousOperation);
						if (this.m_operationsStack.hasNext())
						{
							aPreviousOperation = this.m_operationsStack.pop();
						} else
						{
							throw new InvalidInputException(Text.CALCULATIONS.MISSING_OPEN_PARENTHESIS);
						}
					}
					if (this.m_operationsStack.hasNext())
					{
						if (this.m_operationsStack.peek() instanceof Function)
						{
							pushOutput(this.m_operationsStack.pop());
						}
					}
				} else
				{
					addOperator((Operator) nextToken);
				}
			//check if token is a number
			} else if (nextToken instanceof Number)
			{
				//add the number to the output
				pushOutput(nextToken);
			}
		}
		while (this.m_operationsStack.hasNext())
		{
			this.m_outputQueue.push(this.m_operationsStack.pop());
		}
	}
	
	/**
	 * given an operator, adds it to either the operations stack or the output stack,
	 * based on its priority
	 * 
	 * @param anOperator			the operator to add
	 */
	final private void addOperator(Operator anOperator)
	{
		//make sure the operator is valid
		if (!anOperator.getRepresentation().equals(""))
		{
			int thisOperatorPriority = anOperator.getPriority();
			int previousOperationPriority = -1;
			Token previousOperation = this.m_operationsStack.peek();
			if (previousOperation instanceof Operator)
			{
				previousOperationPriority = ((Operator) previousOperation).getPriority();
				while (previousOperation instanceof Operator && ((thisOperatorPriority < previousOperationPriority) || (anOperator.hasLeftAssociativity() && thisOperatorPriority <= previousOperationPriority)))
				{
					pushOutput(this.m_operationsStack.pop());
					previousOperation = this.m_operationsStack.peek();
					if (previousOperation instanceof Operator)
					{
						previousOperationPriority = ((Operator) previousOperation).getPriority();
					}
				}
			}
			pushOperation(anOperator);
		}
	}
	
	final private void pushOperation(Token anOperation)
	{
		assert anOperation instanceof Function || anOperation instanceof Operator;
		this.m_operationsStack.push(anOperation);
	}
	
	final private void pushOutput(Token anOutputToken)
	{
		//make sure we are outputting valid tokens
		if (!anOutputToken.getRepresentation().equals(""))
		{
			this.m_outputQueue.push(anOutputToken);
		}
	}
	
	final private Expression evaluatePostfix() throws InvalidInputException
	{
		Stack<Expression> expressionStack = new Stack<Expression>();
		while (this.m_outputQueue.hasNext())
		{
			Token nextToken = this.m_outputQueue.pop();
			if (nextToken instanceof Number)
			{
				expressionStack.push((Number) nextToken);
			} else if (nextToken instanceof Function)
			{
				int numberOfArguments = ((Function) nextToken).getNumberOfArguments();
				Expression[] arguments = new Expression[numberOfArguments];
				for (int argIndex = numberOfArguments - 1; argIndex >= 0; argIndex--)
				{
					arguments[argIndex] = expressionStack.pop();
				}
				Expression functionResult = ((Function) nextToken).evaluate(arguments);
				expressionStack.push(functionResult);
			} else if (nextToken instanceof Operator)
			{
				//should not read any opening parenthesis or else there is a syntax error
				if (nextToken instanceof OPENING_PARENTHESIS)
				{
					throw new InvalidInputException(Text.CALCULATIONS.MISSING_CLOSE_PARENTHESIS);
				}
				Expression secondOperand = expressionStack.pop();
				Expression firstOperand = expressionStack.pop();
				Expression operationResult = ((Operator) nextToken).evaluate(firstOperand, secondOperand);
				expressionStack.push(operationResult);
			}
		}
		Expression result = expressionStack.pop();
		if (expressionStack.hasNext())
		{
			throw new InvalidInputException(Text.CALCULATIONS.SYNTAX_ERROR);
		}
		return result;
	}
	
	/**
	 * removes trailing zeroes from an output
	 * 
	 * @param output		output of a calculation
	 * @return				the output of the calculation without trailing zeroes
	 */
	final private static String trimZeroes(String output)
	{
		String copyOfOutput = output;
		if (copyOfOutput.contains("."))
		{
			while (copyOfOutput.substring(copyOfOutput.length() - 1, copyOfOutput.length()).equals("0"))
			{
				if (copyOfOutput.length() > 1)
				{
					copyOfOutput = copyOfOutput.substring(0, copyOfOutput.length() - 1);
				} else
				{
					break;
				}
			}
			if (copyOfOutput.substring(copyOfOutput.length() - 1, copyOfOutput.length()).equals("."))
			{
				copyOfOutput = copyOfOutput.substring(0, copyOfOutput.length() - 1);
			}
		}
		return copyOfOutput;
	}
	
	//Test
	/*
	final public static void main(String[] args)
	{
		Calculate c = new Calculate();
		c.setExactOutput();
		Scanner s = new Scanner(System.in);
		//Calculate.addVariable("x", new BigDecimal("5"));
		try
		{
			String result = c.evaluate(s.nextLine());
			System.out.println("REUSLT:" + result);
		} catch (InvalidInputException e)
		{
			System.out.println(e.getErrorMessage());
			e.printStackTrace();
		} catch ( Exception e ) {
			System.out.println( "Failed to evaluate expression" );
			e.printStackTrace();
		}
		//Fraction f = new Fraction(new BigDecimal("100"), new BigDecimal("13"));
		//System.out.println("RESULT: " + f.mod(new Number(new BigDecimal("-2.5000"))).getExactValue());
	}//*/
	
	/*
	//Unit Test
	final public static void main( String[] args ) throws IOException {
		BufferedReader f = new BufferedReader( new FileReader( "calculate_unit_tests.txt" ) );
		
		Calculate c = new Calculate();
		c.setExactOutput();
		
		String nextLine;
		while ( ( nextLine = f.readLine() ) != null ) {
			nextLine = nextLine.trim();
			if ( nextLine.startsWith( "//" ) || nextLine.equals( "" ) ) {
				//ignore comment
			} else {
				StringTokenizer st = new StringTokenizer( nextLine );
				String expression = st.nextToken();
				String expectedOutput = st.nextToken();
				
				try {
					String programOutput = c.evaluate( expression );
					if ( expectedOutput.equals( programOutput ) ) {
						//ignore
					} else {
						f.close();
						throw new RuntimeException( "Test: " + expression + "\n Expected: " + expectedOutput + ", Found: " + programOutput );
					}
				} catch ( Exception e ) {
					if ( expectedOutput.equals( "-" ) ) {
						//ignore
					} else {
						f.close();
						throw new RuntimeException( "Test: " + expression + "\n Expected: " + expectedOutput + " Found: Exception" );
					}
				}
			}
		}
		System.out.println( "Unit Tests Finished" );
		f.close();
		System.exit( 0 );
	}//*/
	
	/**
	 * runs this calculate program which determines if the the expression
	 * given evaluates to 163, and that it uses six specific numbers
	 * 
	 * @param args		six numbers, that are the cards in indecies 0-5, and a
	 * 					mathematical expression in index 6
	 */
	final public static void main( String[] args ) {
		
		//determine the cards that the user is required to use in his/her
		//163 expression
		ArrayList < Integer > cardsRequiredToBeUsed = new ArrayList < Integer > ();
		for ( int i = 0 ; i <= 5 ; i ++ ) {
			cardsRequiredToBeUsed.add( Integer.valueOf( args[ i ] ) );
		}
		
		//determine the expression inputed by the user
		String expression = args[ 6 ];
		
		//determine all the cards the user used
		String[] cards = expression.split("[+\\-*/()]");
		for ( int i = 0 ; i < cards.length ; i ++ ) {

			try {
				
				//convert card value to an integer value
				Integer cardValue = Integer.valueOf( cards[ i ] );
				
				//the user has used this card, so remove it from the list of cards
				//required to be used
				cardsRequiredToBeUsed.remove( cardValue );
			} catch ( NumberFormatException e ) {
				
				//ignore no numerical things
			}
		}
		
		//if the user has not used all the cards, then the user's expression is wrong
		if ( cardsRequiredToBeUsed.size() > 0 ) {
			System.err.println( "Did not use all 6 cards" );
			System.exit( 1 );
		}
		
		String expectedOutput = "163";
		Calculate c = new Calculate();
		c.setExactOutput();
		
		//check that the user's expression evaluates to 163
		try {
			String evaluateOutput = c.evaluate( expression );
			if ( evaluateOutput.equals( expectedOutput ) ) {
				System.exit( 0 );
			} else {
				System.err.println( "Expression does not evaluate to 163" );
				System.exit( 1 );
			}
		} catch ( Exception e ) {
			System.err.println( "Invalid mathematical expression" );
			System.exit( 1 );
		}
	}
}
