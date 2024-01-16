import de.cae.XYFleet.Database;
import org.jooq.DSLContext;
import org.jooq.Table;
import org.jooq.TableRecord;
import org.jooq.impl.UpdatableRecordImpl;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.restlet.security.User;

import java.util.HashMap;
import java.util.HashSet;

import static org.jooq.codegen.XYFleet.Tables.*;

public class Scenario<R extends org.jooq.UpdatableRecord<R>> extends HashMap<Table<R>, HashSet<R>> {

    private final DSLContext dslContext;
    public Scenario(){
        dslContext=Database.getDSLContext();
    }

    public int  add(Table<R> r, R record) throws NullPointerException{
        record= dslContext.newRecord(r, record);
        record.reset(0);
        record.merge();

        this.computeIfAbsent(r, k -> new HashSet<>());
        this.get(r).add(record);

        return (int) record.get(0);
    }

    public void cleanUp() {
        for(R record : this.get(BOOKINGS)) record.delete();

        for(R record : this.get(VEHICLES)) record.delete();

        for(R record : this.get(INSURANCES)) record.delete();

        for(R record :  this.get(PRICING)) record.delete();

        for (R record : this.get(USERS)) record.delete();
    }

}
