package de.cae.XYFleet;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class test extends ServerResource {
    @Get("txt")
    public String toString() {
        // Print the requested URI path
        return "           __\n" +
                ".-.__      \\ .-.  ___  __\n" +
                "|_|  '--.-.-(   \\/\\;;\\_\\.-._______.-.\n" +
                "(-)___     \\ \\ .-\\ \\;;\\(   \\       \\ \\\n" +
                " Y    '---._\\_((Q)) \\;;\\\\ .-\\     __(_)\n" +
                " I           __'-' / .--.((Q))---'    \\,\n" +
                " I     ___.-:    \\|  |   \\'-'_          \\\n" +
                " A  .-'      \\ .-.\\   \\   \\ \\ '--.__     '\\\n" +
                " |  |____.----((Q))\\   \\__|--\\_      \\     '\n" +
                "    ( )        '-'  \\_  :  \\-' '--.___\\\n" +
                "     Y                \\  \\  \\       \\(_)\n" +
                "     I                 \\  \\  \\         \\,\n" +
                "     I                  \\  \\  \\          \\\n" +
                "     A                   \\  \\  \\          '\\\n" +
                "     |              snd   \\  \\__|           '\n" +
                "                           \\_:.  \\\n" +
                "                             \\ \\  \\\n" +
                "                              \\ \\  \\\n" +
                "                               \\_\\_|";
    }
}
