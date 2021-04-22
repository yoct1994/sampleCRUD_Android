package fcm.test.crud_test.model.data;

public class GetPost {
    private Integer listId;
    private String name;
    private String title;
    private String content;
    private String writeAt;
    private String imageName;
    private boolean mine;

    public Integer getListId() {
        return listId;
    }

    public void setListId(Integer listId) {
        this.listId = listId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getWriteAt() {
        return writeAt;
    }

    public void setWriteAt(String writeAt) {
        this.writeAt = writeAt;
    }

    public boolean isMine() {
        return mine;
    }

    public void setMine(boolean mine) {
        this.mine = mine;
    }
}
