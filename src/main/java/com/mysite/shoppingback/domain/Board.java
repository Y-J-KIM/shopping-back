package com.mysite.shoppingback.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.util.HashSet;
import java.util.Set;

//entity 는 테이블과 같은 클래스
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "imageSet")
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;       //보드번호 (기본키 , 자동증가)

    @Column(length = 500, nullable = false)
    private String title;   //제목

    @Column(length = 2000, nullable = false)
    private String content; //내용

    @Column(length = 50, nullable = false)
    private String writer;  //글쓴이

    @OneToMany(mappedBy = "board",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true )
    @Builder.Default
    @BatchSize(size = 20)
    private Set<BoardImage> imageSet = new HashSet<>(); //중복안되는 이미지객체 리스트

    public void change(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void addImage(String uuid, String fileName) {
        BoardImage boardImage = BoardImage.builder()
                .uuid(uuid)
                .fileName(fileName)
                .board(this)
                .ord(imageSet.size())
                .build();
        imageSet.add(boardImage);
    }

    public void clearImages(){
        imageSet.forEach(boardImage -> boardImage.changeBoard(null));
        this.imageSet.clear();
    }
}
