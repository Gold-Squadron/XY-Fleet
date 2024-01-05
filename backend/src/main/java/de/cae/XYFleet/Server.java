package de.cae.XYFleet;

import org.restlet.*;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.MediaType;
import org.restlet.data.Protocol;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Directory;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import org.restlet.routing.Router;
import org.restlet.routing.Template;
import org.restlet.security.ChallengeAuthenticator;
import org.restlet.security.MapVerifier;


public class Server extends ServerResource {

    //public static final String ROOT_URI = "file:///c:/restlet/docs/api/";

    public static void initServer(){
        try{
            Component component = new Component();
            org.restlet.Server server = component.getServers().add(Protocol.HTTP, 8080);

            component.getDefaultHost().attach("/ldapauthenticator", LDAPAuthenticator.class);
            component.getDefaultHost().attach("/test", Server.class);

            component.getDefaultHost().attachDefault(new XYFleetAuthorizer());

            component.start();

             }catch(Exception e){
            e.printStackTrace();
        }
    }
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
