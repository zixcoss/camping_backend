package com.camp.camping_service.scheduler;

import com.camp.camping_service.services.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ImageScheduler {

    private final ImageService imageService;

    @Scheduled(cron = "0 */5 * * * ?")
    public void cleanup(){
        log.info("start clean up.");
        imageService.cleanupPreUploadFiles();
        log.info("clean up finished.");
    }
}
