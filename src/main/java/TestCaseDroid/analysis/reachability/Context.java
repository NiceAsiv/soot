package TestCaseDroid.analysis.reachability;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Objects;

import lombok.Getter;
import lombok.Setter;
import soot.SootMethod;
import soot.Unit;

/**
 * Context class represents the context of ICFG or BackWardICFG, including the reached node and the call stack.
 */
@Getter
@Setter
public class Context {
    private Deque<Unit> callStack;
    private Deque<SootMethod> methodCallStack;
    private Unit reachedNode;

    /**
     * Default constructor, initializes an empty call stack.
     */
    public Context() {
        callStack = new LinkedList<>();
    }

    /**
     * Constructor, initializes a reached node and an empty call stack.
     * @param reachedNode The reached node
     */
    public Context(Unit reachedNode) {
        this.reachedNode = reachedNode;
        callStack = new LinkedList<>();
        methodCallStack = new LinkedList<>();
    }

    /**
     * Constructor, initializes a reached node and a call stack.
     * @param reachedNode The reached node
     * @param callStack The call stack
     */
    public Context(Unit reachedNode, Deque<Unit> callStack) {
        this.reachedNode = reachedNode;
        this.callStack = callStack;
    }

    /**
     * Constructor, initializes a reached node, a call stack and a method call stack.
     * @param reachedNode The reached node
     * @param callStack The call stack
     * @param methodCallStack The method call stack
     */
    public Context(Unit reachedNode, Deque<Unit> callStack, Deque<SootMethod> methodCallStack) {
        this.reachedNode = reachedNode;
        this.callStack = callStack;
        this.methodCallStack = methodCallStack;
    }

    /**
     * Copies the current context.
     * @return A new context that is a copy of the current context.
     */
    public Context copy() {
        Deque<Unit> newCallStack = new LinkedList<>(this.callStack);
        Deque<SootMethod> newMethodCallStack = new LinkedList<>(this.methodCallStack);
        return new Context(this.reachedNode,newCallStack,newMethodCallStack);
    }

    /**
     * Checks if the current context is equal to the specified object.
     * @param obj The object to check
     * @return True if the current context is equal to the specified object, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Context) {
            Context other = (Context) obj;
            return Objects.equals(reachedNode, other.reachedNode) && callStack.equals(other.callStack);
        }
        return false;
    }
    public Deque<Unit> getReversedCallStack() {
        Deque<Unit> reversedCallStack = new LinkedList<>();
        Iterator<Unit> it = this.callStack.descendingIterator();
        while (it.hasNext()) {
            reversedCallStack.add(it.next());
        }
        return reversedCallStack;
    }
    @Override
    public String toString() {
        //输出调用链
        StringBuilder sb = new StringBuilder();
        for(Unit unit : getReversedCallStack()) {
            sb.append(unit.toString()).append(" -> ");
        }
        return sb.toString();
    }
}