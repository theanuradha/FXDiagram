package de.itemis.javafx.diagram;

import de.itemis.javafx.diagram.Extensions;
import de.itemis.javafx.diagram.XNode;
import de.itemis.javafx.diagram.XRapidButton;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;

@SuppressWarnings("all")
public class Placer extends ObjectBinding<Point2D> {
  private XRapidButton button;
  
  private double xPos;
  
  private double yPos;
  
  public Placer(final XRapidButton button, final double xPos, final double yPos) {
    this.button = button;
    this.xPos = xPos;
    this.yPos = yPos;
    this.activate();
  }
  
  public void activate() {
    final XNode node = this.button.getHost();
    DoubleProperty _layoutXProperty = node.layoutXProperty();
    DoubleProperty _layoutYProperty = node.layoutYProperty();
    ReadOnlyObjectProperty<Bounds> _layoutBoundsProperty = node.layoutBoundsProperty();
    this.bind(_layoutXProperty, _layoutYProperty, _layoutBoundsProperty);
  }
  
  protected Point2D computeValue() {
    Point2D _xblockexpression = null;
    {
      final XNode node = this.button.getHost();
      Bounds _layoutBounds = node.getLayoutBounds();
      final Bounds boundsInDiagram = Extensions.localToDiagram(node, _layoutBounds);
      Point2D _xifexpression = null;
      boolean _notEquals = ObjectExtensions.operator_notEquals(boundsInDiagram, null);
      if (_notEquals) {
        Point2D _xblockexpression_1 = null;
        {
          double _width = boundsInDiagram.getWidth();
          Bounds _layoutBounds_1 = this.button.getLayoutBounds();
          double _width_1 = _layoutBounds_1.getWidth();
          double _multiply = (2 * _width_1);
          final double totalWidth = (_width + _multiply);
          double _height = boundsInDiagram.getHeight();
          Bounds _layoutBounds_2 = this.button.getLayoutBounds();
          double _height_1 = _layoutBounds_2.getHeight();
          double _multiply_1 = (2 * _height_1);
          final double totalHeight = (_height + _multiply_1);
          double _minX = boundsInDiagram.getMinX();
          Bounds _layoutBounds_3 = this.button.getLayoutBounds();
          double _width_2 = _layoutBounds_3.getWidth();
          double _multiply_2 = (1.5 * _width_2);
          double _minus = (_minX - _multiply_2);
          double _multiply_3 = (this.xPos * totalWidth);
          double _plus = (_minus + _multiply_3);
          double _minY = boundsInDiagram.getMinY();
          Bounds _layoutBounds_4 = this.button.getLayoutBounds();
          double _height_2 = _layoutBounds_4.getHeight();
          double _multiply_4 = (1.5 * _height_2);
          double _minus_1 = (_minY - _multiply_4);
          double _multiply_5 = (this.yPos * totalHeight);
          double _plus_1 = (_minus_1 + _multiply_5);
          Point2D _point2D = new Point2D(_plus, _plus_1);
          final Point2D position = _point2D;
          _xblockexpression_1 = (position);
        }
        _xifexpression = _xblockexpression_1;
      } else {
        _xifexpression = null;
      }
      _xblockexpression = (_xifexpression);
    }
    return _xblockexpression;
  }
  
  public double getXPos() {
    return this.xPos;
  }
  
  public double getYPos() {
    return this.yPos;
  }
}