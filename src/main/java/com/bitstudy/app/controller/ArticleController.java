package com.bitstudy.app.controller;

import org.springframework.web.bind.annotation.RequestMapping;

/* 할 일 : view 의 EndPoint 관련 컨트롤러

    /articles	                GET	게시판 페이지
    /articles/{article-id}	    GET	게시글 페이지
    /articles/serach	        GET	게시판 검색 전용 페이지
    /articles/serach/hashtag	GET	게시판 해시태그 검색 전용 페이지

    Thymeleaf : 뷰 파일은 HTML 로 작업 할건데, Thymeleaf 을 설치 함으로써 스프링은 이제 html 파일을 마크업 언어로 보지 않고, Thymeleaf 템플릿 파일로 인식한다.
                그래서 이 HTML 파일들을 아무데서나 작성할 수 없고, resources > templates 폴더 안에만 작성 가능하다.
                그 외의 css ,img ,js 들은 resources > static 폴더 안에 작성 가능

*/
@RequestMapping("/articles") // 모든 경로들은 / articles 로 시작하니까 클래스 레벨에 1차로 @RequestMapping("/articles") 걸기
public class ArticleController {

}
