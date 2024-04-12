package com.gmail.merikbest2015.repository;

import com.gmail.merikbest2015.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("""
            SELECT user FROM User user
            WHERE UPPER(user.fullName) LIKE UPPER(CONCAT('%',:username,'%')) AND user.active = true
            OR UPPER(user.username) LIKE UPPER(CONCAT('%',:username,'%')) AND user.active = true
            """)
    List<User> searchListMembersByUsername(@Param("username") String username);

    @Query("""
            SELECT CASE WHEN count(blockedUser) > 0 THEN true ELSE false END FROM User user
            LEFT JOIN user.userBlockedList blockedUser
            WHERE user.id = :userId
            AND blockedUser.id = :blockedUserId
            """)
    boolean isUserBlocked(@Param("userId") Long userId, @Param("blockedUserId") Long blockedUserId);

    @Query("""
            SELECT CASE WHEN count(user) > 0 THEN true ELSE false END FROM User user
            LEFT JOIN user.following following
            WHERE user.id = :userId AND user.privateProfile = false
            OR user.id = :userId AND user.privateProfile = true AND following.id = :authUserId
            """)
    boolean isUserHavePrivateProfile(@Param("userId") Long userId, @Param("authUserId") Long authUserId);
}
