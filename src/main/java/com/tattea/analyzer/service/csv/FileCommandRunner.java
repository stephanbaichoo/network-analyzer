package com.tattea.analyzer.service.csv;

import java.util.List;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import java.io.File;
import java.util.Arrays;

@Service
public class FileCommandRunner {

    //private static final String csvDirectory = "";

    public void cutAllCSVFiles(String csvDirectory) {

        //File f = new File("/home/tattea/test");
        //File[] matchingFiles = f.listFiles((dir, name) -> name.startsWith("nfcapd.current"));
        //assert matchingFiles != null;
        //String currentName = Arrays.stream(matchingFiles).findFirst().orElseThrow().getName();
        //mv /home/tattea/test1/!(nfcapd.current.13589)  /home/tattea/test/tartget1
         runCommand(String.format("mv /home/tattea/test/!(nfcapd.current.63303) /home/tattea/test1"));
        runCommand(String.format("mv //home/tattea/NetFlow_cvs /home/tattea/test1"));
         runCommand(String.format("mv /home/tattea/test/nfcapd.202207180025 /home/tattea/test1", csvDirectory));

    }

    @SneakyThrows
    public void runCommand(String cmd) {
        Runtime r = Runtime.getRuntime();

        String[] commands = { "bash", "-c", cmd };

        Process p = r.exec(commands);
        p.waitFor();
    }

    public String buildListCmd(List<String> cmds) {
        return cmds.stream().map(cmd -> cmd.concat(" ;")).collect(Collectors.joining());
    }
}
