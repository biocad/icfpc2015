package ru.biocad.game

object BeeGenerator {
    def generate(bees: Array[Bee], seeds: Array[Int], length: Int): Array[Array[Bee]] = {
        val modulus = math.pow(2, 31).toInt
        val multiplier = 1103515245
        val increment = 12345
        val ulen = bees.length
        seeds.map { seed =>
            var gseed = seed
            Iterator.range(0, length).map { _ =>
                val cb = bees((gseed >> 16) & 0x7fff % ulen)
                gseed = (multiplier * gseed + increment) % modulus
                cb
            }.toArray
        }
    }
}
