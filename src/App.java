import java.text.*;

import Lexer.Lexer;
import Token.*;

public class App {
    private static final String code = "сумма = 1 + 2;";
    public static void main(String[] args) throws Exception {
        Lexer lexer = new Lexer(code);
        lexer.analys();
        
        for(Token token : lexer.getTokenList()){
            System.out.println(MessageFormat.format("name {0} text {1}",  token.getToken().getName(),token.getText()));
        }
        
       
    }
}
