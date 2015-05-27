grammar Przemi;

prog: block 
; 

block: ( stat? NEWLINE )* 
;

stat:	  IF cond THEN blockif elsecond ENDIF          #if
      | ID '=' expr1		              #assign
	| PRINT ID   		              #print
      | SET ID                              #set
;

expr1:  expr2			        #single1
      | expr2 MULT expr2	        #mult 
      | expr2 DIV expr2		        #div
      | expr2 ADD expr2               #add 
      | expr2 SUB expr2               #sub  
;

elsecond: ELSE blockelse
      |
;

blockif: block
;

blockelse: block
;

cond:   ID '==' INT           #equal
      | ID '>'  INT           #more
      | ID '<'  INT           #less
      | ID '!=' INT           #notequal
      | ID '>=' INT           #moreoreq
      | ID '<=' INT           #lessoreq
;

expr2:  INT			                #int
      | REAL			          #real
      | TOINT expr2		          #toint
      | TOREAL expr2		          #toreal
      | '(' expr1 ')'		          #par
;	

      
PRINT: 'p' 
;

SET: 'set'
;

IF: 'if'
;

ELSE: 'else'
;

THEN: 'do'
;

ENDIF: 'end'
;

TOINT: '(int)'
;

TOREAL: '(double)'
;

ID:   ('a'..'z'|'A'..'Z')+
;

REAL: '0'..'9'+'.''0'..'9'+
;

INT: '0'..'9'+
;

ADD: '+'
;

SUB: '-'
;

DIV: '/'
;  

MULT: '*'
;

NEWLINE:	'\r'? '\n'
;

WS:   (' '|'\t')+ { skip(); }
;
