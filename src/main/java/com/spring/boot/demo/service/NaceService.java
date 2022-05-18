package com.spring.boot.demo.service;

import com.google.gson.Gson;
import com.spring.boot.demo.dto.NaceDetailsDto;
import com.spring.boot.demo.exception.NoDataFoundException;
import com.spring.boot.demo.helper.NaceRev2FileProcessor;
import com.spring.boot.demo.jpa.entity.NaceDataEntity;
import com.spring.boot.demo.jpa.repository.NaceDataRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class NaceService {

    private NaceDataRepository naceDataRepository;

    private NaceRev2FileProcessor naceRev2FileProcessor;

    @Autowired
    public NaceService(NaceDataRepository naceDataRepository, NaceRev2FileProcessor naceRev2FileProcessor) {
        this.naceDataRepository = naceDataRepository;
        this.naceRev2FileProcessor = naceRev2FileProcessor;
    }

    public void putNaceDetails() {
        log.info("Start process");
        naceDataRepository.deleteAll();
        List<NaceDataEntity> naceDataEntityList = naceRev2FileProcessor.processNaceRevFIle();
        if (CollectionUtils.isEmpty(naceDataEntityList)) {
            log.info("No Records found in File to insert into database");
            return;
        }
        naceDataRepository.saveAll(naceDataEntityList);
        log.info("Processed Nace Rev file and persisted the data into data base");
    }


    public Optional<NaceDetailsDto> fetchNaceDetailByOrderId(Long orderId) {
        log.info("Fetching Nace Details for orderId:{}  ", orderId);
        Optional<NaceDataEntity> naceDataEntityResp = naceDataRepository.findById(orderId);
        if (naceDataEntityResp.isPresent()) {
            log.info("Nace Details found for orderId:{} , Details:{} ", orderId, naceDataEntityResp.get());
            return populateNaceDetailsDto(naceDataEntityResp.get());
        }
        log.info("Nace Details  not found for orderId:{} ", orderId);
        return Optional.empty();
    }

    public void deleteNaceDetailByOrderId(Long orderId) {
        try {
            log.info("deleting  Nace Details for orderId:{} ", orderId);
            naceDataRepository.deleteById(orderId);
            log.info("deleted  Nace Details for orderId:{} ", orderId);
        } catch (EmptyResultDataAccessException e) {
            log.error("No Data Found to delete for orderId:{} ", orderId);
            throw new NoDataFoundException(String.format("No Records Found to delete for orderId:%s", orderId), e);
        }
    }

    private Optional<NaceDetailsDto> populateNaceDetailsDto(NaceDataEntity naceDataEntity) {
        Gson gson = new Gson();
        String jsonSting = gson.toJson(naceDataEntity);
        return Optional.ofNullable(gson.fromJson(jsonSting, NaceDetailsDto.class));
    }

}
