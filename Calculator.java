/**@author Oksana Yaremchuk
 *         November 24, 2008
 */
//import java.util.StringTokenizer;
import java.util.*;

public class Calculator 
{
    private ArrayStack<Character> stack_arr = new ArrayStack<Character>();

    private ArrayStack<Double> stack_int = new ArrayStack<Double>();

    final String  VALID_SYMBOLS = "()1234567890^/*+-\t ";

    final String OPERATORS = "+-*/^";

	
    /**public String preprocessing(String expression)
    * Preprocess the expression to remove the white spaces and to include the multiply
    * sign where it missed.
    * @param String expression - the given expression passed to the function.
    * @return return the processed expression.
    *
    * */
    public String preprocessing(String expression)
    {
        String st =null;
        try
        {
            if(expression.contains(" "))
            {
                expression = expression.replace(" ", "");
            }

            for(int i = 1; i < expression.length(); i++ )
            {
                if(VALID_SYMBOLS.indexOf(expression.substring(i-1,i)) >= 0)
                {
                    if(expression.substring(i,i+1).contains("(")     &&
                        !expression.substring(i-1, i).contains("+")  &&
                        !expression.substring(i-1, i).contains("(")  &&
                        !expression.substring(i-1, i).contains("-")  &&
                        !expression.substring(i-1, i).contains("*")  &&
                        !expression.substring(i-1,i).contains("/"))
                      {
                        st = new String(expression);
                        st = new StringBuffer(st).insert(i, "*").toString();
                        expression=st;

                      }else if( expression.substring(i,i+1).contains(")") &&
                                i!=expression.length()-1)
                      {
                            if( !expression.substring(i+1,i+2).contains("+") &&
                                !expression.substring(i+1,i+2).contains(")") &&
                                !expression.substring(i+1,i+2).contains("-") &&
                                !expression.substring(i+1,i+2).contains("*") &&
                                !expression.substring(i+1,i+2).contains("/"))
                             {
                                st = new String(expression);
                                st = new StringBuffer(st).insert(i+1, "*").toString();
                                expression=st;

                             }//if prev
                      }//if
                }else{
                    throw new IllegalStateException();
                }

            }//for loop

            return expression;

        }// try

        catch(IllegalStateException  e)
        {
             System.out.println("Error! The expression has illegal characters.");

             return null;
        }

    }//Preprocessing method
	
	
 
    /**String convert(String expres)
    * Convert the processed expression into postfix expression.
    * @param String expres - the given processed expression.
    * @return returns postfix expression.
    */
    public String convert(String expres)
    {
        StringBuilder postfix = new StringBuilder();
	 
        char[] char_array = new char[80];
	 
        char_array = expres.toCharArray();

        for(int i=0; i<char_array.length; i++)
        {
            if(Character.isDigit(char_array[i]))
            {
                postfix.append(char_array[i]);
            }else{
                postfix.append(" ");

                if(char_array[i]=='(')
                {
                    stack_arr.push(char_array[i]);

                }else if(precedence(char_array[i])>0){

                    while(!stack_arr.empty() && precedence(stack_arr.peek())>=precedence(char_array[i]))
                    {
                        postfix.append(stack_arr.pop());
                        postfix.append(" ");
                    }

                    stack_arr.push(char_array[i]);

                }else if (char_array[i]==')'){

                    if(stack_arr.empty())
                    {
                            return "";
                    }else if(!stack_arr.empty() && stack_arr.peek()=='('){

                        stack_arr.pop();

                    }else{

                        while(!stack_arr.empty() && stack_arr.peek()!='(')
                        {
                            postfix.append(stack_arr.pop());
                            postfix.append(" ");
                        }

                        if(stack_arr.empty())
                        {
                            return "";

                        }else if(stack_arr.peek().equals('(')){
                            stack_arr.pop();
                        }
                    }//else

                }//else

            }//else
		
        }//for loop
 
	while(!stack_arr.empty())
	{
            if(stack_arr.peek()=='(')
            {
                return "";

            }else{

                postfix.append(" ");
            }

            postfix.append(stack_arr.pop());

	}//while
	
	return postfix.toString();

    }//convert
	
    /**int precedence(char ch)
    * Takes char as a argument to the function and evaluate its precedence: 1 if '+' or '-'
    * 2 if '*' or '/' and 3 if '^' (^ stands for unary sign). Also gives "precedence" to open
    * parenthesis '('
    * @param char ch - the given character to be evaluated.
    * @return returns integer value (precedence) of the given character.
    */
    private int precedence(char ch)
    {
         if(ch == '+' || ch == '-'){ return 1;}

         else if(ch == '*' || ch == '/'){return 2;}

         else if(ch== '('){return 0;}

         else if(ch== '^') return 3;

         else return -1;

    }//precedence

 
    /**double evaluation(String postfix_expr)
    * Evaluate the postfix expression.
    * @param String postfix_expr - given postfix expression to be evaluated.
    * @return returns a double value of the expression.
    */
    public double evaluation(String postfix_expr)
    {
        double answer = 0;

        StringTokenizer st = new StringTokenizer(postfix_expr);

        while(st.hasMoreTokens())
        {
            String temp_token = st.nextToken();

            if( Character.isDigit(temp_token.charAt(0)))
            {
               Double operand = (Double.parseDouble(temp_token));

                stack_int.push(operand);

            }else if(isOperator(temp_token.charAt(0))){

                double result = evaluateExpr(temp_token.charAt(0));

                stack_int.push(result);

            }//else

        }//while

        answer = stack_int.pop();

        if(stack_int.empty())
        {
             return answer;
        }

        return -1;

    }//evaluation


    /**boolean isOperator(char ch)
    * Determines weather a character is an operator.
    * @param char ch - the character to be tested.
    * @return true if the character is an operator.
    */
    private boolean isOperator(char ch)
    {
         return OPERATORS.indexOf(ch) != -1;

    }//isOperator

 
 
    /**evaluateExpr(char oper)
    * Evaluates the current operation.
    * this function pops two operands off the operand stack and aplies the operator.
    * @param  char oper - a character representing the operator.
    * @return returns result of applying operator.
    */
    private double evaluateExpr(char oper)
    {
        double d1_value = 0;
        double d2_value = 0;
        double result = 0;

        switch (oper)
        {
            case '+': d1_value = stack_int.pop();
                      d2_value = stack_int.pop();
                      result = d1_value + d2_value;
                      break;
            case '-': d1_value = stack_int.pop();
                      d2_value = stack_int.pop();
                      result = d2_value - d1_value;
                      break;
            case '*': d1_value = stack_int.pop();
                      d2_value = stack_int.pop();
                      result = d1_value * d2_value;
                      break;
            case '/': d1_value = stack_int.pop();
                      d2_value = stack_int.pop();
                      result =  d2_value / d1_value;
                      break;
            case '^': d1_value = stack_int.pop();
                      result = - d1_value ;
                      break;
        }//switch

        return result;

    }//evaluateExpr
	
	
	
	
    public static void main(String[] args)
    {
        Calculator calc = new Calculator();
        String str_1 = "^3*154 (12 + 3)";
        System.out.println("Original: " +str_1);
        String t = calc.preprocessing(str_1);

        if(t!= null)
        {
            System.out.println("Preprocessed: " +t);
            String you_0 = calc.convert(t);

            if(you_0 != "")
            {
                System.out.println("Postfix: " +you_0);
                double our_0 = calc.evaluation(you_0);
                System.out.println("The value: " +our_0);

            }else if(you_0 == ""){

                System.out.println("Unbalanced paranthesis!");
            }
        }//if

        System.out.println();
        String str_2 = "12/(43-40)2";
        System.out.println("Original: " +str_2);
        String n = calc.preprocessing(str_2);

        if(n!= null)
        {
            System.out.println("Preprocessed: " +n);
            String y = calc.convert(n);

            if(y != "")
            {
                System.out.println("Postfix: " +y);
                double o = calc.evaluation(y);
                System.out.println("The value: " +o);

            }else if(y == ""){

                System.out.println("Unbalanced paranthesis!");
            }
        }//if

        System.out.println();
        String str_3 = "1+2+3+4+5";
        System.out.println("Original: " +str_3);

        if(calc.preprocessing(str_3)!=null)
        {
            System.out.println("Preprocessed: " +calc.preprocessing(str_3));
            if(calc.convert(calc.preprocessing(str_3))!=null)
            {
                System.out.println("Postfix: " +calc.convert(calc.preprocessing(str_3)));
                System.out.println("The value: " +calc.evaluation(calc.convert(calc.preprocessing(str_3))));
            }
        }//if

        System.out.println();
        String str_4 = "(45-(^12)-5)";
        System.out.println("Original: " +str_4);

        if(calc.preprocessing(str_4)!=null)
        {
            System.out.println("Preprocessed: " +calc.preprocessing(str_4));
            if(calc.convert(calc.preprocessing(str_4))!=null)
            {
                System.out.println("Postfix: " +calc.convert(calc.preprocessing(str_4)));
                System.out.println("The value: " +calc.evaluation(calc.convert(calc.preprocessing(str_4))));
            }
        }//IF

        System.out.println();
        String str_5 = "15/3+7*(13-5)";
        System.out.println("Original: " +str_5);

        if(calc.preprocessing(str_5)!=null)
        {
            System.out.println("Preprocessed: " +calc.preprocessing(str_5));

            if(calc.convert(calc.preprocessing(str_5))!=null)
            {
                System.out.println("Postfix: " +calc.convert(calc.preprocessing(str_5)));
                System.out.println("The value: " +calc.evaluation(calc.convert(calc.preprocessing(str_5))));
            }
        }//if

        System.out.println();
        String str_6 = "(100+1)(4-2)";
        System.out.println("Original: " +str_6);

        if(calc.preprocessing(str_6)!=null)
        {
            System.out.println("Preprocessed: " +calc.preprocessing(str_6));
            if(calc.convert(calc.preprocessing(str_6))!=null)
            {
                System.out.println("Postfix: " +calc.convert(calc.preprocessing(str_6)));
                System.out.println("The value: " +calc.evaluation(calc.convert(calc.preprocessing(str_6))));
            }
        }//if

        System.out.println();
        String str_7 = "12&34*(98-67)";
        System.out.println("Original: " +str_7);

        if(calc.preprocessing(str_7)!=null)
        {
            System.out.println("Preprocessed: " +calc.preprocessing(str_7));
            if(calc.convert(calc.preprocessing(str_7))!=null)
            {
                System.out.println("Postfix: " +calc.convert(calc.preprocessing(str_7)));
                System.out.println("The value: " +calc.evaluation(calc.convert(calc.preprocessing(str_7))));
            }
        }//if

        System.out.println();
        String str_8 = "((53-23-1)/43+4)";
        System.out.println("Original: " +str_8);

        if(calc.preprocessing(str_8)!=null)
        {
            System.out.println("Preprocessed: " +calc.preprocessing(str_8));
            if(calc.convert(calc.preprocessing(str_8))!=null)
            {
                System.out.println("Postfix: " +calc.convert(calc.preprocessing(str_8)));
                System.out.println("The value: " +calc.evaluation(calc.convert(calc.preprocessing(str_8))));
            }
        }//if

        System.out.println();
        String str_9 = "((12-4-5)^3*^154 (12 + ^3)/55+13*5)";
        System.out.println("Original: " +str_9);

        if(calc.preprocessing(str_9)!=null)
        {
            System.out.println("Preprocessed: " +calc.preprocessing(str_9));
            if(calc.convert(calc.preprocessing(str_9))!=null)
            {
                System.out.println("Postfix: " +calc.convert(calc.preprocessing(str_9)));
                System.out.println("The value: " +calc.evaluation(calc.convert(calc.preprocessing(str_9))));
            }
        }//if


        System.out.println();
        String str_10 = "((32+1) +3";

        System.out.println("Original: " +str_10);
        String st1 = calc.preprocessing(str_10);

        if(st1 != null)
        {
            System.out.println("Preprocessed: " +st1);

            String you_1 = calc.convert(st1);

            if(you_1 != "")
            {
                System.out.println("Postfix: " +you_1);

                double our_1 = calc.evaluation(you_1);

                System.out.println("The value: " +our_1);

            }else if(you_1 == ""){

                System.out.println("Unbalanced paranthesis!");

            }
        }//if

        System.out.println();

        String str_11 = "( ^2+5) * 6/8 )";
        calc.preprocessing(str_11);

        System.out.println("Original: " +str_11);
        String str2 = calc.preprocessing(str_11);

        if(str2!= null)
        {
            System.out.println("Preprocessed: " +str2);
            String you_2 = calc.convert(str2);

            if(you_2 != "")
            {
                System.out.println("Postfix: " +you_2);
                double our_2 = calc.evaluation(you_2);

                System.out.println("The value: " +our_2);

            }else if(you_2 == ""){

                System.out.println("Unbalanced paranthesis!");
            }

        }//if

        System.out.println();

        String str_12 = "^3*^154 (12 + ^3)";
        System.out.println("Original: " +str_12);

        String str3 = calc.preprocessing(str_12);

        if(str3!= null)
        {
            System.out.println("Preprocessed: " +str3);
            String you_3 = calc.convert(str3);

            if(you_3 != "")
            {
                System.out.println("Postfix: " +you_3);
                double our_3 = calc.evaluation(you_3);
                System.out.println("The value: " +our_3);

            }else if(you_3 == ""){

                System.out.println("Unbalanced paranthesis!");
            }

        }//if

    }//main

/**********************************OUTPUT***********************************
Original: ^3*154 (12 + 3)
Preprocessed: ^3*154*(12+3)
Postfix:  3 ^ 154 *  12 3 +  *
The value: -6930.0

Original: 12/(43-40)2
Preprocessed: 12/(43-40)*2
Postfix: 12  43 40 -  / 2 *
The value: 8.0

Original: 1+2+3+4+5
Preprocessed: 1+2+3+4+5
Postfix: 1 2 + 3 + 4 + 5 +
The value: 15.0

Original: (45-(^12)-5)
Preprocessed: (45-(^12)-5)
Postfix:  45   12 ^  - 5 - 
The value: 52.0

Original: 15/3+7*(13-5)
Preprocessed: 15/3+7*(13-5)
Postfix: 15 3 / 7  13 5 -  * +
The value: 61.0

Original: (100+1)(4-2)
Preprocessed: (100+1)*(4-2)
Postfix:  100 1 +   4 2 -  *
The value: 202.0

Original: 12&34*(98-67)
Error! The expression has illegal characters.

Original: ((53-23-1)/43+4)
Preprocessed: ((53-23-1)/43+4)
Postfix:   53 23 - 1 -  43 / 4 + 
The value: 4.674418604651163

Original: ((12-4-5)^3*^154 (12 + ^3)/55+13*5)
Preprocessed: ((12-4-5)*^3*^154*(12+^3)/55+13*5)
Postfix:   12 4 - 5 -   3 ^ *  154 ^ *  12  3 ^ +  * 55 / 13 5 * + 
The value: 291.8

Original: ((32+1) +3
Preprocessed: ((32+1)+3
Unbalanced paranthesis!

Original: ( ^2+5) * 6/8 )
Preprocessed: (^2+5)*6/8)
Postfix:   2 ^ 5 +  6 * 8 / 
The value: 2.25

Original: ^3*^154 (12 + ^3)
Preprocessed: ^3*^154*(12+^3)
Postfix:  3 ^  154 ^ *  12  3 ^ +  *
The value: 4158.0
BUILD SUCCESSFUL (total time: 0 seconds)

*/

}//Calculator

