package pobj.pinboard.editor.commands;

import java.util.ArrayList;
import java.util.List;

import pobj.pinboard.document.Clip;
import pobj.pinboard.editor.EditorInterface;

public class CommandAdd implements Command {
	
	private EditorInterface editor;
	private List<Clip> toAdd = new ArrayList<>();
	
	public CommandAdd(EditorInterface editor, Clip toAdd) {
		this.editor = editor;
		this.toAdd.add(toAdd);
	}
	
	public CommandAdd(EditorInterface editor, List<Clip> toAdd) {
		this.editor = editor;
		this.toAdd.addAll(toAdd);
	}
	
	public void execute() {
		this.editor.getBoard().addClip(this.toAdd);
	}
	
	public void undo() {
		this.editor.getBoard().removeClip(this.toAdd);
	}

}
