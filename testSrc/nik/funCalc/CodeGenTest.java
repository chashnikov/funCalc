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

  public void testParentheses() {
    assertResult("print 2*(2-3);", "-2\n");
  }

  public void testTwoPrints() {
    assertResult("print 1;print 2;", "1\n2\n");
  }

  public void testVariable() {
    assertResult("a=1;print a;", "1\n");
  }

  public void testVoidFun() {
    assertResult("fun myprint(i) { print i; } myprint(239);", "239\n");
  }

  public void testFunWithOneArg() {
    assertResult("fun sqr(i) { return i*i; } print sqr(2);", "4\n");
  }

  public void testFunWithTwoArgs() {
    assertResult("fun sum(a,b) { return a+b; } print sum(2,3);", "5\n");
  }

  public void testIfLess() {
    assertResult("if (1<2) print 1;", "1\n");
    assertResult("if (2<2) print 1;", "");
  }

  public void testIfEq() {
    assertResult("if (2==2) print 1;", "1\n");
    assertResult("if (2==3) print 1;", "");
  }

  public void testIfGreater() {
    assertResult("if (2>3) print 1;", "");
    assertResult("if (2>0-3) print 1;", "1\n");
  }

  public void testIfElse() {
    assertResult("if (1>2) print 1; else print 2;", "2\n");
    assertResult("if (1<2) print 1; else print 2;", "1\n");
  }

  public void testGreaterEq() {
    assertFalse("1>=2");
    assertTrue("1>=1");
    assertTrue("1>=0");
  }

  public void testLessEq() {
    assertTrue("1<=2");
    assertTrue("1<=1");
    assertFalse("1<=0");
  }

  public void testNotEq() {
    assertTrue("1!=2");
    assertFalse("1!=1");
  }

  public void testWhile() {
    assertResult("a = 1; b = 0; while (a<=5) { b = b+a; a=a+1; } print b;", "15\n");
    assertResult("while (0<0) { print 1; } print 2;", "2\n");
  }

  public void testRecFun() {
    assertResult("fun fac(n) { if (n==0) r = 1; else r = n*fac(n-1); return r;} print fac(5);", "120\n");
  }

  public void testFib() {
    assertResult("fun fib(n) { " +
                 "  if (n==0) r = 0; " +
                 "  else if (n==1) r = 1; " +
                 "  else r = fib(n-1) + fib(n-2); " +
                 "  return r;" +
                 "} " +
                 "print fib(6);", "8\n");
  }

  private void assertTrue(String expression) {
    assertResult("if (" + expression + ") print 1;", "1\n");
  }

  private void assertFalse(String expression) {
    assertResult("if (" + expression + ") print 1;", "");
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
