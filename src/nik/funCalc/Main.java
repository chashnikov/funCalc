package nik.funCalc;

import nik.funCalc.codegen.GenerationException;
import nik.funCalc.codegen.ProgramCodeGenerator;
import nik.funCalc.parsing.*;
import nik.funCalc.tree.StatementsNode;
import org.apache.commons.cli.*;

import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * @author nik
 */
public class Main {
  private static final Options OPTIONS = new Options()
      .addOption("c", "compile-only", false, "do not run generated class-file")
      .addOption("d", "output-dir", true, "output directory name")
      .addOption("s", "stacktrace", false, "print stacktrace if exception is thrown")
      .addOption("h", "help", false, "show help message");

  public static void main(String[] args) {
    boolean printStackTrace = false;
    try {
      CommandLine commandLine = new GnuParser().parse(OPTIONS, args);
      if (commandLine.hasOption('h')) {
        printUsage();
        return;
      }

      printStackTrace = commandLine.hasOption('s');
      String[] remaining = commandLine.getArgs();
      if (remaining.length == 0) {
        throw new ParseException("Input file is not specified");
      }
      String filePath = remaining[0];
      File file = new File(filePath);
      BufferedReader reader = new BufferedReader(new FileReader(file));

      StatementsNode node;
      try {
        Lexer lexer = new Lexer(reader);
        node = new ParserImpl(lexer).parseProgram();
      }
      finally {
        reader.close();
      }

      String className = getClassName(file.getName());
      File outputDir = new File(commandLine.getOptionValue('d', "gen"));
      ProgramCodeGenerator.generate(node, outputDir, className);

      if (!commandLine.hasOption('c')) {
        try {
          URLClassLoader loader = new URLClassLoader(new URL[]{outputDir.toURI().toURL()}, Main.class.getClassLoader());
          Class<?> aClass = Class.forName(className, true, loader);
          aClass.getMethod("main", String[].class).invoke(null, new Object[]{new String[0]});
        }
        catch (Exception e) {
          System.err.println("Cannot execute: " + e.getMessage());
          if (printStackTrace) {
            e.printStackTrace();
          }
        }
      }
    }
    catch (ParseException e) {
      System.out.println("Cannot parse command line: "+e.getMessage());
      printUsage();
      if (printStackTrace) {
        e.printStackTrace();
      }
    }
    catch (IOException e) {
      System.err.println("I/O error: "+e.getMessage());
      if (printStackTrace) {
        e.printStackTrace();
      }
    }
    catch (ParsingException e) {
      System.err.println("Cannot parse program: "+e.getMessage());
      if (printStackTrace) {
        e.printStackTrace();
      }
    }
    catch (GenerationException e) {
      System.err.println("Cannot generate code: "+e.getMessage());
      if (printStackTrace) {
        e.printStackTrace();
      }
    }
  }

  private static void printUsage() {
    new HelpFormatter().printHelp("java -jar funCalc.jar [options] <file path>", OPTIONS);
  }

  private static String getClassName(String fileName) {
    int i = fileName.indexOf('.');
    return i != -1 ? fileName.substring(0, i) : fileName;
  }
}
