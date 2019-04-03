package pobj.pinboard.editor.commands;

import java.util.ArrayList;
import java.util.List;

import pobj.pinboard.document.Clip;
import pobj.pinboard.document.ClipGroup;
import pobj.pinboard.editor.EditorInterface;

public class CommandGroup implements Command {
	
	private EditorInterface e;
	private List<Clip> clips = new ArrayList<>();
	private ClipGroup cg; //sert de mémoire temporaire dans laquelle on met les clips créés lors d'une exécution
	
	public CommandGroup(EditorInterface e, Clip clip) {
		this.e = e;
		this.clips.add(clip);
	}
	
	public CommandGroup(EditorInterface e, List<Clip> clips) {
		this.e = e;
		this.clips.addAll(clips);
	}
	
	public void execute() {
		cg = new ClipGroup();
		this.e.getBoard().removeClip(this.clips); //je vide la board
		for(Clip c : this.clips) cg.addClip(c);
		e.getBoard().addClip(cg);
	}
	
	public void undo() {
		this.e.getBoard().removeClip(cg);
		this.e.getBoard().addClip(this.clips);
	}

}
