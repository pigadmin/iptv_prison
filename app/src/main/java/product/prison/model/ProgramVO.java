package product.prison.model;

import java.io.Serializable;
import java.util.List;


/**
 * 节目
 * @author Administrator
 *
 */
public class ProgramVO implements Serializable {


	//节目名称
	private String name;
	
	//备注
	private String remark;
	
	//高度
	private Integer height;
	
	//宽度
	private Integer width;
	
	//1:横屏，2:竖屏
	private Integer screenType;
	
	//节目时长
	private Integer time;
	
	private MaterialVO background;
	
	private List<ProgramContentVO> contents; //屏幕分割

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getTime() {
		return time;
	}

	public void setTime(Integer time) {
		this.time = time;
	}


	public List<ProgramContentVO> getContents() {
		return contents;
	}

	public void setContents(List<ProgramContentVO> contents) {
		this.contents = contents;
	}

	public MaterialVO getBackground() {
		return background;
	}

	public void setBackground(MaterialVO background) {
		this.background = background;
	}

	public Integer getScreenType() {
		return screenType;
	}

	public void setScreenType(Integer screenType) {
		this.screenType = screenType;
	}
	
	
}
