import kotlin.random.*

interface Randomizer {
	fun next(): Tetromino
}

class MemorylessRandomizer : Randomizer {
	override fun next() = Tetromino.spawn(Tetromino.Type.values().run { get(Random.nextInt(indices)) })
}

class BagRandomizer(private val bagCount: Int) : Randomizer {
	private val bag = mutableListOf<Tetromino>()

	override fun next(): Tetromino {
		if (bag.size == 0) {
			repeat(bagCount) {
				Tetromino.Type.values().forEach { bag.add(Tetromino.spawn(it)) }
			}
			bag.shuffle()
		}

		return bag.removeLast()
	}
}