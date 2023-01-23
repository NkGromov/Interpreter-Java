package AST;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import Parser.FunctionDefiniton;

public class FnCallNode extends ExpressionNode {
  private String name;
  private List<Integer> arguments = new ArrayList<>();

  public FnCallNode(String name, List<Integer> arguments) {
    this.name = name;
    this.arguments = arguments;
  }

  @Override
  public void applyNode(Map<String, Integer> scope, Map<String, FunctionDefiniton> fnDefinitions) {
    FunctionDefiniton fn = fnDefinitions.get(name);
    List<ExpressionNode> body = fn.getBody();
    for (ExpressionNode node : body) {
      node.applyNode(scope);
    }
  }

  public List<Integer> getArguments() {
    return arguments;
  }

  public String getName() {
    return name;
  }

}
