import java.util.Stack;

class LLVMGenerator{
   
   static String header_text = "";
   static String main_text = "";
   static int reg = 1;
   static int br = 0;

   static Stack<Integer> brstack = new Stack<Integer>();

// -------------------------------------------------
   // Wyswietlanie
   static void printf_i32(String id){
      main_text += "%"+reg+" = load i32* %"+id+"\n";
      reg++;
      main_text += "%"+reg+" = call i32 (i8*, ...)* @printf(i8* getelementptr inbounds ([4 x i8]* @strpi, i32 0, i32 0), i32 %"+(reg-1)+")\n";
      reg++;
   }

   static void printf_double(String id){
      main_text += "%"+reg+" = load double* %"+id+"\n";
      reg++;
      main_text += "%"+reg+" = call i32 (i8*, ...)* @printf(i8* getelementptr inbounds ([4 x i8]* @strpd, i32 0, i32 0), double %"+(reg-1)+")\n";
      reg++;
   }

// ---------------------------------------------------
   // Pobieranie danych 
   static void scanf_i32(String id){
      main_text += "%"+reg+" = call i32 (i8*, ...)* @__isoc99_scanf(i8* getelementptr inbounds ([3 x i8]* @strs, i32 0, i32 0), i32* %"+id+")\n";
      reg++;      
   }

   static void scanf_double(String id){
      main_text += "%"+reg+" = call i32 (i8*, ...)* @__isoc99_scanf(i8* getelementptr inbounds ([3 x i8]* @strs, i32 0, i32 0), double* %"+id+")\n";
      reg++;      
   }

// --------------------------------------------------
   // Deklaracja
   static void declare_i32(String id){
      main_text += "%"+id+" = alloca i32\n";
   }

   static void declare_double(String id){
      main_text += "%"+id+" = alloca double\n";
   }

// --------------------------------------------------
   // Przypisywanie
   static void assign_i32(String id, String value){
      main_text += "store i32 "+value+", i32* %"+id+"\n";
   }

   static void assign_double(String id, String value){
      main_text += "store double "+value+", double* %"+id+"\n";
   }

// --------------------------------------------------
   // Wczytywanie
   static void load_i32(String id){
      main_text += "%"+reg+" = load i32* %"+id+"\n";
      reg++;
   }

   static void load_double(String id){
      main_text += "%"+reg+" = load double* %"+id+"\n";
      reg++;
   }

//---------------------------------------------------------------------------------------

   // Instrukcja warunkowa IF

   static void conditionHelper(String id, String value, String cond){
     main_text += "%"+reg+" = load i32* %"+id+"\n";
     reg++;
     main_text += "%"+reg+" = icmp "+cond+" i32 %"+(reg-1)+", "+value+"\n";
     reg++;
   }

   static void icmpeq(String id, String value){
     conditionHelper(id, value, "eq");
   }

   static void icmpmore(String id, String value){
     conditionHelper(id, value, "sgt");
   }

   static void icmpless(String id, String value){
     conditionHelper(id, value, "slt");
   }

   static void icmpneq(String id, String value){
     conditionHelper(id, value, "ne");
   }

   static void icmpmoreoreq(String id, String value){
     conditionHelper(id, value, "sge");
   }

   static void icmplessoreq(String id, String value){
     conditionHelper(id, value, "sle");
   }

   static void ifstart(){
     br++;
     main_text += "br i1 %"+(reg-1)+", label %true"+br+", label %false"+br+"\n";
     main_text += "true"+br+":\n";
     brstack.push(br);
   }

   static void ifend(){
   }

   static void elsestart(){
     int b = brstack.pop();
     main_text += "ret i32 0\n";
     main_text += "false"+b+":\n";
   }

   static void elseend(){
   }


// Petla

   static void repeatstart(String repetitions){
     declare_i32(Integer.toString(reg));
     int counter = reg;
     reg++;
     assign_i32(Integer.toString(counter), "0");    
     br++;
     main_text += "br label %cond"+br+"\n";
     main_text += "cond"+br+":\n";

     load_i32(Integer.toString(counter));
     add_i32("%"+(reg-1), "1");
     assign_i32(Integer.toString(counter), "%"+(reg-1));

     main_text += "%"+reg+" = icmp slt i32 %"+(reg-2)+", "+repetitions+"\n";
     reg++;

     main_text += "br i1 %"+(reg-1)+", label %true"+br+", label %false"+br+"\n";
     main_text += "true"+br+":\n";
     brstack.push(br);
   }

   static void repeatend(){
     int b = brstack.pop();
     main_text += "br label %cond"+b+"\n";
     main_text += "false"+b+":\n";
   }


//---------------------------------------------------------------------------------------
   // Operacje arytmetyczne

   // Dodawanie

   // Dodawanie zmiennych typu Integer
   static void add_i32(String val1, String val2){
      main_text += "%"+reg+" = add i32 "+val1+", "+val2+"\n";
      reg++;
   }

   // Dodawanie zmiennych typu Double
   static void add_double(String val1, String val2){
      main_text += "%"+reg+" = fadd double "+val1+", "+val2+"\n";
      reg++;
   }

   //----------------------------
   // Odejmowanie

   // Odejmowanie zmiennych typu Integer
   static void sub_i32(String val1, String val2){
      main_text += "%"+reg+" = sub i32 "+val2+", "+val1+"\n";
      reg++;
   }

   // Odejmowanie zmiennych typu Double
   static void sub_double(String val1, String val2){
      main_text += "%"+reg+" = fsub double "+val2+", "+val1+"\n";
      reg++;
   }

   //----------------------------
   // Mnożenie

   // Mnożenie zmiennych typu Integer
   static void mult_i32(String val1, String val2){
      main_text += "%"+reg+" = mul i32 "+val1+", "+val2+"\n";
      reg++;
   }

   // Mnożenie zmiennych typu Double
   static void mult_double(String val1, String val2){
      main_text += "%"+reg+" = fmul double "+val1+", "+val2+"\n";
      reg++;
   }

   //----------------------------
   // Dzielenie

   // Dzielenie zmiennych typu Integer
   static void div_i32(String val1, String val2){
      main_text += "%"+reg+" = udiv i32 "+val2+", "+val1+"\n";
      reg++;
   }

   // Dzielenie zmiennych typu Double
   static void div_double(String val1, String val2){
      main_text += "%"+reg+" = fdiv double "+val2+", "+val1+"\n";
      reg++;
   }

   static void sitofp(String id){
      main_text += "%"+reg+" = sitofp i32 "+id+" to double\n";
      reg++;
   }

   static void fptosi(String id){
      main_text += "%"+reg+" = fptosi double "+id+" to i32\n";
      reg++;
   }


   static String generate(){
      String text = "";
      text += "declare i32 @printf(i8*, ...)\n";
      text += "declare i32 @__isoc99_scanf(i8*, ...)\n";
      text += "@strpi = constant [4 x i8] c\"%d\\0A\\00\"\n";
      text += "@strpd = constant [4 x i8] c\"%f\\0A\\00\"\n";
      text += "@strs = constant [3 x i8] c\"%d\\00\"\n";
      text += header_text;
      text += "define i32 @main() nounwind{\n";
      text += main_text;
      text += "ret i32 0 }\n";
      return text;
   }

}
