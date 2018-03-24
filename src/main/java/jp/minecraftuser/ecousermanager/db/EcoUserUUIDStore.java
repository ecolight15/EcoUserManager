
package jp.minecraftuser.ecousermanager.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import jp.minecraftuser.ecousermanager.EcoUserManager;
import org.bukkit.OfflinePlayer;

/**
 * UUID/プレイヤー名ストアクラス
 * @author ecolight
 */
public class EcoUserUUIDStore {
    private EcoUserManager plg = null;
    private EcoUserUUIDDB db = null;

    /**
     * コンストラクタ
     * @param plg プラグインインスタンス
     */
    public EcoUserUUIDStore(EcoUserManager plg) {
        this.plg = plg;
        db = new EcoUserUUIDDB(plg);
    }

    /**
     * 閉塞処理
     */
    public void dbFinalize() {
        if (db != null) db.dbFinalize();
    }

    /**
     * プレイヤーデータ追加
     * @param p プレイヤーインスタンス
     */
    public void checkPlayer(OfflinePlayer p) {
        db.checkPlayer(p);
    }

     /**
     * 指定UUIDのプレイヤーの最後のサーバー内での名称を取得する
     * @param uuid 検索UUID
     * @return プレイヤー名を返却
     */
    public String latestName(UUID uuid) {
        return db.latestName(uuid);
    }

    /**
     * プレイヤー名から最後のUUIDを検索する
     * @param name プレイヤー名
     * @return UUID
     */
    public UUID latestUUID(String name) {
        return db.latestUUID(name);
    }

    /**
     * 指定したプレイヤーのユーザーが使用したサーバー内でのプレイヤー名を列挙する
     * 検索するプレイヤーは最後にサーバーで観測した指定プレイヤー名のUUIDが用いられる
     * @param p 検索プレイヤー名
     * @return プレイヤー名のリストを返却する
     */
    public ArrayList<EcoUserUUIDData> getNameList(String p) {
        return db.listName(latestUUID(p));
    }

    /**
     * 指定したプレイヤー名を使用したことのあるプレイヤーのUUIDを列挙する
     * @param p 検索プレイヤー名
     * @return UUIDのリストを返却する
     */
    public ArrayList<EcoUserUUIDData> getUUIDList(String p) {
        return db.listUUID(p);
    }

    /**
     * 全データの返却
     * @return 全データ
     */
    public ArrayList<EcoUserUUIDData> getAllList() {
        return db.listAll();
    }
}
