package com.ghm.guesthousemanagementsystem.controller;

import com.ghm.guesthousemanagementsystem.dto.ContentPageDto;
import com.ghm.guesthousemanagementsystem.service.ContentPageService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/content-pages")
@AllArgsConstructor
public class ContentPageController {
    private final ContentPageService pageService;

    @PostMapping
    public ResponseEntity<ContentPageDto> createContentPage(@RequestBody ContentPageDto contentPageDto) {
        return ResponseEntity.ok(pageService.createContentPage(contentPageDto));
    }

    @GetMapping
    public ResponseEntity<List<ContentPageDto>> getAllContentPages() {
        return ResponseEntity.ok(pageService.getAllContentPages());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContentPageDto> getContentPageById(@PathVariable UUID id) {
        return ResponseEntity.ok(pageService.getContentPageById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContentPageDto> updateContentPage(@PathVariable UUID id, @RequestBody ContentPageDto pageDto) {
        return ResponseEntity.ok(pageService.updateContentPage(id, pageDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContentPage(@PathVariable UUID id) {
        pageService.deleteContentPage(id);
        return ResponseEntity.noContent().build();
    }
}
