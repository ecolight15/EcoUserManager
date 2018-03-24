
package jp.minecraftuser.ecousermanager.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import jp.minecraftuser.ecousermanager.EcoUserManager;
import org.bukkit.OfflinePlayer;

/**
 * UUID情報保管DBクラス
 * @author ecolight
 */
public class EcoUserUUIDDB {
    private EcoUserManager plg = null;
    private Logger log = null;
    private Connection con = null;

    /**
     * コンストラクタ
     * @param plg プラグインインスタンス
     */
    public EcoUserUUIDDB(EcoUserManager plg) {
        this.plg = plg;
        log = plg.getLogger();
        String msgDBpath = plg.getDataFolder().getPath()+"/uuid.db";
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:"+msgDBpath);
            con.setAutoCommit(false);
            
            // 必要テーブルの追加
            Statement stmt = con.createStatement();
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS UUIDTABLE(MOSTUUID INTEGER NOT NULL, LEASTUUID INTEGER NOT NULL, NAME STRING NOT NULL, TIME INTEGER NOT NULL);");
            stmt.close();
            log.info("DataBase Loaded.");
        } catch (SQLException ex) {
            log.warning("コネクション失敗："+ex.getMessage());
        } catch (ClassNotFoundException ex) {
            log.warning("DBシステム異常："+ex.getMessage());
        }
    }

    /**
     * 閉塞処理用
     */
    public void EcoUserUUIDDB_() {
        dbFinalize();
    }

    /**
     * 閉塞処理
     */
    public void dbFinalize() {
        if (con == null) {return;}
        try {
            con.close();
            con = null;
        } catch (SQLException ex) {
            log.warning("DBシステム異常："+ex.getMessage());
        }
    }

    /**
     * 新規プレイヤー登録
     * @param player プレイヤーインスタンス
     */
    public void checkPlayer(OfflinePlayer player) {
        if (con == null) {return;}
        String latestName = latestName(player.getUniqueId());
        // 最後に登録してある名前と現在の名前が同じなら登録の必要なし
        // 最後に登録した名前がない(新規)、または名前が変わっている場合には継続
        if ((latestName != null) && (player.getName().equals(latestName))) {
            log.info("not change name["+latestName+"]");
            return;
        }

        PreparedStatement prep = null;
        try {
            // UUIDテーブルに現在の名前を登録
            Date date = new Date();
            UUID uuid = player.getUniqueId();
            prep = con.prepareStatement("INSERT INTO UUIDTABLE(MOSTUUID, LEASTUUID, NAME, TIME) VALUES (?, ?, ?, ?);");
            prep.setLong(1, uuid.getMostSignificantBits());
            prep.setLong(2, uuid.getLeastSignificantBits());
            prep.setString(3, player.getName());
            prep.setLong(4, date.getTime());
            prep.executeUpdate();
            con.commit();
            prep.close();
            if (latestName == null) {
                log.info("Added uuid name["+player.getName()+"]");            
            } else {
                log.info("Added uuid name["+latestName+"]→["+player.getName()+"]");
            }
        } catch (SQLException ex) {
            log.info(ex.getLocalizedMessage());
            log.info(ex.getMessage());
            log.info(ex.getSQLState());
            if (prep != null) try { prep.close(); } catch (SQLException ex1) {
                Logger.getLogger(EcoUserUUIDDB.class.getName()).log(Level.SEVERE, null, ex1); }
        }
    }

    /**
     * 指定UUIDのプレイヤーの最後のサーバー内での名称を取得する
     * @param uuid 検索UUID
     * @return プレイヤー名を返却
     */
    public String latestName(UUID uuid) {
        if (con == null) {return null;}
        String name = null;
        PreparedStatement prep = null;
        ResultSet rs = null;
        try {
            prep = con.prepareStatement("SELECT NAME FROM UUIDTABLE WHERE MOSTUUID = ? AND LEASTUUID = ? ORDER BY TIME DESC");
            prep.setLong(1, uuid.getMostSignificantBits());
            prep.setLong(2, uuid.getLeastSignificantBits());
            rs = prep.executeQuery();
            boolean result = rs.next();
            if (result) {
                name = rs.getString("NAME");
            }
            rs.close();
            prep.close();
        } catch (SQLException ex) {
            log.info(ex.getLocalizedMessage());
            log.info(ex.getMessage());
            log.info(ex.getSQLState());
            if (rs != null) try { rs.close(); } catch (SQLException ex1) {
                Logger.getLogger(EcoUserUUIDDB.class.getName()).log(Level.SEVERE, null, ex1); }
            if (prep != null) try { prep.close(); } catch (SQLException ex1) {
                Logger.getLogger(EcoUserUUIDDB.class.getName()).log(Level.SEVERE, null, ex1); }
        }

        return name;
    }

    /**
     * プレイヤー名から最後のUUIDを検索する
     * @param name プレイヤー名
     * @return UUID
     */
    public UUID latestUUID(String name) {
        if (con == null) {return null;}
        UUID uuid = null;
        PreparedStatement prep = null;
        ResultSet rs = null;
        try {
            prep = con.prepareStatement("SELECT * FROM UUIDTABLE WHERE NAME LIKE ? ORDER BY TIME DESC");
            prep.setString(1, name);
            rs = prep.executeQuery();
            boolean result = rs.next();
            if (result) {
                long most = rs.getLong("MOSTUUID");
                long least = rs.getLong("LEASTUUID");
                uuid = new UUID(most, least);
            }
            rs.close();
            prep.close();
        } catch (SQLException ex) {
            log.info(ex.getLocalizedMessage());
            log.info(ex.getMessage());
            log.info(ex.getSQLState());
            if (rs != null) try { rs.close(); } catch (SQLException ex1) {
                Logger.getLogger(EcoUserUUIDDB.class.getName()).log(Level.SEVERE, null, ex1); }
            if (prep != null) try { prep.close(); } catch (SQLException ex1) {
                Logger.getLogger(EcoUserUUIDDB.class.getName()).log(Level.SEVERE, null, ex1); }
        }

        return uuid;
    }

    /**
     * 指定したUUIDが使用したサーバー内でのプレイヤー名を列挙する
     * @param uuid 検索UUID
     * @return プレイヤー名のリストを返却する
     */
    public ArrayList<EcoUserUUIDData> listName(UUID uuid) {
        if (con == null) {return null;}
        ArrayList<EcoUserUUIDData> list = new ArrayList<>();
        PreparedStatement prep = null;
        ResultSet rs = null;
        try {
            prep = con.prepareStatement("SELECT * FROM UUIDTABLE WHERE MOSTUUID = ? AND LEASTUUID = ? ORDER BY TIME ASC");
            prep.setLong(1, uuid.getMostSignificantBits());
            prep.setLong(2, uuid.getLeastSignificantBits());
            rs = prep.executeQuery();
            while(true) {
                boolean result = rs.next();
                if (!result) break;
                list.add(new EcoUserUUIDData(plg, new UUID(rs.getLong("MOSTUUID"), rs.getLong("LEASTUUID")), rs.getString("NAME"), new Date(rs.getLong("TIME"))));
            }
            rs.close();
            prep.close();
        } catch (SQLException ex) {
            log.info(ex.getLocalizedMessage());
            log.info(ex.getMessage());
            log.info(ex.getSQLState());
            if (rs != null) try { rs.close(); } catch (SQLException ex1) {
                Logger.getLogger(EcoUserUUIDDB.class.getName()).log(Level.SEVERE, null, ex1); }
            if (prep != null) try { prep.close(); } catch (SQLException ex1) {
                Logger.getLogger(EcoUserUUIDDB.class.getName()).log(Level.SEVERE, null, ex1); }
        }
        if (list.isEmpty()) return null;
        return list;
    }

    /**
     * 指定したプレイヤー名を使用したことのあるプレイヤーのUUIDを列挙する
     * @param name 検索プレイヤー名
     * @return UUIDのリストを返却する
     */
    public ArrayList<EcoUserUUIDData> listUUID(String name) {
        if (con == null) {return null;}
        ArrayList<EcoUserUUIDData> list = new ArrayList<>();
        PreparedStatement prep = null;
        ResultSet rs = null;
        try {
            prep = con.prepareStatement("SELECT * FROM UUIDTABLE WHERE NAME = ? ORDER BY TIME ASC");
            prep.setString(1, name);
            rs = prep.executeQuery();
            while(true) {
                boolean result = rs.next();
                if (!result) break;
                list.add(new EcoUserUUIDData(plg, new UUID(rs.getLong("MOSTUUID"), rs.getLong("LEASTUUID")), rs.getString("NAME"), new Date(rs.getLong("TIME"))));
            }
            rs.close();
            prep.close();
        } catch (SQLException ex) {
            log.info(ex.getLocalizedMessage());
            log.info(ex.getMessage());
            log.info(ex.getSQLState());
            if (rs != null) try { rs.close(); } catch (SQLException ex1) {
                Logger.getLogger(EcoUserUUIDDB.class.getName()).log(Level.SEVERE, null, ex1); }
            if (prep != null) try { prep.close(); } catch (SQLException ex1) {
                Logger.getLogger(EcoUserUUIDDB.class.getName()).log(Level.SEVERE, null, ex1); }
        }
        if (list.isEmpty()) return null;
        return list;
    }

    /**
     * 全データの返却
     * @return 全データ
     */
    public ArrayList<EcoUserUUIDData> listAll() {
        if (con == null) {return null;}
        ArrayList<EcoUserUUIDData> list = new ArrayList<>();
        PreparedStatement prep = null;
        ResultSet rs = null;
        try {
            prep = con.prepareStatement("SELECT * FROM UUIDTABLE ORDER BY NAME ASC");
            rs = prep.executeQuery();
            while(true) {
                boolean result = rs.next();
                if (!result) break;
                list.add(new EcoUserUUIDData(plg, new UUID(rs.getLong("MOSTUUID"), rs.getLong("LEASTUUID")), rs.getString("NAME"), new Date(rs.getLong("TIME"))));
            }
            rs.close();
            prep.close();
        } catch (SQLException ex) {
            log.info(ex.getLocalizedMessage());
            log.info(ex.getMessage());
            log.info(ex.getSQLState());
            if (rs != null) try { rs.close(); } catch (SQLException ex1) {
                Logger.getLogger(EcoUserUUIDDB.class.getName()).log(Level.SEVERE, null, ex1); }
            if (prep != null) try { prep.close(); } catch (SQLException ex1) {
                Logger.getLogger(EcoUserUUIDDB.class.getName()).log(Level.SEVERE, null, ex1); }
        }
        if (list.isEmpty()) return null;
        return list;
    }

}
