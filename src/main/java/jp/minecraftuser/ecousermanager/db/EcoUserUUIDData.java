
package jp.minecraftuser.ecousermanager.db;

import java.util.Date;
import java.util.UUID;
import jp.minecraftuser.ecousermanager.EcoUserManager;

/**
 * UUID/プレイヤー名情報
 * @author ecolight
 */
public class EcoUserUUIDData {
    private EcoUserManager plg = null;
    private UUID uuid = null;
    private String name = null;
    private Date time = null;
    
    /**
     * コンストラクタ
     * @param plg プラグインインスタンス
     */
    public EcoUserUUIDData(EcoUserManager plg) {
        this.plg = plg;
    }

    /**
     * コンストラクタ(値設定込み)
     * @param plg プラグインインスタンス
     * @param uuid UUID
     * @param name プレイヤー名
     * @param time 時刻
     */
    public EcoUserUUIDData(EcoUserManager plg, UUID uuid, String name, Date time) {
        this.plg = plg;
        this.uuid = uuid;
        this.name = name;
        this.time = time;
    }
    
    /**
     * 保持するUUIDを取得する
     * @return UUID
     */
    public UUID getUUID() {
        return uuid;
    }
    
    /**
     * 保持するプレイヤー名を取得する
     * @return プレイヤー名
     */
    public String getName() {
        return name;
    }
    
    /**
     * 保持する時刻情報を取得する
     * @return 時刻
     */
    public Date getTime() {
        return time;
    }
}
