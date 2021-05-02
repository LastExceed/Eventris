import kotlin.random.*

class Tetromino(
	val type: Type,
	val orientation: Orientation,
	val x: Int,
	val y: Int
) {
	val blocks = type.defaultBlocks.map {
		val offset = type.size - 1
		when (orientation) {
			Orientation.North -> it
			Orientation.East -> Pair(it.second, -it.first + offset)
			Orientation.South -> Pair(-it.first + offset, -it.second + offset)
			Orientation.West -> Pair(-it.second + offset, it.first)
		}
	}.toSet()

	fun copy(
		orientation: Orientation = this.orientation,
		x: Int = this.x,
		y: Int = this.y
	) = Tetromino(type, orientation, x, y)

	companion object {
		fun random() = Tetromino(
			Type.values().run { get(Random.nextInt(size)) },
			Orientation.North,
			stageWidth / 2 - 2,
			stageHeight + 1
		)
	}

	enum class Type(
		val defaultBlocks: Set<Pair<Int, Int>>,
		val size: Int,
		val color: Color
	) {
		I(setOf(0 to 2, 1 to 2, 2 to 2, 3 to 2), 4, Color.Cyan),
		J(setOf(0 to 2, 0 to 1, 1 to 1, 2 to 1), 3, Color.Blue),
		L(setOf(0 to 1, 1 to 1, 2 to 1, 2 to 2), 3, Color.Orange),
		O(setOf(1 to 1, 1 to 2, 2 to 1, 2 to 2), 4, Color.Yellow),
		S(setOf(0 to 1, 1 to 1, 1 to 2, 2 to 2), 3, Color.Green),
		T(setOf(0 to 1, 1 to 1, 2 to 1, 1 to 2), 3, Color.Purple),
		Z(setOf(0 to 2, 1 to 2, 1 to 1, 2 to 1), 3, Color.Red),
	}

	enum class Orientation {
		North,
		East,
		South,
		West
	}
}

enum class Color(val r: Float, val g: Float, val b: Float) {
	Cyan(0f, 1f, 1f),
	Blue(0f, 0f, 1f),
	Orange(1f, 0.5f, 0f),
	Yellow(1f, 1f, 0f),
	Green(0f, 1f, 0f),
	Purple(0.5f, 0f, 1f),
	Red(1f, 0f, 0f),

	White(1f, 1f, 1f),
	Gray(0.5f, 0.5f, 0.5f),
	Black(0f, 0f, 0f);

	fun isNotBlack() = this != Black
}