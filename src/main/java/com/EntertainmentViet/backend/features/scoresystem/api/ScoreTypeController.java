package com.EntertainmentViet.backend.features.scoresystem.api;

import com.EntertainmentViet.backend.features.scoresystem.boundary.ScoreTypeBoundary;
import com.EntertainmentViet.backend.features.scoresystem.dto.ScoreTypeDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
@Async
@Validated
@RequestMapping(path = ScoreTypeController.REQUEST_MAPPING_PATH)
@Slf4j
public class ScoreTypeController {

    public static final String REQUEST_MAPPING_PATH = "/admin/{admin_id}/scoreTypes";

    private final ScoreTypeBoundary scoreTypeService;

    @GetMapping
    public CompletableFuture<ResponseEntity<List<ScoreTypeDto>>> findAll() {
        return CompletableFuture.completedFuture(ResponseEntity.ok().body(scoreTypeService.findAll()));
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public CompletableFuture<ResponseEntity<UUID>> create(@PathVariable("admin_id") UUID adminId,
                                                          @RequestBody @Valid ScoreTypeDto scoreTypeDto) {
        return CompletableFuture.completedFuture(scoreTypeService.create(adminId, scoreTypeDto)
                .map(newScoreTypeId -> ResponseEntity
                        .ok()
                        .body(newScoreTypeId)
                )
                .orElse(ResponseEntity.badRequest().build())
        );
    }

    @DeleteMapping(value = "/{id}")
    public CompletableFuture<ResponseEntity<Void>> delete(@PathVariable("admin_id") UUID adminId, @PathVariable("id") UUID id) {
        if (scoreTypeService.delete(id)) {
            return CompletableFuture.completedFuture(ResponseEntity.ok().build());
        }
        return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
    }
}
