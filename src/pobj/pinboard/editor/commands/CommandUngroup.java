package pobj.pinboard.editor.commands;

import java.util.ArrayList;
import java.util.List;

import pobj.pinboard.document.ClipGroup;
import pobj.pinboard.editor.EditorInterface;

public class CommandUngroup implements Command {
	
	private EditorInterface e;
	private List<ClipGroup> clips = new ArrayList<>();
	
	public CommandUngroup(EditorInterface e, ClipGroup clip) {
		this.e = e;
		clips.add(clip);
	}
	
	public CommandUngroup(EditorInterface e, List<ClipGroup> clips) {
		this.e = e;
		this.clips.addAll(clips);
	}
	
	public void execute() {
		for(ClipGroup c : this.clips) {
			this.e.getBoard().removeClip(c); //on supprime les clips groupés de la board
			this.e.getBoard().addClip(c.getClips()); //on les rajoute tous dans la board
		}
	}
	
	public void undo() {
		for(ClipGroup c : this.clips) {
			this.e.getBoard().removeClip(c.getClips()); //on supprime les clips de la board qui ont été dégroupés
			this.e.getBoard().addClip(c); //on remet dans la board les clips groupés
		}
	}

}
