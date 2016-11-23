package de.fxdiagram.core.behavior

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.core.XNode
import de.fxdiagram.core.XShape
import de.fxdiagram.core.command.AnimationCommand
import de.fxdiagram.core.command.MoveCommand
import javafx.geometry.Point2D
import javafx.scene.input.MouseEvent
import org.eclipse.xtend.lib.annotations.Data

import static extension de.fxdiagram.core.extensions.CoreExtensions.*

class MoveBehavior <T extends XShape> extends AbstractHostBehavior<T> {
	
	DragContext dragContext
	
	@FxProperty boolean manuallyPlaced
	
	new(T host) {
		super(host)
	}
	
	override doActivate() {
	}
	
	protected def hasMoved() {
		dragContext != null && (dragContext.initialX != host.layoutX || dragContext.initialY != host.layoutY)
	}
	
	protected def AnimationCommand createMoveCommand() {
		new MoveCommand(
			host,
			dragContext.initialX, dragContext.initialY,
			host.layoutX, host.layoutY
		)
	}
	
	override getBehaviorKey() {
		MoveBehavior
	}
	
	def void mousePressed(MouseEvent it) {
		startDrag(screenX, screenY)
	}
	
	def startDrag(double screenX, double screenY) {
		val initialPositionInScene = host.parent.localToScene(host.layoutX, host.layoutY)
		dragContext = new DragContext(
			host.layoutX,
			host.layoutY,
			screenX,
			screenY,
			initialPositionInScene
		)
		if(host instanceof XNode) {
			val node = host as XNode
			(node.incomingConnections + node.outgoingConnections).forEach[
				connectionRouter.splineShapeKeeperEnabled = true
			]
		}
	}
	
	def void mouseDragged(MouseEvent it) {
		val newPositionInScene = new Point2D(
			dragContext.initialPosInScene.x + screenX - dragContext.mouseAnchorX,
			dragContext.initialPosInScene.y + screenY - dragContext.mouseAnchorY)
		val newPositionInDiagram = host.parent.sceneToLocal(newPositionInScene)
		dragTo(newPositionInDiagram)
	}
	
	
	def void mouseReleased(MouseEvent it) {
		if(hasMoved) {
			val moveCommand = createMoveCommand
			if(moveCommand != null) {
				host.root.commandStack.execute(moveCommand)
				manuallyPlaced = true
			}
		}
	}

	protected def dragTo(Point2D newPositionInDiagram) {
		if(newPositionInDiagram != null) {
			host.layoutX = newPositionInDiagram.x
			host.layoutY = newPositionInDiagram.y			
		}
	}
	
	@Data 
	static class DragContext {
		double initialX
		double initialY
		double mouseAnchorX 
		double mouseAnchorY
		Point2D initialPosInScene
	}
}
