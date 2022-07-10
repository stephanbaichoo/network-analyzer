package com.tattea.analyzer.service.csv;

import java.util.List;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
public class FileCommandRunner {

    private static final String csvDirectory = "";

    public void cutAllCSVFiles(String csvDirectory) {
        runCommand("mv *.csv target");
    }

    @SneakyThrows
    private void runCommand(String cmd) {
        Runtime r = Runtime.getRuntime();

        String[] commands = { "bash", "-c", cmd };

        Process p = r.exec(commands);
        p.waitFor();
    }

    private String buildListCmd(List<String> cmds) {
        return cmds.stream().map(cmd -> cmd.concat(" ;")).collect(Collectors.joining());
    }
}
