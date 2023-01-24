import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import AST.StatementsNode;
import Lexer.Lexer;
import Parser.Parser;

public class App {
    public static void main(String[] args) throws Exception {
        Path path = Paths.get("code.gl");
        Stream<String> lines = Files.lines(path);
        String fullString = lines.collect(Collectors.joining("\n"));
        lines.close();

        Lexer lexer = new Lexer(fullString);
        lexer.analys();
        
        Parser parser = new Parser(lexer.getTokenList());
        StatementsNode root =  parser.parseCode();
        parser.run(root);
 
    }
}
