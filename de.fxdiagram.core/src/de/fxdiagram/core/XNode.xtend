package de.fxdiagram.core

import de.fxdiagram.annotations.logging.Logging
import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.Lazy
import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.annotations.properties.ReadOnly
import de.fxdiagram.core.anchors.Anchors
import de.fxdiagram.core.anchors.RectangleAnchors
import de.fxdiagram.core.behavior.MoveBehavior
import de.fxdiagram.core.model.DomainObjectHandle
import de.fxdiagram.core.model.StringHandle
import javafx.collections.ObservableList
import javafx.scene.effect.DropShadow
import javafx.scene.effect.Effect
import javafx.scene.effect.InnerShadow

import static javafx.collections.FXCollections.*

import static extension de.fxdiagram.core.extensions.BoundsExtensions.*

@Logging
@ModelNode(#['layoutX', 'layoutY', 'domainObject', 'width', 'height'])
class XNode extends XShape {

	@FxProperty @Lazy double width
	@FxProperty @Lazy double height
	@FxProperty @ReadOnly DomainObjectHandle domainObject
	@FxProperty ObservableList<XConnection> incomingConnections = observableArrayList
	@FxProperty ObservableList<XConnection> outgoingConnections = observableArrayList
	
	Effect mouseOverEffect
	Effect selectionEffect
	Effect originalEffect

	Anchors anchors
	
 	new(DomainObjectHandle domainObject) {
 		domainObjectProperty.set(domainObject)
 	}
 	
 	new(String name) {
 		this(new StringHandle(name))	
 	}
 	
 	def getKey() {
		val key = domainObject?.key
 		if(key == null)
 			LOG.severe("XNodes key is null")
 		key
 	}
 		
	protected def createMouseOverEffect() {
		new InnerShadow
	}

	protected def createSelectionEffect() {
		new DropShadow() => [
			offsetX = 4.0
			offsetY = 4.0
		]
	}

 	override doActivatePreview() {
		mouseOverEffect = createMouseOverEffect
		selectionEffect = createSelectionEffect
		anchors = createAnchors
 	}
 	
	protected def Anchors createAnchors() {
		new RectangleAnchors(this)
	}

	override doActivate() {
		addBehavior(new MoveBehavior(this))
		onMouseEntered = [
			originalEffect = node.effect
			node.effect = mouseOverEffect ?: originalEffect
		]
		onMouseExited = [
			node.effect = originalEffect
		]
		if(node instanceof XActivatable)
			(node as XActivatable).activate
	}
	
	override selectionFeedback(boolean isSelected) {
		if (isSelected) {
			effect = selectionEffect
			scaleX = 1.05
			scaleY = 1.05
			(outgoingConnections + incomingConnections).forEach[toFront]
		} else {
			effect = null
			scaleX = 1.0
			scaleY = 1.0
		}
	}
	
	override getSnapBounds() {
		node.boundsInParent.scale(1 / scaleX, 1 / scaleY)
	}

	def getAnchors() {
		anchors
	}

	override minWidth(double height) {
		if (widthProperty != null)
			widthProperty.get
		else
			super.minWidth(height)
	}

	override minHeight(double width) {
		if (heightProperty != null)
			heightProperty.get
		else
			super.minHeight(width)
	}

	override prefWidth(double height) {
		if (widthProperty != null)
			widthProperty.get
		else
			super.prefWidth(height)
	}

	override prefHeight(double width) {
		if (heightProperty != null)
			heightProperty.get
		else
			super.prefHeight(width)
	}

	override maxWidth(double height) {
		if (widthProperty != null)
			widthProperty.get
		else
			super.maxWidth(height)
	}

	override maxHeight(double width) {
		if (heightProperty != null)
			heightProperty.get
		else
			super.maxHeight(width)
	}
	
	override toString() {
		class.name + " (" + domainObject?.key + ")" 
	}
	
}
