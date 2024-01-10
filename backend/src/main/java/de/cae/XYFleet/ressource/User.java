package de.cae.XYFleet.ressource;

import org.jooq.DeleteResultStep;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.codegen.XYFleet.tables.Users;
import org.jooq.codegen.XYFleet.tables.records.UsersRecord;
import org.restlet.data.Form;
import org.restlet.data.Parameter;
import org.restlet.resource.*;

import java.util.Map;

import static de.cae.XYFleet.authentication.XYAuthorizer.*;
import static org.jooq.codegen.XYFleet.Tables.BOOKINGS;
import static org.jooq.codegen.XYFleet.Tables.USERS;

public class User extends XYServerResource {
    private int bookingIdentifier;
    @Override
    protected void doInit() throws ResourceException {
        super.doInit();
        try {
            bookingIdentifier = Integer.parseInt(getAttribute("bookingIdentifier"));
        } catch (NumberFormatException e) {
            bookingIdentifier = -1;
        }
    }
    @Put("txt?value&param")
    public String createUser(){
        Form form = getQuery();
        Map<String, String> values = form.getValuesMap();
        UsersRecord record = new UsersRecord(Integer.parseInt(values.get("id")), values.get("name"),values.get("passwort"), values.get("role"),  Byte.valueOf(values.get("is_driver")));
        dslContext.instertInto(USERS, record);
        for (Parameter parameter : form) {
            System.out.print("parameter " + parameter.getName());
            System.out.println("/" + parameter.getValue());

            switch(parameter.getName()) {
                case "id":
                    break;
                case "name":
                    break;
                case "role":
                    break;
                case "is_driver":
                    break;
                default:
                    //throw new IllegalArgumentException("");
            }
        }
        return "true";

    }
    @Get("txt?value")
    public String toString(){
        checkInRole(ROLE_SECURITY);
        //SELECT * FROM users where id = {bookingIdentifier}
        UsersRecord result = dslContext.fetchOne(USERS, USERS.ID.eq(bookingIdentifier));

        return  result != null ? result.formatJSON(jSONFormat) : null;
        }
    @Post()
    public String changeUser(){

        return null;
    }
    @Delete
    public String deleteUser(){
        checkInRole(ROLE_ADMIN);

        String result = this.toString();

        //DELETE users WHERE id = {bookingIdentifier}
        if(Integer.parseInt(getClientInfo().getUser().getName()) != bookingIdentifier){
            dslContext.delete(USERS).where(USERS.ID.eq(bookingIdentifier)).execute();
        }else{
            return "you cannot delete the account you are logged in with!!";
        }
        return result;
    }

}
