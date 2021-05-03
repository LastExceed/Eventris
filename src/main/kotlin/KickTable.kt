import Tetromino.*

interface RotationSystem {
	fun getKicktable(type: Type, from: Orientation, rotation: Rotation): List<Pair<Int, Int>>
}

object NoKicks : RotationSystem {
	override fun getKicktable(type: Type, from: Orientation, rotation: Rotation): List<Pair<Int, Int>> {
		return listOf(+0 to +0)
	}
}

object SRS : RotationSystem {
	override fun getKicktable(type: Type, from: Orientation, rotation: Rotation): List<Pair<Int, Int>> {
		return when (type) {
			Type.I -> when (from) {
				Orientation.North -> when (rotation) {
					Rotation.Clockwise -> listOf(+0 to +0, -2 to +0, +1 to +0, -2 to -1, +1 to +2)
					Rotation.CounterClockwise -> listOf(+0 to +0, -1 to +0, +2 to +0, -1 to +2, +2 to -1)
					Rotation.OneEighty -> listOf()
				}
				Orientation.East -> when (rotation) {
					Rotation.Clockwise -> listOf(+0 to +0, -1 to +0, +2 to +0, -1 to +2, +2 to -1)
					Rotation.CounterClockwise -> listOf(+0 to +0, +2 to +0, -1 to +0, +2 to +1, -1 to -2)
					Rotation.OneEighty -> listOf()
				}
				Orientation.South -> when (rotation) {
					Rotation.Clockwise -> listOf(+0 to +0, +2 to +0, -1 to +0, +2 to +1, -1 to -2)
					Rotation.CounterClockwise -> listOf(+0 to +0, +1 to +0, -2 to +0, +1 to -2, -2 to +1)
					Rotation.OneEighty -> listOf()
				}
				Orientation.West -> when (rotation) {
					Rotation.Clockwise -> listOf(+0 to +0, +1 to +0, -2 to +0, +1 to -2, -2 to +1)
					Rotation.CounterClockwise -> listOf(+0 to +0, -2 to +0, +1 to +0, -2 to -1, +1 to +2)
					Rotation.OneEighty -> listOf()
				}
			}
			else -> when (from) {
				Orientation.North -> when (rotation) {
					Rotation.Clockwise -> listOf(+0 to +0, -1 to +0, -1 to +1, +0 to -2, -1 to -2)
					Rotation.CounterClockwise -> listOf(+0 to +0, +1 to +0, +1 to +1, +0 to -2, +1 to -2)
					Rotation.OneEighty -> listOf()
				}
				Orientation.East -> when (rotation) {
					Rotation.Clockwise -> listOf(+0 to +0, +1 to +0, +1 to -1, +0 to +2, +1 to +2)
					Rotation.CounterClockwise -> listOf(+0 to +0, +1 to +0, +1 to -1, +0 to +2, +1 to +2)
					Rotation.OneEighty -> listOf()
				}
				Orientation.South -> when (rotation) {
					Rotation.Clockwise -> listOf(+0 to +0, +1 to +0, +1 to +1, +0 to -2, +1 to -2)
					Rotation.CounterClockwise -> listOf(+0 to +0, -1 to +0, -1 to +1, +0 to -2, -1 to -2)
					Rotation.OneEighty -> listOf()
				}
				Orientation.West -> when (rotation) {
					Rotation.Clockwise -> listOf(+0 to +0, -1 to +0, -1 to -1, +0 to +2, -1 to +2)
					Rotation.CounterClockwise -> listOf(+0 to +0, -1 to +0, -1 to -1, +0 to +2, -1 to +2)
					Rotation.OneEighty -> listOf()
				}
			}
		}
	}
}

object SRSjstris : RotationSystem {
	override fun getKicktable(type: Type, from: Orientation, rotation: Rotation): List<Pair<Int, Int>> {
		return if (rotation == Rotation.OneEighty) {
			when (from) {
				Orientation.North -> listOf(0 to 0, 0 to 1)
				Orientation.East -> listOf(0 to 0, 1 to 0)
				Orientation.South -> listOf(0 to 0, 0 to -1)
				Orientation.West -> listOf(0 to 0, -1 to 0)
			}
		} else SRS.getKicktable(type, from, rotation)
	}

}