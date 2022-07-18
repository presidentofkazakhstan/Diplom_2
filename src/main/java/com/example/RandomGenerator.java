package com.example;

import org.apache.commons.lang3.RandomStringUtils;

public class RandomGenerator {

    public String userEmail = RandomStringUtils.randomAlphabetic(10) + "@mail.ru";

    public String userPassword = RandomStringUtils.randomAlphabetic(10);

    public String userName = RandomStringUtils.randomAlphabetic(10);
}
