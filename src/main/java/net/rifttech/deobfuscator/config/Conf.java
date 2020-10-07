package net.rifttech.deobfuscator.config;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public class Conf {
    private String inputFile = "input.jar";

    private List<String> generic = Arrays.asList("int-xor", "long-xor");

    private List<String> radon = Arrays.asList("flow", "string", "number");
}
