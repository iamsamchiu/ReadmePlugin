package org.jenkinsci.plugins.readme;
import hudson.Extension;
import hudson.model.*;

/**
 * 此類別的主要功能是透過繼承Jenkins Extension的RootAction來擴充Jenkins首頁的
 * 主選單，並在選單中加入一個自訂的Readme連結。 
 * 程式中的ReadMeDescriptor子類別則是負責實作Descriptor，主要功能是封裝處理
 * global.jelly的資料，讓使用者可以在global configuration頁面上設定Readme的URL位
 * 置。
 * 
 * @author Sam Chiu
 */
@Extension
public class ReadMeRootAction implements RootAction, Describable<ReadMeRootAction> {

	/*
	 * index.jelly將會透過此method取得Jenkins的PrimaryView。
	 */
    public View getPrimaryView() {
        return Hudson.getInstance().getPrimaryView();
    }
    /*
     * 指定連結的顯示名稱
     * @see hudson.model.Action#getDisplayName()
     */
    @Override
    public String getDisplayName() {
        return "Read Me";
    }
    /*
     * 使用預設的help icon。
     * @see hudson.model.Action#getIconFileName()
     */
    @Override
    public String getIconFileName() {
        return "help.png";
    }
    /*
     * 在此方法中指定url。
     * @see hudson.model.Action#getUrlName()
     */
    @Override
    public String getUrlName() {
        return "/readme";
    }

    /*
     * getDescriptor為生成Descriptor的唯一界面，是一種Singleton pattern，也就是說
     * 此ReadMeDescriptor類別在系統上只會有單一個個體。產生ReadMeDescriptor物件的方式
     * 是透過Hudson物件的getDescriptor方法，並傳入欲生成的類別作為參數。
     * @see hudson.model.Describable#getDescriptor()
     */
    @Override
    public ReadMeDescriptor getDescriptor() {
        return (ReadMeDescriptor)Hudson.getInstance().getDescriptor(getClass());
    }

    /**
     * 此類別的功能主要用於擴充Jenkins global configuration頁面，並提供使用者可以指定
     * Readme URL。
     */
    @Extension
    public static final class ReadMeDescriptor extends Descriptor<ReadMeRootAction> {
        // 設定預設的ReadmeURL，預設我們將readme.html放置於Readme自身plugin的目錄下
        private String readMeUrl = "/plugin/Readme/readme.html";
        
        public ReadMeDescriptor() {
        	// 在class的建構子呼叫load()可以從硬碟讀取之前透過save()的資料
            load();
        }

        @Override
        public boolean configure(org.kohsuke.stapler.StaplerRequest req,
                    net.sf.json.JSONObject formData) throws FormException {
        	
        	// formData為一個JSONObject，Jelly的網頁資料便是透過此一物件封裝,
        	// "readMeUrl"則為global.jelly中所對應的data field
            readMeUrl = formData.getString("readMeUrl");
            save();
            return super.configure(req, formData);
        }
        
        /*
         * 覆寫getDisplayName，回傳的字串將顯示於Jenkins global configuration設定頁面
         * @see hudson.model.Descriptor#getDisplayName()
         */
        @Override
        public String getDisplayName() {
            return "Readme Configuration";
        }

        /*
         * get方法，index.jelly夠過getReadMeUrl取得readMeUrl變數。
         */
        public String getReadMeUrl() {
            return readMeUrl;
        }

    }

}
