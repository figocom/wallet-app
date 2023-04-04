package com.softex.figo.walletapp.controller;

import com.softex.figo.walletapp.config.security.AuthUserDetails;
import com.softex.figo.walletapp.domain.Note;
import com.softex.figo.walletapp.exception.ItemNotFoundException;
import com.softex.figo.walletapp.repository.NoteRepository;
import com.softex.figo.walletapp.response.DataDTO;
import com.softex.figo.walletapp.response.ErrorDTO;
import com.softex.figo.walletapp.response.WebResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user-notes")
@RequiredArgsConstructor
public class UserNotesController {
    private final NoteRepository noteRepository;
    @PostMapping("/create")
    public ResponseEntity<WebResponse<?>> create(@RequestParam String title, @RequestParam String content) {
        AuthUserDetails userDetails= (AuthUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Note note = new Note(title, content, userDetails.authUser());
        noteRepository.save(note);
        return ResponseEntity.ok(new WebResponse<>(new DataDTO<>(note)));
    }
    @GetMapping("/get/{id}")
    public ResponseEntity<WebResponse<?>> get(@PathVariable Long id) {
        Note note = null;
        try {
            note = noteRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("Note not found"));
        } catch (ItemNotFoundException e) {
            throw new RuntimeException(e);
        }
        AuthUserDetails  authUserDetails = (AuthUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(note.getUser().getUsername().equals(authUserDetails.authUser().getUsername()))) {
            return ResponseEntity.badRequest().body(new WebResponse<>(new ErrorDTO("You are not authorized to access this note",404)));
        }
        return ResponseEntity.ok(new WebResponse<>(new DataDTO<>(note)));
    }
    @GetMapping("/get-all")
    public ResponseEntity<WebResponse<?>> getAll() {
        AuthUserDetails userDetails= (AuthUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(new WebResponse<>(new DataDTO<>(noteRepository.findAllByUser(userDetails.authUser()))));
    }

}
