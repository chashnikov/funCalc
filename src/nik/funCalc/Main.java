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
    String filePath = args[0];
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
    ProgramCodeGenerator.generate(node, new File("gen"), className);
  }

  private static String getClassName(String fileName) {
    int i = fileName.indexOf('.');
    return i != -1 ? fileName.substring(0, i) : fileName;
  }
}
