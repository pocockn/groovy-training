/**
 * Created by pocockn on 22/03/16.
 */
import java.awt.BorderLayout as BL
import javax.swing.WindowConstants as WC
import groovy.swing.SwingBuilder
import javax.swing.ImageIcon

String base = 'http://chart.apis.google.com/chart?'

def params = [cht:'p3', chs:'200x100',chd:'t:60,40', chl: 'Hello|World']

/*
* Use of closure.
*  A closure is a block of code, delimited by curly braces
*  Arrow notation is used to specify dummy arguments
*  In the closure below, two dummy arguments are k an v which represent the
*  key and value for each entry. The expression on the right side of the array
*  says to substitute each key and value into a GString separated by an equals sign
*  collect method takes eachs entry in the map and converts it into a string with
*  the key assigned to the value
*
*  Use groovy method join
*  Takes a single argument thats used as a separator
 */

String qs = params.collect {k,v -> "$k=$v" }.join('&')

/*
* assert is used for testing
* takes a boolean, if true returns nothing, if false it will give a detailed
* error to the console
 */

params.each {
    k,v -> assert qs.contains("$k=$v")
}

SwingBuilder.edt {
    frame(title:'Hello, Chart!', pack: true,
            visible:true, defaultCloseOperation:WC.EXIT_ON_CLOSE) {
        label(icon:new ImageIcon("$base$qs".toURL()),
                constraints:BL.CENTER)
    }
}







