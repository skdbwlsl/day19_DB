package day19_DB;
//1단계. 학생정보 저장하기
public class StudentDTO { //DTO :데이터를 나르고 저장하고 사용하는 용도
	private String stNum;
	private String name;
	private int age;
	
	//alt+shift+s :단축기
	public String getStNum() {
		return stNum;
	}
	public void setStNum(String stNum) {
		this.stNum = stNum;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}

}
