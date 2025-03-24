# 일정 관리 API

## 프로젝트 소개
Spring Boot를 사용하여 일정 관리 API를 개발합니다.  
사용자는 일정을 등록, 조회, 수정, 삭제할 수 있습니다.  
이 프로젝트는 MySQL 데이터베이스를 사용하여 데이터를 관리합니다.

---

## 사용 기술
- **Backend**: Spring Boot, Spring MVC, Spring Data JPA
- **Language**: Java 17
- **Build Tool**: Gradle
- **Database**: MySQL
- **Templating Engine**: Thymeleaf
- **Utilities**: Lombok

---

## 기능

- 일정 생성
- 전체 일정 조회
- 선택 일정 조회
- 일정 수정
- 일정 삭제

---
## ERD

![schedule Database](images/Schedule_ERD.png)

---

## API 명세서
|       | HTTP Method | URL | Path Variable |Request Parameter| Request Body(dto)                                                                             | Response                                                                                                                                                                                                                                                                                                         | HTTP 상태 코드 |
|-------|-------------|-----|---------------|--|-----------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|------------|
| 일정 생성 | POST        |/schedules| X             |X| <pre>{<br/>    “to_do”: “할 일”, <br/>    “writer”: “작성자명”, <br/>    “password”: “1234”<br/>}</pre> | <pre>{<br/>    “id”: “1”, <br/>    “to_do”: “할 일”, <br/>    “writer”: “작성자명”, <br/>    “created_at”: “작성일”, <br/>    “updated_at”: “수정일” <br/>}</pre>                                                                                                                                                            | 201: 정상 등록 
|전체 일정 조회| GET         |/schedules| X             |X| X                                                                                             | <pre>[<br/>{<br/>    “id”: “1”, <br/>    “to_do”: “할 일”, <br/>    “writer”: “작성자명”, <br/>    “created_at”: “작성일”, <br/>    “updated_at”: “수정일” <br/>}<br/>{ <br/>    “id”: “2”, <br/>    “to_do”: “할 일”, <br/>    “writer”: “작성자명”, <br/>    “created_at”: “작성일”, <br/>    “updated_at”: “수정일”<br/>}<br/>]</pre> | 200: 정상 조회
|선택 일정 조회| GET         |/schedules/{id}| id (Long)     |X| X                                                                                             | <pre>{<br/>    “id”: “1”, <br/>    “to_do”: “할 일”, <br/>    “writer”: “작성자명”, <br/>    “created_at”: “작성일”, <br/>    “updated_at”: “수정일”<br/>}</pre>                                                                                                                                                             | 200: 정상 조회 
|일정 수정| PUT, PATCH  |/schedules/{id}| id (Long)|X| <pre>{<br/>    “to_do”: “수정한 일정”, <br/>    “writer”: “작성자명”, <br/>    “password”: “1234"<br/>}</pre> | <pre>{<br/>    “id”: “1”,<br/>    “to_do”: “수정한 일정”, <br/>    “writer”: “작성자명”, <br/>    “created_at”: “작성일”,<br/>    “update_at”: “수정일”<br/>}</pre>                                                                                                                                                             |200: 정상 수정
|일정 삭제|DELETE|/schedules/{id}|id (Long)|X| X                                                                                             | <pre>{<br/>    “msg”: “일정 삭제 완료!”<br/>}</pre>                                                                                                                                                                                                                                                                    | 200: 정상 삭제                                                                                                                                                                                                                                                                 

---
## 프로젝트 작성자
이 프로젝트는 **김태정**이 개발하였습니다.  
🔗 [김태정 GitHub](https://github.com/xaehub)
