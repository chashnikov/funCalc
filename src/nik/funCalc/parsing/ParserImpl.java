package nik.funCalc.parsing;

import nik.funCalc.tree.*;

import java.util.ArrayList;
import java.util.List;

/*
 expression := addExpression
 addExpression := multExpression (('+'|'-') multExpression)*
 multExpression := primaryExpression (('*'|'/') primaryExpression)*
 primaryExpression := '(' expression ')'
 primaryExpression := id '(' expression (',' expression)* ')'
 statement := expression ';'
 statement := 'print' expression ';'
 statement :=

 program := (statement)+
*/

/**
 * @author nik
 */
public class ParserImpl implements Parser {
  private Lexer myLexer;

  public ParserImpl(Lexer lexer) {
    myLexer = lexer;
  }

  private Expression parsePrimaryExpression() {
    return null;
  }

  private Expression parseMultExpression() {
    Expression expression = parsePrimaryExpression();
    TokenType type = myLexer.nextToken();
    while (type == TokenType.SYMBOL && (myLexer.getToken().equals("*") || myLexer.getToken().equals("/"))) {
      Expression next = parseMultExpression();
      expression = new BinaryExpression(myLexer.getToken().equals("*") ? Operations.MULT : Operations.DIV, expression, next);
    }
    return expression;
  }

  private Expression parseAddExpression() {
    Expression expression = parseMultExpression();
    TokenType type = myLexer.nextToken();
    while (type == TokenType.SYMBOL && (myLexer.getToken().equals("+") || myLexer.getToken().equals("-"))) {
      Expression next = parseMultExpression();
      expression = new BinaryExpression(myLexer.getToken().equals("+") ? Operations.ADD : Operations.SUB, expression, next);
    }
    return expression;
  }

  private Expression parseExpression() {
    return parseAddExpression();
  }

  private Statement parseStatement() {
    TokenType type = myLexer.nextToken();
    if (type == TokenType.IDENTIFIER && myLexer.getToken().equals("print")) {
      Expression expression = parseExpression();
      symbolExpected(";");
      return new PrintStatement(expression);
    }
    myLexer.pushBack();
    Expression expression = parseExpression();
    symbolExpected(";");
    return new ExpressionStatement(expression);
  }

  private void symbolExpected(final String text) {
    tokenExpected(TokenType.SYMBOL, text);
  }

  private void tokenExpected(TokenType type, String text) {
    myLexer.nextToken();
    if (type != myLexer.getTokenType()) {
      throw new ParsingException(type.getName()+" expected but "+myLexer.getToken()+" found");
    }
    if (text != null && !text.equals(myLexer.getToken())) {
      throw new ParsingException(text+" expected but "+myLexer.getToken()+" found");
    }
  }

  public StatementsNode parseProgram() {
    List<Statement> statements = new ArrayList<Statement>();
    do {
      Statement statement = parseStatement();
      statements.add(statement);
    } while (myLexer.getTokenType() == TokenType.EOF);
    return new StatementsNode(statements.toArray(new Statement[statements.size()]));
  }
}
