package com.softex.figo.walletapp.controller;

import com.softex.figo.walletapp.response.ErrorDTO;
import com.softex.figo.walletapp.response.WebResponse;
import com.softex.figo.walletapp.service.ValuateConvertService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/currency")
@RequiredArgsConstructor
public class CurrencyConvertController {
    private final ValuateConvertService valuateConvertService;

    @GetMapping(value = "/convert/{from}/{to}/{amount}")
    public ResponseEntity<WebResponse<?>>convert(
            @RequestParam(value = "from", required = true) @PathVariable(name = "from") String from,
            @RequestParam(value = "to", required = true) @PathVariable(name = "to") String text,
            @RequestParam(value = "amount", required = true) @PathVariable(name = "amount") String amount
    ) {
        WebResponse<?> response = valuateConvertService.convert(from, text, amount);
        return ResponseEntity.status(response.data() instanceof ErrorDTO errorDTO? errorDTO.getError_code():200).body(response);
    }
}

