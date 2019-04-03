package pobj.pinboard.editor.commands;

import java.util.ArrayList;
import java.util.List;

import pobj.pinboard.document.Clip;
import pobj.pinboard.editor.EditorInterface;

public class CommandMove implements Command {
	
	@SuppressWarnings("unused")
	private EditorInterface e;
	private double x;
	private double y;
	private List<Clip> clips = new ArrayList<>();
	
	public CommandMove(EditorInterface e, Clip clip, double x, double y) {
		this.e = e;
		clips.add(clip);
		this.x = x;
		this.y = y;
	}
	
	public CommandMove(EditorInterface e, List<Clip> clip, double x, double y) {
		this.e = e;
		clips.addAll(clip);
		this.x = x;
		this.y = y;
	}
	
	public void execute() {
		for(Clip c : clips) c.move(x, y);
	}
	
	public void undo() {
		for(Clip c : clips) c.move(-x, -y);
	}

}
