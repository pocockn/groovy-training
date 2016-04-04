/**
 * Created by pocockn on 30/03/16.
 */
class Stadium {
    int id
    String name
    String city
    String state
    String team
    double latitude
    double longitude

    String toString() {
        "{$team,$name,$latitude,$longitude}"
    }



}
