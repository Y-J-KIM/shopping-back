package com.mysite.shoppingback.Repository;

import com.mysite.shoppingback.domain.Board;
import com.mysite.shoppingback.Repository.search.BoardSearch;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.Optional;

//JpaRepository 상속후 <테이블클래스명, ID 타입>
public interface BoardRepository extends JpaRepository<Board, Long>, BoardSearch {
    //CRUD 자동 생성
    //커스텀 메소드 만들기
    //Page<Board> findByTitleContainingOrderByBnoDesc(String keyword, Pageable pageable);
    //@Query("select b from Board b where b.title like concat('%',:keyword,'%')")
    //Page<Board> findKeyword(String keyword, Pageable pageable);

    //엔티티 그래프로 imageSet 도 즉시 가져오기
    @EntityGraph(attributePaths = "imageSet")
    @Query("select b from Board b where b.bno = :bno")
    Optional<Board> findByIdWithImages(Long bno);
}
