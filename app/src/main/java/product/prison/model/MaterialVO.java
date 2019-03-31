package product.prison.model;

/**
 * 素材表
 */
public class MaterialVO implements java.io.Serializable {
	
	
	//路径
	private String path;
	
	//类型1:背景 2:音频 3:视频 4:图片 5:FLASH 6: PPT 7:word 8:excel 9:PDF 10:文字
	private Integer classify;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Integer getClassify() {
		return classify;
	}

	public void setClassify(Integer classify) {
		this.classify = classify;
	}
	
	// get set 方法
	
}