/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.minecraftuser.ecousermanager.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import jp.minecraftuser.ecousermanager.EcoUserManager;
import org.bukkit.OfflinePlayer;

/**
 *
 * @author ecolight
 */
public class EcoUserUUIDStore {
    private EcoUserManager plg = null;
    private EcoUserUUIDDB db = null;

    public EcoUserUUIDStore(EcoUserManager plg) {
        this.plg = plg;
        db = new EcoUserUUIDDB(plg);
    }
    public void finalize() {
        if (db != null) db.Finalize();
    }
    public void checkPlayer(OfflinePlayer p) {
        db.checkPlayer(p);
    }
    public String latestName(UUID uuid) {
        return db.latestName(uuid);
    }
    public UUID latestUUID(String name) {
        return db.latestUUID(name);
    }
    public ArrayList<EcoUserUUIDData> getNameList(String p) {
        return db.listName(latestUUID(p));
    }
    public ArrayList<EcoUserUUIDData> getUUIDList(String p) {
        return db.listUUID(p);
    }
    public ArrayList<EcoUserUUIDData> getAllList() {
        return db.listAll();
    }
//    public HashMap<String, UUID> getTest(String... mames) {
//        return db.getMojangUUID(mames);
//    }
}
