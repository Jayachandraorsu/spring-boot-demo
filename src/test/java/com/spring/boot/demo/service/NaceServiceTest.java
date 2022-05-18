package com.spring.boot.demo.service;

import com.spring.boot.demo.dto.NaceDetailsDto;
import com.spring.boot.demo.exception.NoDataFoundException;
import com.spring.boot.demo.helper.NaceRev2FileProcessor;
import com.spring.boot.demo.jpa.entity.NaceDataEntity;
import com.spring.boot.demo.jpa.repository.NaceDataRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class NaceServiceTest {
    NaceService naceService;

    private NaceDataRepository naceDataRepository;

    private NaceRev2FileProcessor naceRev2FileProcessor;


    @BeforeEach
    public void init() {
        naceDataRepository = mock(NaceDataRepository.class);
        naceRev2FileProcessor = mock(NaceRev2FileProcessor.class);
        naceService = new NaceService(naceDataRepository, naceRev2FileProcessor);
    }

    @Test
    public void putNaceDetails_whenFileHasData_shouldMakeCallMethodCall_saveAll() {
        NaceDataEntity mockEntity = mockNaceDataEntity();
        List<NaceDataEntity> naceDataEntityList = Arrays.asList(mockEntity);
        when(naceRev2FileProcessor.processNaceRevFIle()).thenReturn(naceDataEntityList);
        naceService.putNaceDetails();
        verify(naceRev2FileProcessor, times(1)).processNaceRevFIle();
        ArgumentCaptor<List> argumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(naceDataRepository, atLeast(1)).saveAll(argumentCaptor.capture());
        List capturedResponse = argumentCaptor.getValue();
        assertEquals(1, capturedResponse.size());
        NaceDataEntity naceDataEntity = (NaceDataEntity) capturedResponse.get(0);
        assertEquals(123123l, naceDataEntity.getOrderId());
        assertEquals("Items Exclude", naceDataEntity.getItemsExclude());
    }

    @Test
    public void putNaceDetails_whenFIleIsEmpty_shouldNotMakeCallMethodCall_saveAll() {
        List<NaceDataEntity> naceDataEntityList = new ArrayList<>();
        when(naceRev2FileProcessor.processNaceRevFIle()).thenReturn(naceDataEntityList);
        naceService.putNaceDetails();
        verify(naceRev2FileProcessor, times(1)).processNaceRevFIle();
        ArgumentCaptor<List> argumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(naceDataRepository, atMost(0)).saveAll(argumentCaptor.capture());

    }

    @Test
    public void fetchNaceDetailByOrderId_whenOderIdExist_ShouldReturnOrderDetails() {
        when(naceDataRepository.findById(eq(1234l))).thenReturn(Optional.ofNullable(mockNaceDataEntity()));
        Optional<NaceDetailsDto> response = naceService.fetchNaceDetailByOrderId(1234l);
        assertTrue(response.isPresent());
        assertEquals(123123l, response.get().getOrderId());
        assertEquals("Items Exclude", response.get().getItemsExclude());

    }

    @Test
    public void fetchNaceDetailByOrderId_whenOderIdNotExist_ShouldReturnEmptyObj() {
        when(naceDataRepository.findById(eq(2334l))).thenReturn(Optional.empty());
        Optional<NaceDetailsDto> response = naceService.fetchNaceDetailByOrderId(2334l);
        assertTrue(response.isEmpty());
    }

    @Test
    public void deleteNaceDetailByOrderId_whenOderIdExist_ShouldDeleteRecord() {
        doNothing().when(naceDataRepository).deleteById(eq(2334l));
        naceService.deleteNaceDetailByOrderId(2334l);
        verify(naceDataRepository, times(1)).deleteById(eq(2334l));
    }

    @Test
    public void deleteNaceDetailByOrderId_whenOderIdNotExist_ShouldThrowException() {
        doThrow(EmptyResultDataAccessException.class).when(naceDataRepository).deleteById(eq(2334l));
        Assertions.assertThrows(NoDataFoundException.class, () -> naceService.deleteNaceDetailByOrderId(2334l));
    }

    private NaceDataEntity mockNaceDataEntity() {
        NaceDataEntity mockEntity = new NaceDataEntity();
        mockEntity.setOrderId(123123l);
        mockEntity.setItemsExclude("Items Exclude");
        return mockEntity;
    }
}