import de.cae.XYFleet.Database;
import org.jooq.DSLContext;
import org.jooq.Table;
import org.jooq.TableRecord;
import org.jooq.impl.UpdatableRecordImpl;
import org.restlet.security.User;

import java.util.HashMap;
import java.util.HashSet;

import static org.jooq.codegen.XYFleet.Tables.*;

public class Scenario<R extends org.jooq.UpdatableRecord<R>> extends HashMap<Table<R>, HashSet<R>> {

    private final DSLContext dslContext;

    public Scenario() {
        dslContext = Database.getDSLContext();
    }

    public int add(Table<R> r, R record) throws NullPointerException {
        record = dslContext.newRecord(r, record);
        record.reset(0);
        record.merge();

        this.computeIfAbsent(r, k -> new HashSet<>());
        this.get(r).add(record);

        return (int) record.get(0);
    }

    public int merge(Table<R> r, R record) throws NullPointerException {
        record = dslContext.newRecord(r, record);
        record.merge();

        this.computeIfAbsent(r, k -> new HashSet<>());
        this.get(r).add(record);

        return (int) record.get(0);
    }

    public void cleanUp() {
        if (this.get(BOOKINGS) != null)
            for (R record : this.get(BOOKINGS)) record.delete();
        if (this.get(VEHICLES) != null)
            for (R record : this.get(VEHICLES)) record.delete();
        if (this.get(INSURANCES) != null)
            for (R record : this.get(INSURANCES)) record.delete();
        if (this.get(PRICING) != null)
            for (R record : this.get(PRICING)) record.delete();
        if (this.get(USERS) != null)
            for (R record : this.get(USERS)) record.delete();
    }

}
