package campus_jdbc_practice;
import java.sql.*;
import java.util.Random;
import java.util.UUID;

public class JDBCBatchInsert {
	public static void main(String[] args) {
		String url = "jdbc:mysql://localhost:3306/mysql";
		String user = "SY";
		String password = "SY@1234";
		String sql = "INSERT INTO users (id, name, age) VALUES (?, ?, ?)";

		try (Connection conn = DriverManager.getConnection(url, user, password);
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			// 자동 커밋 비활성화
			conn.setAutoCommit(false);

			// 랜덤 데이터 준비
			Random random = new Random();
			String[] names = {"지수", "영희", "철수", "민수", "서영", "은지", "현우", "준호", "세희"};

			// 배치에 추가
			for (int i = 0; i < 100; i++) { // 100개의 레코드 삽입을 가정
				String id = UUID.randomUUID().toString();  // id를 위해 랜덤 UUID 생성
				String name = names[random.nextInt(names.length)]; // 랜덤 이름 생성
				int age = random.nextInt(100); // 0부터 99 사이의 나이를 랜덤으로 생성

				pstmt.setString(1, id);
				pstmt.setString(2, name);
				pstmt.setInt(3, age);
				pstmt.addBatch();
			}

			// 배치 실행
			int[] updateCounts = pstmt.executeBatch();

			// 커밋
			conn.commit();

			System.out.println("Inserted " + updateCounts.length + " rows.");

		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
}