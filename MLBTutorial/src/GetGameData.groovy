import groovy.sql.Sql

import java.util.regex.Matcher

/**
 * Created by pocockn on 01/04/16.
 */
class GetGameData {

    def day
    def month
    def year

    String base = 'http://gd2.mlb.com/components/game/mlb/'

    Map stadiumMap = [:]

    Map abbrevs = [
            ana:'Los Angeles (A)', ari:'Ari',bal:'Baltimore',
            chn:'Chicago (N)', col:'Colorado', hou:'Houston',
            ari:'Arizona', bos:'Boston', cin:'Cincinnati',
            det:'Detroit', kca:'Kansas City', atl:'Atlanta',
            cha:'Chicago (A)', cle:'Cleveland', flo:'Florida',
            lan:'Los Angeles (N)',mil:'Milwaukee', nyn:'New York (N)',
            pit:'Pittsburgh', sfn:'San Francisco', tex:'Texas',
            min:'Minnesota', oak:'Oakland', sdn:'San Diego',
            sln:'St. Louis', tor:'Toronto',nya:'New York (A)',
            phi:'Philadelphia', sea:'Seattle', tba:'Tampa Bay',
            was:'Washington'
    ]

    // constructor
    GetGameData(){
        fillInStadiumMap()
    }

    def fillInStadiumMap() {
        Sql db = Sql.newInstance(
                'jdbc:h2:build/baseball',
                'org.h2.Driver'
        )

        db.eachRow("Select * from stadium") { row ->
            Stadium stadium = new Stadium(
                    name:row.name,
                    team:row.team,
                    latitude: row.latitude,
                    longitude:row.longitude
            )
            stadiumMap[stadium.team] = stadium

        }
        db.close()
    }

    def getGame(away, home, num) {
        println "${abbrevs[away]} at ${abbrevs[home]} on ${month}/${day}/${year}"

        def url = base + "year_${year}/month_${month}/day_${day}/"

        def game = "gid_${year}_${month}_${day}_${away}mlb_${home}mlb_${num}/boxscore.xml"
        // Parse the XML data
        def boxscore = new XmlSlurper().parse("$url$game")
        // Extract the data below
        def awayName = boxscore.@away_fname
        def awayScore = boxscore.linescore[0].@away_team_runs
        def homeName = boxscore.@home_fname
        def homeScore = boxscore.linescore[0].@home_team_runs

        println "$awayName $awayScore, $homeName $homeScore (game $num)"

        GameResult result = new GameResult(home: homeName,
        away: awayName,
        hScore: homeScore,
        aScore: awayScore,
        stadium: stadiumMap[home]
        )

        return result
    }

    def getGames() {
        // Create empty list
        def gameResults = []

        println "Games for ${month}/${day}/${year}"

        String url = base + "year_${year}/month_${month}/day_${day}/"
        String gamePage = url.toURL().text

        def pattern = /\"gid_${year}_${month}_${day}_(\w*)mlb_(\w*)mlb_(\d)/

        Matcher m = gamePage =~ pattern

        if (m) {
            m.count.times { line->
                String away = m[line][1]
                String home = m[line][2]
                String num = m[line][3]
                try {
                    GameResult gr = this.getGame(away,home,num)
                    gameResults << gr
                } catch (Exception e) {
                    println "${abbrevs[away]} at ${abbrevs[home]} not started yet"
                }
            }
        }

        return gameResults

    }


}
