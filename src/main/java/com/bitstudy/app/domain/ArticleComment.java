package com.bitstudy.app.domain;
import java.time.LocalDateTime;

public class ArticleComment {
    private long id; // 게시글 고유 아이디
    private String content; // 본문

    //메타데이터
    private LocalDateTime createAt; // 생성일자
    private String createBy; // 생성자
    private LocalDateTime modifiedAt; // 수정일자
    private String modifiedBy; // 수정자

    private Article article;
    /* 연관관계 맵핑
        - 연관관계 없이 만들면 private Long articleId; 이런식으로 (관계형 데이터베이스 스타일) 하면 된다.
        - 그런데 우리는 Article 과 ArticleComment 가 관계를 맺고 있는걸 객체지향적으로 표현하려고 이렇게 쓸거다.
    */
}
