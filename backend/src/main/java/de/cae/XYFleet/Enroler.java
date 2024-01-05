package de.cae.XYFleet;

import org.restlet.Application;
import org.restlet.Request;
import org.restlet.data.ClientInfo;
import org.restlet.security.Role;
import org.restlet.security.User;

import java.util.List;

public class Enroler implements org.restlet.security.Enroler {
    public void enrole(ClientInfo clientInfo) {
        Request request = Request.getCurrent();
        User user = request.getClientInfo().getUser();
        if (user != null) {
            List<Role> roles = request.getClientInfo().getRoles();
            if (roles != null) {
                for (Role role : roles) {
                    // example of role creation
                    clientInfo.getRoles().add(Role.get(Application.getCurrent(), role.getName()));
                }
            }
        }
    }
}
