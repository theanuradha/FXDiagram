package de.fxdiagram.mapping.shapes;

import de.fxdiagram.core.XConnectionLabel;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.mapping.AbstractMapping;
import de.fxdiagram.mapping.IMappedElementDescriptor;
import de.fxdiagram.mapping.reconcile.LabelReconcileBehavior;
import de.fxdiagram.mapping.shapes.BaseShapeInitializer;

@SuppressWarnings("all")
public class BaseConnectionLabel<T extends Object> extends XConnectionLabel {
  public BaseConnectionLabel() {
    BaseShapeInitializer.initializeLazily(this);
  }
  
  public BaseConnectionLabel(final IMappedElementDescriptor<T> descriptor) {
    super(descriptor);
  }
  
  @Override
  public IMappedElementDescriptor<T> getDomainObjectDescriptor() {
    DomainObjectDescriptor _domainObjectDescriptor = super.getDomainObjectDescriptor();
    return ((IMappedElementDescriptor<T>) _domainObjectDescriptor);
  }
  
  @Override
  public void doActivate() {
    super.doActivate();
    LabelReconcileBehavior _labelReconcileBehavior = new LabelReconcileBehavior(this);
    this.addBehavior(_labelReconcileBehavior);
  }
  
  @Override
  public String getType() {
    IMappedElementDescriptor<T> _domainObjectDescriptor = this.getDomainObjectDescriptor();
    AbstractMapping<T> _mapping = _domainObjectDescriptor.getMapping();
    return _mapping.getID();
  }
}
