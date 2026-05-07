package com.commongrant.service;

import com.commongrant.config.ResourceNotFoundException;
import com.commongrant.dto.*;
import com.commongrant.model.*;
import com.commongrant.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FunderService {

    private final FunderRepository funderRepo;

    public List<FunderDto> listAll() {
        return funderRepo.findByActiveTrue().stream().map(this::toDto).collect(Collectors.toList());
    }

    public Page<FunderDto> search(String focus, String region, Pageable pageable) {
        return funderRepo.search(focus, region, pageable).map(this::toDto);
    }

    public FunderDto getById(Long id) {
        Funder f = funderRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Funder not found: " + id));
        return toDto(f);
    }

    private FunderDto toDto(Funder f) {
        return FunderDto.builder()
                .id(f.getId()).name(f.getName()).description(f.getDescription())
                .minGrant(f.getMinGrant()).maxGrant(f.getMaxGrant())
                .focusAreas(f.getFocusAreas()).geographicFocus(f.getGeographicFocus())
                .nextDeadline(f.getNextDeadline())
                .build();
    }
}

