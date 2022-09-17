package simulator

import domain._

object Verificators{

  extension(comp:List[Competitor]){
    def checkifSomeoneStillResistance(): Boolean =
      comp.find(competitor => 
          competitor.resistance >= SHOOT_RESISTANCE_COST
      ).isDefined
  }
}
