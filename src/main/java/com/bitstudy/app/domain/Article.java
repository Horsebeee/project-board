package com.bitstudy.app.domain;

import java.time.LocalDateTime;

public class Article {
    private long id; // 게시글 고유 아이디
    private String title; // 제목
    private String content; // 본문
    private String hashtag; // 해시태그

    //메타데이터
    private LocalDateTime createAt; // 생성일자
    private String createBy; // 생성자
    private LocalDateTime modifiedAt; // 수정일자
    private String modifiedBy; // 수정자
}
