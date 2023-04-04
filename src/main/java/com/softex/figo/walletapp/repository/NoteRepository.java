package com.softex.figo.walletapp.repository;

import com.softex.figo.walletapp.domain.AuthUser;
import com.softex.figo.walletapp.domain.Note;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository extends JpaRepository<Note, Long> {
    Note findAllByUser(AuthUser authUser);

}
