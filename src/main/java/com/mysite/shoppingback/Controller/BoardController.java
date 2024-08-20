package com.mysite.shoppingback.Controller;

import com.mysite.shoppingback.DTO.BoardDTO;
import com.mysite.shoppingback.DTO.PageRequestDTO;
import com.mysite.shoppingback.DTO.PageResponseDTO;
import com.mysite.shoppingback.Service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@RestController
@RequestMapping("/api/board")
@Log4j2
@RequiredArgsConstructor
public class BoardController {

    @Value("${org.zerock.upload.path}")
    private String uploadPath;

    private final BoardService boardService;

//    @GetMapping("/list")
//    public void list(PageRequestDTO pageRequestDTO, Model model) {
//        //controller에서 리턴이 void 일때 요청주소 /board/list와 같은 template html이 표시됨
//        PageResponseDTO<BoardListAllDTO> responseDTO =
//                boardService.listWithAll(pageRequestDTO);
//        log.info(responseDTO);
//        model.addAttribute("responseDTO", responseDTO);//화면에 전달
//    }

    //@PreAuthorize("hasRole('USER')")
    @GetMapping("/register")
    public void register() {

    }

//    @PostMapping("/register")
//    public String registerPost(@Valid BoardDTO boardDTO,
//                               BindingResult bindingResult,
//                               RedirectAttributes redirectAttributes) {
//        log.info("board Post register......");
//        if (bindingResult.hasErrors()) {
//            log.error("errors.....");
//            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
//            return "redirect:/board/register";
//        }
//        log.info(boardDTO.toString());
//
//        Long bno = boardService.register(boardDTO);
//        redirectAttributes.addFlashAttribute("result", bno);
//
//        return "redirect:/board/list";
//    }
//
//    //board/read.html , board/modify.html
//    @PreAuthorize("isAuthenticated()") //인증된 유저만 게시글 읽기, 수정하기 가능
//    @GetMapping({"/read","/modify"})
//    public void read(Long bno, PageRequestDTO pageRequestDTO, Model model) {
//        BoardDTO boardDTO = boardService.readOne(bno);
//        log.info(boardDTO.toString());
//        model.addAttribute("dto", boardDTO);
//    }
//
//    @PreAuthorize("principal.username == #boardDTO.writer")
//    @PostMapping("/modify")
//    public String modify(PageRequestDTO pageRequestDTO,
//                         @Valid BoardDTO boardDTO,
//                         BindingResult bindingResult,
//                         RedirectAttributes redirectAttributes) {
//        log.info("board Modify Post......");
//        String link = pageRequestDTO.getLink(); //페이지 유지
//        long bno = boardDTO.getBno();
//        link = link + "&bno=" + bno; //링크에 bno 추가
//
//        if(bindingResult.hasErrors()) {
//            log.error("errors.....");
//            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
//            redirectAttributes.addFlashAttribute("bno", bno);
//            //유효성검사 에러시 다시 수정페이지로 돌아감
//            return "redirect:/board/modify?"+link;
//        }
//        boardService.modify(boardDTO);
//        redirectAttributes.addFlashAttribute("result","modified");
//        redirectAttributes.addFlashAttribute("bno", bno);
//        return "redirect:/board/read?"+link; //수정후 읽기 페이지로 이동
//    }
//
//    @PreAuthorize("principal.username == #boardDTO.writer")
//    @PostMapping("/remove")
//    public String remove(BoardDTO boardDTO, RedirectAttributes redirectAttributes) {
//        Long bno = boardDTO.getBno();
//        log.info("remove post.."+ bno);
//
//        boardService.remove(bno);
//
//        //게시물이 데이터베이스상에서 삭제되었다면 첨부파일 삭제
//        log.info(boardDTO.getFileNames());
//        List<String> fileNames = boardDTO.getFileNames();
//        if(fileNames != null && fileNames.size() > 0) {
//            removeFiles(fileNames);
//        }
//
//        redirectAttributes.addFlashAttribute("result","removed");
//
//        return "redirect:/board/list";
//    }

    // 게시글 목록 조회 (페이징, 검색 포함)
    @GetMapping("/list")
    public ResponseEntity<PageResponseDTO<BoardDTO>> list(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam(required = false) String keyword) {

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(page)
                .size(size)
                .keyword(keyword)
                .build();

        PageResponseDTO<BoardDTO> responseDTO = boardService.list(pageRequestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/register")
    public ResponseEntity<Long> register(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("writer") String writer,
            @RequestParam(value = "image", required = false) MultipartFile image) {

        BoardDTO boardDTO = BoardDTO.builder()
                .title(title)
                .content(content)
                .writer(writer)
                .build();

        if (image != null && !image.isEmpty()) {
            String imagePath = saveImage(image);
            boardDTO.setImagePath(imagePath);
        }

        Long bno = boardService.register(boardDTO);
        return ResponseEntity.ok(bno);
    }

    private String saveImage(MultipartFile image) {
        String uploadDir = "path/to/upload/dir";
        String originalFilename = image.getOriginalFilename();
        String filePath = Paths.get(uploadDir, originalFilename).toString();

        try {
            Files.copy(image.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to store image file " + originalFilename, e);
        }

        return filePath;
    }

    @GetMapping("/read")
    public ResponseEntity<BoardDTO> read(@RequestParam("bno") Long bno) {
        BoardDTO boardDTO = boardService.readOne(bno);
        return ResponseEntity.ok(boardDTO);
    }

    @PostMapping("/modify")
    public ResponseEntity<Void> modifyBoard(@RequestBody BoardDTO boardDTO) {
        boardService.modify(boardDTO);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/remove")
    public ResponseEntity<Void> removeBoard(@RequestBody Long bno) {
        boardService.remove(bno);
        return ResponseEntity.ok().build();
    }


    private void removeFiles(List<String> files) {
        for (String fileName : files) {
            Resource resource = new FileSystemResource(uploadPath + File.separator + fileName);

            String resourceName = resource.getFilename();
            try {
                String contentType = Files.probeContentType(resource.getFile().toPath());
                resource.getFile().delete();
                //썸네일이 존재한다면
                if (contentType.startsWith("image")) {
                    File thumbnailFile = new File(uploadPath + File.separator +"s_" + fileName);
                    thumbnailFile.delete();
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        } //end for
    }

}
