package com.softex.figo.walletapp.repository;

import com.softex.figo.walletapp.domain.AuthUser;
import com.softex.figo.walletapp.domain.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
    @Transactional
    @Modifying
    @Query("UPDATE Photo set deleted=true where user=:authUser")
    void deleteByUser(@Param("authUser") AuthUser authUser);

    Optional<Photo> findByIdAndDeleted(Long aLong, boolean deleted);
}
