package de.fxdiagram.eclipse.xtext;

import com.google.common.base.Objects;
import com.google.inject.Injector;
import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.eclipse.xtext.XtextDomainObjectProvider;
import de.fxdiagram.mapping.AbstractMapping;
import de.fxdiagram.mapping.IMappedElementDescriptor;
import de.fxdiagram.mapping.XDiagramConfig;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import org.eclipse.xtext.resource.IResourceServiceProvider;

@ModelNode({ "provider", "mappingConfigID", "mappingID" })
@SuppressWarnings("all")
public abstract class AbstractXtextDescriptor<ECLASS_OR_ESETTING extends Object> implements IMappedElementDescriptor<ECLASS_OR_ESETTING>, DomainObjectDescriptor {
  private AbstractMapping<ECLASS_OR_ESETTING> mapping;
  
  public AbstractXtextDescriptor(final String mappingConfigID, final String mappingID, final XtextDomainObjectProvider provider) {
    this.providerProperty.set(provider);
    this.mappingConfigIDProperty.set(mappingConfigID);
    this.mappingIDProperty.set(mappingID);
  }
  
  @Override
  public AbstractMapping<ECLASS_OR_ESETTING> getMapping() {
    AbstractMapping<ECLASS_OR_ESETTING> _xblockexpression = null;
    {
      boolean _equals = Objects.equal(this.mapping, null);
      if (_equals) {
        XDiagramConfig.Registry _instance = XDiagramConfig.Registry.getInstance();
        String _mappingConfigID = this.getMappingConfigID();
        final XDiagramConfig config = _instance.getConfigByID(_mappingConfigID);
        String _mappingID = this.getMappingID();
        AbstractMapping<?> _mappingByID = config.getMappingByID(_mappingID);
        this.mapping = ((AbstractMapping<ECLASS_OR_ESETTING>) _mappingByID);
      }
      _xblockexpression = this.mapping;
    }
    return _xblockexpression;
  }
  
  public void injectMembers(final Object object) {
    IResourceServiceProvider _resourceServiceProvider = this.getResourceServiceProvider();
    Injector _get = _resourceServiceProvider.<Injector>get(Injector.class);
    _get.injectMembers(object);
  }
  
  protected abstract IResourceServiceProvider getResourceServiceProvider();
  
  @Override
  public boolean equals(final Object obj) {
    if ((obj instanceof AbstractXtextDescriptor<?>)) {
      boolean _and = false;
      boolean _and_1 = false;
      XtextDomainObjectProvider _provider = this.getProvider();
      XtextDomainObjectProvider _provider_1 = ((AbstractXtextDescriptor<?>)obj).getProvider();
      boolean _equals = Objects.equal(_provider, _provider_1);
      if (!_equals) {
        _and_1 = false;
      } else {
        String _mappingConfigID = this.getMappingConfigID();
        String _mappingConfigID_1 = ((AbstractXtextDescriptor<?>)obj).getMappingConfigID();
        boolean _equals_1 = Objects.equal(_mappingConfigID, _mappingConfigID_1);
        _and_1 = _equals_1;
      }
      if (!_and_1) {
        _and = false;
      } else {
        String _mappingID = this.getMappingID();
        String _mappingID_1 = ((AbstractXtextDescriptor<?>)obj).getMappingID();
        boolean _equals_2 = Objects.equal(_mappingID, _mappingID_1);
        _and = _equals_2;
      }
      return _and;
    } else {
      return false;
    }
  }
  
  @Override
  public int hashCode() {
    String _mappingConfigID = this.getMappingConfigID();
    int _hashCode = _mappingConfigID.hashCode();
    int _multiply = (31 * _hashCode);
    String _mappingID = this.getMappingID();
    int _hashCode_1 = _mappingID.hashCode();
    int _multiply_1 = (37 * _hashCode_1);
    int _plus = (_multiply + _multiply_1);
    XtextDomainObjectProvider _provider = this.getProvider();
    int _hashCode_2 = _provider.hashCode();
    int _multiply_2 = (79 * _hashCode_2);
    return (_plus + _multiply_2);
  }
  
  /**
   * Automatically generated by @ModelNode. Needed for deserialization.
   */
  public AbstractXtextDescriptor() {
  }
  
  public void populate(final ModelElementImpl modelElement) {
    modelElement.addProperty(providerProperty, XtextDomainObjectProvider.class);
    modelElement.addProperty(mappingConfigIDProperty, String.class);
    modelElement.addProperty(mappingIDProperty, String.class);
  }
  
  private ReadOnlyObjectWrapper<XtextDomainObjectProvider> providerProperty = new ReadOnlyObjectWrapper<XtextDomainObjectProvider>(this, "provider");
  
  public XtextDomainObjectProvider getProvider() {
    return this.providerProperty.get();
  }
  
  public ReadOnlyObjectProperty<XtextDomainObjectProvider> providerProperty() {
    return this.providerProperty.getReadOnlyProperty();
  }
  
  private ReadOnlyStringWrapper mappingConfigIDProperty = new ReadOnlyStringWrapper(this, "mappingConfigID");
  
  public String getMappingConfigID() {
    return this.mappingConfigIDProperty.get();
  }
  
  public ReadOnlyStringProperty mappingConfigIDProperty() {
    return this.mappingConfigIDProperty.getReadOnlyProperty();
  }
  
  private ReadOnlyStringWrapper mappingIDProperty = new ReadOnlyStringWrapper(this, "mappingID");
  
  public String getMappingID() {
    return this.mappingIDProperty.get();
  }
  
  public ReadOnlyStringProperty mappingIDProperty() {
    return this.mappingIDProperty.getReadOnlyProperty();
  }
}
