package de.cae.XYFleet.ressource;

import org.jooq.Field;
import org.jooq.impl.UpdatableRecordImpl;
import org.restlet.resource.ResourceException;

import java.time.LocalDate;
import java.util.Map;

public  abstract class EntryResource extends XYServerResource{
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
    abstract public String deleteEntry() throws ResourceException;
    @Override
    abstract public String editEntry() throws ResourceException;
    abstract public String handlePut(Map<String, String> values) throws ResourceException;
    //cast queue input according to the fields Datatype
    protected void setFieldValueHelper(UpdatableRecordImpl record, Field<?> field, String value){
        if(field.getType()==Integer.class){
            record.set((Field<Integer>)field, Integer.parseInt(value));
        }else{
            if(field.getType()== LocalDate.class){
                record.set((Field<LocalDate>)field, LocalDate.parse(value));
            }else{
                if(field.getType()==String.class){
                    record.set((Field<String>)field, value);
                }
            }
        }
    }
}
