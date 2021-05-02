import com.kgl.glfw.*
import kotlinx.coroutines.*

const val stageWidth = 10
const val stageHeight = 20

val virtualHeight
	get() = stageHeight + 3 + stageHeight + 1

object Game {
	val stage = MutableList(virtualHeight) { Array(stageWidth) { Color.Black } }
	var currentPiece = Tetromino.random()

	fun onKeyDown(key: KeyboardKey) {
		when (key) {
			KeyboardKey.LEFT -> {
				tryMove(MoveDirection.Left)
				Das.begin(DasDirection.Left)
			}
			KeyboardKey.RIGHT -> {
				tryMove(MoveDirection.Right)
				Das.begin(DasDirection.Right)
			}
			KeyboardKey.DOWN -> {
				tryMove(MoveDirection.Down, true)
			}
			KeyboardKey.SPACE -> {
				tryMove(MoveDirection.Down, true)
				lockPiece()
			}
			KeyboardKey.A -> tryRotate(Rotation.CounterClockwise)
			KeyboardKey.D -> tryRotate(Rotation.Clockwise)
			KeyboardKey.LEFT_SHIFT -> tryRotate(Rotation.OneEighty)
			else -> {}
		}
	}

	fun onKeyUp(key: KeyboardKey) {
		val directionToCancel = when (key) {
			KeyboardKey.LEFT -> DasDirection.Left
			KeyboardKey.RIGHT -> DasDirection.Right
			else -> return
		}
		if (Das.direction == directionToCancel) Das.cancel()
	}

	private fun tryMove(direction: MoveDirection, repeat: Boolean = false) {
		val xOffset = when (direction) {
			MoveDirection.Left -> -1
			MoveDirection.Right -> 1
			MoveDirection.Down -> 0
		}
		val yOffset = if (direction == MoveDirection.Down) -1 else 0

		var succeededOnce = false
		do {
			val new = currentPiece.run { copy(x = x + xOffset, y = y + yOffset) }
			if (new.fits) {
				currentPiece = new
				succeededOnce = true
			}
		} while (repeat && new.fits)

		if (succeededOnce) Engine.drawFrame()
	}

	private fun tryRotate(rotation: Rotation) {
		val orientations = Tetromino.Orientation.values()
		val index = orientations.indexOf(currentPiece.orientation)
		val offset = when (rotation) {
			Rotation.Clockwise -> 1
			Rotation.CounterClockwise -> 3
			Rotation.OneEighty -> 2
		}

		val newOrientation = orientations[(index + offset) % 4]

		val (xOffset, yOffset) = when (newOrientation) {
			Tetromino.Orientation.North -> 0 to 0
			Tetromino.Orientation.East -> 0 to 0
			Tetromino.Orientation.South -> 0 to 0
			Tetromino.Orientation.West -> 0 to 0
		}

		val new = currentPiece.copy(orientation = newOrientation)
		if (new.fits) {
			currentPiece = new
			Engine.drawFrame()
		}
	}

	private val Tetromino.fits
		get() = blocks.all {
			val blockX = x + it.first
			val blockY = y + it.second
			blockX in stage.first().indices && blockY >= 0 && !(blockY in stage.indices && stage[blockY][blockX].isNotBlack())
		}

	private fun lockPiece() {
		with(currentPiece) {
			blocks.forEach {
				stage[y + it.second][x + it.first] = type.color
			}
		}
		currentPiece = Tetromino.random()

		stage.removeAll { it.all { it.isNotBlack() } }
		repeat(virtualHeight - stage.size) {
			stage.add(Array(stageWidth) { Color.Black })
		}

		Engine.drawFrame()
	}

	object Das {
		private const val delay = 80L

		private var nextTime: Long? = null
		var direction: DasDirection? = null
		private var job: Job? = null

		fun begin(direction: DasDirection) {
			nextTime = System.currentTimeMillis() + delay
			this.direction = direction
			job = GlobalScope.launch {
				delay(delay)
				Glfw.postEmptyEvent()
			}
		}

		fun cancel() {
			nextTime = null
			direction = null
			job?.cancel()
		}

		fun execute() {
			(nextTime ?: return).let { if (System.currentTimeMillis() < it) return }
			when (direction) {
				DasDirection.Left -> tryMove(MoveDirection.Left, true)
				DasDirection.Right -> tryMove(MoveDirection.Right, true)
				null -> return
			}
		}
	}
}



enum class DasDirection {
	Left,
	Right
}

enum class MoveDirection {
	Left,
	Right,
	Down
}

enum class Rotation {
	Clockwise,
	CounterClockwise,
	OneEighty
}