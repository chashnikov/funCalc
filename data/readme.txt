Язык "Калькулятор с функциями", версия 1.0

Команда funCalc.bat <file-path> вызывает компилятор для файла <file-path> и запускает скомпилированный код
(funCalc.bat -h выводит список дополнительных опций)

Пример кода - в файле examples.fc


Возможности языка:
 1. Арифметические операции (+,-,*,/) с целыми числами, вывод результата оператором 'print'
 2. Запись и чтение целочисленных переменных (переменная объявляется первым присваиванием)
 3. Операторы if и while (в качестве условия используется сравенение двух целочисленных выражений)
 4. Функции от одного или нескольких параметров; если функция возвращает значение, 'return' должен быть последним оператором;
    допускаются рекурсивные вызовы.

Грамматика:
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

Используемые библиотеки:
  Apache Commons CLI, лицензия Apache 2.0
  ASM, лицензия Apache 2.0