package com.EntertainmentViet.backend.features.scoresystem.api;

import com.EntertainmentViet.backend.features.common.utils.RestUtils;
import com.EntertainmentViet.backend.features.organizer.dto.event.CreateEventDto;
import com.EntertainmentViet.backend.features.scoresystem.boundary.ScoreTypeBoundary;
import com.EntertainmentViet.backend.features.scoresystem.dto.ScoreTypeDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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

    public static final String REQUEST_MAPPING_PATH = "/admin/{id}/scoreTypes";

    private final ScoreTypeBoundary scoreTypeService;

    @GetMapping
    public CompletableFuture<ResponseEntity<List<ScoreTypeDto>>> findAll(JwtAuthenticationToken token, @PathVariable UUID id) {
//        if (!id.equals(RestUtils.getUidFromToken(token)) && !RestUtils.isTokenContainPermissions(token, "ROOT")) {
//            log.warn(String.format("The token don't have enough access right to update information of talent with uid '%s'", id));
//            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
//        }
        return CompletableFuture.completedFuture(ResponseEntity.ok().body(scoreTypeService.findAll(id)));
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public CompletableFuture<ResponseEntity<Long>> create(JwtAuthenticationToken token,
                                                          @PathVariable("id") UUID id,
                                                          @RequestBody @Valid ScoreTypeDto scoreTypeDto) {
//        if (!id.equals(RestUtils.getUidFromToken(token)) && !RestUtils.isTokenContainPermissions(token, "ROOT")) {
//            log.warn(String.format("The token don't have enough access right to get information of organizer with uid '%s'", id));
//            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
//        }

        return CompletableFuture.completedFuture(scoreTypeService.create(id, scoreTypeDto)
                .map(newScoreTypeId -> ResponseEntity
                        .ok()
                        .body(newScoreTypeId)
                )
                .orElse(ResponseEntity.badRequest().build())
        );
    }
}
