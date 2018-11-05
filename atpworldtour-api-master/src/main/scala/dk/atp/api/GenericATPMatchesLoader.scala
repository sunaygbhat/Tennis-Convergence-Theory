package dk.atp.api

import domain._
import dk.atp.api.tournament._
import com.typesafe.scalalogging.slf4j.Logger
import org.slf4j.LoggerFactory

class GenericATPMatchesLoader(tournamentApi: TournamentAtpApi, numOfThreads: Int = 16) extends ATPMatchesLoader {

  private val logger = Logger(LoggerFactory.getLogger(getClass()))

  /**Loads tennis matches from http://www.atpworldtour.com/ web site.*/
  def loadMatches(year: Int): List[TennisMatch] = {

    //not supported in Scala 2.10  collection.parallel.ForkJoinTasks.defaultForkJoinPool.setParallelism(numOfThreads)
    // read: http://stackoverflow.com/questions/5424496/scala-parallel-collections-degree-of-parallelism
    logger.info("Getting tournaments for year %d".format(year))
    val tournaments = tournamentApi.parseTournaments(year)
    val filteredTournaments = tournaments.filter(t => !t.tournamentUrl.isEmpty() && !t.tournamentUrl.endsWith("mds.pdf"))

    val matchesComposite = filteredTournaments.par.flatMap { tournament =>

      logger.info("Getting markets for tournament %s".format(tournament.tournamentName))
      val tennisMatches = tournamentApi.parseTournament(tournament.tournamentUrl)

      tennisMatches.par.map { tennisMatch =>
        try {
          val matchFacts = tournamentApi.parseMatchFacts(tennisMatch)

          TennisMatch(tournament.toTournament,
            matchFacts.playerAFacts.playerName, matchFacts.playerBFacts.playerName, tennisMatch.score, matchFacts.winner, matchFacts.round, matchFacts.durationMinutes,
            matchFacts.playerAFacts.toPlayerFacts, matchFacts.playerBFacts.toPlayerFacts)

        } catch {
          case e: Exception =>
            logger.error("Error on getting match facts: %s".format(tennisMatch), e)
            throw e
        }
      }
    }

    matchesComposite.toList
  }

}