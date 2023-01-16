import AST.StatementsNode;
import Lexer.Lexer;
import Parser.Parser;

public class App {
    private static final String code = "сумма = 1 + (2 + 4); КОНСОЛЬ сумма;";
    public static void main(String[] args) throws Exception {
        Lexer lexer = new Lexer(code);
        lexer.analys();
        
        Parser parser = new Parser(lexer.getTokenList());
        StatementsNode root =  parser.parseCode();
        parser.run(root);
 
    }
}
