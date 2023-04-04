package com.softex.figo.walletapp.controller;

import com.softex.figo.walletapp.dto.PlanDto;
import com.softex.figo.walletapp.response.ErrorDTO;
import com.softex.figo.walletapp.response.WebResponse;
import com.softex.figo.walletapp.service.PlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/plan")
@RequiredArgsConstructor
public class PlanController {
    private final PlanService service;
    @PostMapping("/create")
    public ResponseEntity<WebResponse<?>> createPlan(@Valid @RequestBody PlanDto planDto){
        WebResponse<?> serviceResponse= service.createPlan(planDto);
        return ResponseEntity.status(serviceResponse.data() instanceof ErrorDTO errorDTO? errorDTO.getError_code():201).body(serviceResponse);
    }
    @GetMapping("/get-all")
    public ResponseEntity<WebResponse<?>> getAllPlans(){
        WebResponse<?> response=service.getAll();
        return ResponseEntity.ok(response);
    }
    @GetMapping("/getDailyCirculations")
    public ResponseEntity<WebResponse<?>> getDailyCirculations() {
        WebResponse<?> circulations = service.getDailyPlans();
        return ResponseEntity.status(circulations.data() instanceof ErrorDTO errorDTO ? errorDTO.getError_code() : 200).body(circulations);
    }
    @GetMapping("/getWeeklyPlans")
    public ResponseEntity<WebResponse<?>> getWeeklyPlans( ) {
        WebResponse<?> response = service.getWeeklyPlans();
        return ResponseEntity.status(response.data() instanceof ErrorDTO errorDTO ? errorDTO.getError_code() : 200).body(response);
    }

    @GetMapping("/getMonthlyPlans")
    public ResponseEntity<WebResponse<?>> getMonthlyPlans() {
        WebResponse<?> response = service.getMonthlyPlans();
        return ResponseEntity.status(response.data() instanceof ErrorDTO errorDTO ? errorDTO.getError_code() : 200).body(response);
    }

    @GetMapping("/getYearlyPlans")
    public ResponseEntity<WebResponse<?>> getYearlyPlans() {
        WebResponse<?> response = service.getYearlyPlans();
        return ResponseEntity.status(response.data() instanceof ErrorDTO errorDTO ? errorDTO.getError_code() : 200).body(response);
    }
    @GetMapping("/getPlansByMonth/{year-month}") // day format yyyy-MM
    public ResponseEntity<WebResponse<?>> getPlanByMonth(@PathVariable("year-month") String parameter){
        WebResponse<?> response=service.getPlansByMonth( parameter);
        return ResponseEntity.status(response.data() instanceof ErrorDTO errorDTO ? errorDTO.getError_code() : 200).body(response);
    }

    @GetMapping("/getDailyPlansByDay/{day}") // day format: yyyy-MM-dd
    public ResponseEntity<WebResponse<?>> getDailyPlansByDay( @PathVariable String day) {
        WebResponse<?> response = service.getDailyPlansByDay( day);
        return ResponseEntity.status(response.data() instanceof ErrorDTO errorDTO ? errorDTO.getError_code() : 200).body(response);
    }
}
