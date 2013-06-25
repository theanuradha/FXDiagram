package de.itemis.javafx.diagram.binding;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringExpression;

@SuppressWarnings("all")
public class StringExpressionExtensions {
  public static StringExpression operator_plus(final StringExpression left, final Object right) {
    return Bindings.concat(left, right);
  }
  
  public static StringExpression operator_plus(final Object left, final StringExpression right) {
    return Bindings.concat(left, right);
  }
}