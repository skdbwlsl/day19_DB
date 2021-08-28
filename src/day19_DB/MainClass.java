package day19_DB;

import java.util.ArrayList;
import java.util.Scanner;

public class MainClass {

	public static void main(String[] args) {
		//단계3. 객체만들기
		DBClass db = new DBClass();
		Scanner input = new Scanner(System.in);
		int num;
		
		//단계4. 데이터 연동을 위해선 sql디벨로퍼(.exe)를 열어서 로그인 후, 접속을 한다.
		while(true) {
			System.out.println("1.등록 2.전체보기 3.삭제 4.수정");
			num= input.nextInt();
			switch (num) {
			case 1:
				System.out.println("학번 입력");
				String stNum = input.next();
				System.out.println("이름 입력");
				String name = input.next();
				System.out.println("나이 입력");
				int age = input.nextInt();
				
				//int result = db.saveData(stNum, name, age);
				int result = db.saveData02(stNum, name, age); //db클래스의 return result;을 받아준다
				if(result ==1) {
					System.out.println("성공적으로 저장");
				}else {
					System.out.println("동일 아이디가 존재합니다");
				}
				break;

			case 2 :
			//모든 데이터 가져오기
				//어레이 리스트 형태로 가져왔다
				ArrayList<StudentDTO> list = db.getUsers();//getUsers기능을 DBClass로 넘어가서 만들어준다
				if(list.size()==0) {
					System.out.println("저장된 데이터가 없습니다.");
				}else {
					for(int i = 0;i<list.size();i++) {
						System.out.println("학번 : " + list.get(i).getStNum());
						System.out.println("이름 : " + list.get(i).getName());
						System.out.println("나이 : " + list.get(i).getAge());
						System.out.println("-------------------------");
					}
				}
				break;
				
			case 3 :
				System.out.println("삭제 학번 입력 ");
				String userNum= input.next();
				int re = db.delete(userNum);

				if(re==1) {
					System.out.println("삭제 되었습니다");
				}else {
					System.out.println("해당 학번은 존재하지 않는다");
				}
				break;
				
			case 4 :
				System.out.println("수정할 아이디 입력(존재하는 아이디를 입력해 주세요)");
				String stNum1 = input.next();
				System.out.println("수정할 이름 입력");
				String name1 = input.next();
				System.out.println("수정할 나이 입력");
				int age1 = input.nextInt();
				
				if(db.modify(stNum1, name1, age1)==1) {  //db.modify로 넘겨준다
					System.out.println("수정이 완료 되었습니다");
				}else {//0이 들어온다면 해당아이디는 존재하지 않는것
					System.out.println("해당 아이디는 존재하지 않습니다");
				}
				break;
			}
	}

}
}