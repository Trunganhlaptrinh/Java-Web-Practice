package com.concert.booking.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@Configuration
@EnableMongoAuditing
public class MongoConfig {
    // KÃ­ch hoáº¡t tá»± Ä‘á»™ng Ä‘iá»n @CreatedDate vÃ  @LastModifiedDate trÃªn toÃ n bá»™ Documents
}
