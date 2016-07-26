package eg.edu.alexu.csd.oop.draw;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

public abstract class MyShape implements Cloneable, Shape {
	protected Map<String, Double> properties1= new  HashMap<String, Double>();
	 protected Point  point=new Point();
	 protected Color innerColor=null;
	 protected Color outerColor=null;
	@Override
	public void setPosition(Point position) {
		this.point = position ;
		
	}
	@Override
	public Point getPosition() {
		return this.point;
	}
	@Override
	public void setProperties(Map<String, Double> properties) {
		return ;
	}
	@Override
	public Map<String, Double> getProperties() {
		return this.properties1;
	}
	@Override
	public void setColor(Color color) {
		outerColor = color ;
		
	}
	@Override
	public Color getColor() {
		return outerColor;
	}
	@Override
	public void setFillColor(Color color) {
		innerColor = color ;
		
	}
	@Override
	public Color getFillColor() {
		return this.innerColor ;
	}
	@Override
	public void draw(Graphics canvas) {
		// TODO Auto-generated method stub
		
	}
	public Object clone() throws CloneNotSupportedException{
		return null ;
	}
}