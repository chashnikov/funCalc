package nik.funCalc;

import junit.framework.TestCase;
import nik.funCalc.codegen.ProgramCodeGenerator;
import nik.funCalc.parsing.Lexer;
import nik.funCalc.parsing.ParserImpl;

import java.io.*;

/**
 * @author nik
 */
public class CodeGenTest extends TestCase {
  public void testNoOp() {
    assertResult("1;", "");
  }

  public void testPrint() {
    assertResult("print 1;", "1\n");
  }

  public void testAdd() {
    assertResult("print 1+1;", "2\n");
  }

  public void testSub() {
    assertResult("print 5-10;", "-5\n");
  }

  public void testMult() {
    assertResult("print 5*10;", "50\n");
  }

  public void testDiv() {
    assertResult("print 99/3;", "33\n");
  }

  public void testAssociativity() {
    assertResult("print 1-2-3;", "-4\n");
  }

  public void testTwoPrints() {
    assertResult("print 1;print 2;", "1\n2\n");
  }

  public void testVariable() {
    assertResult("a=1;print a;", "1\n");
  }

  private void assertResult(String program, String result) {
    try {
      ParserImpl parser = new ParserImpl(new Lexer(new StringReader(program)));
      final String className = "test";
      final byte[] classContent = ProgramCodeGenerator.generateClassContent(parser.parseProgram(), className);
      ClassLoader loader = new ClassLoader(getClass().getClassLoader()) {
        protected Class<?> findClass(String name) throws ClassNotFoundException {
          if (className.equals(name)) {
            Class<?> aClass = defineClass(name, classContent, 0, classContent.length);
            resolveClass(aClass);
            return aClass;
          }
          return super.findClass(name);
        }
      };
      Class<?> aClass = Class.forName(className, true, loader);

      ByteArrayOutputStream out = new ByteArrayOutputStream();
      PrintStream oldOut = System.out;
      try {
        System.setOut(new PrintStream(out));
        aClass.getMethod("main", String[].class).invoke(null, new Object[]{new String[0]});
      }
      finally {
        System.setOut(oldOut);
      }
      assertEquals(result, out.toString().replace("\r", ""));
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
