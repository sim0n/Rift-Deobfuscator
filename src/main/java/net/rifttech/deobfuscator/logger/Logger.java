package net.rifttech.deobfuscator.logger;

public class Logger {

    public void info(String message) {
        System.out.println("[*] " + message);
    }

    public void warn(String message) {
        System.out.println("[!] " + message);
    }

    public void emptyLine() {
        System.out.println();
    }

    public void printLogo() {
        System.out.println("#######################");
        System.out.println("#                     #");
        System.out.println("#  RIFT DEOBFUSCATOR  #");
        System.out.println("#                     #");
        System.out.println("#######################");
    }
}
