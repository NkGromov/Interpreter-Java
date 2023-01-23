package AST;

import java.util.ArrayList;
import java.util.List;

public class PrototypeNode extends ExpressionNode {
  private String name;
  private List<String> arguments = new ArrayList<>();

  public PrototypeNode(String name, List<String> arguments) {
    this.name = name;
    this.arguments = arguments;
  }

  public List<String> getArguments() {
    return arguments;
  }

  public String getName() {
    return name;
  }
  
}
