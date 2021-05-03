import Tetromino.*

abstract class RotationSystem {
	abstract fun getKicktable(type: Type, from: Orientation, to : Orientation): List<Pair<Int, Int>>
}

object NoKicks : RotationSystem() {
	override fun getKicktable(type: Type, from: Orientation, to : Orientation): List<Pair<Int, Int>> {
		return listOf(+0 to +0)
	}
}

object SRS : RotationSystem() {
	override fun getKicktable(type: Type, from: Orientation, to: Orientation): List<Pair<Int, Int>> {
		return when (type) {
			Type.I -> when (from) {
				Orientation.North -> when (to) {
					Orientation.North -> error("invalid rotation")
					Orientation.East  -> listOf(+0 to +0, -2 to +0, +1 to +0, -2 to -1, +1 to +2)
					Orientation.South -> listOf(+0 to +0)
					Orientation.West  -> listOf(+0 to +0, -1 to +0, +2 to +0, -1 to +2, +2 to -1)
				}
				Orientation.East -> when (to) {
					Orientation.North -> listOf(+0 to +0, +2 to +0, -1 to +0, +2 to +1, -1 to -2)
					Orientation.East  -> error("invalid rotation")
					Orientation.South -> listOf(+0 to +0, -1 to +0, +2 to +0, -1 to +2, +2 to -1)
					Orientation.West  -> listOf(+0 to +0)
				}
				Orientation.South -> when (to) {
					Orientation.North -> listOf(+0 to +0)
					Orientation.East  -> listOf(+0 to +0, +1 to +0, -2 to +0, +1 to -2, -2 to +1)
					Orientation.South -> error("invalid rotation")
					Orientation.West  -> listOf(+0 to +0, +2 to +0, -1 to +0, +2 to +1, -1 to -2)
				}
				Orientation.West -> when (to) {
					Orientation.North -> listOf(+0 to +0, +1 to +0, -2 to +0, +1 to -2, -2 to +1)
					Orientation.East  -> listOf(+0 to +0)
					Orientation.South -> listOf(+0 to +0, -2 to +0, +1 to +0, -2 to -1, +1 to +2)
					Orientation.West  -> error("invalid rotation")
				}
			}
			else -> when (from) {
				Orientation.North -> when (to) {
					Orientation.North -> error("invalid rotation")
					Orientation.East  -> listOf(+0 to +0, -1 to +0, -1 to +1, +0 to -2, -1 to -2)
					Orientation.South -> listOf(+0 to +0)
					Orientation.West  -> listOf(+0 to +0, +1 to +0, +1 to +1, +0 to -2, +1 to -2)
				}
				Orientation.East -> when (to) {
					Orientation.North -> listOf(+0 to +0, +1 to +0, +1 to -1, +0 to +2, +1 to +2)
					Orientation.East  -> error("invalid rotation")
					Orientation.South -> listOf(+0 to +0, +1 to +0, +1 to -1, +0 to +2, +1 to +2)
					Orientation.West  -> listOf(+0 to +0)
				}
				Orientation.South -> when (to) {
					Orientation.North -> listOf(+0 to +0)
					Orientation.East  -> listOf(+0 to +0, -1 to +0, -1 to +1, +0 to -2, -1 to -2)
					Orientation.South -> error("invalid rotation")
					Orientation.West  -> listOf(+0 to +0, +1 to +0, +1 to +1, +0 to -2, +1 to -2)
				}
				Orientation.West -> when (to) {
					Orientation.North -> listOf(+0 to +0, -1 to +0, -1 to -1, +0 to +2, -1 to +2)
					Orientation.East  -> listOf(+0 to +0)
					Orientation.South -> listOf(+0 to +0, -1 to +0, -1 to -1, +0 to +2, -1 to +2)
					Orientation.West  -> error("invalid rotation")
				}
			}
		}
	}

}