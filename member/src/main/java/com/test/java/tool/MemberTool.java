package com.test.java.tool;

import java.util.List;
import java.util.Map;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MemberTool {

	private final JdbcTemplate jdbcTemplate;

	@Tool(description = """
			회원 아이디로 회원의 기본 정보와 상세 정보를 조회합니다.
			id 매개변수에는 반드시 회원 아이디만 넣으세요.
			예) hong, dog, cat
			id 매개변수의 잘못된 예) hong회원 정보를 알려줘
			""")
	public String getMember(String id) {

		try {

			String sql = """
					select
						u.id,
						u.name,
						i.age,
						i.address,
						case i.gender
							when 'm' then '남자'
							when 'f' then '여자'
							else '미상'
						end as gender
					from tblUser u
						inner join tblUserInfo i
							on u.id = i.id
								where u.id = ?
					""";

			Map<String, Object> member = jdbcTemplate.queryForMap(sql, id);

			return """
					회원 아이디: %s
					이름: %s
					나이: %s
					주소: %s
					성별: %s
					""".formatted(member.get("id"), member.get("name"), member.get("age"), member.get("address"),
					member.get("gender"));

		} catch (Exception e) {
			System.out.println("MemberTool.getMember");
			e.printStackTrace();
		}

		return "회원 조회 중 오류가 발생했습니다.";

	}

	@Tool(description = """
			전체 회원 목록을 조회합니다.
			사용자가 전체 회원, 회원 목록, 가입된 회원을 물어볼 때 사용하세요.
			비밀번호는 조회하지 않습니다.
			""")
	public String getMemberList() {

		try {

			System.out.println("MemberTool.getMemberList 호출됨");

			String sql = """
					select
					    u.id,
					    u.name,
					    i.age,
					    i.address,
					    case i.gender
					        when 'm' then '남자'
					        when 'f' then '여자'
					        else '미상'
					    end as gender
					from tblUser u
					    inner join tblUserInfo i
					        on u.id = i.id
					order by u.id
					""";

			List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);

			if (list.isEmpty()) {
				return "등록된 회원이 없습니다.";
			}

			StringBuilder sb = new StringBuilder();

			for (Map<String, Object> row : list) {
				sb.append("""
						회원 아이디: %s
						이름: %s
						나이: %s
						주소: %s
						성별: %s

						""".formatted(row.get("ID"), row.get("NAME"), row.get("AGE"), row.get("ADDRESS"),
						row.get("GENDER")));
			}

			return sb.toString();

		} catch (Exception e) {
			e.printStackTrace();
			return "회원 목록 조회 중 오류가 발생했습니다: " + e.getMessage();
		}
	}

	@Tool(description = """
			회원 아이디로 회원 활동 정보를 조회합니다.
			게시글 수, 댓글 수, 최근 게시글 목록을 조회할 때 사용하세요.
			id 매개변수에는 반드시 회원 아이디만 넣으세요.
			예: hong, dog, cat
			""")
	public String getMemberActivity(String id) {

		try {

			System.out.println("MemberTool.getMemberActivity 호출됨");
			System.out.println("id = " + id);

			String memberSql = """
					select name
					from tblUser
					where id = ?
					""";

			String name = jdbcTemplate.queryForObject(memberSql, String.class, id);

			String countSql = """
					select
					    (select count(*) from tblBoard where id = ?) as boardCount,
					    (select count(*) from tblComment where id = ?) as commentCount
					from dual
					""";

			Map<String, Object> count = jdbcTemplate.queryForMap(countSql, id, id);

			String boardSql = """
					select
					    seq,
					    subject,
					    to_char(regdate, 'yyyy-mm-dd hh24:mi:ss') as regdate
					from tblBoard
					where id = ?
					order by regdate desc
					fetch first 3 rows only
					""";

			List<Map<String, Object>> boards = jdbcTemplate.queryForList(boardSql, id);

			StringBuilder sb = new StringBuilder();

			sb.append("""
					회원 아이디: %s
					이름: %s
					게시글 수: %s
					댓글 수: %s

					최근 게시글:
					""".formatted(id, name, count.get("BOARDCOUNT"), count.get("COMMENTCOUNT")));

			if (boards.isEmpty()) {
				sb.append("최근 게시글이 없습니다.");
			} else {
				for (Map<String, Object> board : boards) {
					sb.append("""
							- [%s] %s (%s)
							""".formatted(board.get("SEQ"), board.get("SUBJECT"), board.get("REGDATE")));
				}
			}

			return sb.toString();

		} catch (EmptyResultDataAccessException e) {
			return "회원 활동 조회 실패: 존재하지 않는 회원입니다. id=" + id;
		} catch (Exception e) {
			e.printStackTrace();
			return "회원 활동 조회 중 오류가 발생했습니다: " + e.getMessage();
		}
	}

}
