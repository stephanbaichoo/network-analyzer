package com.tattea.analyzer.service.csv;

import com.tattea.analyzer.domain.Netflow;
import com.tattea.analyzer.service.NetflowService;
import com.tattea.analyzer.service.dto.NetflowDTO;
import com.tattea.analyzer.service.mapper.EntityMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.XSlf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class CSVPeriodicSave {

    private static final String csvDirectory = "/home/tattea/test";
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
    @SneakyThrows
    //@Scheduled(cron = "*/40 * * * * *") // will save each 10 seconds for eg : "*/10 * * * * *"
    public void triggerAllCSVSave() {
        log.info("CSV Save at : {}", LocalDateTime.now()); // create a log in order to debug/check if the saves are successful

        try {
            fndumpsConvertToCsv(); // convert all nfcapd dumps to csv

            Thread.sleep(5000 ); // wait 5 seconds

            //


            getAllCSVFromDirectory()
                .stream()
                .map(File::getAbsolutePath) // get path from each file
                .flatMap(csvPath -> csvFileReader.getNetflows(csvPath).stream()) // get netflow data from each file
                .forEach(netflowService::save);


            fndumpsMoveToCsv();
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

    public void fndumpsConvertToCsv() {
        Stream.of(csvDirectory)
            .map(File::new) // convert the string to a new file object
            .map(File::listFiles)
            .filter(Objects::nonNull)
            .flatMap(Arrays::stream)
            .filter(File::isFile) // check if the file is a file or a directory
            .filter(file -> !file.getName().contains("current"))
            .filter(file -> !FilenameUtils.getExtension(file.getName()).equals(CSV)) // ignore all csv
            .forEach(file -> convertToCSV(file.getName()));


    }

    public void fndumpsMoveToCsv() {
        Stream.of(csvDirectory)
            .map(File::new) // convert the string to a new file object
            .map(File::listFiles)
            .filter(Objects::nonNull)
            .flatMap(Arrays::stream)
            .filter(File::isFile) // check if the file is a file or a directory
            .filter(file -> !file.getName().contains("current"))
            .filter(file -> !FilenameUtils.getExtension(file.getName()).equals(CSV)) // ignore all csv
            .forEach(file -> Moveallfile(file.getName()));


    }
    private void convertToCSV(String fileName) {
        String nfdumpCmd = String.format("nfdump -r %s -n 0  -O bps -o extended > %s.csv", fileName, fileName);
        String cd = "cd ".concat(csvDirectory);
        fileCommandRunner.runCommand(fileCommandRunner.buildListCmd(List.of(cd, nfdumpCmd)));

    }
    private void Moveallfile(String fileName) {
        fileCommandRunner.cutAllCSVFiles(fileName);

    }
}



