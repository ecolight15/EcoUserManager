
package jp.minecraftuser.ecousermanager.config;

import jp.minecraftuser.ecousermanager.EcoUserManager;
import jp.minecraftuser.ecoframework.PluginFrame;
import jp.minecraftuser.ecoframework.ConfigFrame;
import jp.minecraftuser.ecousermanager.db.EcoUserUUIDStore;

/**
 * デフォルトコンフィグインスタンス
 * @author ecolight
 */
public class EcoUserManagerConfig extends ConfigFrame{
    private EcoUserUUIDStore store = null;
    /**
     * コンストラクタ
     * @param plg_ 
     */
    public EcoUserManagerConfig(PluginFrame plg_) {
        super(plg_);
        store = new EcoUserUUIDStore((EcoUserManager)plg);
    }
    
    /**
     * 保持するUUID/プレイヤー名ストアのインスタンスを取得
     * @return EcoUserUUIDStoreインスタンス
     */
    public EcoUserUUIDStore getStore() {
        return this.store;
    }
}
