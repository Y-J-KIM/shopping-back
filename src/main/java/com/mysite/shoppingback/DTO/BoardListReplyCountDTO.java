package com.mysite.shoppingback.DTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BoardListReplyCountDTO {
    //리스트 화면목록에 표시되는 내용
    private Long bno;
    private String title;
    private String writer;
    private LocalDateTime regDate;

    private Long replyCount; // 댓글 갯수
}
