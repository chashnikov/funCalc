package nik.funCalc;

import nik.funCalc.codegen.ProgramCodeGenerator;
import nik.funCalc.parsing.Lexer;
import nik.funCalc.parsing.ParserImpl;
import nik.funCalc.tree.StatementsNode;

import java.io.*;

/**
 * @author nik
 */
public class Main {
  public static void main(String[] args) throws IOException {
    String fileName = args[0];
    BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)));

    StatementsNode node;
    try {
      Lexer lexer = new Lexer(reader);
      node = new ParserImpl(lexer).parseProgram();
    }
    finally {
      reader.close();
    }

    File outputDir = new File("out");
    outputDir.mkdirs();
    String className = getClassName(fileName);
    ProgramCodeGenerator.generate(node, outputDir, className);
  }

  private static String getClassName(String fileName) {
    int i = fileName.indexOf('.');
    return i != -1 ? fileName.substring(0, i) : fileName;
  }
}
