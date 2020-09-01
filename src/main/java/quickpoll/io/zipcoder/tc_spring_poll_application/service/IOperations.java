package quickpoll.io.zipcoder.tc_spring_poll_application.service;

import org.springframework.data.domain.Page;

public interface IOperations<T> {
    public Page<T> findPaginated(final int page, final int size);
}
