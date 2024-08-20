package com.mysite.shoppingback.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "board")
public class BoardImage implements Comparable<BoardImage> {

    @Id
    private String uuid;     //고유 ID
    private String fileName; //파일이름
    private int ord; //순번

    @ManyToOne
    private Board board; //여러개이미지 == 한개 게시글

    @Override
    public int compareTo(BoardImage other) {
        return this.ord - other.ord; //이미지객체의 순서를 순번으로 정함
    }

    public void changeBoard(Board board) {
        this.board = board;
    }
}
