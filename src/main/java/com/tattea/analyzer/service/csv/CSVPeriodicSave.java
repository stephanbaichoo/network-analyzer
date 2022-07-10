package com.tattea.analyzer.service.csv;

import com.tattea.analyzer.domain.Netflow;
import com.tattea.analyzer.service.NetflowService;
import com.tattea.analyzer.service.dto.NetflowDTO;
import com.tattea.analyzer.service.mapper.EntityMapper;
import java.io.File;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.org.apache.commons.io.FilenameUtils;

@Slf4j
@Transactional
@Service
public class CSVPeriodicSave {

    private static final String csvDirectory = "C:\\Users\\steph\\Documents\\Projects\\analyser\\src\\main\\resources\\static";
    private static final String CSV = "csv";
    private final CSVFileReader csvFileReader;
    private final NetflowService netflowService;
    private final EntityMapper<NetflowDTO, Netflow> entityMapper;
    private final FileCommandRunner fileCommandRunner;

    @Autowired
    public CSVPeriodicSave(
        CSVFileReader csvFileReader,
        NetflowService netflowService,
        EntityMapper<NetflowDTO, Netflow> entityMapper,
        FileCommandRunner fileCommandRunner
    ) {
        this.csvFileReader = csvFileReader;
        this.netflowService = netflowService;
        this.entityMapper = entityMapper;
        this.fileCommandRunner = fileCommandRunner;
    }

    /*
    * This method (Cron Job) will trigger at each interval to save all the csv from netflow in csv Directory
    * Then, after successful save into the MySQL database, we can query the data afterwards

     */
    @Scheduled(cron = "*/10 * * * * *") // will save each 10 seconds for eg : "*/10 * * * * *"
    public void triggerAllCSVSave() {
        log.info("CSV Save at : {}", LocalDateTime.now().toString()); // create a log in order to debug/check if the saves are successful

        try {
            getAllCSVFromDirectory()
                .stream()
                .map(File::getAbsolutePath) // get path from each file
                .flatMap(csvPath -> csvFileReader.getNetflows(csvPath).stream()) // get netflow data from each file
                .forEach(netflowService::save);

            fileCommandRunner.cutAllCSVFiles(csvDirectory);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<File> getAllCSVFromDirectory() {
        return Stream
            .of(csvDirectory)
            .map(File::new) // convert the string to a new file object
            .map(File::listFiles)
            .filter(Objects::nonNull)
            .flatMap(Arrays::stream)
            .filter(File::isFile) // check if the file is a file or a directory
            .filter(file -> FilenameUtils.getExtension(file.getName()).equals(CSV)) // check if the extension ends with .csv
            .collect(Collectors.toList()); // return all the csv files to be saved
    }
}
