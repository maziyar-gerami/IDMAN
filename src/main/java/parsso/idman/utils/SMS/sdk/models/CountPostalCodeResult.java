/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parsso.idman.Utils.SMS.sdk.models;


import com.google.gson.JsonObject;

/**
 * @author mohsen
 */
public class CountPostalCodeResult {

    private final String section;
    private final Long value;

    public CountPostalCodeResult(JsonObject json) {
        this.section = json.get("section").getAsString();
        this.value = json.get("value").getAsLong();
    }

    public String getSection() {
        return section;
    }

    public long getValue() {
        return value;
    }

}