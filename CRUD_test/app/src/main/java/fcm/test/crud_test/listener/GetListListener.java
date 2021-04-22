package fcm.test.crud_test.listener;

import java.util.List;

import fcm.test.crud_test.model.data.GetPost;

public interface GetListListener {
    void loadList(List<GetPost> lists);
    void onFail();
}
