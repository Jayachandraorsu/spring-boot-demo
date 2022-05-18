package com.spring.boot.demo.controller;

import com.spring.boot.demo.dto.ErrorResponse;
import com.spring.boot.demo.dto.NaceDetailsDto;
import com.spring.boot.demo.exception.NaceDataProcessingException;
import com.spring.boot.demo.exception.NoDataFoundException;
import com.spring.boot.demo.service.NaceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class NaceDataControllerTest {
    NaceDataController naceDataController;

    NaceService naceService;

    @BeforeEach
    public void init() {
        naceService = mock(NaceService.class);
        naceDataController = new NaceDataController(naceService);
    }

    @Test
    public void putNaceDetails_whenFileProcessed_shoudReturnSucessMessage() {
        doNothing().when(naceService).putNaceDetails();
        naceDataController.putNaceDetails();
        ResponseEntity<String> responseEntity = naceDataController.putNaceDetails();
        String response = responseEntity.getBody();
        assertEquals(response, "Nace Data file processed successfully");
    }

    @Test
    public void putNaceDetails_whenFileNotProcessed_shoudReturnSucessMessage() {
        doThrow(NaceDataProcessingException.class).when(naceService).putNaceDetails();
        naceDataController.putNaceDetails();
        ResponseEntity<String> responseEntity = naceDataController.putNaceDetails();
        String response = responseEntity.getBody();
        assertEquals(response, "Exception occurred while Processing file");
    }

    @Test
    public void fetchNaceDetails_whenOrderIdExist_shoudReturnNaceDetailsDto() {
        NaceDetailsDto naceDetailsDto = new NaceDetailsDto();
        naceDetailsDto.setOrderId(234342l);
        naceDetailsDto.setCode("A");
        naceDetailsDto.setItemsAlsoInclude("ItemsAlsoInclude");
        doReturn(Optional.ofNullable(naceDetailsDto)).when(naceService).fetchNaceDetailByOrderId(eq(234342l));
        ResponseEntity<?> responseEntity = naceDataController.fetchNaceDetails(234342l);
        NaceDetailsDto response = (NaceDetailsDto) responseEntity.getBody();
        assertEquals(234342l, response.getOrderId());
        assertEquals("A", response.getCode());
        assertEquals("ItemsAlsoInclude", response.getItemsAlsoInclude());

    }

    @Test
    public void fetchNaceDetails_whenOrderIdNotExist_shoudReturnErrorMessage() {
        doReturn(Optional.empty()).when(naceService).fetchNaceDetailByOrderId(eq(234342l));
        ResponseEntity<?> responseEntity = naceDataController.fetchNaceDetails(234342l);
        ErrorResponse response = (ErrorResponse) responseEntity.getBody();
        assertEquals("Nace Details not found for orderId:234342", response.getErrorMessage());
    }

    @Test
    public void fetchNaceDetails_whenException_shoudReturnSucessMessage() {
        doThrow(RuntimeException.class).when(naceService).fetchNaceDetailByOrderId(eq(234342l));
        ResponseEntity<?> responseEntity = naceDataController.fetchNaceDetails(234342l);
        ErrorResponse response = (ErrorResponse) responseEntity.getBody();
        assertEquals("Internal Service Error while fetching Nace Details for Order Id:234342", response.getErrorMessage());
    }

    @Test
    public void deleteNaceDetails_whenOrderIdExist_shoudReturnNaceDetailsDto() {
        doNothing().when(naceService).deleteNaceDetailByOrderId(eq(234342l));
        ResponseEntity<String> responseEntity = naceDataController.deleteNaceDetails(234342l);
        String response = responseEntity.getBody();
        assertEquals("Nace Details has been deleted  for 234342", response);


    }

    @Test
    public void deleteNaceDetails_whenOrderIdNotExist_shoudReturnErrorMessage() {
        doThrow(new NoDataFoundException("No Records Found to delete for orderId:234342")).when(naceService).deleteNaceDetailByOrderId(eq(234342l));
        ResponseEntity<String> responseEntity = naceDataController.deleteNaceDetails(234342l);
        String response = responseEntity.getBody();
        assertEquals("No Records Found to delete for orderId:234342", response);
    }

    @Test
    public void deleteNaceDetails_whenException_shoudReturnErrorMessage() {
        doThrow(RuntimeException.class).when(naceService).deleteNaceDetailByOrderId(eq(234342l));
        ResponseEntity<String> responseEntity = naceDataController.deleteNaceDetails(234342l);
        String response = responseEntity.getBody();
        assertEquals("Internal Service Error while deleting  Nace Details for Order Id:234342", response);
    }

}