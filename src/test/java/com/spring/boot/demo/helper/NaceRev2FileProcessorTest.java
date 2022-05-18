package com.spring.boot.demo.helper;

import com.spring.boot.demo.exception.NaceDataProcessingException;
import com.spring.boot.demo.jpa.entity.NaceDataEntity;
import jdk.jfr.Description;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class NaceRev2FileProcessorTest {
    NaceRev2FileProcessor processor = new NaceRev2FileProcessor();

    @Description("When FIle exists, File Should be processed and populate the Entity Objects")
    @Test
    public void processNaceRevFIle_whenFileExist_shouldReadAndPopulateEntity() {
        ReflectionTestUtils.setField(processor, "naceRevF2ilePath", "NaceDetails.xlsx");
        List<NaceDataEntity> entityList = processor.processNaceRevFIle();
        assertNotNull(entityList);
        assertEquals(996, entityList.size());

    }

    @Description("When not FIle exists, should throw NaceDataProcessingException")
    @Test
    public void processNaceRevFIle_whenFileNotExist_shouldThrow_NaceDataProcessingException() {
        ReflectionTestUtils.setField(processor, "naceRevF2ilePath", "NaceDetails_Invalid.xlsx");
        Assertions.assertThrows(NaceDataProcessingException.class, () -> {
            processor.processNaceRevFIle();
        });
    }

}