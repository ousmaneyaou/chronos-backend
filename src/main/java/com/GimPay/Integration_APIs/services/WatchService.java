package com.GimPay.Integration_APIs.services;

import com.GimPay.Integration_APIs.dtos.WatchDto;
import com.GimPay.Integration_APIs.entity.Category;
import com.GimPay.Integration_APIs.entity.Watch;
import com.GimPay.Integration_APIs.repository.CategoryRepository;
import com.GimPay.Integration_APIs.repository.WatchRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WatchService {

    private final WatchRepository watchRepository;
    private final CategoryRepository categoryRepository;

    // Constructeur explicite — remplace @RequiredArgsConstructor
    public WatchService(WatchRepository watchRepository, CategoryRepository categoryRepository) {
        this.watchRepository = watchRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<WatchDto.Response> getAll() {
        return watchRepository.findByActiveTrue()
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    public WatchDto.Response getById(Long id) {
        return watchRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new RuntimeException("Montre non trouvée: " + id));
    }

    public List<WatchDto.Response> getByCategory(Long categoryId) {
        return watchRepository.findByCategoryIdAndActiveTrue(categoryId)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<WatchDto.Response> search(String query) {
        return watchRepository.search(query)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    public WatchDto.Response create(WatchDto.Request req) {
        Category category = categoryRepository.findById(req.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Catégorie non trouvée: " + req.getCategoryId()));
        Watch watch = Watch.builder()
                .name(req.getName()).brand(req.getBrand())
                .description(req.getDescription()).price(req.getPrice())
                .imageUrl(req.getImageUrl()).stock(req.getStock())
                .reference(req.getReference()).material(req.getMaterial())
                .movement(req.getMovement()).waterResistance(req.getWaterResistance())
                .category(category).active(true)
                .build();
        return toDto(watchRepository.save(watch));
    }

    public WatchDto.Response update(Long id, WatchDto.Request req) {
        Watch watch = watchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Montre non trouvée: " + id));
        Category category = categoryRepository.findById(req.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Catégorie non trouvée: " + req.getCategoryId()));
        watch.setName(req.getName());
        watch.setBrand(req.getBrand());
        watch.setDescription(req.getDescription());
        watch.setPrice(req.getPrice());
        watch.setImageUrl(req.getImageUrl());
        watch.setStock(req.getStock());
        watch.setReference(req.getReference());
        watch.setMaterial(req.getMaterial());
        watch.setMovement(req.getMovement());
        watch.setWaterResistance(req.getWaterResistance());
        watch.setCategory(category);
        return toDto(watchRepository.save(watch));
    }

    public void delete(Long id) {
        Watch watch = watchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Montre non trouvée: " + id));
        watch.setActive(false);
        watchRepository.save(watch);
    }

    public WatchDto.Response toDto(Watch w) {
        return WatchDto.Response.builder()
                .id(w.getId()).name(w.getName()).brand(w.getBrand())
                .description(w.getDescription()).price(w.getPrice())
                .imageUrl(w.getImageUrl()).stock(w.getStock())
                .reference(w.getReference()).material(w.getMaterial())
                .movement(w.getMovement()).waterResistance(w.getWaterResistance())
                .categoryId(w.getCategory() != null ? w.getCategory().getId() : null)
                .categoryName(w.getCategory() != null ? w.getCategory().getName() : null)
                .active(w.getActive()).createdAt(w.getCreatedAt())
                .build();
    }
}