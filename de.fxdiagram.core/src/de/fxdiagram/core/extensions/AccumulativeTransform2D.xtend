package de.fxdiagram.core.extensions

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.ReadOnly
import javafx.geometry.Point2D
import javafx.scene.transform.Affine
import javafx.scene.transform.Transform

import static de.fxdiagram.core.extensions.NumberExpressionExtensions.*

import static extension java.lang.Math.*

/**
 * Container for a {@link Transform} that allows basic 2D transformations.
 * 
 * As opposed to a plain {@link Affine} and the internal transform of a {@link Node}, this class 
 * allows accumulative (relative) changes, e.g. rotate another x degrees.
 *
 * As opposed to the {@link TransformExtensions} this class exposes scale, rotation and translation 
 * as JavaFX {@link Property Properties}, such that clients can e.g. react on changes of the scale. 
 */
class AccumulativeTransform2D {

	public static val MIN_SCALE = EPSILON

	@FxProperty @ReadOnly double scale = 1
	@FxProperty @ReadOnly double rotate = 0
	@FxProperty @ReadOnly double translateX
	@FxProperty @ReadOnly double translateY

	Affine transform

	new() {
		transform = new Affine
		translateXProperty.bind(transform.txProperty)
		translateYProperty.bind(transform.tyProperty)
	}

	def translateRelative(double deltaX, double deltaY) {
		transform.tx = transform.tx + deltaX
		transform.ty = transform.ty + deltaY
	}

	def setTranslate(double newX, double newY) {
		transform.tx = newX
		transform.ty = newY
	}

	def setTranslate(Point2D newPoint) {
		transform.tx = newPoint.x
		transform.ty = newPoint.y
	}

	def setTranslateX(double newX) {
		transform.tx = newX
	}

	def setTranslateY(double newY) {
		transform.ty = newY
	}

	def scaleRelative(double deltaScale) {
		val safeNewScale = max(MIN_SCALE, scale * deltaScale)
		val safeDeltaScale = safeNewScale / scale
		applyRotationAndScale(safeNewScale, rotate)
		transform.tx = safeDeltaScale * transform.tx
		transform.ty = safeDeltaScale * transform.ty
		scaleProperty.set(safeNewScale)
		return safeDeltaScale
	}

	def setScale(double newScale) {
		scaleProperty.set(max(MIN_SCALE, newScale))
		applyRotationAndScale(scale, rotate)
		return scaleProperty.get()
	}

	def rotateRelative(double deltaAngle, double pivotX, double pivotY) {
		val newAngle = rotate + deltaAngle
		applyRotationAndScale(scale, newAngle)
		val radians = deltaAngle.toRadians
		val cos = cos(radians)
		val sin = sin(radians)
		transform.tx = cos * transform.tx + sin * transform.ty + pivotX - cos * pivotX - sin * pivotY
		transform.ty = -sin * transform.tx + cos * transform.ty + pivotY + sin * pivotX - cos * pivotY
		rotateProperty.set(newAngle)
	}

	def rotateRelative(double deltaAngle) {
		val newAngle = rotate + deltaAngle
		applyRotationAndScale(scale, newAngle)
		val radians = deltaAngle.toRadians
		val cos = cos(radians)
		val sin = sin(radians)
		transform.tx = cos * transform.tx + sin * transform.ty
		transform.ty = -sin * transform.tx + cos * transform.ty
		rotateProperty.set(newAngle)
	}

	def setRotate(double newAngle) {
		rotateProperty.set(newAngle)
		applyRotationAndScale(scale, rotate)
	}

	protected def applyRotationAndScale(double newScale, double newAngle) {
		val radians = newAngle.toRadians
		val cos = cos(radians)
		val sin = sin(radians)
		transform.mxx = newScale * cos
		transform.mxy = newScale * sin
		transform.myx = newScale * -sin
		transform.myy = newScale * cos
	}

	def Transform getTransform() {
		transform
	}
}
