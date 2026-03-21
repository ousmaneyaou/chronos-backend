package com.GimPay.Integration_APIs.config;

import com.GimPay.Integration_APIs.entity.Category;
import com.GimPay.Integration_APIs.entity.User;
import com.GimPay.Integration_APIs.entity.Watch;
import com.GimPay.Integration_APIs.repository.CategoryRepository;
import com.GimPay.Integration_APIs.repository.UserRepository;
import com.GimPay.Integration_APIs.repository.WatchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final CategoryRepository categoryRepository;
    private final WatchRepository watchRepository;
    private final UserRepository userRepository;

    public DataInitializer(CategoryRepository categoryRepository,
                           WatchRepository watchRepository,
                           UserRepository userRepository) {
        this.categoryRepository = categoryRepository;
        this.watchRepository = watchRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        if (categoryRepository.count() > 0) return;
        log.info("Initialisation des données de démo...");

        Category luxe      = categoryRepository.save(Category.builder().name("Luxe").description("Haute horlogerie suisse").build());
        Category sport     = categoryRepository.save(Category.builder().name("Sport").description("Montres sportives et robustes").build());
        Category classique = categoryRepository.save(Category.builder().name("Classique").description("Élégance intemporelle").build());
        Category connectee = categoryRepository.save(Category.builder().name("Connectée").description("Smartwatches modernes").build());

        // ── Luxe ──
        watchRepository.save(Watch.builder()
                .name("Royal Oak Chronographe").brand("Audemars Piguet")
                .description("Icône de l'horlogerie suisse, cadran bleu, bracelet intégré acier.")
                .price(new BigDecimal("8000")).stock(3).reference("26331ST")
                .material("Acier inoxydable").movement("Automatique Cal. 2385").waterResistance("50m")
                .category(luxe).active(true)
                .imageUrl("https://images.unsplash.com/photo-1523170335258-f5ed11844a49?w=400").build());

        watchRepository.save(Watch.builder()
                .name("Submariner Date").brand("Rolex")
                .description("La référence ultime des montres de plongée.")
                .price(new BigDecimal("1200")).stock(5).reference("126610LN")
                .material("Oystersteel").movement("Automatique Cal. 3235").waterResistance("300m")
                .category(luxe).active(true)
                .imageUrl("https://images.unsplash.com/photo-1547996160-81dfa63595aa?w=400").build());

        watchRepository.save(Watch.builder()
                .name("Calatrava Ref. 5196").brand("Patek Philippe")
                .description("La quintessence de l'élégance classique en or jaune 18K.")
                .price(new BigDecimal("2000")).stock(2).reference("5196J-001")
                .material("Or jaune 18K").movement("Manuel Cal. 215 PS").waterResistance("30m")
                .category(luxe).active(true)
                .imageUrl("https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=400").build());

        // ── Classique ──
        watchRepository.save(Watch.builder()
                .name("Speedmaster Moonwatch").brand("Omega")
                .description("La montre des astronautes, portée sur la Lune en 1969.")
                .price(new BigDecimal("6000")).stock(8).reference("310.30.42.50.01.001")
                .material("Acier inoxydable").movement("Manuel Cal. 3861").waterResistance("50m")
                .category(classique).active(true)
                .imageUrl("https://images.unsplash.com/photo-1587836374828-4dbafa94cf0e?w=400").build());

        watchRepository.save(Watch.builder()
                .name("Santos de Cartier").brand("Cartier")
                .description("Premier bracelet-montre de l'histoire, design iconique.")
                .price(new BigDecimal("7000")).stock(7).reference("WSSA0009")
                .material("Acier & Or jaune").movement("Automatique Cal. 1847 MC").waterResistance("100m")
                .category(classique).active(true)
                .imageUrl("https://images.unsplash.com/photo-1547996160-81dfa63595aa?w=400").build());

        // ── Sport ──
        watchRepository.save(Watch.builder()
                .name("Seamaster 300").brand("Omega")
                .description("Montre de plongée professionnelle, style James Bond.")
                .price(new BigDecimal("5000")).stock(10).reference("234.30.41.21.01.001")
                .material("Acier inoxydable").movement("Automatique Co-Axial").waterResistance("300m")
                .category(sport).active(true)
                .imageUrl("https://images.unsplash.com/photo-1551816230-ef5deaed4a26?w=400").build());

        watchRepository.save(Watch.builder()
                .name("Navitimer B01").brand("Breitling")
                .description("Chrono aviation légendaire avec calculateur de vol.")
                .price(new BigDecimal("5500")).stock(6).reference("AB0137241B1A1")
                .material("Acier inoxydable").movement("Automatique COSC Cal. B01").waterResistance("30m")
                .category(sport).active(true)
                .imageUrl("https://images.unsplash.com/photo-1524592094714-0f0654e20314?w=400").build());

        watchRepository.save(Watch.builder()
                .name("Carrera Chronograph").brand("TAG Heuer")
                .description("Chronographe sportif inspiré de la course automobile.")
                .price(new BigDecimal("5060")).stock(6).reference("CBN2010.BA0642")
                .material("Acier inoxydable").movement("Automatique Calibre Heuer 02").waterResistance("100m")
                .category(sport).active(true)
                .imageUrl("https://images.unsplash.com/photo-1612817159949-195b6eb9e31a?w=400").build());

        // ── Connectée ──
        watchRepository.save(Watch.builder()
                .name("Apple Watch Ultra 2").brand("Apple")
                .description("Smartwatch ultra-robuste pour les sports extrêmes.")
                .price(new BigDecimal("9500")).stock(20).reference("MQDY3LL/A")
                .material("Titane").movement("GPS + Cellular").waterResistance("100m")
                .category(connectee).active(true)
                .imageUrl("https://images.unsplash.com/photo-1434493789847-2f02dc6ca35d?w=400").build());

        if (!userRepository.existsByEmail("ousmane@watchstore.com")) {
            userRepository.save(User.builder()
                    .email("ousmane@watchstore.com")
                    .firstName("Ousmane").lastName("Yaou")
                    .phone("+221778596661").address("Dakar, Sénégal")
                    .build());
        }

        log.info("Données initialisées : 4 catégories, 9 montres, 1 utilisateur");
    }
}