/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.algebra.command;

import java.util.Stack;

/**
 *
 * @author antev
 */
public class CommandInvoker {

    private final Stack<Command> commandStack = new Stack<>();

    public void execute(Command command) {
        command.execute();
        commandStack.push(command);
    }

    public void undo() {
        if (!commandStack.isEmpty()) {
            Command command = commandStack.pop();
            command.undo();
        }
    }

    public boolean isEmpty() {
        return commandStack.isEmpty();
    }

    public void executeNext() {
        if (!isEmpty()) {
            Command command = commandStack.pop();
            command.execute();
        }
    }

    public void clear() {
        commandStack.clear();
    }
}
