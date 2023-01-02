package com.bitstudy.app.repository;

/* 할 일 : TDD 를 위해서 임시로 만들어 놓은 저장소 (DB 접근 하기위한 파일)
*
* - TDD 만드는 방법
*   1) 우클릭 > Go to > Test (ctrl + shift + T)
*   2) Junit5 버전인지 확인
*
* */

/* HAL 확인해보기. 서비스 실행하고, 브라우저에서 localhost:8080/ 넣기
*
*  테스트 만들기 (test > controller > DataRestTest.java)
*
* */

import com.bitstudy.app.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


@RepositoryRestResource
public interface ArticleRepository extends JpaRepository<Article, Long> {

}
