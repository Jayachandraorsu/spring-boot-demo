package com.spring.boot.demo.main;

import com.spring.boot.demo.dto.ErrorResponse;
import com.spring.boot.demo.dto.NaceDetailsDto;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = SpringBootDemoApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SpringBootDemoApplicationIT {
    @LocalServerPort
    private int port;
    TestRestTemplate restTemplate = new TestRestTemplate();
    private static final String PUT_NACE_DETAILS_PATH = "/v1/api/naceData/putNaceDetails";
    private static final String GET_NACE_DETAILS_PATH = "/v1/api/naceData/getNaceDetails";
    private static final String DELETE_NACE_DETAILS_PATH = "/v1/api/naceData/deleteNaceDetails";

    @Test
    @Order(1)
    public void putNaceDetails_shouldProcessNaceFile_insertDataToDatabase() throws JSONException {
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                createURLWithPort(PUT_NACE_DETAILS_PATH),
                HttpMethod.PUT, null, String.class);
        String expected = "Nace Data file processed successfully";
        assertEquals(expected, responseEntity.getBody());
    }

    @Test
    @Order(2)
    public void getNaceDetails_whenOrderIdExist_ShouldReturnNaceDetailsDto() throws JSONException {
        ResponseEntity<NaceDetailsDto> responseEntity = restTemplate.exchange(
                createURLWithPort(GET_NACE_DETAILS_PATH + "?orderId=398481"),
                HttpMethod.GET, null, NaceDetailsDto.class);
        NaceDetailsDto naceDetailsDto = responseEntity.getBody();
        assertEquals(398481, naceDetailsDto.getOrderId());
        assertEquals(1, naceDetailsDto.getLevel());
        assertEquals("A", naceDetailsDto.getCode());
        assertEquals("AGRICULTURE, FORESTRY AND FISHING", naceDetailsDto.getDescription());
        assertEquals("This section includes the exploitation of vegetal and animal natural" +
                " resources, comprising the activities of growing of crops, raising and breeding of animals, " +
                "harvesting of timber and other plants, animals or animal products from a farm or" +
                " their natural habitats.", naceDetailsDto.getItemsInclude());
        assertEquals("A", naceDetailsDto.getReferenceToIsic());
        assertTrue(StringUtils.isEmpty(naceDetailsDto.getRuling()));
        assertTrue(StringUtils.isEmpty(naceDetailsDto.getItemsAlsoInclude()));
        assertTrue(StringUtils.isEmpty(naceDetailsDto.getItemsExclude()));
    }

    @Test
    @Order(3)
    public void getNaceDetails_whenOrderIdNotExist_ErrorResponse() throws JSONException {
        ResponseEntity<ErrorResponse> responseEntity = restTemplate.exchange(
                createURLWithPort(GET_NACE_DETAILS_PATH + "?orderId=39848199"),
                HttpMethod.GET, null, ErrorResponse.class);
        ErrorResponse errorResponse = responseEntity.getBody();
        assertNotNull(errorResponse != null);
        assertEquals("Nace Details not found for orderId:39848199", errorResponse.getErrorMessage());
    }

    @Test
    @Order(4)
    public void deleteNaceDetails_whenOrderIdExist_ShouldDeleteRecord() throws JSONException {
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                createURLWithPort(DELETE_NACE_DETAILS_PATH + "?orderId=398481"),
                HttpMethod.DELETE, null, String.class);
        String errorResponse = responseEntity.getBody();
        assertNotNull(errorResponse != null);
        assertEquals("Nace Details has been deleted  for 398481", errorResponse);
    }

    @Test
    @Order(5)
    public void deleteNaceDetails_whenOrderIdNotExist_ShouldDeleteRecord() throws JSONException {
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                createURLWithPort(DELETE_NACE_DETAILS_PATH + "?orderId=398481"),
                HttpMethod.DELETE, null, String.class);
        String errorResponse = responseEntity.getBody();
        assertNotNull(errorResponse != null);
        assertEquals("No Records Found to delete for orderId:398481", errorResponse);
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

}