package com.EntertainmentViet.backend.features.config.api;

import com.EntertainmentViet.backend.domain.entities.config.FinanceConfig;
import com.EntertainmentViet.backend.features.config.boundary.ConfigBoundary;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
@Async
@Validated
@RequestMapping(path = ConfigController.REQUEST_MAPPING_PATH)
public class ConfigController {

  public static final String REQUEST_MAPPING_PATH = "/config";

  public static final String FINANCE_PATH = "/finance";

  private final ConfigBoundary configService;

  @GetMapping(value = FINANCE_PATH)
  public CompletableFuture<ResponseEntity<FinanceConfig>> getFinanceConfig() {
    return CompletableFuture.completedFuture(configService.getFinance()
        .map(financeConfig -> ResponseEntity
            .ok()
            .body(financeConfig)
        )
        .orElse(ResponseEntity.notFound().build())
    );
  }

  @PostMapping(value = FINANCE_PATH)
  public CompletableFuture<ResponseEntity<FinanceConfig>> updateFinanceConfig(@RequestBody @Valid FinanceConfig financeConfig) {
    configService.configFinance(financeConfig);
    return CompletableFuture.completedFuture(ResponseEntity.ok().body(financeConfig));
  }
}
