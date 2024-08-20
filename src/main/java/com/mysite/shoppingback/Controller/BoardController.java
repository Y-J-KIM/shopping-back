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

    @Value("${com.mysite.upload.path}")
    private String uploadPath;

    private final BoardService boardService;

    //@PreAuthorize("hasRole('USER')")
    @GetMapping("/register")
    public void register() {

    }

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
        String uploadDir = "C:/upload";
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
