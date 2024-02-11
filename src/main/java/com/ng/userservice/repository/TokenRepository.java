package com.ng.userservice.repository;

import com.ng.userservice.entity.Token;
import com.ng.userservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Integer> {
    @Query("select t from Token t where t.user.id = ?1 and (t.expired = false or t.revoked = false)")
    List<Token> findAllValidTokenByUser(Integer id);

    Optional<Token> findByToken(String token);

    Token findByTokenAndUser(String jwtToken, User user);
}
