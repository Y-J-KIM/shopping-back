package com.mysite.shoppingback.Service;



import com.mysite.shoppingback.DTO.*;
import com.mysite.shoppingback.domain.Board;
import com.mysite.shoppingback.domain.User;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.stream.Collectors;

public interface BoardService {
    //새 글을 등록
    Long register(BoardDTO boardDTO);
    //글 조회 (글번호)
    BoardDTO readOne(Long bno);
    //글 수정
    void modify(BoardDTO boardDTO);
    //삭제
    void remove(Long bno);

    PageResponseDTO<BoardDTO> list(PageRequestDTO request);

    //댓글갯수 포함
    PageResponseDTO<BoardListReplyCountDTO> listWithReplyCount(PageRequestDTO pageRequestDTO);

    //댓글갯수 , 이미지 포함
    PageResponseDTO<BoardListAllDTO> listWithAll(PageRequestDTO pageRequestDTO);

    //디폴트 메소드 추가 (디폴트 메소드는 추상메서드가 아니라 구현가능)
    default Board dtoToEntity(BoardDTO boardDTO) {

        Board board = Board.builder()
                .bno(boardDTO.getBno())
                .title(boardDTO.getTitle())
                .content(boardDTO.getContent())
                .writer(convertToEntity(boardDTO.getWriter()))
                .build();

        if(boardDTO.getFileNames() != null){
            boardDTO.getFileNames().forEach(fileName -> {
                String[] arr = fileName.split("_");
                board.addImage(arr[0], arr[1]);
            });
        }
        return board;
    }

    private User convertToEntity(UserDTO userDTO) {
        User user = new User();
        user.setUserId(userDTO.getUserId());
        user.setUsername(userDTO.getUsername());
        // 기타 필요한 필드 설정
        return user;
    }

    private UserDTO convertToDTO(User user) {
        if (user == null) {
            return null; // user가 null인 경우 처리
        }
        return UserDTO.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                // 필요한 추가 필드 설정
                .build();
    }

    default BoardDTO entityToDto(Board board) {
        BoardDTO boardDTO = BoardDTO.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .content(board.getContent())
                .writer(convertToDTO(board.getWriter()))
                .regDate(board.getRegDate())
                .modDate(board.getModDate())
                .build();

        List<String> fileNames =
            board.getImageSet().stream().sorted()
                    .map(boardImage -> boardImage.getUuid()+"_"+boardImage.getFileName())
                    .collect(Collectors.toList());

        boardDTO.setFileNames(fileNames);

        return boardDTO;
    }

}

