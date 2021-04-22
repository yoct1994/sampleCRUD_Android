package fcm.test.crud_test.listener;

public interface ListPostListener {
    void postList();
    void onFail();
    void notFoundUser();
}
