package com.spring.boot.demo.helper;

import com.spring.boot.demo.exception.NaceDataProcessingException;
import com.spring.boot.demo.jpa.entity.NaceDataEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class NaceRev2FileProcessor {
    @Value("${nacedata.input.file.path}")
    private String naceRevF2ilePath;

    private DataFormatter formatter = new DataFormatter();

    public List<NaceDataEntity> processNaceRevFIle() {
        List<NaceDataEntity> naceDataEntityList = new ArrayList<>();
        log.info("Start reading file");
        try {
            FileInputStream fileInputStream = new FileInputStream(ResourceUtils.getFile("classpath:" + naceRevF2ilePath));
            XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
            XSSFSheet worksheet = workbook.getSheetAt(0);

            for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
                NaceDataEntity naceDataEntity = new NaceDataEntity();
                XSSFRow row = worksheet.getRow(i);
                naceDataEntity.setOrderId((long) row.getCell(0).getNumericCellValue());
                naceDataEntity.setLevel((long) row.getCell(1).getNumericCellValue());
                naceDataEntity.setCode(getCellValue(row, 2));
                naceDataEntity.setParent(getCellValue(row, 3));
                naceDataEntity.setDescription(getCellValue(row, 4));
                naceDataEntity.setItemsInclude(getCellValue(row, 5));
                naceDataEntity.setItemsAlsoInclude(getCellValue(row, 6));
                naceDataEntity.setRuling(getCellValue(row, 7));
                naceDataEntity.setItemsExclude(getCellValue(row, 8));
                naceDataEntity.setReferenceToIsic(getCellValue(row, 9));
                naceDataEntityList.add(naceDataEntity);
            }
            log.info("Finished reading file");

        } catch (FileNotFoundException fne) {
            log.error("FileNotFoundException occurred while processing file ");
            throw new NaceDataProcessingException("File Not Exist:", fne);

        } catch (IOException ioe) {
            log.error("IOException occurred while processing file ");
            throw new NaceDataProcessingException("IOException occurred while processing file:", ioe);
        }
        return naceDataEntityList;
    }

    private String getCellValue(XSSFRow row, int index) {
        return formatter.formatCellValue(row.getCell(index));
    }

}
