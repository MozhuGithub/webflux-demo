package com.kn.webflux.dao;

import com.kn.webflux.entity.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * @ClassName: UserDao
 * @Description TODO:
 * @Date: 2019/11/3 21:52
 * @Author: Kn
 */
@Repository
public interface UserDao extends ReactiveMongoRepository<User,String> {
    /**
     * @Description TODO: 根据年龄段查找用户
     * @Param: [start, end]
     * @Return: reactor.core.publisher.Flux<com.kn.webflux.entity.User>
     * @Author: Kn
     * @Date: 2019/11/4 17:46
     */
    Flux<User> findByAgeBetween(int start, int end);

}
