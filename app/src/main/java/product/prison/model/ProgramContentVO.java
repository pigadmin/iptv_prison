package product.prison.model;

import java.io.Serializable;
import java.util.List;


public class ProgramContentVO implements Serializable {

	//上
	private Integer contentTop;
	
	//左
	private Integer contentLeft;
	
	//宽
	private Integer contentWidth;
	
	//高
	private Integer contentHeight;
	
	//(图片才有)播放特效:0没有,1渐变
	private Integer playType;
	
	//文字才有
	private String txt;
	
	//图片播放时间间隔
	private Integer contentInterval;
	
	//素材类型
	private Integer classify;
	
	private List<MaterialVO> materials;
	// get set 方法

	public Integer getContentTop() {
		return contentTop;
	}

	public void setContentTop(Integer contentTop) {
		this.contentTop = contentTop;
	}

	public Integer getContentLeft() {
		return contentLeft;
	}

	public void setContentLeft(Integer contentLeft) {
		this.contentLeft = contentLeft;
	}

	public Integer getContentWidth() {
		return contentWidth;
	}

	public void setContentWidth(Integer contentWidth) {
		this.contentWidth = contentWidth;
	}

	public Integer getContentHeight() {
		return contentHeight;
	}

	public void setContentHeight(Integer contentHeight) {
		this.contentHeight = contentHeight;
	}

	public Integer getPlayType() {
		return playType;
	}

	public void setPlayType(Integer playType) {
		this.playType = playType;
	}

	public String getTxt() {
		return txt;
	}

	public void setTxt(String txt) {
		this.txt = txt;
	}

	public Integer getContentInterval() {
		return contentInterval;
	}

	public void setContentInterval(Integer contentInterval) {
		this.contentInterval = contentInterval;
	}

	public Integer getClassify() {
		return classify;
	}

	public void setClassify(Integer classify) {
		this.classify = classify;
	}

	public List<MaterialVO> getMaterials() {
		return materials;
	}

	public void setMaterials(List<MaterialVO> materials) {
		this.materials = materials;
	}
}
