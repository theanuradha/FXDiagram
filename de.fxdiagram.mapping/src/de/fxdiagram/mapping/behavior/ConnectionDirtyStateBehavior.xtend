package de.fxdiagram.mapping.behavior

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.core.XConnection
import de.fxdiagram.core.behavior.AbstractDirtyStateBehavior
import de.fxdiagram.mapping.AbstractConnectionMappingCall
import de.fxdiagram.mapping.ConnectionMapping
import de.fxdiagram.mapping.IMappedElementDescriptor
import de.fxdiagram.mapping.NodeMapping
import de.fxdiagram.mapping.NodeMappingCall
import de.fxdiagram.mapping.XDiagramConfigInterpreter
import java.util.NoSuchElementException
import javafx.animation.Animation
import javafx.animation.KeyFrame
import javafx.animation.KeyValue
import javafx.animation.Timeline

import static de.fxdiagram.core.behavior.DirtyState.*

import static extension de.fxdiagram.core.extensions.DurationExtensions.*
import static extension de.fxdiagram.core.extensions.DoubleExpressionExtensions.*

class ConnectionDirtyStateBehavior<T> extends AbstractDirtyStateBehavior<XConnection> {

	Animation dirtyAnimation
	
	@FxProperty double dirtyAnimationValue
	
	double strokeWidth
	
	new(XConnection host) {
		super(host)
	}

	override getDirtyState() {
		val descriptor = host.domainObject
		if (descriptor instanceof IMappedElementDescriptor<?>) {
			try {
				return descriptor.withDomainObject [ domainObject |
					val connectionMapping = descriptor.mapping as ConnectionMapping<T>
					val sourceState = checkConnectionEnd(domainObject as T, connectionMapping, true)
					if(sourceState == CLEAN)
						return checkConnectionEnd(domainObject as T, connectionMapping, false)
					else 
						return sourceState
				]
			} catch (NoSuchElementException exc) {
				return DANGLING
			}
		} else {
			return CLEAN
		}
	}

	protected def <U> checkConnectionEnd(T domainObject, ConnectionMapping<T> connectionMapping, boolean isSource) {
		val interpreter = new XDiagramConfigInterpreter
		val node = if(isSource) host.source else host.target
		val nodeMappingCall = (if(isSource) connectionMapping.source else connectionMapping.target) as NodeMappingCall<U, T>
		if (nodeMappingCall != null) {
			val nodeObject = interpreter.select(nodeMappingCall, domainObject).head as U
			if (nodeObject == null)
				return DIRTY
			val resolvedNodeDescriptor = interpreter.getDescriptor(nodeObject, nodeMappingCall.mapping)
			if (resolvedNodeDescriptor != node.domainObject)
				return DIRTY
			else
				return CLEAN
		} else {
			val nodeDescriptor = node.domainObject
			if (nodeDescriptor instanceof IMappedElementDescriptor<?>) {
				val nodeMapping = nodeDescriptor.mapping
				if (nodeMapping instanceof NodeMapping<?>) {
					val nodeMappingCasted = nodeMapping as NodeMapping<U>
					val siblingMappingCalls = if(isSource) 
							nodeMappingCasted
								.outgoing
								.filter[ mapping == connectionMapping ]
								.map[it as AbstractConnectionMappingCall<T, U>]
						else
							nodeMappingCasted
								.incoming
								.filter[ mapping == connectionMapping ]
								.map[it as AbstractConnectionMappingCall<T, U>]
					return nodeDescriptor.withDomainObject [ nodeDomainObject |
						val nodeObjectCasted = nodeDomainObject as U
						for (siblingMappingCall : siblingMappingCalls) {
							if (interpreter.select(siblingMappingCall, nodeObjectCasted).exists[interpreter.getDescriptor(it, siblingMappingCall.mapping) == host.domainObject])
								return CLEAN
						}
						return DIRTY
					]
				}
			}
		}				
		return DIRTY
	}
	
	
	override protected doActivate() {
		dirtyAnimation = new Timeline => [
			keyFrames += new KeyFrame(0.millis, new KeyValue(dirtyAnimationValueProperty, 1))
			keyFrames += new KeyFrame(300.millis, new KeyValue(dirtyAnimationValueProperty, 0.96))
			keyFrames += new KeyFrame(900.millis, new KeyValue(dirtyAnimationValueProperty, 1.04))
			keyFrames += new KeyFrame(1200.millis, new KeyValue(dirtyAnimationValueProperty, 1))
			autoReverse = true
			cycleCount = Animation.INDEFINITE
		]
	}
	
	override protected dirtyFeedback(boolean isDirty) {
		if(isDirty) {
			strokeWidth = host.strokeWidth;
			(host.labels + #[host.sourceArrowHead?.node, host.targetArrowHead?.node])
				.filterNull
				.forEach[ 
					scaleXProperty.bind(dirtyAnimationValueProperty)
					scaleYProperty.bind(dirtyAnimationValueProperty)
				]
			host.strokeWidthProperty.bind((dirtyAnimationValueProperty - 1) * 40 + strokeWidth) 
			dirtyAnimation.play
		} else {
			(host.labels + #[host.sourceArrowHead?.node, host.targetArrowHead?.node])
				.filterNull
				.forEach[ 
					scaleXProperty.unbind
					scaleYProperty.unbind
					scaleX = 1
					scaleY = 1
				]	
			host.strokeWidthProperty.unbind
			host.strokeWidth = strokeWidth
			dirtyAnimation.stop
		}
	}
}