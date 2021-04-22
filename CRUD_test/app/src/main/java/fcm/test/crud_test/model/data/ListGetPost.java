package fcm.test.crud_test.model.data;

import android.graphics.Bitmap;

import java.util.List;

public class ListGetPost {
    private String name;
    private String title;
    private String content;
    private String writeAt;
    private Bitmap image;

    public ListGetPost(String name, String title, String content, String writeAt, Bitmap image) {
        this.name = name;
        this.content = content;
        this.title = title;
        this.writeAt = writeAt;
        this.image = image;
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

    public String getWriteAt() {
        return writeAt;
    }

    public void setWriteAt(String writeAt) {
        this.writeAt = writeAt;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
