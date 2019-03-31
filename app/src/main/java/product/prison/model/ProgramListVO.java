package product.prison.model;

import java.util.List;


public class ProgramListVO {
	//节目单名称
	private String name;
	//节目
	private List<ProgramVO> programs;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<ProgramVO> getPrograms() {
		return programs;
	}
	public void setPrograms(List<ProgramVO> programs) {
		this.programs = programs;
	}
}