package com.spring.boot.demo.controller;

import com.spring.boot.demo.dto.ErrorResponse;
import com.spring.boot.demo.dto.NaceDetailsDto;
import com.spring.boot.demo.exception.NoDataFoundException;
import com.spring.boot.demo.service.NaceService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

@Slf4j
@RestController
@RequestMapping(value = "/v1/api/naceData")
public class NaceDataController {

    private NaceService naceService;

    private static final String FILE_PROCESSING_SUCCESS_MSG = "Nace Data file processed successfully";

    private static final String DELETE_PROCESSING_SUCCESS_MSG = "Nace Details has been deleted  for %s";

    @Autowired
    public NaceDataController(NaceService naceService) {
        this.naceService = naceService;
    }

    private static final String PUT_NACE_DETAILS_PATH = "/putNaceDetails";
    private static final String GET_NACE_DETAILS_PATH = "/getNaceDetails";
    private static final String DELETE_NACE_DETAILS_PATH = "/deleteNaceDetails";

    @ApiOperation(value = "This endpoint put the Nace Data into Database ", nickname = "PutNaceDetails", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "File has been processed", response = String.class),
            @ApiResponse(code = 401, message = "Unauthorized Access to this information"),
            @ApiResponse(code = 403, message = "Client is Forbidden from accessing this Resource"),
            @ApiResponse(code = 404, message = "Resource Not Found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @RequestMapping(value = PUT_NACE_DETAILS_PATH, produces = TEXT_PLAIN_VALUE, method = RequestMethod.PUT)
    public ResponseEntity<String> putNaceDetails() {
        try {
            naceService.putNaceDetails();
            log.info("File processed successfully");
            return ResponseEntity.status(HttpStatus.OK).body(FILE_PROCESSING_SUCCESS_MSG);
        } catch (Exception e) {
            String errorMessage = "Exception occurred while Processing file";
            log.info("Exception occurred while Processing file, ErrorMessage: ", e.getMessage());
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "This endpoint fetch the Nace Data from Database ", nickname = "fetchNaceDetails", response = NaceDetailsDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Found the Nace Details for given Order Id", response = NaceDetailsDto.class),
            @ApiResponse(code = 401, message = "Unauthorized Access to this information"),
            @ApiResponse(code = 403, message = "Client is Forbidden from accessing this Resource"),
            @ApiResponse(code = 404, message = "Resource Not Found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @RequestMapping(value = GET_NACE_DETAILS_PATH, produces = APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    public ResponseEntity<?> fetchNaceDetails(@RequestParam(required = true, name = "orderId") Long orderId) {
        ErrorResponse errorResponse = new ErrorResponse();

        try {
            final Optional<NaceDetailsDto> order = naceService.fetchNaceDetailByOrderId(orderId);
            if (order.isPresent()) {
                log.info("NaceDetails fetched successfully for oderId:{}", orderId);
                return ResponseEntity.ok(order.get());
            }
            errorResponse.setErrorMessage(String.format("Nace Details not found for orderId:%s", orderId));
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        } catch (Exception exception) {
            errorResponse.setErrorMessage(String.format("Internal Service Error while fetching Nace Details for Order Id:%s", orderId));
            log.info("Exception occurred while fetching Nace data for order Id:{} , ErrorMessageL{} ", orderId, exception.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "This endpoint will delete the Nace Data from Database ", nickname = "DeleteNaceDetails", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OrderId has been deleted", response = String.class),
            @ApiResponse(code = 401, message = "Unauthorized Access to this information"),
            @ApiResponse(code = 403, message = "Client is Forbidden from accessing this Resource"),
            @ApiResponse(code = 404, message = "Resource Not Found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @RequestMapping(value = DELETE_NACE_DETAILS_PATH, produces = TEXT_PLAIN_VALUE, method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteNaceDetails(@RequestParam(required = true, name = "orderId") Long orderId) {
        try {
            naceService.deleteNaceDetailByOrderId(orderId);
            return ResponseEntity.status(HttpStatus.OK).body(String.format(DELETE_PROCESSING_SUCCESS_MSG, orderId));
        } catch (NoDataFoundException exception) {
            log.error(exception.getMessage());
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception exception) {
            String errorMessage = String.format("Internal Service Error while deleting  Nace Details for Order Id:%s", orderId);
            log.error("Exception occurred while deleting Nace data for order Id:{} , ErrorMessageL{} ", orderId, exception.getMessage());
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
