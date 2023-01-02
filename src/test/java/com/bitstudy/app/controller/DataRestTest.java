package com.bitstudy.app.controller;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
     슬라이스 테스트: 기능별(레이어별)로 잘라서 특정 부분(기능)만 테스트하는것

     - 통합 테스트 어노테이션
     @SpringBootTest - 스프링이 관리하는 모든 빈을 등록시켜서 테스트 하기 때문에 무겁다
                       * 테스트할 가볍게 하기 위해서 @WebMvcTest 를 사용해서 web 레이어 관련 빈들만 등록한 상태로 테스트를 할 수도 있다.
                         단, web 레이어 관련된 빈들만 등록되므로 Service 는 등록되지 않는다. 그래서 Mock 관련 어노테이션을 이용해서 가짜로 만들어줘야 한다.

     - 슬라이스 테스트 어노테이션
     1) @WebMvcTest - 슬라이스 테스트에서 대표적인 어노테이션
                      Controller 를 테스트 할 수 있도록 관련 설정을 제공해준다.
                      @WebMvcTest 를 선언하면 web 과 관련된 Bean 만 주입되고, MockMvc 를 알아볼 수 있게 된다.

                      * MockMvc 는 웹 어플리케이션을 어플리케이션 서버에 배포하지 않고, 가짜로 테스트용 MVC 환경을 만들어서 요청 및 전송, 응답기능을 제공해주는 유틸리티 클래스.
                        간단히 말하면, 내가 컨트롤러 테스트 하고 싶을때 실제 서버에 올리지 않고 테스트 용으로 시뮬레이션 해서 MVC 가 되도록 해주는 클래스

                      * 그냥 컨트롤러 슬라이스 테스트 한다고 하면 @WebMvcTest 랑 MockMvc 쓰면 됨

     2) @DataJpaTest - JPA 레포지토리 테스트 할때 사용
                       @Entity 가 있는 엔티티 클래스들을 스캔해서 테스를 위한 JPA 레포지토리들을 설정

                       * @Component 나 @ConfigurationProperties Bean 들은 무시

    3) @RestClientTest - (클라이언트 입장에서의) API 연동 테스트
                          테스트 코드 내에서 Mock 서버를 띄울 수 있다. (response, request 에 대한 사전정의가 가능)




*/
/* 통합테스트로 변경해서 테스트 할 예정 */

//@WebMvcTest

    /* 일단 이 테스트는 실패해야 정상임. 이유는 해당 api 를 찾을수 없기 때문
       콘솔창에 MockHttpServletRequest 부분에 URI="/api/articles 있을거다. 복사해서 브라우저에 http://localhost:8080/api/articles
       넣어보면 데이터가 제대로 나온다.

       그럼 왜 여기선 안되냐면, @WebMvcTest 는 슬라이스 테스트이기 때문에 controller 외의 빈들은 로드하지 않았기 떄문이다.
       그래서 일단 @WebMvcTest 대신 통합테스트(@SpringBootTest)로 돌릴 예정
    */

@Disabled("Spring Data Rest 통합 데이터는 현재 불필요하므로 제외시킴.")
/* 클래스 레벨에 붙여서 해당 테스트 클래스의 모든 메서드들을 체크하지 않게 한다.
 이유는 테스트가 다 통과된걸 확인했고, 당장 개발하는데 계속 돌릴 필요 없기 때문에 */
@SpringBootTest //이것만 있으면 MockMvc 를 알아볼수가 없어서 @AutoConfigureMockMvc 같이 써주기
@AutoConfigureMockMvc
@Transactional /* 테스트를 돌리면 Hibernate 부분에 select 쿼리문이 나오면서 실제 DB 를 건드리는데, 테스트 끝난 이후에 DB를 롤백 시키는 용도 */
    public class DataRestTest {

    /*
        MockMvc 테스트 방법
            1) MockMvc 생성 (빈 준비)
            2) MockMvc 에게 요청에 대한 정보를 입력(주입)
            3) 요청에 대한 응답값을 expect 를 이용해서 테스트 한다.
            4) expect 다 통과 하면 테스트 통과
    */

    private final MockMvc mvc; /* 1) MockMvc 생성 (빈 준비)*/

    public DataRestTest(@Autowired MockMvc mvc) { /* 2) MockMvc 에게 요청에 대한 정보를 입력(주입) */
        this.mvc = mvc;
    }

    /*  [api] - 게시글 리스트 전체 조회 */
    @DisplayName("[api] - 게시글 리스트 전체 조회")
    @Test
    void articleAll() throws Exception {


//        mvc.perform(get("/api/articles"))
//                .andExpect(status().isOk()) // 현재 200이 들어왔는지 확인
//                                            // MockMvcResultMatchers.status
//                .andExpect(content().contentType(MediaType.valueOf("application/hal+json")));

        mvc.perform(get("/api/articles"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("application/hal+json")));

        /** 특별한 import!! (딥다이브)
         1) perform() 안에 get 치고 ctrl + space 누르면 딥다이브 함
         * 그냥 기본으로나오는건 getClass() 인데 그거 엔터치지 말고, ctrl + space 하기
         그러면 다른 방식의 추천들이 엄청 나오는데 그중에
         MockMvcRequestBuilders.get 이라는거 선택할건데, 엔터 치지 말고
         Alt + Enter (static import 를 하기 위해서) 해서 넣기
         그러면 맨 위에 import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get; 생김
         *static import 란 필드나 메서드를 클래스로 지정하지 않고도 코드에서 사용할 수 있도록 하는 기능.

         2) andExpect(status) 부분 설명
         status 치고 ctrl + space 두세번 하면 MockMvcResultMatchers.status() 나옴.
         그거 alt + Enter 로 해서 static import 하기

         3) andExpect(content().contentType()) 부분 설명
         content 검사는 contentType 으로 하고 MediaType 사용함.
         valueOf 안에 들어갈 content-type 은 아까 HAL 의 Response Headers 에 있는 content-type 에 있는거 복사해오기
         * */
    }
    /*  [api] - 게시글 하나 조회 */
    @DisplayName("[api] - 게시글 하나 조회")
    @Test
    void articleOne() throws Exception {

        mvc.perform(get("/api/articles/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("application/hal+json")));
    }

    /*  [api] - 댓글 리스트 전체 조회 */
    @DisplayName("[api] - 댓글 리스트 전체 조회")
    @Test
    void articleCommentAll() throws Exception {

        mvc.perform(get("/api/articleComments"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("application/hal+json")));
    }

    /*  [api] - 댓글 하나 조회 */
    @DisplayName("[api] - 댓글 하나 조회")
    @Test
    void articleCommentOne() throws Exception {

        mvc.perform(get("/api/articleComments/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("application/hal+json")));
    }

    /*  [api] - 게시글 번호가 1번인 댓글 전체 조회 */
    @DisplayName("[api] - 게시글 번호가 1번인 댓글 리스트 조회")
    @Test
    void articleNumberOneArticleCommentAll() throws Exception {

        mvc.perform(get("/api/articles/1/articleComments"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("application/hal+json")));
    }
}