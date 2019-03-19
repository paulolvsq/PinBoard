package pobj.pinboard.document;

import javafx.scene.paint.Color;

public class AbstractClip {
	
	private double left;
	private double top;
	private double right;
	private double bottom;
	private Color color;
	
	public AbstractClip(double left, double top, double right, double bottom, Color color) {
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
		this.color = color;
	}

	/*public void draw(GraphicsContext ctx) {
		ctx.setFill(color);
		ctx.fillRect(left, top, right, bottom);
	}*/

	public double getTop() {
		return this.top;
	}

	public double getLeft() {
		return this.left;
	}

	public double getBottom() {
		return this.bottom;
	}

	public double getRight() {
		return this.right;
	}

	public void setGeometry(double left, double top, double right, double bottom) {
		this.left = left;
		this.right = right;
		this.top = top;
		this.bottom = bottom;
	}

	public void move(double x, double y) {
		this.top = this.top + y;
		this.bottom = this.bottom + y;
		this.left = this.left + x;
		this.right = this.right + x;
	}

	public boolean isSelected(double x, double y) {
		if(x >= left && x <= right && y <= bottom && y >= top) return true;
		return false;
	}

	public void setColor(Color c) {
		this.color = c;
	}

	public Color getColor() {
		return this.color;
	}
	
}
