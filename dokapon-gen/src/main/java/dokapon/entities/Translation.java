package dokapon.entities;

public class Translation {

    private int offsetData = 0;
    private String translation = "";
    private String menuData = null;

    public Translation() {
    }

    public int getOffsetData() {
        return offsetData;
    }

    public void setOffsetData(int offsetData) {
        this.offsetData = offsetData;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public String getMenuData() {
        return menuData;
    }

    public void setMenuData(String menuData) {
        this.menuData = menuData;
    }
}
