package eg.edu.alexu.csd.oop.draw;


public class StackNode {
	private Shape newShape ;
	private Shape oldShape ;
	private int index ;
	
	public StackNode(Shape newS , Shape oldS ,int num){
		setNewShape(newS) ;
		setOldShape(oldS) ;
		setIndex(num) ;
	}

	public Shape getNewShape() {
		return newShape;
	}

	public void setNewShape(Shape newShape) {
		this.newShape = newShape;
	}

	public Shape getOldShape() {
		return oldShape;
	}

	public void setOldShape(Shape oldShape) {
		this.oldShape = oldShape;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
}
