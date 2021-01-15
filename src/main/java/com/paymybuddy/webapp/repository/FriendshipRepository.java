package com.paymybuddy.webapp.repository;

import com.paymybuddy.webapp.model.Friendship;
import com.paymybuddy.webapp.model.FriendshipId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, FriendshipId> {

    Optional<Friendship> findByUser_IdAndAmi_IdAndActifTrue(Integer userId, Integer amiId);
}
