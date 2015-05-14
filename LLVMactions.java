
import java.util.HashMap;
import java.util.Stack;

enum VarType{ INT, REAL, UNKNOWN }

class Value{ 
	public String name;
	public VarType type;
	public Value( String name, VarType type ){
		this.name = name;
		this.type = type;
	}
}

public class LLVMactions extends PrzemiBaseListener {
    
    HashMap<String, VarType> variables = new HashMap<String, VarType>();
    Stack<Value> stack = new Stack<Value>();

// ------------------------------------------------------------------------------

    // Przypisywanie wartosci do zmiennych

    @Override
    public void exitAssign(PrzemiParser.AssignContext ctx) { 
       String ID = ctx.ID().getText();
       Value v = stack.pop();
       variables.put(ID, v.type);
       if( v.type == VarType.INT ){
         LLVMGenerator.declare_i32(ID);
         LLVMGenerator.assign_i32(ID, v.name);
       } 
       if( v.type == VarType.REAL ){
         LLVMGenerator.declare_double(ID);
         LLVMGenerator.assign_double(ID, v.name);
       } 
    }

    @Override 
    public void exitProg(PrzemiParser.ProgContext ctx) { 
       System.out.println( LLVMGenerator.generate() );
    }

    @Override 
    public void exitInt(PrzemiParser.IntContext ctx) { 
         stack.push( new Value(ctx.INT().getText(), VarType.INT) );       
    } 

    @Override 
    public void exitReal(PrzemiParser.RealContext ctx) { 
         stack.push( new Value(ctx.REAL().getText(), VarType.REAL) );       
    } 

//--------------------------------------------------------------------------

    // Funkcje operacji arytmetycznych
    
    // Operacja Dodawania
    @Override 
    public void exitAdd(PrzemiParser.AddContext ctx) { 
       Value v1 = stack.pop();
       Value v2 = stack.pop();
       if( v1.type == v2.type ) {
        if( v1.type == VarType.INT ){
            LLVMGenerator.add_i32(v1.name, v2.name); 
            stack.push( new Value("%"+(LLVMGenerator.reg-1), VarType.INT) ); 
              }
        if( v1.type == VarType.REAL ){
            LLVMGenerator.add_double(v1.name, v2.name); 
            stack.push( new Value("%"+(LLVMGenerator.reg-1), VarType.REAL) ); 
             }
       } else if (v1.type != v2.type){
          if (v1.type == VarType.INT) {
            LLVMGenerator.sitofp(v1.name);
            LLVMGenerator.add_double("%"+(LLVMGenerator.reg-1), v2.name);
            stack.push( new Value("%"+(LLVMGenerator.reg-1), VarType.REAL) );
          }
          if (v2.type == VarType.INT) {
            LLVMGenerator.sitofp(v2.name);
            LLVMGenerator.add_double(v1.name, "%"+(LLVMGenerator.reg-1));
            stack.push( new Value("%"+(LLVMGenerator.reg-1), VarType.REAL) );
          }
       }
    }

    // Operacja Odejmowania
    @Override 
    public void exitSub(PrzemiParser.SubContext ctx) { 
       Value v1 = stack.pop();
       Value v2 = stack.pop();
       if( v1.type == v2.type ) {
          if( v1.type == VarType.INT ){
            LLVMGenerator.sub_i32(v1.name, v2.name); 
            stack.push( new Value("%"+(LLVMGenerator.reg-1), VarType.INT) ); 
          }
          if( v1.type == VarType.REAL ){
            LLVMGenerator.sub_double(v1.name, v2.name); 
            stack.push( new Value("%"+(LLVMGenerator.reg-1), VarType.REAL) ); 
               }
          } else if (v1.type != v2.type){
          if (v1.type == VarType.INT) {
            LLVMGenerator.sitofp(v1.name);
            LLVMGenerator.sub_double("%"+(LLVMGenerator.reg-1), v2.name);
            stack.push( new Value("%"+(LLVMGenerator.reg-1), VarType.REAL) );
          }
          if (v2.type == VarType.INT) {
            LLVMGenerator.sitofp(v2.name);
            LLVMGenerator.sub_double(v1.name, "%"+(LLVMGenerator.reg-1));
            stack.push( new Value("%"+(LLVMGenerator.reg-1), VarType.REAL) );
          }
       }
    }

    // Operacja Mnożenia
    @Override 
    public void exitMult(PrzemiParser.MultContext ctx) { 
       Value v1 = stack.pop();
       Value v2 = stack.pop();
       if( v1.type == v2.type ) {
      	  if( v1.type == VarType.INT ){
            LLVMGenerator.mult_i32(v1.name, v2.name); 
            stack.push( new Value("%"+(LLVMGenerator.reg-1), VarType.INT) ); 
                }
      	  if( v1.type == VarType.REAL ){
            LLVMGenerator.mult_double(v1.name, v2.name); 
            stack.push( new Value("%"+(LLVMGenerator.reg-1), VarType.REAL) ); 
               }
       } else if (v1.type != v2.type){
          if (v1.type == VarType.INT) {
            LLVMGenerator.sitofp(v1.name);
            LLVMGenerator.mult_double("%"+(LLVMGenerator.reg-1), v2.name);
            stack.push( new Value("%"+(LLVMGenerator.reg-1), VarType.REAL) );
          }
          if (v2.type == VarType.INT) {
            LLVMGenerator.sitofp(v2.name);
            LLVMGenerator.mult_double(v1.name, "%"+(LLVMGenerator.reg-1));
            stack.push( new Value("%"+(LLVMGenerator.reg-1), VarType.REAL) );
          }
       }
    }

    // Operacja Dzielenia
    @Override 
    public void exitDiv(PrzemiParser.DivContext ctx) { 
       Value v1 = stack.pop();
       Value v2 = stack.pop();
       if( v1.type == v2.type ) {
          if( v1.type == VarType.REAL ){
            LLVMGenerator.div_double(v1.name, v2.name); 
            stack.push( new Value("%"+(LLVMGenerator.reg-1), VarType.REAL) );
          }
          if( v1.type == VarType.INT ){
            LLVMGenerator.sitofp(v1.name);
            LLVMGenerator.sitofp(v2.name);
            LLVMGenerator.div_double("%"+(LLVMGenerator.reg-2), "%"+(LLVMGenerator.reg-1));
            stack.push( new Value("%"+(LLVMGenerator.reg-1), VarType.REAL) ); 
           } 
       } else if (v1.type != v2.type){
          if (v1.type == VarType.INT) {
            LLVMGenerator.sitofp(v1.name);
            LLVMGenerator.div_double("%"+(LLVMGenerator.reg-1), v2.name);
            stack.push( new Value("%"+(LLVMGenerator.reg-1), VarType.REAL) );
          }
          if (v2.type == VarType.INT) {
            LLVMGenerator.sitofp(v2.name);
            LLVMGenerator.div_double(v1.name, "%"+(LLVMGenerator.reg-1));
            stack.push( new Value("%"+(LLVMGenerator.reg-1), VarType.REAL) );
          }
       }
    }

//--------------------------------------------------------------------------    

    // Konwersja zmiennych

    // Konwersja na zmienna typu Integer
    @Override 
    public void exitToint(PrzemiParser.TointContext ctx) { 
       Value v = stack.pop();
       LLVMGenerator.fptosi( v.name );
       stack.push( new Value("%"+(LLVMGenerator.reg-1), VarType.INT) ); 
    }

    // Konwersja na zmienna typu Double
    @Override 
    public void exitToreal(PrzemiParser.TorealContext ctx) { 
       Value v = stack.pop();
       LLVMGenerator.sitofp( v.name );
       stack.push( new Value("%"+(LLVMGenerator.reg-1), VarType.REAL) ); 
    }

// ---------------------------------------------------------------------

    // Obsluga danych wejsciowych i wyjsciowych

    // Wyswietlanie
    @Override
    public void exitPrint(PrzemiParser.PrintContext ctx) {
       String ID = ctx.ID().getText();
       VarType type = variables.get(ID);
       if( type != null ) {
          if( type == VarType.INT ){
            LLVMGenerator.printf_i32( ID );
          }
          if( type == VarType.REAL ){
            LLVMGenerator.printf_double( ID );
          }
       } else {
          error(ctx.getStart().getLine(), "unknown variable "+ID);
       }
    }

    // Pobieranie danych
    @Override
    public void exitSet(PrzemiParser.SetContext ctx) {
       String ID = ctx.ID().getText();
       if( ! variables.containsKey(ID) ) {
          variables.put(ID, VarType.INT);
          LLVMGenerator.declare_i32(ID);          
       } 
       LLVMGenerator.scanf_i32(ID);
    }

// -------------------------------------------------------------------

    // Obsluga bledow

   void error(int line, String msg){
       System.err.println("Error, line "+line+", "+msg);
       System.exit(1);
   } 
       
}
