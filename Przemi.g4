grammar Przemi;

prog: block 
; 

block: ( stat? NEWLINE )* 
;

stat:	  IF cond THEN blockif ENDIF          #if
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

blockif: block
;

cond:   ID '==' INT           #equal  
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
