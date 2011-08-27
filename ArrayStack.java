import java.util.*;
import java.util.EmptyStackException;

/** Implementation of the interface StackInt<E> using an array.
*   @author Oksana Yaremchuk
*/

public class ArrayStack < E > implements StackInt < E >
{
    private Stack<E> stack = new Stack<E>();

    //Storage for stack.
    private E[] theData;

    //Index to top of stack. Initialy empty.
    private int topOfStack = -1;
    private static final int INITIAL_CAPACITY = 10;

    /** Construct an empty stack with the default
     * initial capacity.
    */
    public ArrayStack ()
    {
        theData = (E[])new Object[INITIAL_CAPACITY];
    }
  
    /** Insert a new item on top of the stack.
     * post: The new item is the top item on the stack.
     *       All other items are one position lower.
     * @param obj The item to be inserted
     * @return The item that was inserted
    */
    public E push(E obj)
    {
        if (topOfStack == theData.length - 1)
        {
            reallocate();
        }

        topOfStack++;
        theData[topOfStack] = obj;
    
        return obj;

    }//push

    /** Remove and return the top item on the stack.
     * pre: The stack is not empty.
     * post: The top item on the stack has been
     *       removed and the stack is one item smaller.
     * @return The top item on the stack
     * @throws EmptyStackException if the stack is empty
    */
    public E pop()
    {
        if (empty())
        {
            throw new EmptyStackException();
        }

        return theData[topOfStack--];
    }//pop

    /** Return the top item on the stack
     * Pre: The stack is not empty
     * Post: The stack remains unchanged
     * @return The top item on the stack
     * @throws EmptyStackException If the stack
     * is empty
    */
    public E peek()
    {
        if (empty())
        {
            throw new EmptyStackException();
        }

        return theData[topOfStack];
    }//peek

    /** Return true if the stack is empty
     * @return True if the stack is empty
    */
    public boolean empty()
    {
        return topOfStack == -1;
    }//empty

    /** Method to reallocate the array containing the stack data
     * Postcondition: The size of the data array has been doubled
     * and all of the data has been copied to the new array
    */
    private void reallocate()
    {
        E[] temp = (E[])new Object[2 * theData.length];
        System.arraycopy(theData, 0, temp, 0, theData.length);
        theData = temp;
    }//reallocate

}//ArrayStack


