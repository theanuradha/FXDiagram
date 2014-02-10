package de.fxdiagram.core.model;

import com.google.common.base.Objects;
import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.model.DomainObjectHandle;
import de.fxdiagram.core.model.DomainObjectProvider;
import de.fxdiagram.core.model.ModelElement;
import de.fxdiagram.core.model.ModelLoad;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;

@ModelNode({ "id", "key", "provider" })
@SuppressWarnings("all")
public class DomainObjectHandleImpl implements DomainObjectHandle {
  private Object cachedDomainObject;
  
  public DomainObjectHandleImpl(final String id, final String key, final DomainObjectProvider provider) {
    this.idProperty.set(id);
    this.keyProperty.set(key);
    this.providerProperty.set(provider);
  }
  
  public Object getDomainObject() {
    boolean _equals = Objects.equal(this.cachedDomainObject, null);
    if (_equals) {
      DomainObjectProvider _provider = this.getProvider();
      Object _resolveDomainObject = _provider.resolveDomainObject(this);
      this.cachedDomainObject = _resolveDomainObject;
    }
    return this.cachedDomainObject;
  }
  
  public boolean equals(final Object obj) {
    boolean _and = false;
    boolean _and_1 = false;
    if (!(obj instanceof DomainObjectHandle)) {
      _and_1 = false;
    } else {
      Class<? extends DomainObjectHandleImpl> _class = this.getClass();
      Class<?> _class_1 = obj.getClass();
      boolean _equals = Objects.equal(_class, _class_1);
      _and_1 = _equals;
    }
    if (!_and_1) {
      _and = false;
    } else {
      String _id = this.getId();
      String _id_1 = ((DomainObjectHandle) obj).getId();
      boolean _equals_1 = Objects.equal(_id, _id_1);
      _and = _equals_1;
    }
    return _and;
  }
  
  /**
   * Automatically generated by @ModelNode. Used in model deserialization.
   */
  public DomainObjectHandleImpl(final ModelLoad modelLoad) {
  }
  
  public void populate(final ModelElement modelElement) {
    modelElement.addProperty(idProperty, String.class);
    modelElement.addProperty(keyProperty, String.class);
    modelElement.addProperty(providerProperty, DomainObjectProvider.class);
  }
  
  private ReadOnlyObjectWrapper<DomainObjectProvider> providerProperty = new ReadOnlyObjectWrapper<DomainObjectProvider>(this, "provider");
  
  public DomainObjectProvider getProvider() {
    return this.providerProperty.get();
  }
  
  public ReadOnlyObjectProperty<DomainObjectProvider> providerProperty() {
    return this.providerProperty.getReadOnlyProperty();
  }
  
  private ReadOnlyStringWrapper idProperty = new ReadOnlyStringWrapper(this, "id");
  
  public String getId() {
    return this.idProperty.get();
  }
  
  public ReadOnlyStringProperty idProperty() {
    return this.idProperty.getReadOnlyProperty();
  }
  
  private ReadOnlyStringWrapper keyProperty = new ReadOnlyStringWrapper(this, "key");
  
  public String getKey() {
    return this.keyProperty.get();
  }
  
  public ReadOnlyStringProperty keyProperty() {
    return this.keyProperty.getReadOnlyProperty();
  }
}
