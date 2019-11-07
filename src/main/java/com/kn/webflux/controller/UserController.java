package com.kn.webflux.controller;

import com.kn.webflux.dao.UserDao;
import com.kn.webflux.entity.User;
import com.kn.webflux.util.CheckUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;


/**
 * @ClassName: UserController
 * @Description TODO:
 * @Date: 2019/11/3 21:53
 * @Author: Kn
 */
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserDao userDao;
    /**
     * @Description TODO: 官方推荐构造函数注入
     * @Param: [userDao]
     * @Return:
     * @Author: Kn
     * @Date: 2019/11/4 10:28
     */
    public UserController(UserDao userDao) {
        this.userDao = userDao;
    }

    /**
     * @Description TODO: 传统方式查所有数据
     * @Param: []
     * @Return: reactor.core.publisher.Flux<com.kn.webflux.entity.User>
     * @Author: Kn
     * @Date: 2019/11/4 10:27
     */
    @GetMapping("/")
    public Flux<User> getAll() {
        return userDao.findAll();
    }
    /**
     * @Description TODO: 响应式流查所有数据
     * @Param: []
     * @Return: reactor.core.publisher.Flux<com.kn.webflux.entity.User>
     * @Author: Kn
     * @Date: 2019/11/4 10:27
     */
    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<User> getStreamAll() {
        return userDao.findAll();
    }

    @PostMapping("/")
    public Mono<User> create(@Valid @RequestBody User user) {
        //jpa的创建和更新都是一个方法，有id是更新，没有是创建
        //不允许传过来的user带id,避免新建垃圾数据
        user.setId(null);
        //创建时某些名称不能含有
        CheckUtils.CheckName(user.getName());
        return userDao.save(user);
    }
    /**
     * @Description TODO: 根据id删除 存在只需要返回状态码200 不存在返回404
     * @Param: [id]
     * @Return: reactor.core.publisher.Mono<org.springframework.http.ResponseEntity<java.lang.Void>>
     * @Author: Kn
     * @Date: 2019/11/3 23:18
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable String id){
        //deleteById 没有返回值不能判断操作是否成功
        //userDao.deleteById(id);
        //注意一下flatMap 和 map的区别：
        //当需要操作数据并返回一个Mono的时候，使用flatMap
        //当不需要操作数据，只主要对数据进行转换的时候使用map
       return userDao.findById(id)
               .flatMap(user -> userDao.delete(user) //这里delete是Void没有返回值，所以then返回一个Mono
               .then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK)))) //返回一个200的状态码
               .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND)); //如果没找到返回一个404的状态码
    }
    /**
     * @Description TODO: 修改数据 存在返回状态码200和user对象 不存在返回404
     * @Param: [id]
     * @Return: reactor.core.publisher.Mono<org.springframework.http.ResponseEntity<com.kn.webflux.entity.User>>
     * @Author: Kn
     * @Date: 2019/11/3 23:47
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<User>> update(@PathVariable("id") String id, @Valid @RequestBody User user ){
       //某些特定名称不能含有
        CheckUtils.CheckName(user.getName());
        //不直接save的原因是如果不带Id过来的话，会变成创建
        return userDao.findById(id)
                .flatMap(u -> {
                    return userDao.save(
                            u.toBuilder()
                                    .age(user.getAge())
                                    .name(user.getName())
                                    .build());   //这里save返回的本身就是一个Mono，所以后面只需要map转成ResponseEntity
                })
                .map(u -> new ResponseEntity<>(u, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    /**
     * @Description TODO: 根据id查找用户 存在返回状态码200和user对象 不存在返回404
     * @Param: [id]
     * @Return: reactor.core.publisher.Mono<org.springframework.http.ResponseEntity<java.lang.Void>>
     * @Author: Kn
     * @Date: 2019/11/4 0:05
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<User>> findById(@PathVariable("id") String id){
        return userDao.findById(id)
                .map(u -> new ResponseEntity<>(u, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * @Description TODO: 根据年龄段找数据
     * @Param: [start, end]
     * @Return: reactor.core.publisher.Flux<com.kn.webflux.entity.User>
     * @Author: Kn
     * @Date: 2019/11/4 17:46
     */
    @GetMapping("/age/{start}/{end}")
    public Flux<User> findByAge(@PathVariable("start") int start, @PathVariable("end") int end) {
        return userDao.findByAgeBetween(start, end);
    }
    @GetMapping(value = "/age/stream/{start}/{end}",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<User> findByAgeStream(@PathVariable("start") int start, @PathVariable("end") int end) {
        return userDao.findByAgeBetween(start, end);
    }

}
