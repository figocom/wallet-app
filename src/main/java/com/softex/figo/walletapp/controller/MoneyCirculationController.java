package com.softex.figo.walletapp.controller;

import com.softex.figo.walletapp.domain.MoneyCirculation;
import com.softex.figo.walletapp.dto.DownloadStatisticRequestDto;
import com.softex.figo.walletapp.dto.MoneyCirculationDto;
import com.softex.figo.walletapp.response.DataDTO;
import com.softex.figo.walletapp.response.ErrorDTO;
import com.softex.figo.walletapp.response.WebResponse;
import com.softex.figo.walletapp.service.ExcelExporter;
import com.softex.figo.walletapp.service.MoneyCirculationService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/money-circulation")
@RequiredArgsConstructor
public class MoneyCirculationController {
    private final MoneyCirculationService moneyCirculationService;

    @PostMapping("/createCirculation")
    public ResponseEntity<WebResponse<?>> createCirculation(@Valid @RequestBody MoneyCirculationDto moneyCirculationDto) {
        WebResponse<?> circulation = moneyCirculationService.createCirculation(moneyCirculationDto);
        return ResponseEntity.status(circulation.data() instanceof ErrorDTO errorDTO ? errorDTO.getError_code() : 200).body(circulation);
    }

    @GetMapping("/getCirculation/{id}")
    public ResponseEntity<WebResponse<?>> getCirculationById(@PathVariable Long id) {
        WebResponse<?> circulation = moneyCirculationService.getCirculation(id);
        return ResponseEntity.status(circulation.data() instanceof ErrorDTO errorDTO ? errorDTO.getError_code() : 200).body(circulation);
    }

    @GetMapping("/getCirculations/{type}")
    public ResponseEntity<WebResponse<?>> getCirculationsByType(@PathVariable String type) {
        WebResponse<?> circulations = moneyCirculationService.getCirculationsByType(type);
        return ResponseEntity.status(circulations.data() instanceof ErrorDTO errorDTO ? errorDTO.getError_code() : 200).body(circulations);
    }

    @GetMapping("/getCirculations")
    public ResponseEntity<WebResponse<?>> getCirculations() {
        WebResponse<?> circulations = moneyCirculationService.getCirculations();
        return ResponseEntity.status(circulations.data() instanceof ErrorDTO errorDTO ? errorDTO.getError_code() : 200).body(circulations);
    }

    @GetMapping("/getCirculationsByCategory/{type}/{id}")
    public ResponseEntity<WebResponse<?>> getCirculationsByCategoryAndType(@PathVariable Long id, @PathVariable String type) {
        WebResponse<?> circulations = moneyCirculationService.getCirculationsByCategoryAndType(id, type);
        return ResponseEntity.status(circulations.data() instanceof ErrorDTO errorDTO ? errorDTO.getError_code() : 200).body(circulations);
    }

    @GetMapping("/getCirculationsBySubCategory/{type}/{id}")
    public ResponseEntity<WebResponse<?>> getCirculationsBySubCategoryAndType(@PathVariable Long id, @PathVariable String type) {
        WebResponse<?> circulations = moneyCirculationService.getCirculationsBySubCategoryAndType(id, type);
        return ResponseEntity.status(circulations.data() instanceof ErrorDTO errorDTO ? errorDTO.getError_code() : 200).body(circulations);
    }

    @GetMapping("/getDailyCirculations/{type}")
    public ResponseEntity<WebResponse<?>> getDailyCirculations(@PathVariable String type) {
        WebResponse<?> circulations = moneyCirculationService.getDailyCirculations(type);
        return ResponseEntity.status(circulations.data() instanceof ErrorDTO errorDTO ? errorDTO.getError_code() : 200).body(circulations);
    }

    @GetMapping("/getWeeklyCirculations/{type}")
    public ResponseEntity<WebResponse<?>> getWeeklyCirculations(@PathVariable String type) {
        WebResponse<?> circulations = moneyCirculationService.getWeeklyCirculations(type);
        return ResponseEntity.status(circulations.data() instanceof ErrorDTO errorDTO ? errorDTO.getError_code() : 200).body(circulations);
    }

    @GetMapping("/getMonthlyCirculations/{type}")
    public ResponseEntity<WebResponse<?>> getMonthlyCirculations(@PathVariable String type) {
        WebResponse<?> circulations = moneyCirculationService.getMonthlyCirculations(type);
        return ResponseEntity.status(circulations.data() instanceof ErrorDTO errorDTO ? errorDTO.getError_code() : 200).body(circulations);
    }

    @GetMapping("/getYearlyCirculations/{type}")
    public ResponseEntity<WebResponse<?>> getYearlyCirculations(@PathVariable String type) {
        WebResponse<?> circulations = moneyCirculationService.getYearlyCirculations(type);
        return ResponseEntity.status(circulations.data() instanceof ErrorDTO errorDTO ? errorDTO.getError_code() : 200).body(circulations);
    }
    @GetMapping("/getCirculationsByMonth/{year-month}/{type}")
    public ResponseEntity<WebResponse<?>> getCirculationByMonth(@PathVariable("year-month") String parameter, @PathVariable String type){
        WebResponse<?> circulations=moneyCirculationService.getCirculationsByMonth(parameter, type);
        return ResponseEntity.status(circulations.data() instanceof ErrorDTO errorDTO ? errorDTO.getError_code() : 200).body(circulations);
    }

    @GetMapping("/getDailyCirculationsByDay/{type}/{day}") // day format: yyyy-MM-dd
    public ResponseEntity<WebResponse<?>> getDailyCirculationsByDay(@PathVariable String type, @PathVariable String day) {
        WebResponse<?> circulations = moneyCirculationService.getDailyCirculationsByDay(type, day);
        return ResponseEntity.status(circulations.data() instanceof ErrorDTO errorDTO ? errorDTO.getError_code() : 200).body(circulations);
    }

    @GetMapping(value = "/getStatisticsFilter")
    public void getStatisticsFilter(@RequestBody(required = false) DownloadStatisticRequestDto downloadStatisticRequestDto, HttpServletResponse response) {
        WebResponse<?> statisticsFilter = moneyCirculationService.getStatisticsFilter(downloadStatisticRequestDto);
        DataDTO<?> dataDTO = (DataDTO<?>) statisticsFilter.data();
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=statistics_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);
        List<MoneyCirculation> mcs = (List<MoneyCirculation>) dataDTO.getData();
        ExcelExporter excelExporter = new ExcelExporter(mcs);

        try {
            excelExporter.export(response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @PatchMapping("/updateCirculation/{id}")
    public ResponseEntity<WebResponse<?>> updateCirculation(@PathVariable Long id, @Valid @RequestBody MoneyCirculationDto moneyCirculationDto) {
        WebResponse<?> circulation = moneyCirculationService.updateCirculation(id, moneyCirculationDto);
        return ResponseEntity.status(circulation.data() instanceof ErrorDTO errorDTO ? errorDTO.getError_code() : 200).body(circulation);
    }

    @DeleteMapping("/deleteCirculation/{id}")
    public ResponseEntity<WebResponse<?>> deleteCirculation(@PathVariable Long id) {
        WebResponse<?> circulation = moneyCirculationService.deleteCirculation(id);
        return ResponseEntity.status(circulation.data() instanceof ErrorDTO errorDTO ? errorDTO.getError_code() : 200).body(circulation);
    }


}

