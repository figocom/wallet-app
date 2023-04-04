package com.softex.figo.walletapp.controller;

import com.softex.figo.walletapp.dto.UpdateUserDto;
import com.softex.figo.walletapp.response.ErrorDTO;
import com.softex.figo.walletapp.response.WebResponse;
import com.softex.figo.walletapp.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/profile")
public class HomeController {
    private final UserService userService;

    public HomeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/balance")
    public ResponseEntity<WebResponse<?>> getBalance() {

        WebResponse<?> response = userService.getBalance();
        return ResponseEntity.status((response.data() instanceof ErrorDTO errorDTO) ? errorDTO.getError_code() : 200).body(response);
    }

    @PostMapping("/editPassword")
    public ResponseEntity<WebResponse<?>> editPassword(@RequestBody UpdateUserDto updateUserDto) {
        WebResponse<?> response = userService.editPassword(updateUserDto);
        return ResponseEntity.status(response.data() instanceof ErrorDTO errorDTO ? errorDTO.getError_code() : 200).body(response);

    }
}
