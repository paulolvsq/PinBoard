package pobj.pinboard.editor;

import java.util.ArrayList;
import java.util.List;

import pobj.pinboard.document.Clip;

public class Clipboard implements ClipboardListener {
	
	private static Clipboard clipboard = new Clipboard();
	private List<Clip> clips = new ArrayList<>();
	private List<ClipboardListener> listeners = new ArrayList<>();
	
	private Clipboard() {
		
	}
	
	public static Clipboard getInstance() {
		return clipboard;
	}
	
	public void copyToClipboard(List<Clip> clips) {
		this.clear();
		for(Clip c : clips) this.clips.add(c.copy());
	}
	
	public List<Clip> copyFromClipboard() {
		List<Clip> copie = new ArrayList<>();
		for(Clip c : this.clips) copie.add(c.copy());
		return copie;
	}
	
	public void clear() {
		this.clips.clear();
		this.clipboardChanged();
	}
	
	public boolean isEmpty() {
		return this.clips.isEmpty();
	}

	public void clipboardChanged() {
		for(ClipboardListener c : listeners) c.clipboardChanged();
	}
	
	public void addListener(ClipboardListener listener) {
		listeners.add(listener);
	}
	
	public void removeListener(ClipboardListener listener) {
		listeners.remove(listener);
	}
	
}
