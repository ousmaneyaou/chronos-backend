package com.GimPay.Integration_APIs.controllers;

import com.GimPay.Integration_APIs.dtos.WatchDto;
import com.GimPay.Integration_APIs.services.WatchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/watches")
@CrossOrigin(origins = "*")
public class WatchController {

    private final WatchService watchService;

    // Constructeur explicite — remplace @RequiredArgsConstructor si Lombok ne marche pas
    public WatchController(WatchService watchService) {
        this.watchService = watchService;
    }

    @GetMapping
    public ResponseEntity<List<WatchDto.Response>> getAll(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long categoryId) {
        if (search != null && !search.isBlank()) {
            return ResponseEntity.ok(watchService.search(search));
        }
        if (categoryId != null) {
            return ResponseEntity.ok(watchService.getByCategory(categoryId));
        }
        return ResponseEntity.ok(watchService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<WatchDto.Response> getById(@PathVariable Long id) {
        return ResponseEntity.ok(watchService.getById(id));
    }

    @PostMapping
    public ResponseEntity<WatchDto.Response> create(@RequestBody WatchDto.Request request) {
        return ResponseEntity.ok(watchService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<WatchDto.Response> update(@PathVariable Long id,
                                                    @RequestBody WatchDto.Request request) {
        return ResponseEntity.ok(watchService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        watchService.delete(id);
        return ResponseEntity.noContent().build();
    }
}