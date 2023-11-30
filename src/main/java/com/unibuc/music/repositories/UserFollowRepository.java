package com.unibuc.music.repositories;

import com.unibuc.music.models.User;
import com.unibuc.music.models.UserFollow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserFollowRepository extends JpaRepository<UserFollow, Long> {

    Optional<UserFollow> findByFollowerAndFollowing(User follower, User following);
}
