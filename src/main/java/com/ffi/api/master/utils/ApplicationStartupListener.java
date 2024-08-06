package com.ffi.api.master.utils;

/**
 *
 * @author USER
 */
import com.ffi.api.master.Constant;
import java.io.PrintStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartupListener implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    Constant constant;
    
    @Value("${server.port}")
    String port;

    @Value("${server.servlet.context-path}")
    String serverPath;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        printBanner(System.out);
    }

    // ANSI escape codes for colors
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_WHITE = "\u001B[37m";

    private void printBanner(PrintStream out) {
        out.println(ANSI_RED);
        out.println(ANSI_RED + "___  ___             _                 _   _  _____ ");
        out.println(ANSI_RED + "|  \\/  |            | |               | | | ||  _  |");
        out.println(ANSI_RED + "| .  . |  __ _  ___ | |_   ___  _ __  | |_| || | | |");
        out.println(ANSI_RED + "| |\\/| | / _` |/ __|| __| / _ \\| '__| |  _  || | | |");
        out.println(ANSI_RED + "| |  | || (_| |\\__ \\| |_ |  __/| |    | | | |\\ \\/' /");
        out.println(ANSI_RED + "\\_|  |_/ \\__,_||___/ \\__| \\___||_|    \\_| |_/ \\_/\\_\\");
        out.println(ANSI_RESET);
        out.println(ANSI_CYAN + "==== IT Solution Department PT.Fast Food Indonesia, Tbk. ====");
        out.println(ANSI_YELLOW + " :: Master HQ " + ":" + port + serverPath + " ::    (backend v " + constant.versionBe + ")");
        out.println(ANSI_RESET);
    }
}
