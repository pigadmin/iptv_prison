package product.prison.model;

public class ViewSizeAndMargins {
	
	int with;
	int height;
	int left;
	int top;
	int right;
	int bottom;
	
	public ViewSizeAndMargins(ProgramContentVO vo){
		this.with = vo.getContentWidth();
		this.height = vo.getContentHeight();
		this.left = vo.getContentLeft();
		this.top = vo.getContentTop();
		this.right = 0;
		this.bottom = 0;
	}
	
	public int getWith() {
		return with;
	}
	public void setWith(int with) {
		this.with = with;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public int getLeft() {
		return left;
	}
	public void setLeft(int left) {
		this.left = left;
	}
	public int getTop() {
		return top;
	}
	public void setTop(int top) {
		this.top = top;
	}
	public int getRight() {
		return right;
	}
	public void setRight(int right) {
		this.right = right;
	}
	public int getBottom() {
		return bottom;
	}
	public void setBottom(int bottom) {
		this.bottom = bottom;
	}
	
	

}
