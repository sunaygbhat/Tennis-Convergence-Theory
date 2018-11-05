Scala API for atpworldtour.com tennis statistics
------------------------------------------------

### How to retrieve tennis matches for year 2011 and store them in a csv file? 


    Tennis matchesCSV example:
    event_time, event_name, surface, num_of_sets, playerA,playerB, winner, score, round, duration_minutes, playerATotalServicePointsWon, playerATotalServicePoints, playerBTotalServicePointsWon, playerBTotalServicePoints
    2011-01-02 00:00:00.000,Brisbane Australia ATP World Tour 250,HARD,2,Robin Soderling,Ryan Harrison,Robin Soderling,6-2; 6-4,R32,66,39,52,28,50
    2011-01-02 00:00:00.000,Brisbane Australia ATP World Tour 250,HARD,2,Michael Berrer,Dudi Sela,Michael Berrer,1-6; 7-6(3); 6-2,R32,152,62,99,62,103
    2011-01-02 00:00:00.000,Brisbane Australia ATP World Tour 250,HARD,2,Matthew Ebden,John Millman,Matthew Ebden,4-6; 6-2; 6-4,R32,104,55,85,48,76
    2011-01-02 00:00:00.000,Brisbane Australia ATP World Tour 250,HARD,2,Denis Istomin,Thiemo de Bakker,Denis Istomin,7-6(5); 6-4,R32,93,49,63,48,72
    2011-01-02 00:00:00.000,Brisbane Australia ATP World Tour 250,HARD,2,Mardy Fish,Adrian Mannarino,Mardy Fish,6-1; 6-4,R32,79,39,51,35,68
    2011-01-02 00:00:00.000,Brisbane Australia ATP World Tour 250,HARD,2,Radek Stepanek,Tobias Kamke,Radek Stepanek,5-7; 6-1; 6-4,R32,124,53,80,52,87
    2011-01-02 00:00:00.000,Brisbane Australia ATP World Tour 250,HARD,2,Ricardas Berankis,Arnaud Clement,Ricardas Berankis,6-4; 6-3,R32,86,37,55,37,64
    2011-01-02 00:00:00.000,Brisbane Australia ATP World Tour 250,HARD,2,Florian Mayer,Bernard Tomic,Florian Mayer,6-2; 6-2,R32,55,32,35,28,58
    2011-01-02 00:00:00.000,Brisbane Australia ATP World Tour 250,HARD,2,Feliciano Lopez,Philipp Petzschner,Feliciano Lopez,6-4; 7-6(11),R32,100,54,77,47,68
    ...
    
    Scala application:
    
    package dk.test
    
    import dk.atp.api.tournament.GenericTournamentAtpApi
    import dk.atp.api._
    
    object MatchesLoadSimple extends App {
      //Increase 3000ms connection timeout, if loading tennis matches fails. 
      //There are dozens of requests sent to atp website, so
      //even with a higher timeout the loadMatches method may sometimes fail. 
      //With a high speed internet connection, loading all tennis matches
      //for 2011 takes between 3-6 seconds.
      var tournamentApi: GenericTournamentAtpApi = new GenericTournamentAtpApi(3000)
      val genericATPMatchesLoader = new GenericATPMatchesLoader(tournamentApi)
      val matches =  genericATPMatchesLoader.loadMatches(2011)
      CSVATPMatchesLoader.toCSVFile(matches, "./target/matches.csv")
    }
