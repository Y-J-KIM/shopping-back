package com.mysite.shoppingback.Repository;
import com.mysite.shoppingback.domain.Reply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface ReplyRepository extends JpaRepository<Reply, Long> {
    //게시글 번호로 검색하므로 쿼리문을 JPA Query 로 만든다.
    //쿼리문이 객체 형식으로 r은 댓글객체 r.board.bno (게시글 번호) :bno (입력변수)
    @Query("select r from Reply r where r.board.bno = :bno")
    Page<Reply> listOfBoard(Long bno, Pageable pageable);

    //게시글에 해당하는 댓글들을 모두 삭제
    void deleteByBoard_Bno(Long bno);
}
