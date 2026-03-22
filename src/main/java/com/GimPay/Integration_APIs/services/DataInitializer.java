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
        this.watchRepository    = watchRepository;
        this.userRepository     = userRepository;
    }

    @Override
    public void run(String... args) {
        if (categoryRepository.count() > 0) return;
        log.info("Initialisation des donnees de demo...");

        // ── Catégories AURUM ──────────────────────────────────────────
        Category cremes  = categoryRepository.save(Category.builder()
                .name("Cremes visage").description("Soins hydratants et eclat").build());
        Category serums  = categoryRepository.save(Category.builder()
                .name("Serums precieux").description("Actifs concentres anti-age").build());
        Category huiles  = categoryRepository.save(Category.builder()
                .name("Huiles et Elixirs").description("Nutrition intense corps et visage").build());
        Category masques = categoryRepository.save(Category.builder()
                .name("Masques royaux").description("Soins intensifs regenerants").build());

        // ── Produits AURUM ────────────────────────────────────────────
        saveP("Creme Absolue Soleil d Or",  "AURUM", "Eclat intense, karite precieux, huile d argan bio certifiee",            new BigDecimal("9000"),  cremes,  50, "https://images.unsplash.com/photo-1598440947619-2c35fc9aa908?w=600");
        saveP("Creme Lumiere Doree",         "AURUM", "Anti-taches, vitamine C pure, acide kojique naturel",                   new BigDecimal("7000"),  cremes,  40, "https://images.unsplash.com/photo-1556228720-195a672e8a03?w=600");
        saveP("Creme Nuit Regeneratrice",    "AURUM", "Reparation cellulaire, retinol encapsule, peptides de soie",            new BigDecimal("2000"), cremes,  35, "https://images.unsplash.com/photo-1571781926291-c477ebfd024b?w=600");
        saveP("Serum Nuit Obsidienne",       "AURUM", "Anti-age profond, retinol pur 0.3%, hyaluronique 3 poids moleculaires", new BigDecimal("4000"), serums,  30, "https://images.unsplash.com/photo-1620916566398-39f1143ab7be?w=600");
        saveP("Elixir Diamant 24 Carats",    "AURUM", "Poudre d or 24K, extrait de caviar, truffe blanche du Perigord",        new BigDecimal("5000"), serums,  15, "https://images.unsplash.com/photo-1608248597279-f99d160bfcbc?w=600");
        saveP("Serum Eclat Perle de Nacre",  "AURUM", "Brightening intense, niacinamide 10%, poudre de perle precieuse",       new BigDecimal("8000"),  serums,  25, "https://images.unsplash.com/photo-1619451334792-150fd785ee74?w=600");
        saveP("Huile Parfumee Ambre Divin",  "AURUM", "Corps et visage, baobab africain, rose musquee du Chili",               new BigDecimal("6000"),  huiles,  45, "https://images.unsplash.com/photo-1556228578-0d85b1a4d571?w=600");
        saveP("Huile Seche Doree",           "AURUM", "Absorption rapide, argousier, figue de barbarie, jojoba dore",          new BigDecimal("1000"),  huiles,  50, "https://images.unsplash.com/photo-1608248597279-f99d160bfcbc?w=600");
        saveP("Masque Nuit Velours Violet",  "AURUM", "Regenerant extreme, collagene marin, acide hyaluronique 3D",            new BigDecimal("3000"),  masques, 20, "https://images.unsplash.com/photo-1571781926291-c477ebfd024b?w=600");
        saveP("Baume Corps Emeraude",        "AURUM", "Hydratation 72h, aloe vera bio, beurre de mangue du Burkina",           new BigDecimal("6000"),  masques, 60, "https://images.unsplash.com/photo-1598440947619-2c35fc9aa908?w=600");

        // ── Utilisateur test ──────────────────────────────────────────
        User user = new User();
        user.setFirstName("Souley Ousmane");
        user.setLastName("YAOU");
        user.setEmail("ousmane@aurum.sn");
        user.setPhone("+221 77 859 66 61");
        user.setAddress("Medina, Dakar, Senegal");
        userRepository.save(user);

        log.info("Donnees initialisees : 4 categories, 10 produits AURUM, 1 utilisateur");
    }

    private void saveP(String name, String brand, String desc, BigDecimal price,
                       Category cat, int stock, String img) {
        Watch w = new Watch();
        w.setName(name);
        w.setBrand(brand);
        w.setDescription(desc);
        w.setPrice(price);
        w.setCategory(cat);
        w.setStock(stock);
        w.setImageUrl(img);
        watchRepository.save(w);
    }
}