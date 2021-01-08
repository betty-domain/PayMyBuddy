package com.paymybuddy.webapp.repository;

import com.paymybuddy.webapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    /**
     * Recherche d'un utilisateur par son email, indépendamment de la casse de l'email fourni
     * @param email email recherché
     * @return User, optional, correspondant à la recherche
     */
    public Optional<User> findUserByEmailIgnoreCase(String email);
}
