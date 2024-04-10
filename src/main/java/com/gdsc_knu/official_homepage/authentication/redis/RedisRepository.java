package com.gdsc_knu.official_homepage.authentication.redis;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RedisRepository extends CrudRepository<RedisToken, String> {
}
