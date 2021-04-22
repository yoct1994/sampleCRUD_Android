package fcm.test.crud_test.model.data;

public class SignUpRequest {
    private String name;
    private String id;
    private String password;

    public SignUpRequest(String name, String id, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }
}
