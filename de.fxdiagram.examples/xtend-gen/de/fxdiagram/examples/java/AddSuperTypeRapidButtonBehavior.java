package de.fxdiagram.examples.java;

import com.google.common.collect.Lists;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRapidButton;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.anchors.TriangleArrowHead;
import de.fxdiagram.core.extensions.ButtonExtensions;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.model.DomainObjectHandle;
import de.fxdiagram.core.tools.AbstractChooser;
import de.fxdiagram.core.tools.ChooserConnectionProvider;
import de.fxdiagram.examples.java.JavaModelProvider;
import de.fxdiagram.examples.java.JavaSuperType;
import de.fxdiagram.examples.java.JavaSuperTypeHandle;
import de.fxdiagram.examples.java.JavaTypeHandle;
import de.fxdiagram.examples.java.JavaTypeModel;
import de.fxdiagram.examples.java.JavaTypeNode;
import de.fxdiagram.lib.model.AbstractConnectionRapidButtonBehavior;
import de.fxdiagram.lib.tools.CoverFlowChooser;
import java.util.Collections;
import java.util.Set;
import javafx.beans.property.ObjectProperty;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.paint.Paint;
import javafx.scene.shape.SVGPath;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class AddSuperTypeRapidButtonBehavior extends AbstractConnectionRapidButtonBehavior<JavaTypeNode,Class<?>,JavaSuperTypeHandle> {
  public AddSuperTypeRapidButtonBehavior(final JavaTypeNode host) {
    super(host);
  }
  
  protected Iterable<Class<?>> getInitialModelChoices() {
    JavaTypeNode _host = this.getHost();
    JavaTypeModel _javaTypeModel = _host.getJavaTypeModel();
    return _javaTypeModel.getSuperTypes();
  }
  
  protected JavaSuperTypeHandle getChoiceKey(final Class<?> superType) {
    JavaModelProvider _domainObjectProvider = this.getDomainObjectProvider();
    JavaTypeNode _host = this.getHost();
    Class<?> _javaType = _host.getJavaType();
    JavaSuperType _javaSuperType = new JavaSuperType(_javaType, superType);
    return _domainObjectProvider.createJavaSuperClassHandle(_javaSuperType);
  }
  
  protected XNode createNode(final JavaSuperTypeHandle key) {
    JavaModelProvider _domainObjectProvider = this.getDomainObjectProvider();
    JavaSuperType _domainObject = key.getDomainObject();
    Class<?> _superType = _domainObject.getSuperType();
    JavaTypeHandle _createJavaTypeHandle = _domainObjectProvider.createJavaTypeHandle(_superType);
    return new JavaTypeNode(_createJavaTypeHandle);
  }
  
  protected JavaModelProvider getDomainObjectProvider() {
    JavaTypeNode _host = this.getHost();
    XRoot _root = CoreExtensions.getRoot(_host);
    return _root.<JavaModelProvider>getDomainObjectProvider(JavaModelProvider.class);
  }
  
  protected AbstractChooser createChooser(final XRapidButton button, final Set<JavaSuperTypeHandle> availableChoiceKeys, final Set<JavaSuperTypeHandle> unavailableChoiceKeys) {
    CoverFlowChooser _xblockexpression = null;
    {
      JavaTypeNode _host = this.getHost();
      Pos _chooserPosition = button.getChooserPosition();
      final CoverFlowChooser chooser = new CoverFlowChooser(_host, _chooserPosition);
      final Procedure1<JavaSuperTypeHandle> _function = new Procedure1<JavaSuperTypeHandle>() {
        public void apply(final JavaSuperTypeHandle it) {
          XNode _createNode = AddSuperTypeRapidButtonBehavior.this.createNode(it);
          chooser.addChoice(_createNode, it);
        }
      };
      IterableExtensions.<JavaSuperTypeHandle>forEach(availableChoiceKeys, _function);
      final ChooserConnectionProvider _function_1 = new ChooserConnectionProvider() {
        public XConnection getConnection(final XNode host, final XNode choice, final DomainObjectHandle key) {
          XConnection _xConnection = new XConnection(host, choice, key);
          final Procedure1<XConnection> _function = new Procedure1<XConnection>() {
            public void apply(final XConnection it) {
              ObjectProperty<Paint> _strokeProperty = it.strokeProperty();
              XDiagram _diagram = CoreExtensions.getDiagram(host);
              ObjectProperty<Paint> _backgroundPaintProperty = _diagram.backgroundPaintProperty();
              TriangleArrowHead _triangleArrowHead = new TriangleArrowHead(it, 10, 15, _strokeProperty, _backgroundPaintProperty, false);
              it.setTargetArrowHead(_triangleArrowHead);
            }
          };
          return ObjectExtensions.<XConnection>operator_doubleArrow(_xConnection, _function);
        }
      };
      chooser.setConnectionProvider(_function_1);
      _xblockexpression = chooser;
    }
    return _xblockexpression;
  }
  
  protected Iterable<XRapidButton> createButtons(final Procedure1<? super XRapidButton> addConnectionAction) {
    JavaTypeNode _host = this.getHost();
    SVGPath _triangleButton = ButtonExtensions.getTriangleButton(Side.TOP, "Discover supertypes");
    XRapidButton _xRapidButton = new XRapidButton(_host, 0.5, 0, _triangleButton, addConnectionAction);
    JavaTypeNode _host_1 = this.getHost();
    SVGPath _triangleButton_1 = ButtonExtensions.getTriangleButton(Side.BOTTOM, "Discover supertypes");
    XRapidButton _xRapidButton_1 = new XRapidButton(_host_1, 0.5, 1, _triangleButton_1, addConnectionAction);
    return Collections.<XRapidButton>unmodifiableList(Lists.<XRapidButton>newArrayList(_xRapidButton, _xRapidButton_1));
  }
}
