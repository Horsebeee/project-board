package com.bitstudy.app.repository;

import com.bitstudy.app.config.JpaConfig;
import com.bitstudy.app.domain.Article;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.INT_2D_ARRAY;

@DataJpaTest
/** Slide Test 란 지난번 TDD 때 각 메서드들이 다 남남으로 서로를 알아보지 못하게 만들었었다.
 *  이것처럼 메서드들을 각각 테스트한 결과를 서로 못보게 잘라서 만드는 것
 *
 * */
@Import(JpaConfig.class)
/*
    원래대로 라면 JPA 에서 모든 정보를 컨트롤 해야하는데 JpaConfig 의 경우는 읽어오지 못한다.
    이유는 이건 시스템에서 만든게 아니라 우리가 별도로 만든 파일이기 때문. 그래서 따로 import 를 걸어줘야 한다.
    안걸어 주면 config 안에 명시해놨던 JpaConfig 기능이 동작하지 않는다.
*/

class JpaRepositoryTest {

    private final ArticleRepository articleRepository;
    private final ArticleCommentRepository articleCommentRepository;
    /*
        원래는 둘다 @Autowired 가 붙어야 하는데,
        Junit5 버전 과 최신 버전의 스프링 부트를 이용하면 Test 에서 생성자 주입패턴을 사용할 수 있다.

    */

    /* 생성자 만들기 - 여기서는 다른 파일에서 매개변수로 보내주는걸 받는거라서 위에랑 상관 없이 @Autowired 를 붙여야 한다. */
    public JpaRepositoryTest(@Autowired ArticleRepository articleRepository, @Autowired ArticleCommentRepository articleCommentRepository) {
        this.articleRepository = articleRepository;
        this.articleCommentRepository = articleCommentRepository;
    }

    /**  - 트랜잭션시 사용하는 메서드
        사용법 : repository명.메서드()
        1) findAll() - 모든 컬럼을 조회 할 때 사용. 페이징(pageable) 가능
                        당연히 select 작업을 하지만, 잠깐 사이에 해당 테이블에 어떤 변화가 있었는지 알 수 없기 때문에 select 전에
                        먼저 최신 데이터를 잡기 위해서 update 를 한다.
                        동작 순서 : update -> select

        2) findById() - 한 건에 대한 데이터 조회시 사용
                        primary key 로 레코드 한 건 조회.
        3) save() - 레코드 저장 할 때 사용 (insert, update)
        4) count() - 레코드 개수 뽑을 때 사용
        5) delete() - 레코드 삭제

        ----------------------------------------------------------------------------------------------------------------


    */


    /* select 테스트 */
    @Test
    @DisplayName("select 테스트")
    void selectTest() {
    /* 셀렉팅을 할거니까 articleRepository 를 기준으로 테스트 할거임.
       maven 방식 : dao > mapper 로 정보를 보내고 DB 갔다 와서 C 까지 돌려 보낼껀데 dao 에서 DTO 를 list 에 담아서 return
    */

       List<Article> articles =  articleRepository.findAll();
       /* assertJ 를 이용해서 테스트
          articles 가 NotNull 이고 사이즈가 ?? 개 이면 통과
       */
       assertThat(articles).isNotNull().hasSize(100);
    }

    /* insert 테스트 */
    @Test
    @DisplayName("insert 테스트")
    void insertTest() {
        // 기존 카운트 구하기
        Long prevCount = articleRepository.count();

        // insert 하기
        articleRepository.save(Article.of("제목","내용","레드"));

        // 기존꺼랑 현재꺼랑 개수 차이 구하기
        assertThat(articleRepository.count() - prevCount).isEqualTo(1);

        /* !! 주의 : 이 상태로 테스트를 돌리면 createAt 이거 못찾는다고 에러남.
              이유 : japConfig 파일에 auditing 을 쓰겠다고 세팅을 해놨는데,
                    해당 엔티티(Article.java) 에서 auditing 을 쓴다고 명시를 안해놓은 상태라서,
                    엔티티 가서 클래스 레벨로 @EntityListeners(AuditingEntityListener.class) 걸어주자
        */

    }

    /* update 테스트 */
    @Test
    @DisplayName("update 테스트")
    void updateTest() {
        /* 기존 데이터 하나 있어야 되고, 그걸 수정 했을때 어떻게 되는지 관찰
            1) 기존의 영속성 컨텍스트로부터 하나의 엔티티 객체를 가져온다.
            2) 업데이트로 해시태그를 바꾸기

        */
        /*
            순서 1) 기존의 영속성 컨텍스트로부터 하나의 엔티티 객체를 가져온다. (DB에서 한줄 뽑아오기)
                    articleRepository -> 기존의 영속성 컨텍스트로부터
                    findById(1L) -> 하나의 엔티티 객체를 가져온다.
                    orElseThrow() -> 없으면 일단 throw 시켜서 일단 테스트가 끈나게 하자. // 없어도 되긴해

                2) 업데이트로 해시태그를 바꾸기
                    엔티티에 있는 setter 를 이용해서 updateHashtag 에 있는 문자열로 업데이트 하기
                    1. 변수 updateHashtag 에 바꿀 문자열 저장
                    2. 엔티티(article) 에 있는 setter 를 이용해서 변수 updateHashtag 에 있는 문자열을 넣고 (해시태그 바꿀꺼니깐 setHashtag)
                    3. 데이터 베이스에 업데이트 하기


                3) 위에서 바꾼 articleSaved 에 업데이트 된 hashtag 필드에 updateHashtag 에 저장되어 있는 값 ("#asdf") 이 있는지 확인

        */

         Article articles = articleRepository.findById(1L).orElseThrow();

         String updateHashtag = "#asdf";
         articles.setHashtag(updateHashtag);

//         articleRepository.save(articles);
         Article articleSaved = articleRepository.saveAndFlush(articles);

         /*
            save 로 놓고 테스트를 돌리면 콘솔(Run) 탭에 update 구문이 나오지 않고 select 구문만 나온다.
            이유는 영속성 컨텍스트로부터 가져온 데이터를 그냥 sve 만 하고 아무것도 하지않고 끝내버리면 어짜피 롤백 되니까 스프링부트는 다시 원래의 값으로 돌아가질거다.
            그래서 그냥 했다 치고 update 를 하지 않는다.(코드의 유효성은 확인)
            그래서 save 를 하고 flush 를 해줘야 한다.

         */

        /** flush 란 (push 같은 것)
         *  1. 변경점 감지
         *  2. 수정된 Entity 를 sql 저장소에 등록
         *  3. sql 저장소에 있는 쿼리를 DB 에 전송
         * */

        assertThat(articleSaved).hasFieldOrPropertyWithValue("hashtag",updateHashtag);
    }

    /* delete 테스트 */
    @Test
    @DisplayName("delect 테스트")
    void deleteTest() {
        /*
            기존의 데이터들이 있다고 치고, 그중에 값을 하나 꺼내고, 지워야 한다.
            1) 기존의 영속성 컨텍스트로부터 하나의 엔티티 객체를 가져온다. => findById()
            2) 지우면 DB 에서 하나 사라지기 때문에 count 를 구해놓고 => articleRepository.count()
            3) delete 하고 (-1) => articleRepository.delete()
            4) 2번에서 구한 count 와 지금 순간의 개수를 비교해서 1 차이나면 테스트 통과 => .isEqualTO()

        */
        // 데이터 하나 가져오기
        Article articles = articleRepository.findById(1L).orElseThrow();
        // 총 개수 구하기
        Long deleteTestPrevCount = articleRepository.count();
        Long deleteTestCommentPrevCount = articleCommentRepository.count();
        int deleteCommentSize = articles.getArticleComments().size();
        System.out.println("게시글 번호가 1번인 댓글의 개수 : " + deleteCommentSize);
        // 삭제하기
         articleRepository.delete(articles);
        // 아까 개수와 지금 개수 비교해서 1차이가 나면 테스트 완료
        assertThat(deleteTestPrevCount - articleRepository.count()).isEqualTo(1);
        assertThat(articleCommentRepository.count()).isEqualTo(deleteTestCommentPrevCount - deleteCommentSize);
    }


}