package cellsociety_team10;

/**
 * creates individual cell object for the grid
 * 
 * @author Phil Foo, Lucy Zhang, Yumin Zhang
 *
 */


public class Cell {
	private int state;
	private int x;
	private int y;
	private int extraState;
	private double extraState2;
	private double extraState3;

	

	public Cell(){
		x = 0;
		y = 0;
		state = 0;
		extraState=0;
		extraState2=0;
	}
	
	public Cell(int x, int y, int state) {
		this.x = x;
		this.y = y;
		this.state = state;
		extraState = 0;
		extraState2 = 0;
		extraState3 = 0;
	}
	
	public double getExtraState2() {
		return extraState2;
	}
	
	public double getExtraState3() {
		return extraState3;
	}

	public void setExtraState2(double extraState2) {
		this.extraState2 = extraState2;
	}
	
	public void setExtraState3(double extraState3) {
		this.extraState3 = extraState3;
	}

	public int getExtraState() {
		return extraState;
	}

	public void setExtraState(int extraState) {
		this.extraState = extraState;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
}
