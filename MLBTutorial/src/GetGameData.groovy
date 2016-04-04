import groovy.sql.Sql

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


    def url = base + "year_${year}/month_${month}/day_${day}/"

    def game = "gid_${year}_${month}_${day}_${away}mlb_${home}mlb_${num}/boxscore.xml"

    def boxscore = new XmlSlurper().parse("$url$game")

    def awayName = boxscore.@away_fname
    def awayScore = boxscore.linescore[0].@away_team_runs
    def homeName = boxscore.@home_fname
    def homeScore = boxscore.linescore[0].@home_team_runs


}
