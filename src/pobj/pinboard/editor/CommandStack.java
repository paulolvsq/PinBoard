package pobj.pinboard.editor;

import java.util.Stack;

import pobj.pinboard.editor.commands.Command;

public class CommandStack {
	
	//initialiser deux Stack qui correspondent aux piles undo et redo
	private Stack<Command> undo;
	private Stack<Command> redo;
	
	public CommandStack() {
		this.undo = new Stack<>();
		this.redo = new Stack<>();
	}
	
	public void addCommand(Command cmd) {
		this.undo.add(cmd);
		this.redo.clear();
	}
	
	public void undo() {
		Command cmd = undo.pop();
		cmd.undo();
		this.redo.push(cmd);
	}
	
	public void redo() {
		Command cmd = redo.pop();
		cmd.execute();
		this.undo.push(cmd);
	}
	
	public boolean isUndoEmpty() {
		return this.undo.isEmpty();
	}
	
	public boolean isRedoEmpty() {
		return this.redo.isEmpty();
	}

}
