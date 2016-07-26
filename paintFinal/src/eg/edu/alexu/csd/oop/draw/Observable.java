package eg.edu.alexu.csd.oop.draw;


public interface Observable {
	public void notifyObserver(); 
	public void removeObserver(Observer view);
	public void addObserver(Observer view);
}