/**
 * Created by pocockn on 04/04/16.
 */
class GameResult {
    String home
    String away
    String hScore
    String aScore
    Stadium stadium

    String toString() {
     "$home $hScore, $away $aScore"
    }
}
