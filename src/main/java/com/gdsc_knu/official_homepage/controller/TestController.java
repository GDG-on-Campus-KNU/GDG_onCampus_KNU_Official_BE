package com.gdsc_knu.official_homepage.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
@Tag(name="Example",description="Swagger 동작확인 테스트 API")
public class TestController {
    @GetMapping()
    @Operation(summary="테스트용 API",description="안녕하시렵니까")
    public ResponseEntity<String> test(){
        return ResponseEntity.ok("Hello KNU");
    }
}
