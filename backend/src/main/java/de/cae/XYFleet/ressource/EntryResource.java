package de.cae.XYFleet.ressource;

import org.jooq.Field;
import org.jooq.Table;
import org.jooq.codegen.XYFleet.tables.records.UsersRecord;
import org.jooq.impl.UpdatableRecordImpl;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.Objects;

import static de.cae.XYFleet.authentication.XYAuthorizer.ROLE_ADMIN;
import static de.cae.XYFleet.authentication.XYAuthorizer.ROLE_USER;
import static org.jooq.codegen.XYFleet.Tables.BOOKINGS;
import static org.jooq.codegen.XYFleet.Tables.USERS;

public abstract class EntryResource extends XYServerResource {
    protected int identifier;

    @Override
    protected void doInit() throws ResourceException {
        super.doInit();
        try {
            identifier = Integer.parseInt(getAttribute("identifier"));

        } catch (NumberFormatException e) {
            identifier = -1;
        }
    }

    @Override
    public String deleteEntry() throws ResourceException {
        checkInRole(ROLE_ADMIN);

        String result = this.toString();

        //DELETE insurance where id = {Identifier}
        dslContext.delete(table).where(table.field(BOOKINGS.ID).eq(identifier)).execute();
        return result;
    }
    @Override
    public String editEntry() throws ResourceException{
        checkInRole(ROLE_ADMIN);


        Map<String, String> valuesMap = getQuery().getValuesMap();
        valuesMap.remove("id");

        Field<?>[] fields = table.fields();
        //build up query for database
        UpdatableRecordImpl<?> updatableRecord = (UpdatableRecordImpl<?>) dslContext.fetchOne(table, table.field(BOOKINGS.ID).eq(identifier));

        if (updatableRecord == null) throw new ResourceException(404, "given Maintenance Id is missing");

        for (Field<?> field : fields) {
            //checking whether amendments have been requested or not. Setting all types to String type safty in tests
            String name = field.getUnqualifiedName().first();
            String value = valuesMap.get(field.getUnqualifiedName().first());
            if(value != null){
                setFieldValueHelper(updatableRecord, field, value);
            }
        }
        //check if valid call
        if (!updatableRecord.changed())
            throw new ResourceException(400, "nothing to do. no params in query given");
        //check correctness of values
        validatePutCall(updatableRecord);

        //UPDATE insurances SET ({given values}) WHERE id = {Identifier}
        updatableRecord.update();


        return updatableRecord.formatJSON(jSONFormat);
    };

    public String handlePut(Map<String, String> valuesMap) throws ResourceException {
        Field<?>[] fields = table.fields();
        //check if all expected values are given
        UpdatableRecordImpl<?> updatableRecord = (UpdatableRecordImpl<?>) dslContext.newRecord(table);
        //INSERT INTO updatableRecord  VALUES ({query values})
        for (Field<?> field : fields) {
            String name = field.getUnqualifiedName().first();
            String value = valuesMap.get(name);
            if (isNotRequiredNull(name)) {
                if (value == null) throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Missing value for initialization: " + name);
                setFieldValueHelper(updatableRecord, field, value);
            }
        }
        updatableRecord.set(BOOKINGS.ID, null);
        //check correctness of values
        validatePutCall(updatableRecord);
        updatableRecord.merge();
        //CREATE vehicles VALUES ({given values})
        return updatableRecord.formatJSON(jSONFormat);
    }
    public boolean isNotRequiredNull(String name){
     return !Objects.equals(name, "id");
    }
    abstract public void validatePutCall(UpdatableRecordImpl record);
    //abstract public void validatePostCall(UpdatableRecordImpl record);

    //cast queue input according to the fields Datatype
    protected void setFieldValueHelper(UpdatableRecordImpl record, Field<?> field, String value) {
        if (value.isEmpty())
            return;
        if (field.getType() == Integer.class) {
            try {
                record.set((Field<Integer>) field, Integer.parseInt(value));
            } catch (NumberFormatException e) {
                throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, field.getUnqualifiedName().first() + " has an invalid value: " + value + ", not parseable to Integer");
            }
        } else {
            if (field.getType() == LocalDate.class) {
                try {
                    record.set((Field<LocalDate>) field, LocalDate.parse(value));
                } catch (DateTimeParseException e) {
                    throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, field.getUnqualifiedName().first() + " has an invalid value: " + value + ", not parseable to LocalDate");
                }
            } else {
                if (field.getType() == String.class) {
                    record.set((Field<String>) field, value);
                }
            }
            if(field.getType() == Byte.class) {
                try{
                    record.set((Field<Byte>) field, Byte.parseByte(value));
                }catch(NumberFormatException e){
                    throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, field.getUnqualifiedName().first() + " has an invalid value: " + value + ", not parseable to Byte");
                }

            }
        }
    }

    @Override
    public String toString() throws ResourceException {
        //SELECT * FROM table where id = {Identifier}
        UpdatableRecordImpl<?> updatableRecord = (UpdatableRecordImpl<?>) dslContext.fetchOne(table, table.field(BOOKINGS.ID).eq(identifier));

        if (updatableRecord == null) throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "not found");
        return updatableRecord.formatJSON(jSONFormat);
    }

    @Override
    public String createEntity() throws ResourceException {
        return handlePut(getQuery().getValuesMap());
    }
}
