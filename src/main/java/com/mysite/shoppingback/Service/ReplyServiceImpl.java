package com.mysite.shoppingback.Service;

import com.mysite.shoppingback.DTO.PageRequestDTO;
import com.mysite.shoppingback.DTO.PageResponseDTO;
import com.mysite.shoppingback.DTO.ReplyDTO;
import com.mysite.shoppingback.domain.Board;
import com.mysite.shoppingback.domain.Reply;
import com.mysite.shoppingback.Repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class ReplyServiceImpl implements ReplyService{

    private final ReplyRepository replyRepository;
    private final ModelMapper modelMapper;

    @Override
    public Long register(ReplyDTO replyDTO) {
        //Reply reply = modelMapper.map(replyDTO, Reply.class);
        //log.info(reply);
        Board board = Board.builder().bno(replyDTO.getBno()).build();
        Reply reply = Reply.builder()
                .board(board)
                .replyText(replyDTO.getReplyText())
                .replyer(replyDTO.getReplyer())
                .build();
        Long rno = replyRepository.save(reply).getRno();
        return rno;
    }

    //댓글을 번호로 조회해서 리턴
    @Override
    public ReplyDTO read(Long rno) {
        Optional<Reply> replyOptional = replyRepository.findById(rno);
        Reply reply = replyOptional.orElseThrow(); //예외처리 x
        return modelMapper.map(reply, ReplyDTO.class); //reply 엔티티 => DTO
    }

    //화면에서 수정된 댓글 내용을 DB 에서 찾은 댓글에 업데이트해 저장함
    @Override
    public void modify(ReplyDTO replyDTO) {
        Optional<Reply> replyOptional = replyRepository.findById(replyDTO.getRno());
        Reply reply = replyOptional.orElseThrow();
        reply.changeText(replyDTO.getReplyText()); //DTO 의 댓글내용으로 업데이트
        replyRepository.save(reply);
    }

    @Override
    public void remove(Long rno) {
        replyRepository.deleteById(rno);
    }

    @Override
    public PageResponseDTO<ReplyDTO> getListOfBoard(Long bno, PageRequestDTO pageRequestDTO) {
        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() <= 0?
                0:pageRequestDTO.getPage()-1,
                pageRequestDTO.getSize(),
                Sort.by("rno").ascending());

        Page<Reply> result = replyRepository.listOfBoard(bno, pageable);

        List<ReplyDTO> dtoList = result.getContent().stream()
                .map(reply -> modelMapper.map(reply, ReplyDTO.class))
                .collect(Collectors.toList());
        return PageResponseDTO.<ReplyDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total((int)result.getTotalElements())
                .build();
    }
}
