package day19_DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DBClass {
	private String url;
	private String id;
	private String pwd;
	private Connection con;  //연결을 위해 sql에 있는 connection을 가지고온다
	
//데이터베이스 관련 기능들만 처리
	
	//단계2. 생성자만들기 (Class.forName이거 먼저해줘야한다)
	public DBClass() {
		
		try {
			//오라클의 기능을 자바에서 사용하기 위한, 무조건 처음 실행 시켜 준다
			//자바에서 오라클에 연결할 수 있게끔 도와주는 라이브러리를 등록하는 것
			//드라이브를 로드한다라고 표현한다
			Class.forName("oracle.jdbc.driver.OracleDriver"); //이 문구는 무조건 써야한다. [1.드라이브 로드]
			
			//초기화 시키기
			url = "jdbc:oracle:thin:@localhost:1521:xe";  //localhost는 내 pc: 1521는 오라클이 사용하고 있는 포트번호:xe는 오라클 버전의미
			id = "dbwls";//오라클 접속할 때 사용하는 id
			pwd = "dbwls9874";//오라클 접속할 떄 사용하는 pwd 적기
			
			//[2.연결된 객체 얻어오기]
			con = DriverManager.getConnection(url, id, pwd);//이코드가 sql 데이터(오라클정보)를 가져오는것 .일종의 통로 con=db. 오라클에 연결하겠다
			System.out.println(con);   //이렇게 했을 때 oracle.jdbc.driver.T4CConnection@78dd667e게 뜨면 해당하는 오라클에 연결이 잘 된것
		
		} catch (Exception e) {
			e.printStackTrace();
			//처음 드라이브 로드하고 트라이,캐치로 묶은 뒤 실행하면 오류가 뜨는데 이유는 해당드라이버가 없기 때문
			//그래서 ojdbc를 연결해줘야한다. 패키지->프로퍼티스->자바빌드패쓰-> 라이브러리-> 클래스패스->add external->ojdbc6열기->apply
			//연결한 뒤에 다시 실행하면 뜨는 것은 없지만 오류가 나진 않는다
		}
	}
		
	
	/*
	 [순서]
	 1.드라이브 로드(오라클 기능 사용)
	 2.연결된 객체를 얻어온다
	 3.연결된 객체를 이용해서 명령어(쿼리문)을 전송할 수 있는 전공 객체를 얻어온다
	 4.전송 갹채를 이용해서 데이터 베이스에 전송후 결과를 얻어온다.
	 5.얻어온 결과는 int 또는  ResultSet으로 받는다
	 */
	
	//getUsers기능 만들기
	public ArrayList<StudentDTO> getUsers(){
			ArrayList<StudentDTO> list= new ArrayList<StudentDTO>();
			String sql = "select * from newst";//sql쿼리문이랑 똑같이 작성한다. 모든 데이터보기
			try {
			PreparedStatement ps= con.prepareStatement(sql); //3.전송객체 ps얻어옴 . 연결된 객체(con)을 이용해서 명령어(select~)를 실행할 수 있는 실행객체로(ps) 받아온다
			ResultSet rs = ps.executeQuery(); //ps.명령어를 실행해라(executeQuery), ps = con , con= db랑 연결된것
			//resultset은 iterator랑 비슷하다
			
			while(rs.next()) {//다음값이 있다면 참.
				StudentDTO dto = new StudentDTO();
				dto.setStNum(rs.getString("id")); //sql에서의 컬럼명을 적어준다(id)
				dto.setName(rs.getString("name"));//컬럼명인(name)
				dto.setAge(rs.getInt("age"));//컬럼명인(age)
				list.add(dto);//데이터가 계속 있을 수 있으니 dto를 계속 저장하도록 add해준다
			}
			}catch(Exception e) {
				e.printStackTrace();
			}
			return list;
			//여기까지 하고 실행 후 전체보기를 하면 sql에 있던 데이터가 잘 나오는 것을 볼 수 있다	
	}
		
	//case 2 에대한 내용을 작성한다
	public int saveData(String stNum, String name, int age) {
		//데이터 베이스에서는 이렇게 쓴다. -> insert into newst values('111, '홍길동', 20);
		String sql = "insert into newst values('"+stNum+"', '"+name+"', "+age+")";   //방법1.
		
		int result = 0;
		try {
			//역시 전송객체를 가지고온다
			PreparedStatement ps = con.prepareStatement(sql);
		//	ResultSet rs = ps.executeQuery(); //이렇게도 쓰긴 쓰나 보통은 executeupdate를 많이 쓴다 insert의 의미가 아니라 가지고오는것이라서
		//resultset : 데이터를 통으로 가져올때 사용한다(select처럼)
			//한마디로 select를 쿼리문으로 썼다면 무조건 resultset으로 가지고오고 executeQuery으로 받으며, select가 아닌 다른것이면 무조건 int를 가지고오고 update로 받는다
			
			ps.setString(1, stNum); 
			ps.setString(2, name);
			ps.setInt(3, age);
			
			//저장 성공시 1을 반환, 실패시 catch이동이나 0을 반환
			result = ps.executeUpdate();//보통은 이것을 쓴다
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return result; //main에 있는 db.saveData02(stNum, name, age);를 받아준다
	}
	
	public int saveData02(String stNum, String name, int age) {
		//데이터 베이스에서는 이렇게 쓴다. -> insert into newst values('111, '홍길동', 20);
		String sql = "insert into newst values(?,?,?)";  //방법2. ?는 나중에 채워넣겠다는 의미
		int result = 0;
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			//얘네들을 ?에 넣는다. 편한 방법 쓰면 된다
			ps.setString(1, stNum); //1번째에 stNum을 넣겠다 ,인덱스처럼 0부터가 아닌 1부터 시작한다
			ps.setString(2, name);
			ps.setInt(3, age);
			
			result = ps.executeUpdate();//보통은 이것을 쓴다
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return result; 
	}

	//case 3 에 대한 내용 작성
	public int delete(String userNum) {
		int result = 0;
		//delete from newst where id = 'userNum';
		String sql = "delete from newst where id = '"+userNum+"'"; //방법1.
		//String sql =  "delete from newst where id = ?";            방법2.
		
			try {
				//실행객체 불러오기
				PreparedStatement ps =con.prepareStatement(sql);
				result = ps.executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();
			}
		return result;
	}
	
	//case 4 수정부분 만들기
	public int modify(String stNum, String name, int age) {
		int result = 0;
		
		//수정할 땐 update,,, update newst set name = '홍길동', age = 20 where id = 'test';
		String sql = "update newst set name =?, age=?, where id = ?";
		
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, name);
			ps.setInt(2, age);
			ps.setString(3, stNum);
			result= ps.executeUpdate();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}//왜 4번에서 수정할때 오류가나지,,? 똑같이 했는데,,?
}

