package com.gdsc_knu.official_homepage.controller.admin;

import com.gdsc_knu.official_homepage.dto.admin.application.AdminApplicationRequest;
import com.gdsc_knu.official_homepage.dto.admin.application.AdminApplicationResponse;
import com.gdsc_knu.official_homepage.service.admin.AdminClassYearService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/admin/class-year")
public class AdminClassYearController {
    private final AdminClassYearService classYearService;

    @GetMapping()
    @Operation(summary="기수 목록 조회 API", description = "기수를 목록을 조회합니다.")
    public ResponseEntity<List<AdminApplicationResponse.ClassYearResponse>> getClassYearList() {
        return ResponseEntity.ok(classYearService.getClassYearList());
    }

    @GetMapping("/{id}")
    @Operation(summary="기수 단건 조회 API", description = "기수를 단건 조회합니다.")
    public ResponseEntity<AdminApplicationResponse.ClassYearResponse> getClassYear(@PathVariable("id") Long id) {
        return ResponseEntity.ok(classYearService.getClassYear(id));
    }

    @PostMapping()
    @Operation(summary="기수 추가 API", description = "기수를 추가합니다.")
    public ResponseEntity<Void> addClassYear(@RequestBody AdminApplicationRequest.ClassYearRequest classYearRequest) {
        classYearService.addClassYear(classYearRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping()
    @Operation(summary="기수 수정 API", description = "기수의 정보를 수정합니다.")
    public ResponseEntity<Void> updateClassYear(@RequestParam("id") Long id,
                                                @RequestBody AdminApplicationRequest.ClassYearRequest classYearRequest) {
        classYearService.updateClassYear(id, classYearRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping()
    @Operation(summary="기수 삭제 API", description = "기수를 삭제 합니다.")
    public ResponseEntity<Void> deleteClassYear(@RequestParam("id") Long id) {
        classYearService.deleteClassYear(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
