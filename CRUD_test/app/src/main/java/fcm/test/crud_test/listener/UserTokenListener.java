package fcm.test.crud_test.listener;

import fcm.test.crud_test.model.data.UserToken;

public interface UserTokenListener {
    void loadToken(UserToken userToken);
    void onFail();
}
