package com.book.command;

import com.book.command.enums.OptionEnum;
import org.apache.commons.cli.*;

import java.util.Scanner;

public class Main {
    private static final String END = "exit|end";
    private static final HelpFormatter formatter = new HelpFormatter();
    private static final Options options = init();

    public static void main(String[] args) {
        formatter.printHelp("book_listen", options, true);
        String[] ags = null;
        Scanner sc = new Scanner(System.in);
        while(sc.hasNext()){
            try {
                String value = sc.nextLine();
                if(value.matches(END)){
                    System.exit(-1);
                } else {
                    ags = value.split(" ");
                }
                CommandLineParser parser = new DefaultParser();
                CommandLine commandLine = parser.parse(options, ags);
                execute(commandLine);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public static void execute(CommandLine commandLine){
        if(commandLine.hasOption("h")){
            formatter.printHelp("book_listen", options, true);
            return;
        }
        for (OptionEnum optionEnum : OptionEnum.values()) {
            if (commandLine.hasOption(optionEnum.command())) {
                if(!optionEnum.hasArg()){
                    optionEnum.exec(null);
                    continue;
                }
                for (String s : commandLine.getOptionValues(optionEnum.command())) {
                    optionEnum.exec(s);
                }
            }
        }
    }

    public static Options init(){
        Options options = new Options();
        for (OptionEnum optionEnum : OptionEnum.values()) {
            Option opt = new Option(optionEnum.command(), optionEnum.fullCommand(), optionEnum.hasArg(),
                    optionEnum.desc());
            opt.setRequired(optionEnum.required());
            options.addOption(opt);
        }
        return options;
    }
}
