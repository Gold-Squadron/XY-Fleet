import org.jooq.codegen.XYFleet.tables.records.AccessGroupsRecord;
import org.jooq.codegen.XYFleet.tables.records.SettingsRecord;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static de.cae.XYFleet.authentication.XYAuthorizer.*;
import static de.cae.XYFleet.authentication.XYAuthorizer.ROLE_ADMIN;
import static org.jooq.codegen.XYFleet.Tables.ACCESS_GROUPS;
import static org.jooq.codegen.XYFleet.Tables.SETTINGS;

public class SettingResourceTest extends EntryResourceTest{
    @BeforeAll
    public static void initAll() {
        SettingsRecord settingsRecord = new SettingsRecord(0, "darkmode", "true", ADMIN_ID);
        SettingsRecord settingsRecord2 = new SettingsRecord(0, "farbe", "darkmode", ADMIN_ID);
        scenario.add(SETTINGS, settingsRecord2);

        settingsRecord.setId(scenario.add(SETTINGS, settingsRecord));
        uri = "/setting/" + settingsRecord.getId();
        testRecord = settingsRecord;
        testTable = testRecord;
        table = SETTINGS;
    }
    @BeforeEach
    public void setupSingle() {
        scenario.merge(SETTINGS, testRecord);
    }
    @Override
    @ParameterizedTest
    @CsvSource(value = {"key=size&value=banane"}, delimiter = ':')
    public void post_validCall_shouldReturnEntryInDatabase(String params) {
        super.post_validCall_shouldReturnEntryInDatabase(params);
    }

    @Override
    @ParameterizedTest
    @CsvSource(value = {FORBIDDEN+":" + ROLE_USER + ":key=size&value=banane",
            FORBIDDEN+":" + ROLE_SECURITY + ":key=size&value=banane",
            UNAUTHORIZED+": abc:user_id=&key=size&value=banane&",
            BAD_REQUEST+":" + ROLE_ADMIN + ":key=farbe&value=banane",
            BAD_REQUEST+":"+ ROLE_ADMIN +":key="}, delimiter = ':')
    public void post_invalidCall_shouldReturnEntryInDatabase(String responseMessage, String role, String params) {
        super.post_invalidCall_shouldReturnEntryInDatabase(responseMessage, role, params);
    }

    @Override
    @Test
    public void delete_validCall_shouldReturnEntryInDatabase() {
        super.delete_validCall_shouldReturnEntryInDatabase();
    }

    @Override
    @ParameterizedTest
    @CsvSource(value = {
            UNAUTHORIZED+":abc"
    }, delimiter = ':')
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
            UNAUTHORIZED+": abc",
            FORBIDDEN+":" + ROLE_USER,
            FORBIDDEN+":" + ROLE_SECURITY
    }, delimiter = ':')
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
            BAD_REQUEST+":"+ ROLE_ADMIN +":user_id=-1&key=size&value=banane"}, delimiter = ':')
    public void put_invalidCall_shouldThrowResourceException(String responseMessage, String role, String params) {
        super.put_invalidCall_shouldThrowResourceException(responseMessage, role, params);
    }
}
