���� "����������� � ���������", ������ 1.0

������� funCalc.bat <file-path> �������� ���������� ��� ����� <file-path> � ��������� ���������������� ���
(funCalc.bat -h ������� ������ �������������� �����)

������ ���� - � ����� examples.fc


����������� �����:
 1. �������������� �������� (+,-,*,/) � ������ �������, ����� ���������� ���������� 'print'
 2. ������ � ������ ������������� ���������� (���������� ����������� ������ �������������)
 3. ��������� if � while (� �������� ������� ������������ ���������� ���� ������������� ���������)
 4. ������� �� ������ ��� ���������� ����������; ���� ������� ���������� ��������, 'return' ������ ���� ��������� ����������;
    ����������� ����������� ������.

����������:
 expression := addExpression
 addExpression := multExpression (('+'|'-') multExpression)*
 multExpression := primaryExpression (('*'|'/') primaryExpression)*
 primaryExpression := '(' expression ')'
 primaryExpression := id '(' expression (',' expression)* ')'
 primaryExpression := number
 primaryExpression := id
 booleanExpression := expression ('<' | '>' | '==' | '>=' | '<=' | '!=') expression
 statement := expression ';'
 statement := 'print' expression ';'
 statement := id '=' expression ';'
 statement := 'fun' id '(' id (',' id)* ')' statement
 statement := 'return' expression ';'
 statement := 'if' '(' booleanExpression ')' statement ['else' statement]
 statement := 'while' '(' booleanExpression ')' statement
 statement := '{' (statement)+ '}'

 program := (statement)+

������������ ����������:
  Apache Commons CLI, �������� Apache 2.0
  ASM, �������� Apache 2.0