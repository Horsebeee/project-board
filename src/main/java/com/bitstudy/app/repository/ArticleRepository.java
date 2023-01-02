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
import com.bitstudy.app.domain.QArticle;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


@RepositoryRestResource
public interface ArticleRepository extends
        JpaRepository<Article, Long>,
        QuerydslPredicateExecutor<Article>,
        QuerydslBinderCustomizer<QArticle> {
        /* 조심 할 것 : QuerydslBinderCustomizer 는 QArticle 을 사용하는데 이건 build.gradle 에서 queryDsl 을 build 하고 와야함 */

        /* 설명 : QuerydslPredicateExecutor 는 Article 안에 있는 모든 필드에 대한 기본 검색기능을 추가해준다.
        *        순서 : 1. 바인딩
        *              2. 검색용 필드를 추가
        * */

    // 1. 바인딩


    @Override
    default void customize(QuerydslBindings bindings, QArticle root) {
        /* 1. 바인딩
              현재 QuerydslPredicateExecutor 때문에 Article 에 있는 모든 필드에 대한  검색이 열려있는 상태이다.
              근데 우리가 원하는건 선택적 필드(제목, 본문, 글쓴이, 해시태그)만 검색에 사용되도록 하고 싶다.
              그래서 선택적으로 검색을 하게 하기 위해서 bindings.excludeUnlistedProperties 를 쓴다.
              excludeUnlistedProperties 는 리스팅을 하지 않은 프로퍼티를 ㄱ검색에 포함할지 말지를 결정할 수 있는 메서드이다.
              true 면 검색에서 제외, false 는 모든 프로퍼티를 열어주는거
        */
        bindings.excludeUnlistedProperties(true);

        /* 2. 검색용(원하는) 필드를 지정(추가) 하는 부분
              including 을 이용해서 title, content, createBy, hashtag 검색 가능하게 만들 예정.
              (id 는 인증기능 달아서 유저 정보를 알아 올 수 있을 때 할 예정)
              including 사용법 : 'root.필드명'

        */
        bindings.including(root.title,root.content,root.createAt,root.createBy,root.hashtag);

        /* 3. 정확한 검색(and 검색)' 만 됐었는데 'or 검색' 가능하게 바꾸기 */
        bindings.bind(root.title).first(StringExpression::containsIgnoreCase); //Like '%${문자열}%'
        bindings.bind(root.content).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.createAt).first(DateTimeExpression::eq);
        // 날짜니까 DateTimeExpression 으로 하고 , eq 는 equals 의 의미. 날짜필드는 정확한 검색만 되도록 설정 근데 이렇게 하면 시분초가 다 0 으로 인식됨 이 부분은 별도로 시간 처리 할 때 건드릴 예정
        bindings.bind(root.createBy).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.hashtag).first(StringExpression::containsIgnoreCase);
    }
}
