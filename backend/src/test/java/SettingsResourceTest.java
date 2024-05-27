import org.jooq.codegen.XYFleet.tables.records.InsurancesRecord;
import org.jooq.codegen.XYFleet.tables.records.SettingsRecord;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;

import static de.cae.XYFleet.authentication.XYAuthorizer.*;
import static de.cae.XYFleet.authentication.XYAuthorizer.ROLE_ADMIN;
import static org.jooq.codegen.XYFleet.Tables.INSURANCES;
import static org.jooq.codegen.XYFleet.Tables.SETTINGS;

public class SettingsResourceTest extends ResourceTest{
    @BeforeAll
    public static void initAll() {
        SettingsRecord settingsRecord = new SettingsRecord(0, "darkmode", "true", ADMIN_ID);
        SettingsRecord settingsRecord2 = new SettingsRecord(0, "farbe", "darkmode", ADMIN_ID);
        scenario.add(SETTINGS, settingsRecord2);

        settingsRecord.setId(scenario.add(SETTINGS, settingsRecord));
        uri = "/setting";
        table = SETTINGS;
    }
    @Override
    @ParameterizedTest
    @CsvSource(value = {UNAUTHORIZED+": abc"}, delimiter = ':')
    public void get_invalidCall_shouldThrowResourceException(String responseMessage, String role) {
        super.get_invalidCall_shouldThrowResourceException(responseMessage, role);
    }

    @Override
    @Test
    public void get_validCall_shouldReturnEntryInDatabase() {
        super.get_validCall_shouldReturnEntryInDatabase();
    }

    @Override
    @ParameterizedTest
    @CsvSource(value = {
            METHOD_NOT_ALLOWED+":" + ROLE_USER,
            UNAUTHORIZED+":abc"}, delimiter = ':')
    public void delete_invalidCall_shouldThrowResourceException(String responseMessage, String role) {
        super.delete_invalidCall_shouldThrowResourceException(responseMessage, role);
    }

    @Override
    @ParameterizedTest
    @CsvSource(value = {"key=size&value=banane&user_id="}, delimiter = ':')
    public void put_validCall_shouldReturnEntryInDatabase(String params) {
        params+=ADMIN_ID;
        super.put_validCall_shouldReturnEntryInDatabase(params);
    }

    @Override
    @ParameterizedTest
    @CsvSource(value = {
            UNAUTHORIZED+":abc:key=size&value=banane",
            BAD_REQUEST+":" + ROLE_ADMIN + ":key=size&value=banane",
            FORBIDDEN+":"+ ROLE_USER +":key=random&value=something&user_id="}, delimiter = ':')
    public void put_invalidCall_shouldThrowResourceException(String responseMessage, String role, String params) {
        params+=ADMIN_ID;
        super.put_invalidCall_shouldThrowResourceException(responseMessage, role, params);
    }
}
