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

    private Category makeCategory(String name, String description) {
        Category c = new Category();
        c.setName(name);
        c.setDescription(description);
        return categoryRepository.save(c);
    }

    @Override
    public void run(String... args) {
        if (categoryRepository.count() > 0) return;

        log.info("Initialisation des données VELOUR...");

        // ── Catégories ────────────────────────────────────────────────
        Category floral    = makeCategory("Floral",    "Notes florales délicates et féminines");
        Category oriental  = makeCategory("Oriental",  "Notes chaudes, épicées et envoûtantes");
        Category aquatique = makeCategory("Aquatique", "Notes marines fraîches et légères");
        Category boise     = makeCategory("Boisé",     "Notes boisées profondes et élégantes");

        // ── Parfums VELOUR ─────────────────────────────────────────────
        watchRepository.save(Watch.builder()
                .name("Aube Blanche").brand("VELOUR").category(floral)
                .description("Une fragrance aérienne qui évoque la légèreté du matin. Rose de Grasse, iris de Florence et musc blanc se mêlent dans une danse délicate.")
                .price(new BigDecimal("5000")).stock(50)
                .imageUrl("https://images.unsplash.com/photo-1588405748880-12d1d2a59f75?w=600")
                .build());

        watchRepository.save(Watch.builder()
                .name("Nuit d'Orient").brand("VELOUR").category(oriental)
                .description("Un voyage sensoriel vers les souks parfumés. Oud précieux, ambre gris et vanille de Madagascar créent un sillage inoubliable.")
                .price(new BigDecimal("1000")).stock(30)
                .imageUrl("https://images.unsplash.com/photo-1592945403244-b3fbafd7f539?w=600")
                .build());

        watchRepository.save(Watch.builder()
                .name("Écume de Sel").brand("VELOUR").category(aquatique)
                .description("La fraîcheur de l'océan capturée dans un flacon. Bergamote de Calabre, cèdre bleu et sel marin pour une légèreté absolue.")
                .price(new BigDecimal("4000")).stock(40)
                .imageUrl("https://images.unsplash.com/photo-1541643600914-78b084683702?w=600")
                .build());

        watchRepository.save(Watch.builder()
                .name("Vétiver Noir").brand("VELOUR").category(boise)
                .description("La noblesse du vétiver d'Haïti magnifiée par le bois de santal et le poivre rose. Un parfum masculin d'une rare élégance.")
                .price(new BigDecimal("6000")).stock(35)
                .imageUrl("https://images.unsplash.com/photo-1563170351-be82bc888aa4?w=600")
                .build());

        watchRepository.save(Watch.builder()
                .name("Jasmin Absolu").brand("VELOUR").category(floral)
                .description("L'essence pure du jasmin sambac, cueillie à l'aube. Une féminité absolue rehaussée de patchouli et de benjoin doré.")
                .price(new BigDecimal("7000")).stock(25)
                .imageUrl("https://images.unsplash.com/photo-1594035910387-fea47794261f?w=600")
                .build());

        watchRepository.save(Watch.builder()
                .name("Ambre Solaire").brand("VELOUR").category(oriental)
                .description("Un accord chaud et sensuel d'ambre, de résine de labdanum et d'héliotrope. La chaleur du soleil sur la peau.")
                .price(new BigDecimal("1500")).stock(30)
                .imageUrl("https://images.unsplash.com/photo-1585386959984-a4155224a1ad?w=600")
                .build());

        watchRepository.save(Watch.builder()
                .name("Iris Poudré").brand("VELOUR").category(floral)
                .description("La sophistication de l'iris de Florence dans toute sa splendeur. Racine d'iris, violette et cèdre de Virginie.")
                .price(new BigDecimal("2500")).stock(20)
                .imageUrl("https://images.unsplash.com/photo-1596462502278-27bfdc403348?w=600")
                .build());

        watchRepository.save(Watch.builder()
                .name("Bois de Santal").brand("VELOUR").category(boise)
                .description("La pureté crémeuse du santal de Mysore sublimée par la fève tonka et l'encens. Un classique intemporel.")
                .price(new BigDecimal("3000")).stock(35)
                .imageUrl("https://images.unsplash.com/photo-1590736969596-4c04f4e5e527?w=600")
                .build());

        watchRepository.save(Watch.builder()
                .name("Rose Centifolia").brand("VELOUR").category(floral)
                .description("La reine des roses dans un écrin de luxe. Rose centifolia de Grasse, pivoine et litchi pour une romantique absolue.")
                .price(new BigDecimal("2000")).stock(15)
                .imageUrl("https://images.unsplash.com/photo-1523293182086-7651a899d37f?w=600")
                .build());

        // ── Utilisateur démo ───────────────────────────────────────────
        userRepository.save(User.builder()
                .firstName("Ousmane").lastName("Yaou")
                .email("ousmane@velour.com").address("Medina, Dakar")
                .build());

        log.info("Données initialisées : 4 catégories, 9 parfums, 1 utilisateur");
    }
}