package com.tattea.analyzer.service.snmp;

import com.tattea.analyzer.service.TrapService;
import com.tattea.analyzer.service.dto.TrapDTO;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.testcontainers.shaded.org.apache.commons.io.FileUtils;

@Service
public class SNMPReader {

    private static final String snmpFileName = "";

    private final TrapService trapService;

    @Autowired
    public SNMPReader(TrapService trapService) {
        this.trapService = trapService;
    }

    /*
     *
     *
     *
     *
     * */

    public List<TrapDTO> getTraps() throws IOException {
        File file = new File(snmpFileName);
        List<String> snmpText = FileUtils.readLines(file, StandardCharsets.UTF_8);

        // convert snmpText to String (List<String> -> String) assign to variable
        //        // create a list of snmp traps (each snmp trap is separated by a line)
        //        // List<String> of snmps -> TrapDTO (create method, take data from each snmp in the list and
        //        // add to the TrapDTO - trapDTO= TrapDTO.builder().build())
        //        /*   Each TrapDTO to be saved    trapService.save(trapDTO);*/
        //        //trapService.findAll(Pageable.ofSize(1)).forEach(System.out::println);
        //        // or go on Database on right hand side and click on trap table

        return null;
    }
}
