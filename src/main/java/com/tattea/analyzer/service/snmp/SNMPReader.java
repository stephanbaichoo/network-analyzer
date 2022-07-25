package com.tattea.analyzer.service.snmp;

import com.tattea.analyzer.domain.Trap;
import com.tattea.analyzer.repository.TrapRepository;
import com.tattea.analyzer.service.TrapService;
import com.tattea.analyzer.service.dto.TrapDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.testcontainers.shaded.org.apache.commons.io.FileUtils;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class SNMPReader {

    private static final String snmpFileName = "/var/log/snmptt/snmpttunknown.log";

    private final TrapService trapService;

    private final TrapRepository trapRepository;

    @Autowired
    public SNMPReader(TrapService trapService, TrapRepository trapRepository) {
        this.trapService = trapService;
        this.trapRepository = trapRepository;
    }

    public void getTraps() throws IOException {
        trapRepository.deleteAll();
        File file = new File(snmpFileName);
        String snmpText = FileUtils.readFileToString(file, StandardCharsets.UTF_8);

        Arrays.stream(snmpText.split("(?m)^\\s*$"))
            .map(this::buildTrapDTO)
            .filter(Objects::nonNull)
            .forEach(trapService::save);
    }

    private TrapDTO buildTrapDTO(String trap) {
        Supplier<Stream<String>> trapStream = () -> Arrays.stream(trap.split("\n"));

        String unknownTrap = trapStream.get().filter(t -> t.contains("Unknown trap")).findFirst().orElse("");
        if (!unknownTrap.equals("")) {
            String[] unknown_traps = unknownTrap.split(": Unknown trap");
            Date date = new Date(unknown_traps[0].trim());

            Supplier<Stream<String>> values = () -> trapStream.get().filter(t -> !t.contains("Unknown trap"));

            String value = values.get().filter(val -> val.startsWith("Value")).collect(Collectors.joining());

            String trigger = values.get().filter(val -> !val.startsWith("Value")).collect(Collectors.joining());

            return TrapDTO.builder()
                .date(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                .time(date.toInstant().atZone(ZoneId.systemDefault()).toLocalTime().toString())
                .trap(unknown_traps[1])
                .values(value)
                .trigger(trigger)
                .build();
        }
        return null;
    }
}
