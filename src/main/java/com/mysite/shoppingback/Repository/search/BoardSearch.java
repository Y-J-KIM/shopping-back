package com.mysite.shoppingback.Repository.search;

import com.mysite.shoppingback.DTO.BoardListAllDTO;
import com.mysite.shoppingback.DTO.BoardListReplyCountDTO;
import com.mysite.shoppingback.domain.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface BoardSearch {

    Page<Board> search1(Pageable pageable);

    Page<Board> searchAll(String[] types, String keyword, Pageable pageable);

    Page<BoardListReplyCountDTO> searchWithReplyCount(String[] types,
                                                      String keyword,
                                                      Pageable pageable);

    Page<BoardListAllDTO> searchWithAll(String[] types,
                                        String keyword,
                                        Pageable pageable);
}