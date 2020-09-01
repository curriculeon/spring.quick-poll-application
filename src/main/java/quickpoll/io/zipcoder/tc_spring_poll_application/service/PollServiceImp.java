package quickpoll.io.zipcoder.tc_spring_poll_application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import quickpoll.io.zipcoder.tc_spring_poll_application.model.Poll;
import quickpoll.io.zipcoder.tc_spring_poll_application.repositories.PollRepository;

@Service
public class PollServiceImp implements PollService {

    @Autowired
    private PollRepository dao;

    @Override
    public Page<Poll> findPaginated(int page, int size) {
        return dao.findAll(new PageRequest(page,size));

    }

}
