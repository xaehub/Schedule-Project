package com.example.schedule.repository;

import com.example.schedule.dto.ScheduleResponseDto;
import com.example.schedule.entity.Schedule;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class JdbcTemPlateScheduleRepository implements ScheduleRepository{

    private final JdbcTemplate jdbcTemplate;

    public JdbcTemPlateScheduleRepository(DataSource dataSource) {

        // SimpleJdbcInsert -> schedule 데이터 베이스에 데이터를 삽입
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * 일정 생성
     * @param schedule
     * @return 생성한 일정 데이터
     */
    @Override
    public ScheduleResponseDto saveSchedule(Schedule schedule) {

        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);

        jdbcInsert.withTableName("schedule").usingGeneratedKeyColumns("id");

        // 일정 데이터를 Map에 저장
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("to_do", schedule.getToDo());
        parameters.put("writer", schedule.getWriter());
        parameters.put("password", schedule.getPassword());
        parameters.put("created_at", schedule.getCreatedAt());
        parameters.put("updated_at", schedule.getUpdatedAt());


        // 저장 후 생성된 key값을 Number 타입으로 볁환하는 메서드
        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));

        // 키값을 Schedule 객체에 설정
        schedule.setId(key.longValue());

        // 저장한 일정을 ResponseDto로 반환
        return new ScheduleResponseDto(schedule);
    }

    /**
     * 모든 일정 목록 조회
     * @return 모든 일정 데이터
     */
    @Override
    public List<ScheduleResponseDto> findAllSchedules(String updatedAt, String writer) {

        // 모든 일정 조회하는 쿼리문
        String result = "select * from schedule";

        // 모든 타입의 객체든 저장하는 오브젝트 타입의 파라미터 리스트
        List<Object> queryResult = new ArrayList<>();


        // 만약 updatedAt 값이 존재하고 비어있지 않으면
        if(updatedAt != null && !updatedAt.isEmpty()){
            result = result + " where updatedAt = ?";   //쿼리문에 where updatedAt추가
            queryResult.add(updatedAt);                 // 파라미터에 updatedAt값 추가
        }

        // 만약 writer 값이 존재하고 비어있지 않으면
        if(writer != null && !writer.isEmpty()) {
            result = result + " where writer = ?";  // 쿼리문에 where writer 추가
            queryResult.add(writer);                // 파라미터에 writer 추가
        }

        // 위에서 생성된 query문이랑 파라미터로 schedule 데이터 베이스에서 모든 데이터 조회해서 scheduleResponseDto 형태로 반환
        return jdbcTemplate.query(result, queryResult.toArray(), scheduleRowMapper());
    }

    /**
     * 선택한 일정 조회
     * @param id 일정 id
     * @return 선택한 일정 데이터
     */
    @Override
    public Schedule findScheduleByIDOrElseThrow(Long id) {

        // query문으로 schedule 데이터베이스에서 일정 조회하고 최신 수정일 순으로 정렬
        List<Schedule> result = jdbcTemplate.query("select * from schedule where id = ?",scheduleRowMapperV2(), id);

        // id가 잘못되면 예외처리
        return result.stream().findAny().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, " id : " + id + "값을 가진 데이터는 존재하지 않습니다."));
    }

    /**
     * 일정 수정
     * @param id 일정 id
     * @param to_do 할 일
     * @param writer 작성자
     * @param password 비밀번호
     * @return 수정된 데이터
     */
    @Override
    public int updateSchedule(Long id, String to_do, String writer, String password) {

        // 일정 수정
        return jdbcTemplate.update("update schedule set to_do = ?, writer = ?, password = ? where id = ?", to_do, writer, password, id);
    }

    /**
     * 일정 삭제
     * @param id 삭제할 id
     * @return 삭제된 데이터
     */
    @Override
    public int deleteSchedule(Long id) {

        // id값에 맞는 데이터값 삭제
        return jdbcTemplate.update("delete from schedule where id = ?", id);
    }

    // ScheduleResponseDto 객체를 ResultSet에서 읽어와 매핑하는 RowMapper
    private RowMapper<ScheduleResponseDto> scheduleRowMapper() {

        // ResultSet에서 읽은 데이터로 ScheduleResponseDto 객체를 생성하여 반환
        return new RowMapper<ScheduleResponseDto>() {
            @Override
            public ScheduleResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new ScheduleResponseDto(
                        rs.getLong("id"),
                        rs.getString("to_do"),
                        rs.getString("writer"),
                        rs.getTimestamp("created_at").toLocalDateTime(), // 'created_at'을 LocalDateTime으로 변환
                        rs.getTimestamp("updated_at").toLocalDateTime()  // 'updated_at'을 LocalDateTime으로 변환
                );
            }
        };
    }

    // Schedule 객체를 ResultSet에서 읽어와 매핑하는 RowMapper
    private RowMapper<Schedule> scheduleRowMapperV2() {

        // ResultSet에서 읽은 데이터로 Schedule 객체를 생성하여 반환
        return new RowMapper<Schedule>() {
            @Override
            public Schedule mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Schedule(
                        rs.getLong("id"),
                        rs.getString("to_do"),
                        rs.getString("writer"),
                        rs.getString("password"),
                        rs.getTimestamp("created_at").toLocalDateTime(), // 'created_at'을 LocalDateTime으로 변환
                        rs.getTimestamp("updated_at").toLocalDateTime()  // 'updated_at'을 LocalDateTime으로 변환
                );
            }
        };
    }
}
