package pobj.pinboard.editor.tools;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import pobj.pinboard.document.Clip;
import pobj.pinboard.editor.EditorInterface;
import pobj.pinboard.editor.commands.CommandMove;

public class ToolSelection implements Tool {
	
	private double x;
	private double y;
	
	@Override
	public void press(EditorInterface i, MouseEvent e) {
		this.x = e.getX();
		this.y = e.getY();
		if(e.isShiftDown()) i.getSelection().toogleSelect(i.getBoard(), x, y);
		else i.getSelection().select(i.getBoard(), x, y);
	}
	
	@Override
	public void drag(EditorInterface i, MouseEvent e) {
		double decalageX = e.getX();
		double decalageY = e.getY();
		i.getUndoStack().addCommand(new CommandMove(i, i.getSelection().getContents(), decalageX - x, decalageY - y));
		for(Clip c : i.getSelection().getContents()) {
			c.move(decalageX - x, decalageY - y);
		}
		x = e.getX();
		y = e.getY();
	}
	
	@Override
	public void release(EditorInterface i, MouseEvent e) {
		
	}
	
	@Override
	public void drawFeedback(EditorInterface i, GraphicsContext gc) {
		i.getBoard().draw(gc);
		i.getSelection().drawFeedback(gc);
	}
	
	@Override
	public String getName(EditorInterface editor) {
		return "Selection tool";
	}
	
	

}
