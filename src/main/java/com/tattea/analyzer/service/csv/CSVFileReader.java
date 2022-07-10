package com.tattea.analyzer.service.csv;

import com.tattea.analyzer.service.dto.NetflowDTO;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author tattea
 */
@Service
public class CSVFileReader {

    private static final String csvPath =
        "C:\\Users\\steph\\Documents\\Projects\\analyser\\src\\main\\resources\\static\\nfcapd.202205020100.csv";

    @Autowired
    public CSVFileReader() {}

    public List<NetflowDTO> getNetflows(String csvPath) {
        List<List<String>> records = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvPath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.trim().split(" ");
                records.add(Arrays.asList(values));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        var objects = records
            .stream()
            .skip(1) // skip header
            .filter(strings -> !startWithIllegalWords(String.join("", strings)))
            .map(strings ->
                strings.stream().filter(t -> !StringUtils.isEmpty(t)).filter(t -> !t.contains("->")).collect(Collectors.toList())
            )
            .collect(Collectors.toList());

        return objects
            .stream()
            .map(strings -> {
                String[] toArray = strings.toArray(String[]::new);

                for (int i = 0; i < toArray.length; i++) {
                    if (toArray[i].equals("M")) {
                        toArray[i - 1] = toArray[i - 1].concat("M");
                        toArray[i] = "";
                    }
                }
                return List.of(toArray);
            })
            .map(strings -> strings.stream().filter(t -> !StringUtils.isEmpty(t)).collect(Collectors.toList()))
            .map(this::buildNetflow)
            .collect(Collectors.toList());
    }

    public List<NetflowDTO> getNetflows() {
        return getNetflows(csvPath);
    }

    private NetflowDTO buildNetflow(List<String> csvRow) {
        return NetflowDTO
            .builder()
            .dateFirstSeen(LocalDate.parse(csvRow.get(0)))
            .timeFirstSeen(LocalTime.parse(csvRow.get(1)).toString())
            .duration(new BigDecimal(csvRow.get(2)))
            .protocol(csvRow.get(3))
            .srcIp(csvRow.get(4))
            .dstIp(csvRow.get(5))
            .flags(csvRow.get(6))
            .tos(Integer.valueOf(csvRow.get(7)))
            .packetNo(Integer.valueOf(csvRow.get(8)))
            .bytes(csvRow.get(9))
            .pps(csvRow.get(10))
            .bps(csvRow.get(11))
            .bpp(csvRow.get(12))
            .flows(csvRow.get(13))
            .build();
    }

    private boolean startWithIllegalWords(String s) {
        return List.of("Summary", "Time", "Total", "Sys").stream().anyMatch(s::startsWith);
    }
}
