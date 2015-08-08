package ru.biocad.game

trait LCG {
  def generate(seeds: Array[Int], length: Int): Array[Array[Int]] = {
    val modulus = math.pow(2, 31).toInt
    val multiplier = 1103515245
    val increment = 12345
    seeds.map { seed =>
      var gseed = seed
      Iterator.range(0, length).map { _ =>
        val cb = (gseed >> 16) & 0x7fff
        gseed = (multiplier * gseed + increment) % modulus
        cb
      }.toArray
    }
  }
}


object LCGTest extends App with LCG {

  val s = new Array[Int](1)
  s(0) = 17
  val asdf = generate(s, 10).flatten

  println(asdf.mkString(" "))

  assert(asdf sameElements Array(0, 24107, 16552, 12125, 9427, 13152, 21440, 3383, 6873, 16117))
}


trait BeeGenerator {
    def generate(bees: Array[Bee], seeds: Array[Int], length: Int): Array[Array[Bee]] = {
        val modulus = math.pow(2, 31).toInt
        val multiplier = 1103515245
        val increment = 12345
        val ulen = bees.length
        seeds.map { seed =>
            var gseed = seed
            Iterator.range(0, length).map { _ =>
                val cb = bees(((gseed >> 16) & 0x7fff) % ulen)
                gseed = (multiplier * gseed + increment) % modulus
                cb
            }.toArray
        }
    }
}

class AbsBeeGenerator(board : Board) extends BeeGenerator {
    override def generate(bees: Array[Bee], seeds: Array[Int], length: Int): Array[Array[Bee]] = {
      val beezArray = super.generate(bees, seeds, length)
      beezArray.map {
        case beez =>
          beez.map {
            case bee =>
              val leftOffset = math.floor((board.width - bee.width) / 2).toInt
              val members = bee.members.map(member => Cell(q = member.q + leftOffset, r = member.r))
              val pivot = bee.pivot.copy(q = bee.pivot.q + leftOffset)
              Bee(members, pivot)
          }
      }
    }
}