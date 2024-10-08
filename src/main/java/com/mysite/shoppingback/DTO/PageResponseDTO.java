package com.mysite.shoppingback.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class PageResponseDTO<E> {

    private int page;
    private int size;
    private int total;

    //시작페이지 번호
    private int start;
    //끝페이지 번호
    private int end;
    //이전페이지 존재
    private boolean prev;
    //다음페이지 존재
    private boolean next;

    private List<E> dtoList; //제네릭 타입으로 클래스 타입 가능

    //생성자로써 페이지리퀘스트 객체, 데이터리스트, 전체데이터 갯수로 페이지네이션 계산!
    @Builder(builderMethodName = "withAll")
    public PageResponseDTO(PageRequestDTO pageRequestDTO, List<E> dtoList, int total){
        if(total <= 0){
            return; //데이터행이 없을경우 그냥 리턴(1페이지도 없음)
        }
        this.page = pageRequestDTO.getPage();
        this.size = pageRequestDTO.getSize();
        this.total = total;
        this.dtoList = dtoList;

        this.end = (int)(Math.ceil(this.page / 10.0)) * 10; //화면의 마지막 번호
        this.start = this.end -9;                           //화면의 시작번호

        int last = (int)(Math.ceil((total/(double)size))); //마지막 페이지번호

        this.end = end > last ? last : end;
        this.prev = this.start > 1;
        this.next = total > this.end * this.size;
    }

}
