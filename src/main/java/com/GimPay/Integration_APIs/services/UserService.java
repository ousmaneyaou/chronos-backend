package com.GimPay.Integration_APIs.services;

import com.GimPay.Integration_APIs.dtos.UserDto;
import com.GimPay.Integration_APIs.entity.User;
import com.GimPay.Integration_APIs.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Crée un utilisateur OU retourne l'existant si l'email est déjà pris.
     * Simule une connexion par email sans système d'authentification complet.
     */
    public UserDto.Response createOrLogin(UserDto.CreateRequest req) {
        Optional<User> existing = userRepository.findByEmail(req.getEmail());
        if (existing.isPresent()) {
            return toDto(existing.get());
        }
        User user = new User();
        user.setEmail(req.getEmail());
        user.setFirstName(req.getFirstName() != null ? req.getFirstName() : "Client");
        user.setLastName(req.getLastName() != null ? req.getLastName() : "");
        user.setPhone(req.getPhone());
        user.setAddress(req.getAddress());
        user.setCreatedAt(LocalDateTime.now());
        user = userRepository.save(user);
        return toDto(user);
    }

    public UserDto.Response getById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé: " + id));
        return toDto(user);
    }

    private UserDto.Response toDto(User user) {
        UserDto.Response dto = new UserDto.Response();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setPhone(user.getPhone());
        dto.setAddress(user.getAddress());
        dto.setCreatedAt(user.getCreatedAt());
        return dto;
    }
}